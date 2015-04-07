package com.androidfu.example.fmlgeolocation;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.data.Region;
import com.exacttarget.etpushsdk.event.GeofenceResponseEvent;
import com.exacttarget.etpushsdk.util.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ListActivity {

    private static final String KEY_LAST_UPDATED_ON = "last_updated_on";
    private static final Handler mainThread = new Handler(Looper.getMainLooper());
    public static final String DATE_FORMAT = "MM/dd/yyyy @ HH:mm:ss";

    private final Runnable updateUiRunnable = new UpdateUIRunnable();

    private GeofenceAdapter adapter;
    private List<Region> geofenceList = new ArrayList<>();

    @InjectView(android.R.id.list)
    ListView listView;
    @InjectView(R.id.tv_last_update)
    TextView lastUpdatedOn;
    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.tv_version_info)
    TextView versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        if (savedInstanceState != null) {
            lastUpdatedOn.setText(savedInstanceState.getString(KEY_LAST_UPDATED_ON));
            /*
                Neither Region nor Message implement Parcelable so we can't easily preserve our list
                of fences.
             */
        }
        versionInfo.setText(getString(R.string.version_info, ETPush.getSdkVersionName(), ETPush.getSdkVersionCode()));
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        mainThread.removeCallbacks(updateUiRunnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_LAST_UPDATED_ON, lastUpdatedOn.getText().toString());
        /*
            Neither Region nor Message implement Parcelable so we can't easily preserve our list
            of fences.
         */
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unused")
    public void onEvent(final GeofenceResponseEvent event) {
        if (event != null) {
            //noinspection unchecked
            geofenceList = event.getFences();
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            updateGeofenceView();
        } else {
            mainThread.post(updateUiRunnable);
        }
    }

    private void updateGeofenceView() {
        progressBar.setVisibility(View.GONE);
        lastUpdatedOn.setText(getString(R.string.fences_last_updated, new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(System.currentTimeMillis())));
        if (adapter == null) {
            adapter = new GeofenceAdapter(this, R.layout.listview_fence_row, geofenceList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private class UpdateUIRunnable implements Runnable {
        @Override
        public void run() {
            updateGeofenceView();
        }
    }
}
