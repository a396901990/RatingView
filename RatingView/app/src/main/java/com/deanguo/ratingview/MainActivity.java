package com.deanguo.ratingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RatingView view = (RatingView) this.findViewById(R.id.rating_view);
        view.addRatingBar(new RatingBar(8, "难度"));
        view.addRatingBar(new RatingBar(2, "风景"));
        view.addRatingBar(new RatingBar(5, "路况"));
        view.addRatingBar(new RatingBar(8, "难度"));
        view.show();

    }
}
