package com.smartcity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.smartcity.adapter.ComplaintsListAdapter;
import com.smartcity.databasehelper.DatabaseHelper;
import com.smartcity.model.ComplaintItem;
import com.smartcity.util.Constants;
import com.smartcity.util.RecyclerItemClickListener;
import com.smartcity.util.TypefaceSpan;

import java.util.ArrayList;
import java.util.Collections;

public class UserComplaintsActivity extends AppCompatActivity {
    private ImageView fragment_notification_imageview_clear_notification;
    private RecyclerView fragment_notification_recycler_view;
    private ArrayList<ComplaintItem> complaintItemArrayList;
    private ComplaintsListAdapter notificationListAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        findViewByIds();
        restoreActionBar();

        complaintItemArrayList = new ArrayList<ComplaintItem>();

        db = new DatabaseHelper(getApplicationContext());

        complaintItemArrayList = db.getAllComplaints();
        Collections.reverse(complaintItemArrayList);
        System.out.println("Size complaints: " + complaintItemArrayList.size());

        notificationListAdapter = new ComplaintsListAdapter(getApplicationContext(), complaintItemArrayList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_notification_recycler_view.setLayoutManager(layoutManager);
        fragment_notification_recycler_view.setAdapter(notificationListAdapter);

        Constants.notificationCount = 0;

        if(complaintItemArrayList.size() > 0){
            fragment_notification_imageview_clear_notification.setVisibility(View.VISIBLE);
        }else{
            fragment_notification_imageview_clear_notification.setVisibility(View.GONE);
        }

        fragment_notification_imageview_clear_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserComplaintsActivity.this);

                builder.setMessage(getResources().getString(R.string.Are_you_sure_want_to_clear_complaints));

                builder.setPositiveButton(getResources().getString(R.string.Dialog_exit_yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteAllNotification();
                        complaintItemArrayList.clear();
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

        fragment_notification_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(complaintItemArrayList.get(position).getLink()));
                startActivity(browserIntent);
            }
        }));

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
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.actionbar));
        Spannable text = new SpannableString(getResources().getString(R.string.title_complaints));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar));

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
            Intent intent = new Intent(getApplicationContext(), Custom_CameraActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        finish();
        Intent intent = new Intent(getApplicationContext(), Custom_CameraActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
