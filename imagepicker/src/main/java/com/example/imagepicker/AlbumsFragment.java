package com.example.imagepicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment implements ImageAdapter.onAlbumLayoutClick {

    private Fragment fragment;
    RecyclerView recy;
    ConstraintLayout albumLayout;
    TextView albumName,albumSize;
    ImageView albumImage;
    static String lastImageData;
    static String imageCount;
    static ArrayList<Object> albumModels;
    static ArrayList<Object> allImages;
    Context context;
    ImageAdapter adapter;
   android.support.v7.widget.Toolbar toolbar;
   AlbumFragmentSingle singleAlbumFragment;
    FragmentTransaction trans;


   public AlbumsFragment(){}

   public void setFragment(Fragment fragment)
   {
       this.fragment = fragment;
   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_layout,container,false);
        findViews(view);
        return view;
    }

    public void findViews(View view)
    {
        toolbar = view.findViewById(R.id.toolbar1);
        recy = view.findViewById(R.id.recy2);
        albumLayout = view.findViewById(R.id.albumLayout2);
        albumImage = view.findViewById(R.id.albumImage1);
        albumName = view.findViewById(R.id.textView3);
        albumSize = view.findViewById(R.id.textView4);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        setViews();
    }

    public void setViews()
    {
        albumName.setText(" All ");
        albumSize.setText(imageCount);
        Glide.with(context)
                .load(lastImageData)
                .crossFade(20)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(albumImage);


        albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                trans = getFragmentManager().beginTransaction();
                trans.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
                trans.hide(AlbumsFragment.this);
                trans.commit();
                singleAlbumFragment = new AlbumFragmentSingle();
                AlbumModel model = new AlbumModel();
                model.name="All";
                model.albumImages = allImages;
                singleAlbumFragment.setFragment(AlbumsFragment.this,(AlbumModel)model);
                trans = getFragmentManager().beginTransaction();
                trans.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                trans.add(R.id.frame_layout1,singleAlbumFragment);
                trans.commit();

            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragment != null) {
                    trans = getFragmentManager().beginTransaction();
                    trans.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
                    trans.remove(AlbumsFragment.this);
                    trans.commit();

                    trans = getFragmentManager().beginTransaction();
                    trans.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                    trans.show(fragment);
                    trans.commit();
                }
                else {
                    getActivity().finish();
                }

            }
        });

        if(albumModels != null)
        {
            System.out.println("Not Null album Fragment ");
            System.out.println("Not Null album Size "+albumModels.size());
            adapter = new ImageAdapter(context,albumModels,2);
            adapter.setOnAlbumLayoutClick(this);

            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recy.setAdapter(adapter);
            recy.setLayoutManager(manager);
            recy.setItemAnimator(new DefaultItemAnimator());
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onClick(int position) {
        trans = getFragmentManager().beginTransaction();
        trans.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
        trans.hide(AlbumsFragment.this);
        trans.commit();
        singleAlbumFragment = new AlbumFragmentSingle();
        singleAlbumFragment.setFragment(AlbumsFragment.this,((AlbumModel)albumModels.get(position)));
        trans = getFragmentManager().beginTransaction();
        trans.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        trans.add(R.id.frame_layout1,singleAlbumFragment);
        trans.commit();
    }
}
