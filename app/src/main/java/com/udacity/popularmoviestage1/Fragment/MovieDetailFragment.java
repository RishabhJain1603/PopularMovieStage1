package com.udacity.popularmoviestage1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.model.ImageObject;

import org.w3c.dom.Text;

/**
 * Created by rj125e on 8/27/2016.
 */
public class MovieDetailFragment extends Fragment {

    private View view;
    private TextView mMovieTitle,mOverview,mVote,mReleaseDate;
    private ImageView mMovieImageThumnail;

    public MovieDetailFragment(){
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_detail_fragment,container,false);
        initView();
        return view;
    }

    private void initView(){
        Intent intent = getActivity().getIntent();
        ImageObject imgObj = intent.getExtras().getParcelable("image_details");

        mMovieTitle = (TextView) view.findViewById(R.id.movieTitle);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mVote = (TextView) view.findViewById(R.id.vote);
        mReleaseDate = (TextView) view.findViewById(R.id.releaseDate);

        String voteHeader = getResources().getString(R.string.average_rating);
        String releaseDateHeader = getResources().getString(R.string.release_date);

        mMovieTitle.setText(imgObj.getOriginal_title());
        mOverview.setText(imgObj.getOverview());
        mVote.setText(voteHeader+imgObj.getVote_average());
        mReleaseDate.setText(releaseDateHeader+imgObj.getRelease_date());

        mMovieImageThumnail = (ImageView) view.findViewById(R.id.movieImageThumnail);
        String baseurl = "http://image.tmdb.org/t/p/w500";
        Picasso.with(getActivity()).load(baseurl.concat(imgObj.getPoster_path())).into(mMovieImageThumnail);



    }
}
