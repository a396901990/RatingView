package com.deanguo.sample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.deanguo.ratingview.RatingBar;
import com.deanguo.ratingview.RatingView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(new DemoOneFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.demo_one) {
            showFragment(new DemoOneFragment());
        } else if (id == R.id.demo_two) {
            showFragment(new DemoTwoFragment());
        } else if (id == R.id.demo_three) {
            showFragment(new DemoThreeFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFragment(final Fragment fragment) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_view, fragment)
                        .commit();
            }
        });
    }

}
