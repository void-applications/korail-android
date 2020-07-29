package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class ShowPlace extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    String TAG = "주요장소 맵 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    private void findLocation(String location) {
//
//        List<Address> addressList = null;
//
//        if (!location.equals("")) {
//            Geocoder geocoder = new Geocoder(this);
//
//            try {
//
//                addressList = geocoder.getFromLocationName(location, 5);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (addressList != null) {
//
//                if (addressList.size() != 0) {
//
//                    Address address = addressList.get(0);
//
//                    LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
//                    map.addMarker(new MarkerOptions().position(latlng).title(location));
//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
//
//                } else {
//
//                    Toast.makeText(this, "다시 검색해 주세요", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97);






        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}