package com.example.imagepicker;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ImagePreview extends Fragment {


    private ImageView selectedImage,close,done;
    private Context context;
    Fragment fragment;
    private ImageModel model;
    private int type = -1;
    private Bitmap imageBitmap;
    ArrayList<String> path = new ArrayList<>();
    public ImagePreview(){}
    public String directory;

    public void setsFragment(Fragment fragment, ImageModel model)
    {
        this.fragment = fragment;
        this.model = model;
    }

    public void setsFragment(Bitmap bitmap)
    {
        imageBitmap = bitmap;
        type=1;
    }

    public void setsFragment(Bitmap bitmap,int type)
    {
        imageBitmap = bitmap;
        this.type=type;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_preview_layout,container,false);
        findViews(view);
        return view;
    }

    private void findViews(View view)
    {
        selectedImage = view.findViewById(R.id.image4);
        close = view.findViewById(R.id.close2);
        done = view.findViewById(R.id.imageDone);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(type==-1)
        selectedImage.setImageURI(model.imageUri);
        else if (type==1)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            imageBitmap = Bitmap.createBitmap(imageBitmap,0,0,imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
            matrix = new Matrix();
            matrix.preScale(-1.0f,1.0f);
            imageBitmap = Bitmap.createBitmap(imageBitmap,0,0,imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
            selectedImage.setImageBitmap(imageBitmap);
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    imageBitmap = Bitmap.createBitmap(imageBitmap,0,0,imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
                    selectedImage.setImageBitmap(imageBitmap);
                }
            }).run();

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        directory = "/"+context.getResources().getString(R.string.app_name)+"Images";


        setActions();
    }

    private void setActions()
    {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainFragment();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==-1)
                {
                   if(ImagePicker.callBack!=null)
                   {
                       path.add(model.data);
                       getActivity().finish();
                       ImagePicker.callBack.getSelectedImages(path);
                   }
                }
                else
                {
                    if(saveImage())
                    {
                        getActivity().finish();
                        ImagePicker.callBack.getSelectedImages(path);

                    }

                }
            }
        });
    }

    private void backToMainFragment()
    {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.remove(this);
//        transaction.commit();
//
//        transaction = getFragmentManager().beginTransaction();
//        transaction.show(fragment);
//        transaction.commit();
        getFragmentManager().popBackStackImmediate();
    }


    private boolean saveImage() {
        boolean holder = false;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteOutput);
        File photoDirectory = new File(Environment.getExternalStorageDirectory()  + directory );

        if (!photoDirectory.exists()) {
            photoDirectory.mkdirs();
        }
        try{
            File imageFile = new File(photoDirectory,"IMG_"+Calendar.getInstance().getTimeInMillis()+".jpg");
            imageFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(imageFile);
            outStream.write(byteOutput.toByteArray());
            outStream.close();
            MediaScannerConnection.scanFile(context,new String[]{imageFile.getPath()},new String[]{"image/jpeg"},null);
            path.clear();
            path.add(imageFile.getPath());
           holder=true;
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return  holder;
    }
}
