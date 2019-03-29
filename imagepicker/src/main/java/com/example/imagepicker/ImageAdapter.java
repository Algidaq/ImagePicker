package com.example.imagepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    ArrayList<Object> models;
    int type;
    private onAlbumLayoutClick onAlbumLayoutClick;
    private onImageChecked onImageChecked;
    private onSingleImageClick onSingleImageClick;

    private boolean onLongClickIsSet = false;
    private int checkedImages = 0;
    private boolean allowMultiSelection = false;


    public interface onImageChecked
    {
       void onChecked(int postion, int checkedImages, boolean addOrDelete);
    }
    public interface onAlbumLayoutClick
    {
        void onClick(int position);
    }

    public interface onSingleImageClick
    {
        void onImageClick(int position, ImageView image);
    }




    public ImageAdapter(Context context, ArrayList<Object> models, int type)
    {
        this.type = type;
        this.context = context;
        this.models = models;
    }


    public void setOnAlbumLayoutClick(ImageAdapter.onAlbumLayoutClick onAlbumLayoutClick) {
        this.onAlbumLayoutClick = onAlbumLayoutClick;
    }

    public void setOnImageChecked(ImageAdapter.onImageChecked onImageChecked) {
        this.onImageChecked = onImageChecked;
    }

    public void setOnSingleImageClick(ImageAdapter.onSingleImageClick onSingleImageClick) {
        this.onSingleImageClick = onSingleImageClick;
    }

    public void setAllowMultiSelection(boolean allowMultiSelection) {
        this.allowMultiSelection = allowMultiSelection;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder=null;
        if(type==1)
        {
            holder = new Holder(inflater.inflate(R.layout.recy_item_1,viewGroup,false));
        }
        else if(type==2)
        {
            holder = new Holder1(inflater.inflate(R.layout.recy_item_2,viewGroup,false));
        } else if(type==3)
        {
            holder = new Holder2(inflater.inflate(R.layout.recy_item_3,viewGroup,false));
        }
        else if (type==4)
        {
            holder = new Holder3(inflater.inflate(R.layout.recy_item_3,viewGroup,false));

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        switch(type)
        {
            case 1:{
                final Holder holder= (Holder) viewHolder;
                Glide.with(context)
                        .load(((ImageModel)models.get(i)).data)
                        .crossFade(50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image1);
                ViewCompat.setTransitionName(holder.image1, String.valueOf(i) + "_image");
                holder.image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSingleImageClick.onImageClick(i,holder.image1);
                    }
                });

            }
            break;
            case 2:{
                Holder1 holder= (Holder1) viewHolder;
                Glide.with(context)
                        .load(((AlbumModel)models.get(i)).albumImage)
                        .crossFade(50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.albumImage);

                holder.albumName.setText(((AlbumModel) models.get(i)).name);
                holder.albumSize.setText(((AlbumModel) models.get(i)).albumSize);
            }break;
            case 3:{
                final Holder2 holder= (Holder2) viewHolder;
                Glide.with(context)
                        .load(((ImageModel)models.get(i)).data)
                        .crossFade(50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image1);
                ViewCompat.setTransitionName(holder.image1, String.valueOf(i) + "_image");

            }break;
            case 4:{
                final Holder3 holder= (Holder3) viewHolder;
                Glide.with(context)
                        .load(((ImageModel)models.get(i)).data)
                        .crossFade(50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image1);
                ViewCompat.setTransitionName(holder.image1, String.valueOf(i) + "_image");
                holder.image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSingleImageClick.onImageClick(i,holder.image1);
                    }
                });
            }break;
        }

    }

    @Override
    public int getItemCount() {
        return  models.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        ImageView image1;

        public Holder(View view)
        {
            super(view);
            image1 = view.findViewById(R.id.image1);
        }
    }


    public class Holder1 extends RecyclerView.ViewHolder
    {
        ImageView albumImage;
        TextView albumName;
        TextView albumSize;
        ConstraintLayout layout ;

        public Holder1(View view)
        {
            super(view);
            albumImage = view.findViewById(R.id.albumImage);
            albumName = view.findViewById(R.id.textView);
            albumSize = view.findViewById(R.id.textView2);
            layout = view.findViewById(R.id.albumLayout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAlbumLayoutClick.onClick(getAdapterPosition());
                }
            });
        }
    }

    public class Holder2 extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener
    {
        ImageView image1;
        CheckBox box;
        View view1;

        public Holder2(View view)
        {
            super(view);
            image1 = view.findViewById(R.id.image3);
            box = view.findViewById(R.id.checkbox);
            view1 = view.findViewById(R.id.view1);
            box.setChecked(false);
            image1.setLongClickable(true);

            image1.setOnClickListener(this);

            if(allowMultiSelection)
            image1.setOnLongClickListener(this);



        }


        @Override
        public void onClick(View v) {
            System.out.println("Nigga What the Fuck  ");

            if(checkedImages>=1) {
                if (onLongClickIsSet == true) {

                    if (box.isChecked()) {
                        box.setChecked(false);
                        box.setVisibility(View.GONE);
                        view1.setVisibility(View.GONE);
                        --checkedImages;
                        if (checkedImages == 0) {
                            onLongClickIsSet = false;
                        }
                        onImageChecked.onChecked(getAdapterPosition(), checkedImages, false);
                    } else {
                        box.setChecked(true);
                        box.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.VISIBLE);
                        ++checkedImages;
                        onImageChecked.onChecked(getAdapterPosition(), checkedImages, true);

                    }
                }
            }
            else
            {
                onSingleImageClick.onImageClick(getAdapterPosition(),image1);
            }


        }

        @Override
        public boolean onLongClick(View v) {
            if(box.isChecked()==false && onLongClickIsSet==false)
            {
                box.setChecked(true);
                box.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                onLongClickIsSet=true;
                checkedImages++;
                onImageChecked.onChecked(getAdapterPosition(),checkedImages,true);
            }
            return true;
        }
    }

    public class Holder3 extends RecyclerView.ViewHolder
    {
        ImageView image1;
        CheckBox box;
        View view1;

        public Holder3(View view)
        {
            super(view);
            image1 = view.findViewById(R.id.image3);
            box = view.findViewById(R.id.checkbox);
            view1 = view.findViewById(R.id.view1);
            box.setChecked(false);




        }

    }


}
