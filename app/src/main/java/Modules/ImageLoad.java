package Modules;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Models.DiaDiem;

/**
 * Created by Phan Thao on 5/4/2018.
 */
public class ImageLoad extends AsyncTask<Void,Void,Bitmap> {
    private String url;
    private GoogleMap map;
    private boolean isCompleted=false;
    private Marker currentMarker;
    private Activity context;
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ImageLoad(Activity context, String url, GoogleMap map, Marker currentMarker) {
        this.context=context;
        this.currentMarker=currentMarker;
        this.url = url;
        this.map=map;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if (myBitmap == null)
                return null;
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        DiaDiem diaDiem = (DiaDiem) currentMarker.getTag();
        diaDiem.setImgDiaDiem(bitmap);
        map.setInfoWindowAdapter(new MyInfo(context));
    }
}