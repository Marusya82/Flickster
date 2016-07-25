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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // 1 for portrait, 2 for landscape
    int orientation;

    // view lookup cache
    static class ViewHolder {
        @BindView(R.id.tvOverview) TextView tvOverview;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivMovieImage) ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // view lookup cache
    static class ViewHolderPopular {
        @BindView(R.id.ivPopularMovieImage) ImageView popularImage;
        @BindView(R.id.ivOverlay) ImageView overlay;

        public ViewHolderPopular(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {
        return getItem(position).isPopular();
    }

    // Total number of types is 2
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        orientation = context.getResources().getConfiguration().orientation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item type for this position
        int type = getItemViewType(position);

        switch (type) {
            case 0:
                ViewHolder viewHolder;
                View convertView1 = convertView;
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView1 == null) {
                    // Inflate XML layout based on the type
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView1 = inflater.inflate(R.layout.item_movie, parent, false);
                    viewHolder = new ViewHolder(convertView1);
                    convertView1.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView1.getTag();
                }
                // get data item for position
                Movie movie = getItem(position);
                // populate the data into the template view using the data object
                viewHolder.tvOverview.setText(movie.getOverview());
                viewHolder.tvTitle.setText(movie.getOriginalTitle());
                viewHolder.image.setImageResource(0);

                // initialize progress bar for images to load
                final ProgressBar progressBar = (ProgressBar) convertView1.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

                // use backdrop image for the landscape mode, poster for the portrait
                if (orientation == 2) {
                    viewHolder.tvTitle.getLayoutParams().width = 400;
                    viewHolder.tvOverview.getLayoutParams().width = 400;
                }

                Picasso.with(getContext()).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide progress bar on successful load
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });
                return convertView1;

            case 1:
                final ViewHolderPopular viewHolderPopular;
                View convertView2 = convertView;
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView2 == null) {
                    // Inflate XML layout based on the type
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView2 = inflater.inflate(R.layout.item_popular_movie, parent, false);
                    viewHolderPopular = new ViewHolderPopular(convertView2);
                    convertView2.setTag(viewHolderPopular);
                } else {
                    viewHolderPopular = (ViewHolderPopular) convertView2.getTag();
                }

                Movie popularMovie = getItem(position);
                // populate the data into the template view using the data object
                viewHolderPopular.popularImage.setImageResource(0);
                final int id = R.drawable.play_icon2;

                // initialize progress bar for images to load
                final ProgressBar progressBarPopular = (ProgressBar) convertView2.findViewById(R.id.progressBarPopular);
                progressBarPopular.setVisibility(View.VISIBLE);

                Picasso.with(getContext()).load(popularMovie.getBackdropPath()).transform(new RoundedCornersTransformation(10, 10)).into(viewHolderPopular.popularImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide progress bar on successful load
                        progressBarPopular.setVisibility(View.GONE);
                        viewHolderPopular.overlay.setVisibility(View.VISIBLE);
                        viewHolderPopular.overlay.setImageResource(id);
                    }

                    @Override
                    public void onError() {
                    }
                });
                return convertView2;

            default:
                return null;
        }
    }
}
