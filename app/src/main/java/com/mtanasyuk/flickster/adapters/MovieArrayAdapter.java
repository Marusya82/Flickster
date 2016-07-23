package com.mtanasyuk.flickster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mtanasyuk.flickster.R;
import com.mtanasyuk.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // 1 for portrait, 2 for backdrop
    int orientation;

    // view lookup cache
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        orientation = context.getResources().getConfiguration().orientation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Movie movie = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.image.setImageResource(0);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the data into the template view using the data object
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.image.setImageResource(0);

        // initialize progress bar for images to load
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // use backdrop image for the landscape mode, poster for the portrait
        String finalPath;
        if (orientation == 2 && !movie.getBackdropPath().equals("https://image.tmdb.org/t/p/w342/")) {
            finalPath = movie.getBackdropPath();
            progressBar.getLayoutParams().height = 96;

        } else finalPath = movie.getPosterPath();

        Picasso.with(getContext()).load(finalPath).transform(new RoundedCornersTransformation(10,10)).into(viewHolder.image, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Hide progress bar on successful load
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError() {
            }
        });
        //return the view
        return convertView;
    }
}
