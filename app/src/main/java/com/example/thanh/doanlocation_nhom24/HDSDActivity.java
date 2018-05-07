package com.example.thanh.doanlocation_nhom24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Modules.BaseActivity;

public class HDSDActivity extends BaseActivity {

    private ArrayList<String> dsChucNang;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdsd);

        inItToolBar("Danh sách hướng dẫn");

        AnhXa();
        AddSuKien();

        dsChucNang = new ArrayList<>();
        dsChucNang.add("Tìm vị trí.");
        dsChucNang.add("Tìm nhà hàng, quán cafe quanh bạn");
        dsChucNang.add("Thay đổi phạm vi tìm");
        dsChucNang.add("Thêm địa điểm của bạn");
        dsChucNang.add("Tìm đường đi giữa 2 điểm");

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dsChucNang);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void AnhXa() {
        listView = findViewById(R.id.lvDanhSachHD);
    }

    private void AddSuKien() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               ActionSauKhiClick(i);
            }
        });
    }

    private void ActionSauKhiClick(int i) {
        Intent intent = new Intent(HDSDActivity.this,SliderActivity.class);
        int[] lst_image = new int[0];
        String[] lst_description = new String[0];
        switch (i) {
            case 0:
            {
                lst_image = new int[]{R.drawable.tim_vi_tri_1, R.drawable.tim_vi_tri_2, R.drawable.tim_vi_tri_3};
                lst_description = new String[]{"Chọn nút tìm kiếm", "Nhập tên địa điểm cần tìm, chọn địa điểm phù hợp", "Ứng dụng sẽ hiện địa điểm cho bạn"};
            }
            break;
            case 1:
            {
                lst_image = new int[]{R.drawable.tim_nh_cafe1,
                        R.drawable.tim_nh_cafe2,
                        R.drawable.tim_nh_cafe3};
                lst_description = new String[]{"Chọn nút danh sách",
                        "Chọn chức năng tìm nhà hàng hay cafe",
                        "Ứng dụn sẽ hiện địa điểm cho bạn"};
            }
            break;
            case 2:
            {
                lst_image = new int[]{R.drawable.tdpb_1,
                        R.drawable.tdpb_2,
                        R.drawable.tdpb_3};
                lst_description = new String[]{"Chọn nút danh sách",
                        "Chọn chức năng thay đổi phạm vi",
                        "Kéo chọn phạm vi và ấn OK"};
            }
            break;
            case 3:
            {
                lst_image = new int[]{
                        R.drawable.them_dia_diem_1,
                        R.drawable.them_dia_diem_2,
                        R.drawable.them_dia_diem_3,
                        R.drawable.them_dia_diem_4,
                        R.drawable.them_dia_diem_5,
                        R.drawable.them_dia_diem_6,
                        R.drawable.them_dia_diem_7,
                };
                lst_description = new String[]{
                        "Chọn một địa điểm trong các địa điểm gợi ý",
                        "Bấm vào để hiện thêm chức năng",
                        "Chọn thêm địa điểm",
                        "Địa điểm sẽ được thêm vào danh sách",
                        "Bấm vào một địa điểm có sẵn để hiện lên bản đồ",
                        "Địa điểm sẽ được hiển thị lên bản đồ",
                        "Nếu muốn xem danh sách chỉ cần vào danh sách, chọn chức năng địa điểm của bạn",
                };
            }
            break;
            case 4:
            {
                lst_image = new int[]{
                        R.drawable.tim_duong_di_1,
                        R.drawable.tim_duong_di_2,
                        R.drawable.tim_duong_di_3,
                        R.drawable.tim_duong_di_4,
                        R.drawable.tim_duong_di_5,
                        R.drawable.tim_duong_di_6,
                };
                lst_description = new String[]{
                        "Bấm vào + để hiện thêm chức năng",
                        "Chọn thêm chức năng tìm đường",
                        "Bấm vào các vị trí trên để hiện chức năng chọn vị trí",
                        "Kéo bản đồ để lá cờ vào địa điểm bạn muốn chọn",
                        "Bấm tìm đường",
                        "Ứng dụng sẽ hiển thị đường đi đến 2 điểm bạn chọn",
                };
            }
            break;
        }
        Bundle bundle = new Bundle();
        bundle.putIntArray("listImage",lst_image);
        bundle.putStringArray("list",lst_description);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
