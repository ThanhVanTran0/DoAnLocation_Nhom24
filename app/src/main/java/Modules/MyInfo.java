package Modules;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanh.doanlocation_nhom24.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import Models.DiaDiem;

/**
 * Created by Phan Thao on 5/4/2018.
 */

public class MyInfo implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private View mWindow;
    private DiaDiem diaDiem = null;

    public MyInfo(Activity context)
    {
        this.context=context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_maker,null);
    }

    private void renderWindow(Marker marker,View v) {
        LatLng latLng = marker.getPosition();
        TextView name = (TextView) v.findViewById(R.id.name);
        ImageView img=(ImageView) v.findViewById(R.id.img);
        name.setText(marker.getTitle());
        diaDiem = (DiaDiem) marker.getTag();
        if(diaDiem!=null) {
            if(diaDiem.getImgDiaDiem() != null) {
                img.setImageBitmap(diaDiem.getImgDiaDiem());
            }
            else {
                img.setImageResource(R.drawable.img_noimage);
            }
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker,mWindow);
        return mWindow;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}