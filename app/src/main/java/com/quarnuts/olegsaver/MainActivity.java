package com.quarnuts.olegsaver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quarnuts.olegsaver.api.model.GetHospitalInfo;
import com.quarnuts.olegsaver.api.model.Hospital;
import com.quarnuts.olegsaver.api.model.LoginInformation;
import com.quarnuts.olegsaver.api.model.ProfileResponse;
import com.quarnuts.olegsaver.api.retrofit.UserClient;
import com.quarnuts.olegsaver.ui.login.LoginActivity;

import java.util.Objects;

import alirezat775.lib.kesho.Kesho;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.quarnuts.olegsaver.api.Api.BASE_URL;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private GoogleMap mMap;

    protected LocationManager locationManager;

    public String latitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String longitude;
    Kesho kesho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        kesho = new Kesho(this, Kesho.SHARED_PREFERENCES_MANAGER, Kesho.Encrypt.NONE_ENCRYPT, "_hello_world_secret_key_");

        //Создание транзакции


        //Включение ретрофита


        //Логинимся
        if (!kesho.has("token")) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Запрос разрешения
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.format("%.6f", location.getLatitude());
        longitude = String.format("%.6f", location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


}
