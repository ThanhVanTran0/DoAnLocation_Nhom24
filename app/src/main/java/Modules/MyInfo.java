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

/**
 * Created by Phan Thao on 5/4/2018.
 */

class MyInfo implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private Bitmap btm;
    private View mWindow;

    public MyInfo(Activity context, Bitmap result)
    {
        this.context=context;
        this.btm=result;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_maker,null);
    }

    private void renderWindow(Marker marker,View v) {
        LatLng latLng = marker.getPosition();
        TextView name = (TextView) v.findViewById(R.id.name);
        ImageView img=(ImageView) v.findViewById(R.id.img);
        name.setText(marker.getTitle());
        img.setImageBitmap((Bitmap) marker.getTag());
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