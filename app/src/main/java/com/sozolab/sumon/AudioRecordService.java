package com.sozolab.sumon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

public class AudioRecordService extends Service {

    private AudioManager audioManager;
    private AudioRecorder audioRecorder;
    private static final String TAG = AudioRecordService.class.getSimpleName();;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        audioRecorder = new AudioRecorder(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        audioManager.setMode(audioManager.MODE_NORMAL);
        audioManager.setBluetoothScoOn(true);
        audioManager.startBluetoothSco();
        audioManager.setSpeakerphoneOn(false);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        String activity = intent.getStringExtra("activity");

        if(audioRecorder != null){
            audioRecorder.startAudioRecordProcess(activity);
        }else{
            Log.d(TAG, "audioRecorder is null");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart()");
    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {
        Log.d(TAG, "onStop()");
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        audioRecorder.stopRecording();
        audioRecorder = null;
        audioManager.stopBluetoothSco();
        audioManager.setMode(audioManager.MODE_NORMAL);
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
    }

    @Override
    public void onLowMemory() {

    }

}
