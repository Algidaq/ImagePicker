package com.example.imagepicker;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumModel extends Object implements Serializable {
    String name = "";
    String albumImage = "";
    String albumSize ="";
    String albumId = "";
    Uri albumUri = null;
    ArrayList<Object> albumImages ;
}
