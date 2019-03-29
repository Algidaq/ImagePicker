package com.example.imagepicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.util.ArrayList;

public class Utilz {


    public static  float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public  static float convertPixelsToDp(float px,Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static ArrayList<Object> getAllImages(Context context)
    {
        ArrayList<Object> holder = null;
        int index = 0;
        ContentResolver resolver = context.getContentResolver();
        String [] projection = {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.Media.BUCKET_ID};
        String orderSet = MediaStore.Images.Media.DATE_TAKEN;
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                         projection,null,null,orderSet+" DESC");

        if(cursor.getCount()>0) {
            holder = new ArrayList<>();
            AlbumsFragment.imageCount = String.valueOf(cursor.getCount());
            while (cursor.moveToNext()) {
                ImageModel model = new ImageModel();
                index = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                model.id = cursor.getString(index);
                System.out.println("Buket Id "+ model.id);
                index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                model.data = cursor.getString(index);
                model.imageUri = Uri.parse(model.data);
                model.filePath = model.data;
                holder.add(model);

            }
           AlbumsFragment.lastImageData =  ((ImageModel) holder.get(0)).data;
            AlbumsFragment.allImages = holder;

        }
        setAlbums(context);

        return holder;
    }

    private static void setAlbums(final Context context)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("In The Thread ");

        ArrayList<Object> holder = null;
        String []  projction1 = {" DISTINCT "+MediaStore.Images.Media.BUCKET_ID};
        String [] projection2 ={MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
        String  [] projection3 = {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver rs = context.getContentResolver();
        Cursor cursor =rs.query(uri,projction1,null,null,null);
        String orderBy = MediaStore.Images.Media.DATE_TAKEN+" DESC";
        if(cursor.getCount()>0)
        {
            Cursor cursor2;
            holder = new ArrayList<>();
            while(cursor.moveToNext())
            {

                AlbumModel model = new AlbumModel();
                model.albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                System.out.println(" Album Id "+model.albumId);
                String where2 = MediaStore.Images.Media.BUCKET_ID+" = "+model.albumId;
                 cursor2 = MediaStore.Images.Media.query(rs,uri,projection2,where2,orderBy);
                 if(cursor2.getCount()>0)
                 {
                     cursor2.moveToNext();
                     model.name =cursor2.getString(0);
                     model.albumImage = cursor2.getString(1);
                     cursor2 = MediaStore.Images.Media.query(rs,uri,projection3,where2, orderBy);
                     model.albumImages = getImagesForAlbum(cursor2);
                     System.out.println("Album Images Size "+model.albumImages.size());
                     model.albumSize = model.albumImages.size()+"";
                     holder.add(model);
                 }
            }

            AlbumsFragment.albumModels = holder;
        }


            }
        }).start();

    }


    private static ArrayList<Object> getImagesForAlbum(Cursor cursor)
    {
        ArrayList<Object> holder = null;

        if(cursor.getCount()>0)
        {
            int index;
            holder = new ArrayList<>();
            while(cursor.moveToNext())
            {
                ImageModel model = new ImageModel();
                index = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                model.id = cursor.getString(index);
                index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                model.data = cursor.getString(index);
                model.imageUri = Uri.parse(model.data);
                model.filePath = model.data;
                holder.add(model);
            }
        }

        return holder;
    }
}
