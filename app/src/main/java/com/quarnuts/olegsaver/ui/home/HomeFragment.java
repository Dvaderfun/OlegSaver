package com.quarnuts.olegsaver.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alirezat775.lib.kesho.Kesho;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quarnuts.olegsaver.R;
import com.quarnuts.olegsaver.ui.login.LoginActivity;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.text_home);
        Kesho kesho = new Kesho(getContext(), Kesho.SHARED_PREFERENCES_MANAGER, Kesho.Encrypt.NONE_ENCRYPT, "_hello_world_secret_key_");

        textView.setText(kesho.pull("role", "Paramedic"));

        root.findViewById(R.id.exit).setOnClickListener(view -> {
            kesho.clear();
            getActivity().finishAffinity();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        /*mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);*/

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* map = googleMap;

        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Updates the location and zoom of the MapView

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(49.840105, 24.02), 10);
        map.animateCamera(cameraUpdate);*/
    }
}