package com.example.thanh.doanlocation_nhom24;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

//Activity Trang chu
public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private MaterialSearchView searchView;
    private NavigationView navigationView;
    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton fBtnTimDuong, fBtnThemDiaDiem, fBtnMyLoacation;
    private Dialog dialog,dialog_chonBanKinh;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private double latitude = 0;
    private double longitude = 0;
    private int PROXIMITY_RADIUS = 5000; //Bán kính tìm kiếm
    private LocationManager locationManager;
    private SeekBar seekBarBanKinh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inItToolBar("");

        AnhXa();
        ThemSuKien();

        if (!isGooglePlayServiceAvailable())
            finish();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void ThemSuKien() {
        //Add Nav drawer
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//      Search view

        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TaoMarkerDiaDiem(query);
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
        fBtnMyLoacation.setOnClickListener(this);
    }

    private void AnhXa() {
        //Add Material Search
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fBtnThemDiaDiem = floatingActionMenu.findViewById(R.id.fabBtnThemDiaDiem);
        fBtnTimDuong = floatingActionMenu.findViewById(R.id.fabBtnTimDuong);
        fBtnMyLoacation = findViewById(R.id.fBtnMyLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu, menu);
        MenuItem item = menu.findItem(R.id.itsearch);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
//            Close dialog
            case R.id.btnQuayLai: {
                dialog.dismiss();
            }
            break;
            case R.id.btnClose_CBK:
            {
                dialog_chonBanKinh.dismiss();
            }
            break;
            case R.id.btnDongY_CBK:
            {
                int BK = seekBarBanKinh.getProgress();
                if(BK == 0) {
                    Toast.makeText(this, "Bán kính tối thiểu là 1 KM", Toast.LENGTH_SHORT).show();
                }
                else {
                    PROXIMITY_RADIUS = BK*1000;
                }
                dialog_chonBanKinh.dismiss();
            }
            break;
            case R.id.fabBtnThemDiaDiem: {
                Toast.makeText(this, "Them dia diem", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.fabBtnTimDuong: {
                Toast.makeText(this, "Tim duong", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.fBtnMyLocation: {
                Location location = getLocation();
                if (location == null) {
                    Toast.makeText(this, "Đang tìm địa điểm", Toast.LENGTH_SHORT).show();
                } else {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    googleMap.setMyLocationEnabled(true);

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13),1500,null);
                }
            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (searchView.isSearchOpen())
            searchView.closeSearch();
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navThongtin: {
                CustomDialog();
            }
            break;
            case R.id.navPhanhoi: {
                Intent intent = new Intent(MainActivity.this, PhanHoiActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.navLocation: {
                Intent intent = new Intent(MainActivity.this, DiaDiemCuaBanActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.navTimNhaHang:
            {
                TaoMarkerDiaDiem("restaurant");
            }
            break;
            case R.id.navTimCafe:
            {
                TaoMarkerDiaDiem("cafe");
            }
            break;
            case R.id.navThayDoiBanKinh:
            {
                ShowDialogChonBanKinh();
            }
            break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void ShowDialogChonBanKinh() {
        dialog_chonBanKinh = new Dialog(this);
        dialog_chonBanKinh.setContentView(R.layout.dialog_chon_ban_kinh);
        dialog_chonBanKinh.setCanceledOnTouchOutside(false);
        Button btnClose = dialog_chonBanKinh.findViewById(R.id.btnClose_CBK);
        Button btnOk = dialog_chonBanKinh.findViewById(R.id.btnDongY_CBK);
        final TextView textView = dialog_chonBanKinh.findViewById(R.id.txtKMHienThi);
        seekBarBanKinh = dialog_chonBanKinh.findViewById(R.id.sbBanKinh);
        seekBarBanKinh.setProgress(PROXIMITY_RADIUS/1000);
        seekBarBanKinh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String KM = String.valueOf(i) + " KM";
                textView.setText(KM);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnClose.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        dialog_chonBanKinh.show();
    }

    private void CustomDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.thongtin_dialog_custom);
        dialog.setCanceledOnTouchOutside(false);
        Button btnQuayLai = (Button) dialog.findViewById(R.id.btnQuayLai);
        btnQuayLai.setOnClickListener(MainActivity.this);
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        Location location = getLocation();
        if(location == null) {
            Toast.makeText(this, "Không tìm được vị trí của bạn", Toast.LENGTH_SHORT).show();
        }
        else {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public boolean isGooglePlayServiceAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private Location getLocation() {
        if (googleMap != null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        200);
            } else {
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null)
                    return location;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            }
        }
        return null;
    }

    private void TaoMarkerDiaDiem(String query){
        latitude = googleMap.getMyLocation().getLatitude();
        longitude = googleMap.getMyLocation().getLongitude();
        String type = query;
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDviXBV3UhvjTvNkDrZvv5i9sg_9Ekxwuo");
        //Mượn tạm api của thắng
        String a = googlePlacesUrl.toString();
        Log.e("URL", googlePlacesUrl.toString());
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }
}
