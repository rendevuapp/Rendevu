package com.rendevu.main;
/**
 *   Ricardo Cantu
 *  This class holds the configurations for the tab view.
 *  This implements each of the three fragment.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main2Activity extends UncaughtExceptionActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_LOCATION = 34;


    //Button invite;

    //private InvitationClass sendInvite;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     *
     *referencing firebase for data storage
     *
     */
    public static final String TAG = Main2Activity.class.getSimpleName();
    private static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();



            /**
            *
            * Listener and Handler for send invite button
            * */
            /*invite = findViewById(R.id.sendInvites);

            invite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    InvitationClass Inv = new InvitationClass();
                    try {
                        Inv.onInviteClicked(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
/**
 *  @Override
 *  public void onFinishUserDialog(String addedName) {
 *
 *      //Contact name and Phone number added in dialog are passed here
 *      //to be inserted into firebase.
 *
 *      try {
 *          Toast.makeText(this, "ADDED: " + addedName, Toast.LENGTH_SHORT).show();
 *      } catch (Exception e) {
 *          e.printStackTrace();
 *      }
 *  }
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.menu_main2, menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Josh
     * Handler for sending invitation when send invite
     * button is clicked.
     *
     * */


    /*public void onInviteClicked(View v) {
        try {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, REQUEST_INVITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            String TAG = "";
            Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

            if (requestCode == REQUEST_INVITE) {
                if (resultCode == RESULT_OK) {
                    // Get the invitation IDs of all sent messages
                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + id);
                    }
                } else {
                    // Sending failed or it was canceled, show failure message to the user
                    // ...
                    Toast.makeText(getApplicationContext(), "Failed Invite!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
            
       



                              
    /**
     * Josh
     *****************
     * LOGOUT METHOD *
     *****************
     * (uses ExceptionClass)
     *
     * When logout button is pressed,
     * user is signed out of app, and sent back to the home screen.
     *
     * ExceptionClass with try/catch
     * handle exceptions thrown from trying to determine
     * if a valid user exists to log out of the app.
     * */
    /*public FirebaseAuthException(String errorCode, String message){

    }*/
    public void onLogoutClick(View vu) throws ExceptionClass, FirebaseAuthClass{
        try {
            isUserLoggedIn(); //first check if this user is actually signed in.

            FirebaseAuth.getInstance().signOut();  //also removes Firebase persistence until next login.

            //Toast.makeText(getApplicationContext(), "You Are Now Logged Out......Goodbye", Toast.LENGTH_SHORT).show();
        } catch (ExceptionClass e) {
            processError(e);
            //e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Logout error", Toast.LENGTH_SHORT).show();
        }finally{
            //executes whether a current user exists or not.
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
            finish();  //disables back button from navigating back into the app.
        }
    }

    private void isUserLoggedIn() throws ExceptionClass, FirebaseAuthClass{
        try{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No Users to Logout!", Toast.LENGTH_SHORT).show();
            }
        }catch (IllegalStateException e){

            throw new ExceptionClass(e.getMessage(),"ILLEGAL_STATE_EXCEPTION");
        }catch(NullPointerException e){

            throw new ExceptionClass(e.getMessage(),"NULL_POINTER_EXCEPTION");
        }
    }

    private static void processError(ExceptionClass e) throws ExceptionClass, FirebaseAuthClass {

        switch(e.getErrorCode()){
            case "NOT_SIGNED_IN_EXCEPTION":
                System.out.println("The user is not currently signed in.");
                throw e;
            case "ILLEGAL_STATE_EXCEPTION":
                System.out.println("State of current user cannot be determined.");
                throw e;
            case "NULL_POINTER_EXCEPTION":
                System.out.println("No value to be referenced.");
                break;
            default:
                System.out.println("Unknown exception occurred, writing to log."+e.getMessage());
                e.printStackTrace();
        }
    }
    /********************
     *END LOGOUT METHOD**
     ********************/



    /*
    * Josh
    *   Back button when already logged in will minimize the app.
    * */
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        this.moveTaskToBack(true);
    }








                              
    /**
     * Josh
     * Controls for the pop-up dialog fragment,
     * could probably be useful for something else
     *
     * */
    public void onClick(View view) {
        // close existing dialog fragments
        try {
            android.app.FragmentManager manager = getFragmentManager();
            android.app.Fragment frag = manager.findFragmentByTag("fragment_edit_name");
            if (frag != null) {
                manager.beginTransaction().remove(frag).commit();
            }
            switch (view.getId()) {
                case R.id.addContactActivityButton:
                    MyDialogFragment editNameDialog = new MyDialogFragment();
                    editNameDialog.show(manager, "fragment_edit_name");
                    break;

                //generic alert fragment
                case R.id.showAlertDialogFragment:
                    MyAlertDialogFragment alertDialogFragment = new MyAlertDialogFragment();
                    alertDialogFragment.show(manager, "fragment_edit_name");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE CONTACT TAB.
     *  Ricardo Cantu
     */
    public static class ContactTabFragment extends Fragment {
        FirebaseDatabase database;
        DatabaseReference databaseReference;

        RecyclerView recyclerView;
        FirebaseRecyclerAdapter<User, UserHolder> adapter;

        private List<User> list;

        public ContactTabFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //innDatabaseQuarryRef.orderByChild("InnerCircleMembers").
            //contRef = database.getReference("User");
            //contRef.addValueEventListener(new ValueEventListener() {
            //    @Override
            //    public void onDataChange(DataSnapshot dataSnapshot) {
            //        list = new ArrayList<>();
            //        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
            //            User value = dataSnapshot1.getValue(User.class);
            //            User fire = new User();
            //            String fullname = value.getFullName();
            //            String dob = value.getDOB();
            //            fire.setFullName(fullname);
            //            fire.setDOB(dob);
            //            list.add(fire);
            //        }
            //    }
            //    @Override
            //    public void onCancelled(DatabaseError databaseError) {
            //        Log.w("Error", "Failed to read Value.", databaseError.toException());
            //    }
            //});
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.contact_tab, container, false);


            database = FirebaseDatabase.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            databaseReference = database.getReference().child("UserData").child(uid).child("CircleMembers");
            Query query = databaseReference.orderByKey();

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);

            FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

            adapter = new FirebaseRecyclerAdapter<User, UserHolder>(recyclerOptions){

                @Override
                protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                    holder.setName(model.getFullName());
                    holder.setDOB(model.getDOB());
                    holder.setAvail(model.getAvail());

                    String avail = holder.setAvail(model.getAvail());

                    if(avail.equals("true")){
                      holder.setAvail("Available");
                    }
                    else if(avail.equals("false")) {
                        holder.setAvail("Not Available");
                    }
                }

                @Override
                public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent, false);
                    return new UserHolder(view);
                }
            };
            recyclerView.setAdapter(adapter);
            return rootView;
        }
        @Override
        public void onStart() {
            super.onStart();
            adapter.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            adapter.stopListening();
        }
    }





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
     */
    public static class MainScreenTabFragment extends Fragment implements OnMapReadyCallback {

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
        public MainScreenTabFragment() {
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

                rDatabase.addChildEventListener(new ChildEventListener() {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    int isInCircle = 0;
                    String circle;
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String available = dataSnapshot.child("avail").getValue(String.class);
                        MarkerOptions markerOptions = new MarkerOptions();

                        final String key = dataSnapshot.getKey();

                        //not needed, what was causing the crash has been removed.
                        //if(dataSnapshot.hasChild("Circle"))
                        rDatabase.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                isInCircle = 1;
                                final DatabaseReference cirRef = rDatabase.child(uid);
                                cirRef.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        circle = dataSnapshot.child("Circle").getValue(String.class);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if(available.equals("true") && isInCircle == 1 ) {
                            String lat = dataSnapshot.child("lat").getValue(String.class);
                            String lng = dataSnapshot.child("lng").getValue(String.class);
                            String displayName = dataSnapshot.child("fullname").getValue(String.class);
                            String circle = dataSnapshot.child("Circle").getValue(String.class);
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
            


      //  will test this area 
                        if(markers.containsValue(dataSnapshot.getKey())){
                            Marker marker = markers.get(dataSnapshot.getKey());
                            marker.remove();
                        }//removes previous marker
                              
                        String available = dataSnapshot.child("avail").getValue(String.class);
                        MarkerOptions markerOptions = new MarkerOptions();

                        final String key = dataSnapshot.getKey();
                        //not needed, what was causing the crash has been removed.
                        //if(dataSnapshot.hasChild("Circle"))
                        rDatabase.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange (DataSnapshot dataSnapshot){
                                isInCircle = 1;
                                final DatabaseReference cirRef = rDatabase.child(uid);
                                cirRef.orderByChild("CircleMembers").equalTo(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        circle = dataSnapshot.child("Circle").getValue(String.class);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
                           MainScreenTabFragment.super.getActivity().requestPermissions(
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                switch (position) {
                    case 0:
                        return new ContactTabFragment();
                    case 1:
                        return new MainScreenTabFragment();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                switch (position) {
                    case 0:
                        return "Contacts";
                    case 1:
                        return "Main";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
