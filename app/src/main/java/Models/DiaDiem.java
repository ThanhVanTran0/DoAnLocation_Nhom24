package Models;

import android.graphics.Bitmap;
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
    private Bitmap imgDiaDiem;
    private LatLng latLng;
    private String diaChi;
    private String tenDiaDiem;

    public DiaDiem(Bitmap imgDiaDiem, LatLng latLng, String diaChi, String tenDiaDiem) {
        this.imgDiaDiem = imgDiaDiem;
        this.latLng = latLng;
        this.diaChi = diaChi;
        this.tenDiaDiem = tenDiaDiem;
    }


    public Bitmap getImgDiaDiem() {
        return imgDiaDiem;
    }

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    public void setImgDiaDiem(Bitmap imgDiaDiem) {
        this.imgDiaDiem = imgDiaDiem;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
        b.putDouble("LAT",this.latLng.latitude);
        b.putDouble("LON",this.latLng.longitude);
        if (imgDiaDiem == null) {
            b.putByteArray("BITMAP",null);
        }
        else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgDiaDiem.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bitmap = stream.toByteArray();
            b.putByteArray("BITMAP",bitmap);
        }
        return b;
    }
}