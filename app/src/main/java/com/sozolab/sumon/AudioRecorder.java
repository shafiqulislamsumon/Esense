package com.sozolab.sumon;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class AudioRecorder {

    private String audioFileName = null;
    private String activity;
    private Context context;
    private SimpleDateFormat simpleDateFormat;
    private MediaRecorder mediaRecorder;
    private AudioRecorder audioRecorder = null;
    private String audioDirPath;
    private static final String TAG = AudioRecorder.class.getSimpleName();
    private static final int BYTES_IN_ONE_MB = 1048576; // 1MB = 1024 KB * 1024 Bytes

    public AudioRecorder(Context context){
        this.context = context;
        mediaRecorder = new MediaRecorder();
        activity = "";
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a", Locale.getDefault());
        audioDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ESenseData";
        createAudioDataFolder();
    }

    public void createAudioDataFolder() {

        try {
            File audioFolderDir = new File(audioDirPath);

            if (!audioFolderDir.exists()) {
                audioFolderDir.mkdirs();
                Log.d(TAG, "Audio data folder directory : " + audioDirPath); ///storage/emulated/0/AudioData
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAudioRecordProcess(String activityName){

        activity = activityName;

        if(getAvailableDeviceMemory() > 100){
            configureMediaRecorderSetting();
            startRecording();
            Log.d(TAG, "Recording start");
        } else{
            // don't record
            // outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.3gpp";
            //outputFile = context.getFilesDir().getAbsolutePath(); // store to phone memory
        }
    }

    public void configureMediaRecorderSetting(){

        try {
            if(mediaRecorder != null){
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                mediaRecorder.setAudioEncodingBitRate(256000);
                mediaRecorder.setAudioSamplingRate(44100);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startRecording(){

        try {
            if(mediaRecorder != null){
                String currentDateTime = simpleDateFormat.format(new Date());
                audioFileName = activity + "_" + currentDateTime +".3gpp";
                String outputFilePath = audioDirPath + File.separator + audioFileName;
                Log.d(TAG, "Start recording Audio File : " + outputFilePath);
                mediaRecorder.setOutputFile(outputFilePath);
                mediaRecorder.prepare();
                mediaRecorder.start();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            /*
            * if start() is called before prepare() || prepare() is called after start() or setOutputFormat()
            * then IllegalStateException might occur
            * */
        } catch (IOException e) {
            e.printStackTrace();
            // prepare() fails
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            if(mediaRecorder != null){
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                audioFileName = null;
                activity = "";
                Log.d(TAG, "Audio File saved successfully !");
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            // it is called before start()
        } catch (RuntimeException e) {
            e.printStackTrace();
            // no valid audio/video data has been received
        }
    }

    public long getAvailableDeviceMemory() {

        long availableMegaBytes = 0;
        long availableBytes = 0;

        try {
            String path = "/storage/emulated/0/"; // phone memory
            //String path = "/storage/sdcard1/"; // sd card memory
            //String path = Environment.getDataDirectory().getPath(); // /data
            //String path = Environment.getExternalStorageDirectory().getPath(); // /storage/emulated/0
            StatFs statFs = new StatFs(path);
            //Log.d(TAG, "Storage Path : " + path);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBytes = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
            } else {
                availableBytes = (long)statFs.getBlockSizeLong() * (long)statFs.getAvailableBlocksLong();
            }

            availableMegaBytes = availableBytes / BYTES_IN_ONE_MB;
            Log.d(TAG, "Available Memory : " + availableMegaBytes + " MB");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableMegaBytes;
    }

}
