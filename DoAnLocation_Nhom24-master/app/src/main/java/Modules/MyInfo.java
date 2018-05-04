package Modules;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanh.doanlocation_nhom24.R;
import com.google.android.gms.location.places.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Phan Thao on 5/4/2018.
 */

class MyInfo implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    Bitmap btm;

    public MyInfo(Activity context, Bitmap result)
    {
       this.context=context;
        this.btm=result;
    }
    @Override
    public View getInfoContents(Marker arg0) {
        View v = this.context.getLayoutInflater().inflate(R.layout.custom_maker, null);
        LatLng latLng = arg0.getPosition();
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView address = (TextView) v.findViewById(R.id.address);
        ImageView img=(ImageView) v.findViewById(R.id.img);
        name.setText(arg0.getTitle());
       address.setText(arg0.getSnippet());
        img.setImageBitmap(btm);
        return v;
    }
    @Override
    public View getInfoWindow(Marker arg0) {

        return null;
    }

}
