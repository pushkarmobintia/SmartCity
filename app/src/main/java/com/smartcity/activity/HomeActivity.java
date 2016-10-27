package com.smartcity.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcity.R;
import com.smartcity.fragment.ComplaintsFragment;
import com.smartcity.fragment.HomeFragment;
import com.smartcity.fragment.NavigationDrawerFragment;
import com.smartcity.fragment.NotificationFragment;
import com.smartcity.util.Constants;
import com.smartcity.util.TypefaceSpan;

public class HomeActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

//    /**
//     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
//     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showGlobalContextActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= 23) {
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.black));
            }

        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new MyRoleFragment();
                showSecurityDialog();
                break;
            case 2:
                //fragment = new StatisticsFragment();
                showPrivacyDialog();
                break;
            case 3:
                fragment = new NotificationFragment();
                break;
            case 4:
                fragment = new ComplaintsFragment();
                break;
            case 5:
                //fragment = new SettingsFragment();
                break;
            default:
                break;

        }

        if(fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
            onSectionAttached(position);
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    private void showSecurityDialog(){

        final Dialog dialogForgotPassword= new Dialog(HomeActivity.this);
        dialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPassword.setContentView(R.layout.dialog_message);
        dialogForgotPassword.setCanceledOnTouchOutside(true);
        dialogForgotPassword.setCancelable(true);
        dialogForgotPassword.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialogForgotPassword.show();

        final TextView dialog_message_title = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_title);
        final TextView dialog_message_deading = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_deading);
        final TextView dialog_message_message = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_message);
        final TextView dialog_message_ok = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_ok);


        dialog_message_title.setText(getResources().getString(R.string.security));
        dialog_message_deading.setText(getResources().getString(R.string.security_deading));
        dialog_message_message.setText(getResources().getString(R.string.security_message));

        dialog_message_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogForgotPassword.dismiss();
            }
        });
    }

    private void showPrivacyDialog(){

        final Dialog dialogForgotPassword= new Dialog(HomeActivity.this);
        dialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPassword.setContentView(R.layout.dialog_message);
        dialogForgotPassword.setCanceledOnTouchOutside(true);
        dialogForgotPassword.setCancelable(true);
        dialogForgotPassword.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialogForgotPassword.show();

        final TextView dialog_message_title = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_title);
        final TextView dialog_message_deading = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_deading);
        final TextView dialog_message_message = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_message);
        final TextView dialog_message_ok = (TextView) dialogForgotPassword.findViewById(R.id.dialog_message_ok);


        dialog_message_title.setText(getResources().getString(R.string.privacy));
        dialog_message_deading.setText(getResources().getString(R.string.privacy_deading));
        dialog_message_message.setText(getResources().getString(R.string.privacy_message));

        dialog_message_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogForgotPassword.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        if(item.getItemId() == R.id.action_notification){

            fragment = new NotificationFragment();
            if(fragment != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                onSectionAttached(3);
            }
        }

        if(item.getItemId() == R.id.action_complaints){
            fragment = new ComplaintsFragment();
            if(fragment != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                onSectionAttached(4);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(getResources().getDrawable(R.drawable.ic_drawer));
        actionBar.setTitle(R.string.app_name);
        Spannable text = new SpannableString(getString(R.string.app_name));
        if (Build.VERSION.SDK_INT >= 23) {
            text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.white)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.color.black));
        } else {
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.black));
        }
        actionBar.setTitle(text);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        }else {
            text.setSpan(new TypefaceSpan(this, getResources().getString(R.string.font_name_)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            finish();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().remove(Constants.sharedPreferenceComplaintId).commit();
            return;
        }
        else
        {
            Toast.makeText(getBaseContext(), R.string.toast_exit, Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
