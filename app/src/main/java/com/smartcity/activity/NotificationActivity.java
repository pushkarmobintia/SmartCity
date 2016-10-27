package com.smartcity.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smartcity.R;
import com.smartcity.adapter.NotificationListAdapter;
import com.smartcity.databasehelper.DatabaseHelper;
import com.smartcity.model.NotificationsItem;
import com.smartcity.util.Constants;
import com.smartcity.util.TypefaceSpan;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {
    private ImageView fragment_notification_imageview_clear_notification;
    private RecyclerView fragment_notification_recycler_view;
    private ArrayList<NotificationsItem> notificationItems;
    private NotificationListAdapter notificationListAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewByIds();
        restoreActionBar();

        notificationItems = new ArrayList<NotificationsItem>();

        db = new DatabaseHelper(getApplicationContext());

        notificationItems = db.getAllNotification();
        Collections.reverse(notificationItems);
        System.out.println("Size notification: " + notificationItems.size());

        notificationListAdapter = new NotificationListAdapter(getApplicationContext(), notificationItems);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_notification_recycler_view.setLayoutManager(layoutManager);
        fragment_notification_recycler_view.setAdapter(notificationListAdapter);

        Constants.notificationCount = 0;

        if(notificationItems.size() > 0){
            fragment_notification_imageview_clear_notification.setVisibility(View.VISIBLE);
        }else{
            fragment_notification_imageview_clear_notification.setVisibility(View.GONE);
        }

        fragment_notification_imageview_clear_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);

                builder.setMessage(getResources().getString(R.string.Are_you_sure_want_to_clear_notification));

                builder.setPositiveButton(getResources().getString(R.string.Dialog_exit_yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteAllNotification();
                        notificationItems.clear();
                        notificationListAdapter.notifyDataSetChanged();
                    }

                });

                builder.setNegativeButton(getResources().getString(R.string.Dialog_exit_no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    private void findViewByIds() {
        fragment_notification_recycler_view = (RecyclerView) findViewById(R.id.fragment_notification_recycler_view);
        fragment_notification_imageview_clear_notification = (ImageView) findViewById(R.id.fragment_notification_imageview_clear_notification);
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_white);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.black));
        Spannable text = new SpannableString(getResources().getString(R.string.title_notification));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.black));

        } else {
            text.setSpan(new TypefaceSpan(this, getResources().getString(R.string.font_name_)), 0, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        actionBar.setTitle(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
