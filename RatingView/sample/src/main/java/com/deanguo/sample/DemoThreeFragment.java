package com.deanguo.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deanguo.ratingview.RatingBar;
import com.deanguo.ratingview.RatingView;

/**
 * Created by DeanGuo on 2/29/16.
 */
public class DemoThreeFragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.demo_three_fragment_view, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        final View btn = rootView.findViewById(R.id.center_btn);
        final RatingView view = (RatingView) rootView.findViewById(R.id.rating_view);

        final RatingBar bar1 = new RatingBar(5, "难度");
        final RatingBar bar2 = new RatingBar(8, "风景");
        final RatingBar bar3 = new RatingBar(3, "路况");
        final RatingBar bar4 = new RatingBar(4, "天气");
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
                bar1.setRatingBarColor(getResources().getColor(R.color.blue));

                bar2.setRate(8);
                bar2.setRatingBarColor(getResources().getColor(R.color.lime));

                bar3.setRate(3);
                bar3.setRatingBarColor(getResources().getColor(R.color.red));

                bar4.setRate(4);
                bar4.setRatingBarColor(getResources().getColor(R.color.green));
                view.show();
            }
        });
    }
}
