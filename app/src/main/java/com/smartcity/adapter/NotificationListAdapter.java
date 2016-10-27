package com.smartcity.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartcity.R;
import com.smartcity.model.NotificationsItem;
import com.smartcity.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mobintia-android-developer-1 on 10/3/16.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {

    private ArrayList<NotificationsItem> notificationItems;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_notification_title;
        public TextView list_item_notification_message;
        public TextView list_item_notification_address;
        public TextView list_item_notification_date;
        public TextView list_item_notification_time;
        public TextView list_item_notification_id;
        public TextView list_item_notification_thank_support;
        public TextView list_item_notification_thank_support_name;
        public TextView list_item_notification_thank_details;
        public Button list_item_notification_contact_us;
        public Button list_item_notification_show_status;


        public MyViewHolder(View view)
        {
            super(view);
            list_item_notification_title = (TextView) view.findViewById(R.id.list_item_notification_title);
            list_item_notification_message = (TextView) view.findViewById(R.id.list_item_notification_message);
            list_item_notification_address = (TextView) view.findViewById(R.id.list_item_notification_address);
            list_item_notification_date = (TextView) view.findViewById(R.id.list_item_notification_date);
            list_item_notification_time = (TextView) view.findViewById(R.id.list_item_notification_time);
            list_item_notification_id = (TextView) view.findViewById(R.id.list_item_notification_id);
            list_item_notification_thank_support = (TextView) view.findViewById(R.id.list_item_notification_thank_support);
            list_item_notification_thank_support_name = (TextView) view.findViewById(R.id.list_item_notification_thank_support_name);
            list_item_notification_thank_details = (TextView) view.findViewById(R.id.list_item_notification_thank_details);
            list_item_notification_contact_us = (Button) view.findViewById(R.id.list_item_notification_contact_us);
            list_item_notification_show_status = (Button) view.findViewById(R.id.list_item_notification_show_status);
        }
    }


    public NotificationListAdapter(Context context, ArrayList<NotificationsItem> notificationItems) {
        this.notificationItems = notificationItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final NotificationsItem b= notificationItems.get(position);
        holder.list_item_notification_title.setText(b.getNotification_subject());
        holder.list_item_notification_message.setText(b.getNotification_message());
        holder.list_item_notification_address.setText(b.getNotification_address());
        holder.list_item_notification_id.setText(b.getNotification_complaintId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
        Date testDate = null;
        try {
            testDate = sdf.parse(b.getNotification_date());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        holder.list_item_notification_date.setText(newFormat);

        String[] shortMessage = b.getNotification_sortMessage().split("!");
        holder.list_item_notification_thank_support.setText(shortMessage[0]);

        String[] spliteValues = shortMessage[1].split(" ");
        holder.list_item_notification_thank_support_name.setText(spliteValues[1]);
        holder.list_item_notification_thank_details.setText(spliteValues[2] + " " + spliteValues[3] + " " + spliteValues[4]);

        holder.list_item_notification_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(context.getResources().getString(R.string.are_you_sure_you_want_make_call));

                builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + b.getNotification_contactNumber()));
                        context.startActivity(intent);
                    }

                });

                builder.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                if (alert.isShowing()) {

                } else {
                    alert.show();
                }
            }
        });


        holder.list_item_notification_show_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.constantComplaintURL));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }
}