package com.udacity.mal.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.mal.movieapp.R;
import com.udacity.mal.movieapp.data.Trailer;

import java.util.ArrayList;

/**
 * Created by Seif3 on 4/26/2016.
 */
public class TrailerAdapter extends BaseAdapter
{
    private ArrayList<Trailer> mTrailers;
    private Context mContext;
    LayoutInflater layoutInflater;

    public TrailerAdapter(Context ctx, ArrayList<Trailer> data)
    {
        mTrailers = data;
        mContext = ctx;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mTrailers.size();
    }


    @Override
    public Object getItem(int position)
    {
        return mTrailers.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView = layoutInflater.inflate(R.layout.trailer_list_item, null);
        TextView trailerTitle = (TextView) convertView.findViewById(R.id.trailer_title);
        trailerTitle.setText(mTrailers.get(position).getName());

        trailerTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                        mTrailers.get(position).getKey())));
            }
        });
        return convertView;
    }
}

