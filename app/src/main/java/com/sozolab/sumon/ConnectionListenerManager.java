package com.sozolab.sumon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sozolab.sumon.io.esense.esenselib.ESenseConnectionListener;
import com.sozolab.sumon.io.esense.esenselib.ESenseManager;

public class ConnectionListenerManager implements ESenseConnectionListener {
    private final String TAG = "ConectionLisenerManager";
    private int samplingRate = 50;
    Context context;
    SensorListenerManager sensorListenerManager;
    TextView connectionTextView;
    TextView deviceNameTextView;
    ImageView statusImageView;
    SharedPreferences.Editor sharedPrefEditor;
    ProgressBar progressBar;

    public ConnectionListenerManager(Context context, SensorListenerManager sensorListenerManager, TextView connectionTextView,
                                     TextView deviceNameTextView, ImageView statusImageView, ProgressBar progressBar,
                                     SharedPreferences.Editor sharedPrefEditor){
        this.context = context;
        this.sharedPrefEditor = sharedPrefEditor;
        this.connectionTextView = connectionTextView;
        this.deviceNameTextView = deviceNameTextView;
        this.statusImageView = statusImageView;
        this.progressBar = progressBar;
        this.sensorListenerManager = sensorListenerManager;
    }

    /**
     * Called when the device with the specified name has been found during a scan
     *
     * @param manager device manager
     */
    @Override
    public void onDeviceFound(ESenseManager manager) {
        Log.d(TAG, "onDeviceFound");
    }

    /**
     * Called when the device with the specified name has not been found during a scan
     *
     * @param manager device manager
     */
    @Override
    public void onDeviceNotFound(ESenseManager manager) {
        Log.d(TAG, "onDeviceNotFound");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString("status", "disconnected");
                sharedPrefEditor.commit();

                progressBar.setVisibility(View.GONE);
                connectionTextView.setText("Disconnected");
                deviceNameTextView.setText(manager.getmDeviceName());
                statusImageView.setImageResource(R.drawable.disconnected);
                Toast.makeText(context, "Device not Found !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Called when the connection has been successfully made
     *
     * @param manager device manager
     */
    @Override
    public void onConnected(ESenseManager manager) {
        Log.d(TAG, "onConnected");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString("status", "connected");
                sharedPrefEditor.commit();

                progressBar.setVisibility(View.GONE);
                connectionTextView.setText("Connected");
                deviceNameTextView.setText(manager.getmDeviceName());
                statusImageView.setImageResource(R.drawable.connected);
                Toast.makeText(context, "Device Connected !", Toast.LENGTH_SHORT).show();
            }
        });

        manager.registerSensorListener(sensorListenerManager, samplingRate);
    }

    /**
     * Called when the device has been disconnected
     *
     * @param manager device manager
     */
    @Override
    public void onDisconnected(ESenseManager manager) {
        Log.d(TAG, "onDisconnected");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString("status", "disconnected");
                sharedPrefEditor.commit();

                progressBar.setVisibility(View.GONE);
                connectionTextView.setText("Disconnected");
                deviceNameTextView.setText(manager.getmDeviceName());
                statusImageView.setImageResource(R.drawable.disconnected);
                Toast.makeText(context, "Device Disconnected !", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
