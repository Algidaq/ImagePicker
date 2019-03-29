package com.example.imagepicker;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class CameraFragment extends Fragment implements ImageAdapter.onSingleImageClick {

    private Camera camera;
    private CameraPreview preview;
    private Camera.PictureCallback picture;
    Button takePic;
    ImageView flash,close,imageGallery,flipCam,slideUp;
    FrameLayout frameLayout;
    Camera.Parameters parameters;

    int frontCam,backCam;
    boolean oneCamOnly = false,frontFlag=false , backFlag=false;
    Context context;

   /**-1 means its off 0 means auto 1 means o **/
   int flashFlag = -1;
   int cameraNumbers =0;
   AnimatorSet flipOut;
   private GestureDetector detector;

   float constHeight;

   RecyclerView recy;
   ArrayList<Object> models;
   ImageAdapter adapter;
   AlbumsFragment albumsFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout,container,false);
       findViews(view);
       initCamera();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        context = getContext();
        models = Utilz.getAllImages(context);
        constHeight = Utilz.convertDpToPixel(150f,context);
        setPreview();
        frameLayout.addView(preview);
        setCameraDistance();
        setActions();
        setParametes(this.camera, Camera.Parameters.FLASH_MODE_OFF);
        flipOut =(AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.flip_out);

        if(models != null)
        {
            adapter = new ImageAdapter(context,models,1);
            adapter.setOnSingleImageClick(this);
            LinearLayoutManager manager =  new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recy.setLayoutManager(manager);
            recy.setAdapter(adapter);
            recy.setItemAnimator(new DefaultItemAnimator());

        }






    }


    @Override
    public void onPause() {
        super.onPause();
        this.camera.release();
        this.camera=null;
        System.out.println(" On Pause ");
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("On Resume ");

        if(this.camera==null)
        {

            if(backFlag==true)
            {
               this.camera = Camera.open(backCam);
            }
            else if(frontFlag==true)
            {
                this.camera = Camera.open(frontCam);

            }
            preview.setmCamera(this.camera);
//            preview.refershCamera(this.camera);
            picture = getPicture();

        }

    }

    public void findViews(View view)
    {
        takePic = view.findViewById(R.id.takePic);
        flash = view.findViewById(R.id.flash);
        close = view.findViewById(R.id.close);
        imageGallery = view.findViewById(R.id.imageGallery);
        flipCam = view.findViewById(R.id.flipCam);
        slideUp = view.findViewById(R.id.slideUp);
        frameLayout = view.findViewById(R.id.frame_layout2);
        recy = view.findViewById(R.id.recy1);
    }


    public void initCamera()
    {
        cameraNumbers = Camera.getNumberOfCameras();

        if(cameraNumbers>1)
        {
            frontCam = Camera.CameraInfo.CAMERA_FACING_FRONT;
            backCam = Camera.CameraInfo.CAMERA_FACING_BACK;
            camera = Camera.open(frontCam);
            backFlag=false;
            frontFlag=true;
        }
        else
        {
            backCam = Camera.CameraInfo.CAMERA_FACING_BACK;
            camera = Camera.open(backCam);
            oneCamOnly=true;
            backFlag=true;
            frontFlag=false;

        }
        picture = getPicture();
    }


    public void setPreview()
    {
        if(camera!=null)
        {
            preview = new CameraPreview(context,camera);

        }
    }

    public void setParametes(Camera cam,String mode)
    {
        parameters = cam.getParameters();
        parameters.setFlashMode(mode);
        cam.setParameters(parameters);
        if(mode.contentEquals(Camera.Parameters.FLASH_MODE_AUTO))
        {
            flash.setImageResource(R.drawable.ic_flash_auto);
            flashFlag =0;
        }
        else if (mode.contentEquals(Camera.Parameters.FLASH_MODE_OFF))
        {
            flash.setImageResource(R.drawable.ic_flash_off);
            flashFlag=-1;
        }
        else if (mode.contentEquals(Camera.Parameters.FLASH_MODE_ON))
        {
            flash.setImageResource(R.drawable.ic_flash_on);
            flashFlag=1;
        }

    }

    private Camera.PictureCallback getPicture()
    {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                ImagePreview pre = new ImagePreview();
                if(frontFlag)
                pre.setsFragment(BitmapFactory.decodeByteArray(data,0,data.length));
                else {
                    pre.setsFragment(BitmapFactory.decodeByteArray(data, 0, data.length), 2);
                }


                pre.setEnterTransition(new Fade());
                pre.setExitTransition(new Fade());
                trans.replace(R.id.frame_layout1,pre);
                trans.addToBackStack("ring");
                trans.commit();
                preview.refershCamera(camera);

            }
        };

        return picture;
    }


    public void setActions()
    {

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,picture);
            }
        });
        flash.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flashFlag==-1)
                {
                    setParametes(camera, Camera.Parameters.FLASH_MODE_AUTO);
                }
                else if(flashFlag==0)
                {
                    setParametes(camera, Camera.Parameters.FLASH_MODE_ON);
                }
                else if(flashFlag==1)
                {
                    setParametes(camera, Camera.Parameters.FLASH_MODE_OFF);

                }

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        flipCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              changeCam();
            }
        });

        detector = new GestureDetector(context,new Gesture());

        slideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recy.getVisibility()==View.GONE)
                {
                    recy.setVisibility(View.VISIBLE);
                    slideUp.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                }
                else
                {
                    recy.setVisibility(View.GONE);
                    slideUp.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
            }
        });

        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumsFragment = new AlbumsFragment();
                albumsFragment.setFragment( CameraFragment.this);
                FragmentTransaction trasnaction = getFragmentManager().beginTransaction();
                trasnaction.hide(CameraFragment.this);
                trasnaction.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
                trasnaction.commit();
                trasnaction = getFragmentManager().beginTransaction();
                trasnaction.add(R.id.frame_layout1, albumsFragment);
                trasnaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                trasnaction.commit();
            }
        });
    }


    public void setCameraDistance()
    {
        int distance=8000;
        float scale =getResources().getDisplayMetrics().density*distance;
        frameLayout.setCameraDistance(scale);
    }


    public void changeCam()
    {
        if(this.camera != null)
            this.camera.release();

        if(cameraNumbers>1)
        {
            if(frontFlag==true)
            {
                this.camera = Camera.open(backCam);
                flipOut.setTarget(frameLayout);;
                flipOut.start();
                picture = getPicture();
                preview.refershCamera(this.camera);
                frontFlag=false;
                backFlag=true;
                setParametes(this.camera,Camera.Parameters.FLASH_MODE_OFF);
                flashFlag=-1;
            }
            else if(backFlag==true)
            {

                this.camera = Camera.open(frontCam);
                flipOut.setTarget(frameLayout);;
                flipOut.start();
                picture = getPicture();
                preview.refershCamera(this.camera);
                frontFlag=true;
                backFlag=false;
                setParametes(this.camera,Camera.Parameters.FLASH_MODE_OFF);
                flashFlag=-1;

            }

        }
        else
        {

        }
    }

    @Override
    public void onImageClick(int position, ImageView image) {

       FragmentTransaction trans = getFragmentManager().beginTransaction();
        ImagePreview imagePreview = new ImagePreview();
        imagePreview.setsFragment(this,(ImageModel)models.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imagePreview.setSharedElementEnterTransition(new DetailsTransition());

            trans.addSharedElement(image,"selectedImage");
            imagePreview.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            imagePreview.setSharedElementReturnTransition(new DetailsTransition());
        }
        trans.add(R.id.frame_layout1,imagePreview);
        trans.addToBackStack(null);
        trans.commit();
    }


    public class Gesture extends  GestureDetector.SimpleOnGestureListener
    {

        int y ;
        int oldy=0;
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
          return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int scaleBy = -10;
            y = (int) e2.getY()*-1;
            System.out.println("distanceY "+e2.getY());
            if(y>1 )
            {
                scaleBy*=-1;
                oldy=y;
            }
            float DpYSize = recy.getHeight()+(y);

            if(recy.getHeight() < (int) constHeight && scaleBy >0) {

                if(DpYSize>0)
                {
                    if(e2.getY()*-1>0)
                    recy.getLayoutParams().height = (int) DpYSize;
                }
                else
                {
                    recy.getLayoutParams().height = 1;
                }

            }
            else if (scaleBy<0 && (recy.getHeight()<=constHeight ))
            {
                if(DpYSize>0)
                {
                    if(e2.getY()*-1<30)
                    recy.getLayoutParams().height = (int) DpYSize;

                }
                else
                {
                    recy.getLayoutParams().height =  1;

                }
                System.out.println("Size "+DpYSize);
            }

            recy.requestLayout();

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }





}
