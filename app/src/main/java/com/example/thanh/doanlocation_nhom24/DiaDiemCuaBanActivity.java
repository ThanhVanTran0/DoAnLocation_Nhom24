package com.example.thanh.doanlocation_nhom24;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Adapter.CustomAdapter;
import Models.DiaDiem;
import Modules.BaseActivity;

public class DiaDiemCuaBanActivity extends BaseActivity {
    private ArrayList<DiaDiem> diaDiems;
    private ListView listView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_diem_cua_ban);

        inItToolBar("Địa điểm của bạn");

        AnhXa();
        AddSuKien();
        // TODO: 5/6/2018 Doc tu file len
        diaDiems = new ArrayList<>();

        Intent intent = getIntent();
        int from = intent.getIntExtra("FROM",-1);
        if(from == 1 ){
            Bundle bundle = intent.getExtras();
            if(bundle!=null) {
                double lat = bundle.getDouble("LAT");
                double lon = bundle.getDouble("LON");
                String Ten = bundle.getString("TEN");
                String DiaChi = bundle.getString("DIACHI");
                byte[] arr = bundle.getByteArray("BITMAP");
                Bitmap bitmap = null;
                if(arr !=null) {
                    bitmap = ArrayByteToBitmap(arr);
                }
                DiaDiem diaDiem = new DiaDiem(bitmap,new LatLng(lat,lon),DiaChi,Ten);
                diaDiems.add(diaDiem);
            }
        }
        diaDiems.add(new DiaDiem(null,new LatLng(0,0),"HCM1","HCM"));
        diaDiems.add(new DiaDiem(null,new LatLng(0,0),"HCM2","HCM"));
        diaDiems.add(new DiaDiem(null,new LatLng(0,0),"HCM3","HCM"));
        adapter = new CustomAdapter(this,R.layout.layout_custom_listview_ddut,diaDiems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void AnhXa() {
        listView = (ListView) findViewById(R.id.lvDiaDiemUaThich);
    }

    private void AddSuKien() {
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaDiem diaDiem = diaDiems.get(i);
                showDialogThonBaoTaoDiaDiem(diaDiem);
            }
        });
    }

    private void showDialogThonBaoTaoDiaDiem(final DiaDiem diaDiem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn hiện thị điểm này lên bản đồ");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 5/7/2018 Truyen du lieu va gui ve
                Intent data = new Intent();
                data.putExtras(diaDiem.toBundle());
                setResult(Activity.RESULT_OK,data);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Bitmap ArrayByteToBitmap(byte[] arr) {
        return BitmapFactory.decodeByteArray(arr,0,arr.length);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dia_diem_cua_ban_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.mn_Xoa) {
            int count = listView.getChildCount();
            CheckBox checkBox;
            View v;
            for(int i=count-1;i>=0;i--) {
                v = listView.getChildAt(i);
                checkBox = v.findViewById(R.id.cbXoa);
                if(checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    diaDiems.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }

}
