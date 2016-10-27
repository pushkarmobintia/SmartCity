package com.smartcity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartcity.R;
import com.smartcity.model.Nav_Item;

import java.util.ArrayList;

public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Nav_Item> navDrawerItems;
    public static int current_position = 0;
    private Typeface tf;


    public DrawerListAdapter(Context context, ArrayList<Nav_Item> navDrawerItems)
    {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item, null);
        }

        tf = Typeface.createFromAsset(context.getResources().getAssets(), context.getResources().getString(R.string.font_name));

        View linearlayout_grey_line = convertView.findViewById(R.id.linearlayout_grey_line);
        LinearLayout linearlayout_yellow_lines = (LinearLayout) convertView.findViewById(R.id.linearlayout_yellow_lines);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        RelativeLayout bg = (RelativeLayout) convertView.findViewById(R.id.bg);


        if(position == 2 || position == 4){
            linearlayout_grey_line.setVisibility(View.GONE);
            linearlayout_yellow_lines.setVisibility(View.VISIBLE);
        }else {
            linearlayout_grey_line.setVisibility(View.VISIBLE);
            linearlayout_yellow_lines.setVisibility(View.GONE);
        }

        txtTitle.setTypeface(tf);
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        switch (position) {
            case 0:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));

                break;
            case 1:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));

                break;
            case 2:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));
                break;

            case 3:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));
                break;
            case 4:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));
                break;
            case 5:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));
                break;
            case 6:
                bg.setBackgroundColor(context.getResources().getColor(R.color.drawerBackGround));
                break;

            default:
                break;
        }

        return convertView;
    }
}
