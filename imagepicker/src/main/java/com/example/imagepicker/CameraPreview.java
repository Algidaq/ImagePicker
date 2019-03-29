package com.example.imagepicker;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraPreview(Context context , Camera mCamera) {
        super(context);
        this.mCamera = mCamera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mHolder.getSurface() == null)
        {
            return;
        }

        try{
            //stop preview

            mCamera.stopPreview();

        }catch(Exception e)
        {
            e.printStackTrace();
        }


        try{

            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public void refershCamera(Camera camera)
    {
        try{
            if(mHolder.getSurface()==null)
                return;
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        try{
            camera.stopPreview();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        try{
            camera.setPreviewDisplay(mHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setmCamera(Camera cam)
    {
        this.mCamera =cam;
    }
}
