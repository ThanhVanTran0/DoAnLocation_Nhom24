package com.example.thanh.doanlocation_nhom24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class PhanHoiActivity extends BaseActivity {

    private EditText edEmail,edNoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_hoi);
        inItToolBar("");

        AnhXa();
        AddSuKien();
    }

    private void AddSuKien() {
    }

    private void AnhXa() {
        edEmail = findViewById(R.id.edEmail);
        edNoiDung = findViewById(R.id.edNoiDungPhanHoi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phan_hoi_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.itPhanHoi: {
                Toast.makeText(this, "Vừa mới bấm gửi", Toast.LENGTH_SHORT).show();
            }
            break;
            case android.R.id.home:
            {
                onBackPressed();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
