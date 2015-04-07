package com.androidfu.example.fmlgeolocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exacttarget.etpushsdk.data.Region;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GeofenceAdapter extends ArrayAdapter<Region> {

    public GeofenceAdapter(Context context, int resource, List<Region> geofences) {
        super(context, resource, geofences);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Region geofence = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_fence_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fenceRow.setBackgroundColor(position % 2 == 0 ? 0xFFCCCCCC : 0xFFEEEEEE);
        holder.fenceName.setText(geofence.getName());
        holder.fenceId.setText(geofence.getId());
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'listview_fence_row.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_fenceName)
        TextView fenceName;
        @InjectView(R.id.tv_fenceId)
        TextView fenceId;
        @InjectView(R.id.ll_fenceRow)
        LinearLayout fenceRow;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
