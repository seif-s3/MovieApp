package com.udacity.mal.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.mal.movieapp.R;
import com.udacity.mal.movieapp.data.Review;

import java.util.ArrayList;

/**
 * Created by Seif3 on 4/26/2016.
 * This project was created for the
 * Udacity Developing Android Apps
 * course project.
 */
public class ReviewAdapter extends BaseAdapter
{
    private ArrayList<Review> mReviews;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private static final int NO_REVIEWS_VIEW_TYPE = 0;
    private static final int DEFAULT_REVIEWS_VIEW_TYPE = 1;

    public ReviewAdapter(Context context, ArrayList<Review> objects)
    {
        mReviews = objects;
        mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mReviews.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mReviews.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (mReviews.get(position) == null)
        {
            convertView = layoutInflater.inflate(R.layout.no_reviews_layout, null);
            return convertView;
        }
        convertView = layoutInflater.inflate(R.layout.review_list_item, null);
        TextView reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
        TextView reviewContent = (TextView) convertView.findViewById(R.id.review_content);

        reviewAuthor.setText(mReviews.get(position).getAuthor());
        reviewContent.setText(mReviews.get(position).getContent());

        return convertView;
    }
}
