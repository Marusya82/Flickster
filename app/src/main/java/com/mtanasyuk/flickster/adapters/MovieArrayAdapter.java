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

    // 1 for portrait, 2 for backdrop
    int orientation;

    // view lookup cache
    static class ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        @BindView(R.id.ivMovieImage) ImageView image;
        @BindView(R.id.ivMovieOverlay) ImageView overlay;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // view lookup cache
    static class ViewHolderPopular {
        @BindView(R.id.ivMovieImage) ImageView image;
        @BindView(R.id.ivMovieOverlay) ImageView overlay;

        public ViewHolderPopular(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {
        return getItem(position).isPopular();
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
        orientation = context.getResources().getConfiguration().orientation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for position
        Movie movie = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // Get the data item type for this position
            int type = getItemViewType(position);
            // Inflate XML layout based on the type
//            convertView = getInflatedLayoutForType(type);
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the data into the template view using the data object
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.image.setImageResource(0);

        int id = convertView.getResources().getIdentifier("play_icon.png", "drawable", convertView.getContext().getPackageName());
        viewHolder.overlay.setImageResource(id);

        // initialize progress bar for images to load
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // use backdrop image for the landscape mode, poster for the portrait
        String finalPath;
        if (orientation == 2 && !movie.getBackdropPath().equals("https://image.tmdb.org/t/p/w342/")) {
            finalPath = movie.getBackdropPath();
            progressBar.getLayoutParams().height = 96;
            viewHolder.overlay.getLayoutParams().height = 96;
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

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        if (type == 0) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else if (type == 1) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        } else {
            return null;
        }
    }
}
