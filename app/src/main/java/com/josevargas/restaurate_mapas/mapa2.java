package com.josevargas.restaurate_mapas;

import android.app.Activity;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class mapa2 extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DataBaseManager manager;

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = mapa.class.getSimpleName();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;

    private ListView lista;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_mapa2,container, false);

        lista = (ListView) mLinearLayout.findViewById(R.id.lista);

        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        String[] from = new String[] {manager.CN_NAME,manager.CN_LAT,manager.CN_LON};
        int[] to = new int[] {android.R.id.text1,android.R.id.text2,android.R.id.text2+1};
        cursor = manager.cargarCursor();

        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_spinner_item,cursor,from,to,0);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                manager = new DataBaseManager(getActivity());
                List<String> sede = new ArrayList<String>();
                List<String> latitud = new ArrayList<String>();
                List<String> longitud = new ArrayList<String>();
                Cursor c = manager.cargarCursor();
                if(c.moveToFirst()){
                    do{
                        sede.add(c.getString(1));
                        latitud.add(c.getString(2));
                        longitud.add(c.getString(3));
                    }while (c.moveToNext());
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitud.get(position)),Double.parseDouble(longitud.get(position))), 13));

                //Toast.makeText(getActivity(), sede.get(position)+ "\n" +"("+ latitud.get(position)+ "," + longitud.get(position)+")", Toast.LENGTH_SHORT).show();
            }
        });

        return mLinearLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null){
            getFragmentManager().beginTransaction().remove(f).commit();
        }
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null){
            getFragmentManager().beginTransaction().remove(f).commit();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        manager = new DataBaseManager(getActivity());
        List<String> sede = new ArrayList<String>();
        List<String> latitud = new ArrayList<String>();
        List<String> longitud = new ArrayList<String>();
        Cursor c = manager.cargarCursor();
        if(c.moveToFirst()){
            do{
                sede.add(c.getString(1));
                latitud.add(c.getString(2));
                longitud.add(c.getString(3));
            }while (c.moveToNext());
        }
        for(int i =0;i<sede.size();i++) {
            //Toast.makeText(getApplicationContext(),sede.get(i)+ "\n" +"("+ latitud.get(i)+ "," + longitud.get(i)+")", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getActivity(), getResources().getString(R.string.nsedes) + ": " + sede.size(), Toast.LENGTH_SHORT).show();

       /* List<String> sede = Arrays.asList("Londres", "Medellin", "Paris");
        List<String> latitud = Arrays.asList("51.507351", "6.270530", "48.856614");
        List<String> longitud = Arrays.asList("-0.127758", "-75.572120", "2.352222");
        */

        for(int i =0;i<sede.size();i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(latitud.get(i)), Float.parseFloat(longitud.get(i)))).title(sede.get(i)).snippet("("+latitud.get(i)+","+longitud.get(i)+")")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            //mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(latitud.get(i)), Float.parseFloat(longitud.get(i)))).title(sede.get(i)));
        }

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.268141,-75.567237), 11));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        mMap.clear();
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        setUpMap();
        mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title(getResources().getString(R.string.usuario)).snippet("(" + Double.toString(currentLatitude) + "," + Double.toString(currentLongitude) + ")")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 13));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
}
