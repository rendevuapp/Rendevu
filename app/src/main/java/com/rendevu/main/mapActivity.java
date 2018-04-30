package com.rendevu.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    public  class mapActivity extends Fragment implements OnMapReadyCallback {

        private static final int PERMISSION_REQUEST_LOCATION = 34;
        private FirebaseDatabase mFireBaseDatabase;
        public static final String TAG = Main2Activity.class.getSimpleName();
        private static final int REQUEST_INVITE = 0;  //used for sending invites
        private static GoogleApiClient mGoogleApiClient;

        private FirebaseAuth auth;
        FirebaseDatabase database;
        Marker marker;
        MapView mapView;
        private List<Tracking> list;
        ToggleButton saveLocButton;
        Button refreshButton;

        private FusedLocationProviderClient mFusedLocationClient;
        final double defaultLatitude = 29.424503;
        final double defaultLongtitude = -98.491500;

        private static Double latitude, longtitude;
        protected Location mLastLocation;
        //LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        private DatabaseReference mDatabase, rDatabase;

        @SuppressLint("ValidFragment")
        public mapActivity() {
        }


        @Override
        public void onCreate(Bundle savedInstanceState) throws NullPointerException {
            try {

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(super.getActivity());
                super.onCreate(savedInstanceState);
                database = FirebaseDatabase.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData").child(uid);
                rDatabase = FirebaseDatabase.getInstance().getReference().child("UserData");

                if(!checkPermission()){
                    requestPermissions();

                }else{
                    getLastLocation();
                }
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
                        if(!checkPermission()){
                            requestPermissions();

                        }else{
                            getLastLocation();
                        }
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
         * Return the current state of the permissions needed.
         */
        private boolean checkPermission() {
            int permissionState = ActivityCompat.checkSelfPermission(super.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            return permissionState == PackageManager.PERMISSION_GRANTED;
        }

        private void startLocationPermissionRequest() {
            ActivityCompat.requestPermissions(super.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }

        private void requestPermissions() {
            boolean shouldProvideRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(super.getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);

            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.");

                showSnackbar(R.string.permission_rationale, android.R.string.ok,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Request permission
                                startLocationPermissionRequest();
                            }
                        });

            } else {
                Log.i(TAG, "Requesting permission");
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                startLocationPermissionRequest();
            }
        }

        /**
         * Callback received when a permissions request has been completed.
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            Log.i(TAG, "onRequestPermissionResult");
            if (requestCode == PERMISSION_REQUEST_LOCATION) {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    getLastLocation();
                } else {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
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

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        LinearLayout info = new LinearLayout(getContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getContext());
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getContext());
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);
                        return info;
                    }
                });

                rDatabase.addChildEventListener(new ChildEventListener() {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    int isInCircle = 0;
                    String circle;
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String available = dataSnapshot.child("avail").getValue(String.class);
                        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green_scaled));

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
                        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green_scaled));

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
   
        private void requestLocationPermission() {
               // Permission has not been granted and must be requested.
               if (ActivityCompat.shouldShowRequestPermissionRationale(super.getActivity(),
                       Manifest.permission.ACCESS_FINE_LOCATION)) {
                   // Provide an additional rationale to the user if the permission was not granted
                   // and the user would benefit from additional context for the use of the permission.
                   // Display a SnackBar with a button to request the missing permission.
                   Snackbar.make(super.getView(), "Location Access is required to display markers  ",
                           Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           // Request the permission
                           mapActivity.super.getActivity().requestPermissions(
                                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                   PERMISSION_REQUEST_LOCATION);
                       }
                   }).show();

               } else {

                   Snackbar.make(super.getView(),
                           "Permission is not available. Requesting Location permission.",
                           Snackbar.LENGTH_SHORT).show();
                   // Request the permission. The result will be received in onRequestPermissionResult().
                   super.getActivity().requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                           PERMISSION_REQUEST_LOCATION);
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
