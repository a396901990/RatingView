package com.deanguo.ratingview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RatingView view = (RatingView) this.findViewById(R.id.rating_view);
        RatingBar bar1 = new RatingBar(8, "难度");
        RatingBar bar2 = new RatingBar(8, "难度");
        RatingBar bar3 = new RatingBar(8, "难度");
        RatingBar bar4 = new RatingBar(8, "难度");

        view.addRatingBar(bar1);
        view.addRatingBar(bar2);
        view.addRatingBar(bar3);
        view.addRatingBar(bar4);
        view.show();
    }
}
