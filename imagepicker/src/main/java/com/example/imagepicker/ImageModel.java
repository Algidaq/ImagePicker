package com.example.imagepicker;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ImageModel extends Object implements Serializable {

    String id="";
    Uri imageUri;
    String data ="";
    Bitmap imageBitmaps;
    String album="";
    String filePath="";
}
