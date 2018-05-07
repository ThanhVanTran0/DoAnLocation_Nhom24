package com.example.thanh.doanlocation_nhom24;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import Adapter.SlideAdapter;
import Modules.BaseActivity;

public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter myadapter;
    private int[] lst_images;
    private String[] lst_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            lst_images = bundle.getIntArray("listImage");
            lst_description = bundle.getStringArray("list");

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            myadapter = new SlideAdapter(this,lst_images,lst_description);
            viewPager.setAdapter(myadapter);
        }
        else {
            Toast.makeText(this, "Dữ liệu lỗi", Toast.LENGTH_SHORT).show();
        }
    }
}
