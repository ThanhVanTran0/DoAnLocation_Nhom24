package com.example.thanh.doanlocation_nhom24;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Models.DiaDiem;
import Modules.BaseActivity;
import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.GooglePlacesReadTask;
import Modules.MyInfo;
import Modules.Route;

//Activity Trang chu
public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener, DirectionFinderListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton fBtnTimDuong, fBtnThemDiaDiem, fBtnMyLoacation;
    private Dialog dialog, dialog_chonBanKinh,dialog_timDuong;
    private GoogleMap googleMap;
    private double latitude = 0;
    private double longitude = 0;
    private int PROXIMITY_RADIUS = 5000; //Bán kính tìm kiếm
    private LocationManager locationManager;
    private SeekBar seekBarBanKinh;
    private LinearLayout lnChonDiaDiem;
    private Button cddBtnThoat,cddBtnOk;
    private EditText edDiaDiemXuatPhat,edDiaDiemDen;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int PLACE_AUTOCOMPLETE_DIA_DIEMXUAT_PHAT = 2;
    private final int PLACE_AUTOCOMPLETE_DIA_DIEM_DEN = 3;
    private final  int DIA_DIEM_DEN = 1;
    private final int DIA_DIEM_XUAT_PHAT = 2;
    private final int REQUEST_CODE_FROM_DIA_DIEM_CUA_BAN = 4;

    private LatLng DiaDiemXuatPhat = null;
    private LatLng DiaDiemDen = null;
    private Marker marker = null;
    private LatLng DiaDiemCuaToi = null;

    private int DIA_DIEM_DUOC_CHON = 0;


    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private Marker myMarker = null;
    private DiaDiem diaDiemCanThem = null;
    private GeoDataClient mGeoDataClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isGooglePlayServiceAvailable())
        {
            Toast.makeText(this, "Google Play Service Không hỗ trợ", Toast.LENGTH_SHORT).show();
            finish();
        }

        AnhXa();
        ThemSuKien();

        inItToolBar("");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void ThemSuKien() {
        //Add Nav drawer
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        fBtnTimDuong.setOnClickListener(this);
        fBtnThemDiaDiem.setOnClickListener(this);
        fBtnMyLoacation.setOnClickListener(this);
        cddBtnOk.setOnClickListener(this);
        cddBtnThoat.setOnClickListener(this);
        lnChonDiaDiem.setVisibility(View.INVISIBLE);
    }

    private void AnhXa() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fBtnThemDiaDiem = floatingActionMenu.findViewById(R.id.fabBtnThemDiaDiem);
        fBtnTimDuong = floatingActionMenu.findViewById(R.id.fabBtnTimDuong);
        fBtnMyLoacation = findViewById(R.id.fBtnMyLocation);
        lnChonDiaDiem = findViewById(R.id.lnChonDiaDiem);
        cddBtnOk = findViewById(R.id.cddBtnOk);
        cddBtnThoat = findViewById(R.id.cddBtnThoat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu, menu);
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
            case R.id.btnClose_CBK: {
                dialog_chonBanKinh.dismiss();
            }
            break;
            case R.id.btnDongY_CBK: {
                int BK = seekBarBanKinh.getProgress();
                if (BK == 0) {
                    Toast.makeText(this, "Bán kính tối thiểu là 1 KM", Toast.LENGTH_SHORT).show();
                } else {
                    PROXIMITY_RADIUS = BK * 1000;
                }
                dialog_chonBanKinh.dismiss();
            }
            break;
            case R.id.fabBtnThemDiaDiem: {
                if(diaDiemCanThem == null) {
                    Toast.makeText(this, "Bạn chưa chọn địa điểm. Hãy chọn một địa điểm được đánh dấu.", Toast.LENGTH_SHORT).show();
                }
                else {
                    floatingActionMenu.close(true);
                    Intent intent = new Intent(MainActivity.this,DiaDiemCuaBanActivity.class);
                    intent.putExtras(diaDiemCanThem.toBundle());
                    intent.putExtra("FROM",1);
                    startActivityForResult(intent,REQUEST_CODE_FROM_DIA_DIEM_CUA_BAN);
                    diaDiemCanThem = null;
                }
            }
            break;
            case R.id.fabBtnTimDuong: {
               ShowDialogTimDuong();
            }
            break;
            case R.id.fBtnMyLocation: {
                RequestUpdateLocation();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(DiaDiemCuaToi,14),1000,null);
                googleMap.setMyLocationEnabled(true);
            }
            break;
            case R.id.fBtnChonDiaDiemXuatPhat:
            {
                dialog_timDuong.hide();
                DIA_DIEM_DUOC_CHON = DIA_DIEM_XUAT_PHAT;
                TaoMarkerChonDiaDiemTrenBanDo();
            }
            break;
            case R.id.fBtnChonDiaDiemDen:
            {
                dialog_timDuong.hide();
                DIA_DIEM_DUOC_CHON = DIA_DIEM_DEN;
                TaoMarkerChonDiaDiemTrenBanDo();
            }
            break;
            case R.id.dialog_btnThoat:
            {
                dialog_timDuong.dismiss();
                if(DiaDiemXuatPhat != null) {
                    DiaDiemXuatPhat = null;
                }
                if(DiaDiemDen !=null) {
                    DiaDiemDen = null;
                }
            }
            break;
            case R.id.dialog_btnTimDuong:
            {
                if(DiaDiemXuatPhat == null || DiaDiemDen == null){
                    Toast.makeText(this, "Chưa chọn địa điểm",Toast.LENGTH_SHORT).show();
                }
                else {
                    TimDuongDi();
                    dialog_timDuong.dismiss();
                    DiaDiemDen = null;
                    DiaDiemXuatPhat = null;
                }
            }
            break;
            //            Cdd :Chọn địa điểm
            case R.id.cddBtnOk:
            {
                if(DIA_DIEM_DUOC_CHON == DIA_DIEM_DEN) {
                    DiaDiemDen = marker.getPosition();
                    edDiaDiemDen.setText("[Điểm Tọa Độ]");
                }
                if(DIA_DIEM_DUOC_CHON == DIA_DIEM_XUAT_PHAT) {
                    DiaDiemXuatPhat = marker.getPosition();
                    edDiaDiemXuatPhat.setText("[Điểm Tọa Độ]");
                }
                HienLaiDialogSauKhiChonDiaDiem();
            }
            break;
            case R.id.cddBtnThoat:
            {
                DIA_DIEM_DUOC_CHON = 0;
                HienLaiDialogSauKhiChonDiaDiem();
            }
            break;
            case R.id.edChonDiaDiemDen:
            {
                MoPlaceAutocomplete(PLACE_AUTOCOMPLETE_DIA_DIEM_DEN);
            }
            break;
            case R.id.edChonDiaDiemXuatPhat:
            {
                MoPlaceAutocomplete(PLACE_AUTOCOMPLETE_DIA_DIEMXUAT_PHAT);
            }
            break;
        }

    }

    private void HienLaiDialogSauKhiChonDiaDiem() {
        marker.remove();
        marker = null;
        lnChonDiaDiem.setVisibility(View.INVISIBLE);
        floatingActionMenu.setVisibility(View.VISIBLE);
        dialog_timDuong.show();
    }

    private void TaoMarkerChonDiaDiemTrenBanDo() {
        if(googleMap!=null) {
            floatingActionMenu.setVisibility(View.INVISIBLE);
            lnChonDiaDiem.setVisibility(View.VISIBLE);
            LatLng latLng = googleMap.getCameraPosition().target;
            if(marker !=null) {
                marker.remove();
            }
            marker = googleMap.addMarker(new MarkerOptions().position(latLng));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.choose_location));
        }
    }

    private void TimDuongDi() {
        try {
            new DirectionFinder(this,DiaDiemXuatPhat,DiaDiemDen).execute();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void ShowDialogTimDuong() {
        floatingActionMenu.close(true);
        dialog_timDuong = new Dialog(this);
        dialog_timDuong.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_timDuong.setContentView(R.layout.custom_dialog_tim_duong);
        dialog_timDuong.setCanceledOnTouchOutside(false);
        FloatingActionButton fBtnChonDiemXuatPhat = dialog_timDuong.findViewById(R.id.fBtnChonDiaDiemXuatPhat);
        FloatingActionButton fBtnChonDiemDen = dialog_timDuong.findViewById(R.id.fBtnChonDiaDiemDen);
        Button btnTimDuong = dialog_timDuong.findViewById(R.id.dialog_btnTimDuong);
        Button btnThoat = dialog_timDuong.findViewById(R.id.dialog_btnThoat);
        edDiaDiemXuatPhat = dialog_timDuong.findViewById(R.id.edChonDiaDiemXuatPhat);
        edDiaDiemDen = dialog_timDuong.findViewById(R.id.edChonDiaDiemDen);
        fBtnChonDiemDen.setOnClickListener(this);
        fBtnChonDiemXuatPhat.setOnClickListener(this);
        btnTimDuong.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
        edDiaDiemXuatPhat.setOnClickListener(this);
        edDiaDiemXuatPhat.setKeyListener(null);
        edDiaDiemDen.setOnClickListener(this);
        edDiaDiemDen.setKeyListener(null);
        dialog_timDuong.show();
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.itsearch){
            MoPlaceAutocomplete(PLACE_AUTOCOMPLETE_REQUEST_CODE);
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
                intent.putExtra("FROM",-1);
                startActivityForResult(intent,REQUEST_CODE_FROM_DIA_DIEM_CUA_BAN);
            }
            break;
            case R.id.navTimNhaHang: {
                TaoMarkerDiaDiem("restaurant");
            }
            break;
            case R.id.navTimCafe: {
                TaoMarkerDiaDiem("cafe");
            }
            break;
            case R.id.navThayDoiBanKinh: {
                ShowDialogChonBanKinh();
            }
            break;
            case R.id.navHDSD:
            {
                Intent intent = new Intent(MainActivity.this,HDSDActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.navXoaDiaDiem:
            {
                googleMap.clear();
            }
            break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void ShowDialogChonBanKinh() {
        dialog_chonBanKinh = new Dialog(this);
        dialog_chonBanKinh.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_chonBanKinh.setContentView(R.layout.dialog_chon_ban_kinh);
        dialog_chonBanKinh.setCanceledOnTouchOutside(false);
        Button btnClose = dialog_chonBanKinh.findViewById(R.id.btnClose_CBK);
        Button btnOk = dialog_chonBanKinh.findViewById(R.id.btnDongY_CBK);
        final TextView textView = dialog_chonBanKinh.findViewById(R.id.txtKMHienThi);
        textView.setText(String.valueOf(PROXIMITY_RADIUS/1000) + " KM");
        seekBarBanKinh = dialog_chonBanKinh.findViewById(R.id.sbBanKinh);
        seekBarBanKinh.setProgress(PROXIMITY_RADIUS / 1000);
        seekBarBanKinh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String KM = String.valueOf(i) +"KM";
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.thongtin_dialog_custom);
        dialog.setCanceledOnTouchOutside(false);
        Button btnQuayLai = (Button) dialog.findViewById(R.id.btnQuayLai);
        btnQuayLai.setOnClickListener(MainActivity.this);
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        Intent intent = getIntent();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(googleMap.getCameraPosition()!=null) {
                    if(marker!=null) {
                        if(googleMap.getCameraPosition() != null) {
                            marker.setPosition(googleMap.getCameraPosition().target);
                        }
                    }
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker !=null) {
                    diaDiemCanThem = (DiaDiem) marker.getTag();
                }
                return false;
            }
        });
        double lat = intent.getDoubleExtra("LAT",0);
        double lon = intent.getDoubleExtra("LON",0);
        DiaDiemCuaToi = new LatLng(lat,lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DiaDiemCuaToi, 13));
        googleMap.setMyLocationEnabled(true);
        RequestLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        DiaDiemCuaToi = new LatLng(latitude,longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
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

    private void RequestLocation() {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET
                    }, 10);
                }
            } else {
                RequestUpdateLocation();
            }
        }
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
        googlePlacesUrl.append("&opennow=true");
        googlePlacesUrl.append("&key=" + "AIzaSyD73ix-2OxsdM03JnoTj5gbxwbPRAJZSiM");
        Log.e("URL", googlePlacesUrl.toString());
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask(this);
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Toast.makeText(this, place.getName(), Toast.LENGTH_SHORT).show();
                googleMap.clear();
                LatLng latLng = place.getLatLng();
                getPhotos(place.getId());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(place.getName().toString() + "\n"+ place.getAddress().toString());
                DiaDiem diaDiem = new DiaDiem(null,latLng,place.getAddress().toString(),place.getName().toString());
                myMarker = googleMap.addMarker(markerOptions);
                myMarker.setTag(diaDiem);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13), 1500, null);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this, "Lỗi, cần chỉnh sửa", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_DIA_DIEM_DEN) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edDiaDiemDen.setText(place.getName());
                DiaDiemDen = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this, "Lỗi, cần chỉnh sửa", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_DIA_DIEMXUAT_PHAT) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edDiaDiemXuatPhat.setText(place.getName());
                DiaDiemXuatPhat = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this, "Lỗi, cần chỉnh sửa", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if(requestCode == REQUEST_CODE_FROM_DIA_DIEM_CUA_BAN) {
            if(resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                if(bundle!=null) {
                    double lat = bundle.getDouble("LAT");
                    double lon = bundle.getDouble("LON");
                    String Ten = bundle.getString("TEN");
                    String DiaChi = bundle.getString("DIACHI");
                    byte[] arr = bundle.getByteArray("BITMAP");
                    Bitmap bitmap = null;
                    if(arr !=null) {
                        bitmap = BitmapFactory.decodeByteArray(arr,0,arr.length);
                    }
                    DiaDiem diaDiem = new DiaDiem(bitmap,new LatLng(lat,lon),DiaChi,Ten);
                    googleMap.clear();
                    LatLng latLng = diaDiem.getLatLng();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(diaDiem.getTenDiaDiem() + "\n"+ diaDiem.getDiaChi());
                    myMarker = googleMap.addMarker(markerOptions);
                    myMarker.setTag(diaDiem);
                    googleMap.setInfoWindowAdapter(new MyInfo(this));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13), 1500, null);
                }
            }
        }

    }

    private void MoPlaceAutocomplete(int requestCode) {
        try {
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setCountry("VN").build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(this);
            startActivityForResult(intent,requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        googleMap.clear();

        for (Route route : routes) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            originMarkers.add(googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_start))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_end))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(13);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(googleMap.addPolyline(polylineOptions));
        }
    }

    private void getPhotos(String place_id) {
        final String placeId = place_id;
        final MyInfo info = new MyInfo(this);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();
                        DiaDiem diaDiem = (DiaDiem) myMarker.getTag();
                        diaDiem.setImgDiaDiem(bitmap);
                        googleMap.setInfoWindowAdapter(info);
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,this);
                return;
            }
        }
    }

    private void RequestUpdateLocation(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,this);
    }
}
