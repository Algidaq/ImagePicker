package com.example.imagepicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AlbumFragmentSingle extends Fragment implements ImageAdapter.onSingleImageClick, ImageAdapter.onImageChecked {


    private AlbumModel model;
    private Fragment fragment;
    public static boolean allowMultiSelection = false;
    private RecyclerView recy ;
    private FloatingActionButton done;
    private Toolbar toolbar;
    private Context context;
    private ImageAdapter adapter;
    private GridLayoutManager layoutManager;
    private HashMap<Integer,String> map = new HashMap<>();
    private Fade fade = new Fade();
    private ArrayList<String> filePahts = new ArrayList<>();



    public AlbumFragmentSingle(){}

    public void setFragment(Fragment fragment, AlbumModel model)
    {
        this.model = model;
        this.fragment=fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_album_fragment_layout,container,false);
        findViews(view);
        return view;
    }

    public void findViews(View view)
    {
        recy = view.findViewById(R.id.recy3);
        done = view.findViewById(R.id.fabDone);
        toolbar = view.findViewById(R.id.toolbar2);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        setViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setViews()
    {
        fade.setDuration(200);
        setExitTransition(fade);
        toolbar.setTitle(model.name);
        if(allowMultiSelection)
            adapter = new ImageAdapter(context,model.albumImages,3);
        else
            adapter = new ImageAdapter(context,model.albumImages,4);


        adapter.setOnSingleImageClick(this);
        adapter.setOnImageChecked(this);
        adapter.setAllowMultiSelection(allowMultiSelection);
        layoutManager = new GridLayoutManager(context,4);
        recy.setLayoutManager(layoutManager);
        recy.setAdapter(adapter);
        recy.setItemAnimator(new DefaultItemAnimator());

        setActions();
    }

    public void setActions()
    {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<Integer> iterator = map.keySet().iterator();
                for(int i=0;i<map.size();i++)
                {
                    filePahts.add(map.get(iterator.next()));
                }
                ImagePicker.callBack.getSelectedImages(filePahts);
                getActivity().finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(AlbumFragmentSingle.this)
                        .commit();
                getFragmentManager().beginTransaction()
                        .show(fragment)
                        .commit();
            }
        });
    }

    @Override
    public void onImageClick(int position, ImageView image) {

        ImagePreview preview = new ImagePreview();
        preview.setsFragment(this,((ImageModel)model.albumImages.get(position)));
        preview.setEnterTransition(fade);
        preview.setSharedElementEnterTransition(new DetailsTransition());
        preview.setSharedElementReturnTransition(new DetailsTransition());

        getFragmentManager().beginTransaction()
                .addSharedElement(image,"selectedImage")
                .replace(R.id.frame_layout1,preview)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public void onChecked(int postion, int checkedImages, boolean addOrDelete) {

        int y = postion+1;
        if(addOrDelete)
        {
            map.put(y,((ImageModel)model.albumImages.get(postion)).data);

            if(done.getVisibility()==View.GONE)
            {
                done.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            map.remove(y);
            if(checkedImages==0)
            {
                done.setVisibility(View.GONE);
            }
        }
    }
}
