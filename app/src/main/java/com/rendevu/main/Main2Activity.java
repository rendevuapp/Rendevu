package com.rendevu.main;
/**
 *   Ricardo Cantu
 *  This class holds the configurations for the tab view.
 *  This implements each of the three fragment.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.NullPointerException;


//import javax.xml.crypto.Data;


public class Main2Activity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_LOCATION = 34;

    String s = "Null pointer exception for user.getUid()";



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
    private FirebaseDatabase mFireBaseDatabase;
    public static final String TAG = Main2Activity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;  //used for sending invites
    private static GoogleApiClient mGoogleApiClient;

    private FirebaseAuth auth;

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
    public void onInviteClicked(View v) {
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
    }
    @Override
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
    }
            

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
    public void onLogoutClick(View vu) throws ExceptionClass{
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
            
       

    private void isUserLoggedIn() throws ExceptionClass{
        try{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No Users to Logout!", Toast.LENGTH_SHORT).show();
            }
        }catch(IllegalStateException e){

            throw new ExceptionClass(e.getMessage(),"ILLEGAL_STATE_EXCEPTION");
        }catch(NullPointerException e){

            throw new ExceptionClass(e.getMessage(),"NULL_POINTER_EXCEPTION");
        }
    }

    private static void processError(ExceptionClass e) throws ExceptionClass {

        switch(e.getErrorCode()){
            case "ILLEGAL_STATE_EXCEPTION":
                System.out.println("State of current user cannot be determined.");
                throw e;
            case "NULL_POINTER_EXCEPTION":
                System.out.println("No users are currently logged in.");
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
                    AddUserToCircleDialog addUserToCircleDialog = new AddUserToCircleDialog();
                    addUserToCircleDialog.show(manager, "Add_Contact");
                    break;

                //generic alert fragment
                case R.id.showAlertDialogFragment:
                    MyAlertDialogFragment myAlertDialogFragment = new MyAlertDialogFragment();
                    myAlertDialogFragment.show(manager, "Logout");
                    break;
                //
                case R.id.sendCodeButton:
                    SendUserCircleCode sendUserCircleCode = new SendUserCircleCode();
                    sendUserCircleCode.show(manager, "Send_Circle_Code");
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
