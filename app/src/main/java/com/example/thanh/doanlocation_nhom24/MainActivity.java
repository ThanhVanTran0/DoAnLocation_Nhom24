package com.example.thanh.doanlocation_nhom24;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

//Activity Trang chu
public class MainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private MaterialSearchView searchView;
    private NavigationView navigationView;
    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton fBtnTimDuong, fBtnThemDiaDiem;
    private Dialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inItToolBar("");

        AnhXa();
        ThemSuKien();

    }

    private void ThemSuKien() {
        //Add Nav drawer
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//      Search view
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,"Moi submit: " + query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do something
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        fBtnTimDuong.setOnClickListener(this);
        fBtnThemDiaDiem.setOnClickListener(this);
    }

    private void AnhXa() {
        //Add Material Search
        searchView =(MaterialSearchView) findViewById(R.id.search_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fBtnThemDiaDiem = floatingActionMenu.findViewById(R.id.fabBtnThemDiaDiem);
        fBtnTimDuong =  floatingActionMenu.findViewById(R.id.fabBtnTimDuong);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu,menu);
        MenuItem item = menu.findItem(R.id.itsearch);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnQuayLai:
            {
                dialog.dismiss();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if(searchView.isSearchOpen())
            searchView.closeSearch();
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navThongtin:
            {
                CustomDialog();
            }
            break;
            case R.id.navPhanhoi:
            {
                Intent intent = new Intent(MainActivity.this,PhanHoiActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.navLocation:
            {
                Intent intent = new Intent(MainActivity.this,DiaDiemCuaBanActivity.class);
                startActivity(intent);
            }
            break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void CustomDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.thongtin_dialog_custom);
        dialog.setCanceledOnTouchOutside(false);
        Button btnQuayLai = (Button) dialog.findViewById(R.id.btnQuayLai);
        btnQuayLai.setOnClickListener(MainActivity.this);
        dialog.show();
    }
}
