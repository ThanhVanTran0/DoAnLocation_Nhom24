package Adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thanh.doanlocation_nhom24.R;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public int[] lst_images;
    public String[] lst_description;

    public SlideAdapter(Context context,int[] lst_images,String[] lst_description) {
        this.context = context;
        this.lst_images = lst_images;
        this.lst_description = lst_description;
    }

    @Override
    public int getCount() {
        return lst_description.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.slideimg);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        imgslide.setImageResource(lst_images[position]);
        description.setText(lst_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
