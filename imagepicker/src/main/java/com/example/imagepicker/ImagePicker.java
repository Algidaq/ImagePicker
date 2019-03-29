package com.example.imagepicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;

import java.util.ArrayList;

public class ImagePicker extends AppCompatActivity {

    CameraFragment camFragment;
    FragmentTransaction transaction;
    private static boolean StartGalleryOnly = true;
    private static boolean StartGarlleryAndCamera = false;
    public static ImagePickerCallBack callBack;


    public interface ImagePickerCallBack
    {
        void getSelectedImages(ArrayList<String> filePaths);
    }


    public static void setImagePickerParams(boolean allowMultiSelection,boolean startGalleryOnly,boolean startGarlleryAndCamera,ImagePickerCallBack imagePickerCallBack)
    {
        StartGalleryOnly = startGalleryOnly;
        StartGarlleryAndCamera = startGarlleryAndCamera;
        callBack = imagePickerCallBack;
        AlbumFragmentSingle.allowMultiSelection = allowMultiSelection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        else
        {
            if(StartGarlleryAndCamera && StartGalleryOnly==false) {

                camFragment = new CameraFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.frame_layout1, camFragment);
                transaction.commit();
            }else
                {
                    Utilz.getAllImages(this);
                    AlbumsFragment fragment = new AlbumsFragment();
                    fragment.setFragment(null);
                    fragment.setEnterTransition(new Fade());
                    fragment.setExitTransition(new Fade());
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout1,fragment);
                    transaction.commit();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(StartGarlleryAndCamera && StartGalleryOnly==false) {

                    camFragment = new CameraFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(R.id.frame_layout1, camFragment);
                    transaction.commit();
                }
                else
                {
                    Utilz.getAllImages(this);
                    AlbumsFragment fragment = new AlbumsFragment();
                    fragment.setFragment(null);
                    fragment.setEnterTransition(new Fade());
                    fragment.setExitTransition(new Fade());
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout1,fragment);
                    transaction.commit();
                }
            }
        }
    }
}
