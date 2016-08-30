package com.udacity.popularmoviestage1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.model.ImageObject;

import java.util.ArrayList;

/**
 * Created by rj125e on 8/25/2016.
 */
public class ImageAdapter extends ArrayAdapter<ImageObject> {
    private Context mContext;
    private ArrayList<ImageObject> mImgObjList;
    private int listOfMovies;

    public ImageAdapter(Context context, int resource,ArrayList<ImageObject> mImgList){
        super(context,resource,mImgList);
        mContext = context;
        this.mImgObjList = mImgList;
    }
    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {

        if (mImgObjList != null) {
            listOfMovies = mImgObjList.size();
        }
        return listOfMovies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        ImageObject imgObj = null;
        ImageView imgMovie = null;
        imgObj = mImgObjList.get(position);
        if(convertView == null){
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.grid_layout,null);

            imgMovie = (ImageView) gridView.findViewById(R.id.imgMovie);
            imgMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMovie.setAdjustViewBounds(true);

        }else {
            imgMovie = (ImageView) convertView;
        }

        String baseurl = "http://image.tmdb.org/t/p/w500";
        Picasso.with(mContext).load(baseurl.concat(imgObj.getPoster_path())).into(imgMovie);
        return imgMovie;
    }
}
