package com.example.thanh.doanloacation_nhom24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//Activity Trang chu
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cvRestaurant,cvCafe,cvHospital,cvPark,cvAtm,cvGasStation;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        ThemSuKien();

        toolbar.setTitle("Nhóm 24");
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void ThemSuKien() {
        cvGasStation.setOnClickListener(this);
        cvCafe.setOnClickListener(this);
        cvPark.setOnClickListener(this);
        cvHospital.setOnClickListener(this);
        cvRestaurant.setOnClickListener(this);
        cvAtm.setOnClickListener(this);
    }

    private void AnhXa() {
        toolbar =(Toolbar) findViewById(R.id.toolBarTrangChu);
        cvRestaurant = (CardView) findViewById(R.id.cvRestaurant);
        cvCafe = (CardView) findViewById(R.id.cvCafe);
        cvHospital = (CardView) findViewById(R.id.cvHospital);
        cvPark = (CardView) findViewById(R.id.cvPark);
        cvAtm = (CardView) findViewById(R.id.cvAtm);
        cvGasStation = (CardView) findViewById(R.id.cvGasStation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu,menu);

        MenuItem searchview = menu.findItem(R.id.itSearch);
        SearchView search = (SearchView) searchview.getActionView();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Bắt sự kiện click item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cvAtm:
            {
                Toast.makeText(this, "cvATM", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.cvCafe:
                Toast.makeText(this, "cvCafe", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cvGasStation:
                Toast.makeText(this, "cvGas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cvHospital:
                Toast.makeText(this, "cvHospital", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cvPark:
                Toast.makeText(this, "cvPark", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cvRestaurant:
                Toast.makeText(this, "cvRestaurant", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
