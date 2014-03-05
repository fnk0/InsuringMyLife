package com.gabilheri.insuringmylife.adapters;

import android.app.Activity;
import android.util.Log;
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
    int counter = 0;

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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String children = (String) getChild(groupPosition, childPosition);

        TextView textTitle;
        TextView textDescription;

        if(childPosition == 0) {
            convertView = inflater.inflate(R.layout.listrow_first_element, null);
        } else if(childPosition % 2 != 0) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        } else {
            convertView = inflater.inflate(R.layout.listrow_details_color2, null);
        }

        textTitle = (TextView) convertView.findViewById(R.id.titleView);
        textDescription = (TextView) convertView.findViewById(R.id.descriptionView);
        textTitle.setText(getChildString(childPosition));
        textDescription.setText(children);

        Log.d("Child Position: ",  "" + childPosition);
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
            case 6:
                return "Drivers License Number / State";
            case 7:
                return "Driver's Birthday / Gender";

        }
        return "Test";
    }
}
