package com.smartcity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartcity.R;
import com.smartcity.model.ComplaintItem;

import java.util.ArrayList;

/**
 * Created by mobintia-android-developer-1 on 10/3/16.
 */

public class ComplaintsListAdapter extends RecyclerView.Adapter<ComplaintsListAdapter.MyViewHolder> {

    private ArrayList<ComplaintItem> complaintItemArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_notification_title;
        public TextView list_item_notification_message;
        public TextView list_item_notification_address;
        public TextView list_item_notification_date;
        public TextView list_item_notification_id;
        public Button list_item_complaints_button_add_comments;


        public MyViewHolder(View view)
        {
            super(view);
            list_item_notification_title = (TextView) view.findViewById(R.id.list_item_notification_title);
            list_item_notification_message = (TextView) view.findViewById(R.id.list_item_notification_message);
            list_item_notification_address = (TextView) view.findViewById(R.id.list_item_notification_address);
            list_item_notification_date = (TextView) view.findViewById(R.id.list_item_notification_date);
            list_item_notification_id = (TextView) view.findViewById(R.id.list_item_notification_id);
            list_item_complaints_button_add_comments = (Button) view.findViewById(R.id.list_item_complaints_button_add_comments);
        }
    }


    public ComplaintsListAdapter(Context context, ArrayList<ComplaintItem> complaintItemArrayList) {
        this.complaintItemArrayList = complaintItemArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_complaints, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final ComplaintItem b= complaintItemArrayList.get(position);
        holder.list_item_notification_id.setText("ID "+b.getUserComplaintId());
        holder.list_item_notification_address.setText(b.getAddress());
        holder.list_item_notification_date.setText(b.getDate());

        holder.list_item_complaints_button_add_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintItemArrayList.size();
    }
}