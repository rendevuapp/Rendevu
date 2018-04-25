package com.rendevu.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.NullPointerException;


    /*
    *
    * Notes for Map:
    * -coordinates update when availability button is pressed
    * -location marker needs to refresh with change in user location(blue dot)
    * -old location marker needs to be deleted whenever a new marker is created,
    *       old marker is currently deleted when unavailable button is pressed.
    *
    * -whenever a friend sets them self as available, the other friend's
    *       screen snaps to their location
    *
    * -need to set status as unavailable when user logs out to remove marker.
    *           *update* user's marker removed on logout, but persists on friend's
    *           screen until they log out.
    * */

    /**
     *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE MAIN SCREEN TAB.
     *  Tamim Alekozai
     *
     */
    public  class mapFragment extends Fragment implements OnMapReadyCallback {

        FirebaseDatabase database;
        Marker marker;
        MapView mapView;
        private List<Tracking> list;
        ToggleButton saveLocButton;
        Button refreshButton;
        public static final String TAG = Main2Activity.class.getSimpleName();
        private FusedLocationProviderClient mFusedLocationClient;
        final double defaultLatitude = 29.424503;
        final double defaultLongtitude = -98.491500;
        private static final int PERMISSION_REQUEST_LOCATION = 34;
        private static Double latitude, longtitude;
        protected Location mLastLocation;
        //LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        private static GoogleApiClient mGoogleApiClient;
        private DatabaseReference mDatabase, rDatabase;

        @SuppressLint("ValidFragment")
        public mapFragment() {
        }


        @Override
        public void onCreate(Bundle savedInstanceState) throws NullPointerException {
            try {

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(super.getActivity());
                super.onCreate(savedInstanceState);
               checkPermissions();
                database = FirebaseDatabase.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData").child(uid);
                rDatabase = FirebaseDatabase.getInstance().getReference().child("UserData");

                    getLastLocation();

            } catch (NullPointerException e) {
                System.out.print("method: getUid is trying to reference a null pointer.");
                e.printStackTrace();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            try {
                rootView = inflater.inflate(R.layout.mainscreen_tab, container, false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

                saveLocButton = (ToggleButton) rootView.findViewById(R.id.toggleButton2);

                saveLocButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        String sLat, sLng;
                        if(isChecked){
                            if(latitude != null && longtitude != null){
                                sLat = Double.toString(latitude);
                                sLng = Double.toString(longtitude);
                                }
                            else{
                                sLat = Double.toString(defaultLatitude);
                                sLng = Double.toString(defaultLongtitude);
                            }
                            DatabaseReference newPush = mDatabase;
                            newPush.child("avail").setValue("true");
                            newPush.child("lat").setValue(sLat);
                            newPush.child("lng").setValue(sLng);
                        }
                        else{
                            DatabaseReference newPush = mDatabase;
                            newPush.child("avail").setValue("false");
                        }
                    }
                });

                mapView = (MapView) rootView.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootView;
        }


        @SuppressWarnings("MissingPermission")
        private void getLastLocation() {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(super.getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();

                                latitude= mLastLocation.getLatitude();
                                longtitude= mLastLocation.getLongitude();
                            } else {
                                Log.w(TAG, "getLastLocation:exception", task.getException());
                                showSnackbar(getString(R.string.no_location_detected));
                            }
                        }
                    });
        }

        /**
         * Shows a {@link Snackbar} using {@code text}.
         *
         * @param text The Snackbar text.
         */
        private void showSnackbar(final String text) {
            View container = super.getView().findViewById(R.id.appbar);
            if (container != null) {
                Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
            }
        }

        /**
         * Shows a {@link Snackbar}.
         *
         * @param mainTextStringId The id for the string resource for the Snackbar text.
         * @param actionStringId   The text of the action item.
         * @param listener         The listener associated with the Snackbar action.
         */
        private void showSnackbar(final int mainTextStringId, final int actionStringId,
                                  View.OnClickListener listener) {
            Snackbar.make(super.getView().findViewById(android.R.id.content),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();
        }

        /**
         * permissions request code
         */
        private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

        /**
         * Permissions that need to be explicitly requested from end user.
         */
        private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };


        /**
         * Checks the dynamically-controlled permissions and requests missing permissions from end user.
         */
        protected void checkPermissions() {
            final List<String> missingPermissions = new ArrayList<String>();
            // check all required dynamic permissions
            for (final String permission : REQUIRED_SDK_PERMISSIONS) {
                final int result = ContextCompat.checkSelfPermission(super.getContext(), permission);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission);
                }
            }
            if (!missingPermissions.isEmpty()) {
                // request all missing permissions
                final String[] permissions = missingPermissions
                        .toArray(new String[missingPermissions.size()]);
                ActivityCompat.requestPermissions(super.getActivity(), permissions, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
                onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                        grantResults);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                               @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQUEST_CODE_ASK_PERMISSIONS:
                    for (int index = permissions.length - 1; index >= 0; --index) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                            // exit the app if one permission is not granted

                            Toast.makeText(super.getContext(), "Required permission '" + permissions[index]
                                    + "' not granted, exiting", Toast.LENGTH_LONG).show();

                            return;
                        }
                    }
                    // all permissions were granted
                    ;
                    break;
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {

                final GoogleMap mMap = googleMap;
                final Map<String,Marker> markers = new HashMap();
                mMap.setMyLocationEnabled(true);

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                rDatabase.addChildEventListener(new ChildEventListener() {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    int isInCircle = 0;
                    String circle;
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String available = dataSnapshot.child("avail").getValue(String.class);
                        MarkerOptions markerOptions = new MarkerOptions();

                        String key = dataSnapshot.getKey();

                        rDatabase.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                circle = dataSnapshot.child("Circle").getValue(String.class);
                                isInCircle = 1;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if(available.equals("true") && isInCircle == 1 ) {
                            String lat = dataSnapshot.child("lat").getValue(String.class);
                            String lng = dataSnapshot.child("lng").getValue(String.class);
                            String displayName = dataSnapshot.child("fullname").getValue(String.class);
                            String circle = dataSnapshot.child("Circle").getValue().toString();
                            Double dLat = Double.parseDouble(lat);
                            Double dLng = Double.parseDouble(lng);

                            LatLng newLocation = new LatLng(dLat, dLng);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                            markerOptions.position(newLocation);
                            markerOptions.title(displayName);
                            markerOptions.snippet(circle);
                            Marker mMarker = mMap.addMarker(markerOptions);
                            markers.put(dataSnapshot.getKey(), mMarker);
                        }else if(markers.containsValue(dataSnapshot.getKey())){
                            Marker marker = markers.get(dataSnapshot.getKey());
                            marker.remove();
                        }//added to remove marker if unavailable
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            


      //will test this area 
//                      if(markers.containsValue(dataSnapshot.getKey())){
//                          Marker marker = markers.get(dataSnapshot.getKey());
//                          marker.remove();
//                      }//removes previous marker
                              
                        String available = dataSnapshot.child("avail").getValue(String.class);
                        MarkerOptions markerOptions = new MarkerOptions();

                        String key = dataSnapshot.getKey();
                        rDatabase = database.getReference().child("UserData").child(uid);

                        if(dataSnapshot.hasChild("Circle"))
                        rDatabase.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange (DataSnapshot dataSnapshot){
                                circle = dataSnapshot.child("Circle").getValue().toString();
                                isInCircle = 1;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        if (markers.containsKey(dataSnapshot.getKey())) {
                            markers.get(dataSnapshot.getKey()).remove();
                        }
                        if (available.equals("true") && isInCircle == 1) {
                            String lat = dataSnapshot.child("lat").getValue(String.class);
                            String lng = dataSnapshot.child("lng").getValue(String.class);
                            String displayName = dataSnapshot.child("fullname").getValue(String.class);
                            Double dLat = Double.parseDouble(lat);
                            Double dLng = Double.parseDouble(lng);

                            LatLng newLocation = new LatLng(dLat, dLng);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

                            markerOptions.position(newLocation);
                            markerOptions.title(displayName);
                            markerOptions.snippet(circle);
                            Marker mMarker = mMap.addMarker(markerOptions);
                            markers.put(dataSnapshot.getKey(), mMarker);
                        }else if(markers.containsValue(dataSnapshot.getKey())){
                            Marker marker = markers.get(dataSnapshot.getKey());
                            marker.remove();
                        }//added to remove marker if unavailable
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(markers.containsValue(dataSnapshot.getKey())){
                            Marker marker = markers.get(dataSnapshot.getKey());
                            marker.remove();
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onResume() {
            try {
                super.onResume();
                mapView.onResume();
                mGoogleApiClient.connect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPause() {
            try {
                super.onPause();
                mapView.onPause();
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            try {
                super.onDestroy();
                mapView.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            try {
                super.onSaveInstanceState(outState);
                mapView.onSaveInstanceState(outState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLowMemory() {
            try {
                super.onLowMemory();
                mapView.onLowMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
