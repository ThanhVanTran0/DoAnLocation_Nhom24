package Modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.thanh.doanlocation_nhom24.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Phan Thao on 5/4/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View Window;
    private Context context;
    public CustomInfoWindowAdapter(Context context){
        this.context= context;
        Window = LayoutInflater.from(context).inflate(R.layout.custom_maker,null);
    }
    private void Text(Marker marker,View view){
        String title = marker.getTitle();
        TextView name = (TextView)view.findViewById(R.id.name);
        if(!name.equals("")){
            name.setText(title);
        }
        String snippet = marker.getSnippet();
       TextView address = (TextView)view.findViewById(R.id.address);
        if(!address.equals("")){
            address.setText(snippet);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {

        Text(marker,Window);
        return Window;
    }

    @Override
    public View getInfoContents(Marker marker) {

        Text(marker,Window);
        return Window;
    }
}
