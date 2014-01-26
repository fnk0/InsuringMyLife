package com.gabilheri.insuringmylife.adapters;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gabilheri.insuringmylife.ListRowGroup;
import com.gabilheri.insuringmylife.R;

/**
 * Created by marcus on 1/5/14.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {


    private final SparseArray<ListRowGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter(Activity act, SparseArray<ListRowGroup> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String children = (String) getChild(groupPosition, childPosition);
        TextView textTitle = null;
        TextView textDescription = null;

        if(convertView == null) {
            if(childPosition == 0) {
                convertView = inflater.inflate(R.layout.listrow_first_element, null);
            } else if(childPosition % 2 != 0) {
                convertView = inflater.inflate(R.layout.listrow_details, null);
            } else {
                convertView = inflater.inflate(R.layout.listrow_details_color2, null);
            }
        }

        textTitle = (TextView) convertView.findViewById(R.id.titleView);
        textDescription = (TextView) convertView.findViewById(R.id.descriptionView);
        textTitle.setText(getChildString(childPosition));
        textDescription.setText(children);

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
            }
        });
        */
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        ListRowGroup group = (ListRowGroup) getGroup(groupPosition);
        TextView title = (TextView) convertView.findViewById(R.id.textView1);
        title.setText(group.string);

       // ((CheckedTextView) convertView).setChecked(isExpanded);
       // ImageView imageView = (ImageView) convertView.findViewById(R.id.groupImage);
        /*
        if(group.brand.equals("Ford")) {
            imageView.setImageResource(android.R.drawable.ford);
        } else if(group.brand.equals("Toyota")) {
            imageView.setImageResource(android.R.drawable.toyota);
        } else if(group.brand.equals("Audi")) {
            imageView.setImageResource(android.R.drawable.audi);
        } else if(group.brand.equals("Volvo")) {
            imageView.setImageResource(android.R.drawable.volvo);
        } else if(group.brand.equals("Volkswagen")) {
            imageView.setImageResource(android.R.drawable.volkswagen);
        } else if(group.brand.equals("Jeep")) {
            imageView.setImageResource(android.R.drawable.jeep);
        } else if(group.brand.equals("Chevrolet")) {
            imageView.setImageResource(android.R.drawable.chevrolet);
        } else if(group.brand.equals("Infinity")) {
            imageView.setImageResource(android.R.drawable.infinity);
        } else if(group.brand.equals("Dodge")) {
            imageView.setImageResource(android.R.drawable.dodge);
        } else if(group.brand.equals("BMW")) {
            imageView.setImageResource(android.R.drawable.bmw);
        } else if(group.brand.equals("Hyundai")) {
            imageView.setImageResource(android.R.drawable.hyundai);
        } else {
            imageView.setImageResource(android.R.drawable.ic_launcher);
        }
        */
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    private static String getChildString(int position) {

        switch (position) {
            case 0:
                return "";
            case 1:
                return "Police Number";
            case 2:
                return "Model";
            case 3:
                return "Color";
            case 4:
                return "License Plate";
            case 5:
                return "Main Driver";
            default:
                break;
        }
        return "Test";
    }
}
