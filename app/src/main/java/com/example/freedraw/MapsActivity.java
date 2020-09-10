package com.example.freedraw;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context context;
    List<LatLng> arraylistoflatlng;
    List<Polyline> polylineList;

    Projection projection;
    public double latitude;
    public double longitude;


    FrameLayout fram_map;
    Button btn_draw_State;
    Boolean Is_MAP_Moveable = false;
    private Polyline line;
    private Polygon polygon;

    boolean available = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = MapsActivity.this;

        fram_map = findViewById(R.id.fram_map);
        btn_draw_State = findViewById(R.id.btn_draw_State);


        btn_draw_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Is_MAP_Moveable = !Is_MAP_Moveable;
            }
        });
        arraylistoflatlng = new ArrayList<>();
        polylineList = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                int x_co = Math.round(x);
                int y_co = Math.round(y);

                projection = mMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);

                LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                int eventaction = event.getAction();

                if(available){
                    //clear the previous polygon first. Write code here
                    available = false;
                }

                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        // finger touches the screen
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // finger moves on the screen
                        arraylistoflatlng.add(new LatLng(latitude, longitude));
                        PolylineOptions rectOptions = new PolylineOptions()
                                .addAll(arraylistoflatlng)
                                .geodesic(true)
                                .color(Color.BLACK);
                        line = mMap.addPolyline(rectOptions);
                        polylineList.add(line);
//                        line.setJointType(JointType.ROUND);
                        break;
                    case MotionEvent.ACTION_UP:
                        // finger leaves the screen
                        polygon = mMap.addPolygon(new PolygonOptions()
                                .addAll(arraylistoflatlng)
                                .fillColor(Color.argb(100, 20, 137, 238)));
                        available = true;
                        break;
                }
                return Is_MAP_Moveable;
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
}