package Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;


/**
 * Created by Thanh on 4/30/2018.
 */

public class DiaDiem implements Serializable{
    private byte[] imgDiaDiem;
    private double lat;
    private double lon;
    private String diaChi;
    private String tenDiaDiem;

    public DiaDiem(Bitmap imgDiaDiem, LatLng latLng, String diaChi, String tenDiaDiem) {
        this.imgDiaDiem = bitMapToArray(imgDiaDiem);
        this.lat = latLng.latitude;
        this.lon = latLng.longitude;
        this.diaChi = diaChi;
        this.tenDiaDiem = tenDiaDiem;
    }


    public Bitmap getImgDiaDiem() {
        if(imgDiaDiem == null)
            return null;
        else {
            return BitmapFactory.decodeByteArray(imgDiaDiem,0,imgDiaDiem.length);
        }
    }

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    public void setImgDiaDiem(Bitmap imgDiaDiem) {
        this.imgDiaDiem = bitMapToArray(imgDiaDiem);
    }

    public LatLng getLatLng() {
        return new LatLng(lat,lon);
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lon = latLng.longitude;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Bundle toBundle() {
        Bundle b= new Bundle();
        b.putString("DIACHI",this.diaChi);
        b.putString("TEN",this.tenDiaDiem);
        b.putDouble("LAT",this.lat);
        b.putDouble("LON",this.lon);
        b.putByteArray("BITMAP",imgDiaDiem);
        return b;
    }

    private byte[] bitMapToArray(Bitmap imgDiaDiem) {
        if(imgDiaDiem == null)
            return null;
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgDiaDiem.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bitmap = stream.toByteArray();
            return bitmap;
        }
    }
}
