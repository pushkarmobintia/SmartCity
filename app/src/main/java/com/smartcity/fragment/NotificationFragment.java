package com.smartcity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smartcity.R;
import com.smartcity.adapter.NotificationListAdapter;
import com.smartcity.databasehelper.DatabaseHelper;
import com.smartcity.model.NotificationsItem;
import com.smartcity.util.Constants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mobintia on 20/10/16.
 */
public class NotificationFragment extends Fragment {

    private ImageView fragment_notification_imageview_clear_notification;
    private RecyclerView fragment_notification_recycler_view;
    private ArrayList<NotificationsItem> notificationItems;
    private NotificationListAdapter notificationListAdapter;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notification, container, false);

        findViewByIds(rootView);

        notificationItems = new ArrayList<NotificationsItem>();

        db = new DatabaseHelper(getActivity());

        notificationItems = db.getAllNotification();
        Collections.reverse(notificationItems);
        System.out.println("Size notification: " + notificationItems.size());

        notificationListAdapter = new NotificationListAdapter(getActivity(), notificationItems);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

        return rootView;
    }

    private void findViewByIds(View rootView) {
        fragment_notification_recycler_view = (RecyclerView) rootView.findViewById(R.id.fragment_notification_recycler_view);
        fragment_notification_imageview_clear_notification = (ImageView) rootView.findViewById(R.id.fragment_notification_imageview_clear_notification);
    }

}
