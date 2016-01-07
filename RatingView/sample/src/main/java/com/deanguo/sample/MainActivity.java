package com.deanguo.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deanguo.ratingview.RatingBar;
import com.deanguo.library.*;
import com.deanguo.ratingview.RatingView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) this.findViewById(R.id.center_btn);
        final RatingView view = (RatingView) this.findViewById(R.id.rating_view);

        final RatingBar bar1 = new RatingBar(8, "难度");
        final RatingBar bar2 = new RatingBar(8, "难度");
        final RatingBar bar3 = new RatingBar(8, "难度");
        final RatingBar bar4 = new RatingBar(8, "难度");
        view.setAnimatorListener(new RatingView.AnimatorListener() {
            @Override
            public void onRotateStart() {

            }

            @Override
            public void onRotateEnd() {
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRatingStart() {

            }

            @Override
            public void onRatingEnd() {
            }
        });
        view.addRatingBar(bar1);
        view.addRatingBar(bar2);
        view.addRatingBar(bar3);
        view.addRatingBar(bar4);
        view.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.clear();
                bar1.setRate(5);
                bar2.setRate(1);
                bar2.setRate(3);
                view.show();
            }
        });
    }
}
