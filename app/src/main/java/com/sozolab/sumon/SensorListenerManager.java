package com.sozolab.sumon;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.sozolab.sumon.io.esense.esenselib.ESenseConfig;
import com.sozolab.sumon.io.esense.esenselib.ESenseEvent;
import com.sozolab.sumon.io.esense.esenselib.ESenseSensorListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SensorListenerManager implements ESenseSensorListener {

    private final String TAG = "SensorListenerManager";
    private long timeStamp;
    private double[] accel;
    private double[] gyro;
    private boolean dataCollecting;

    Context context;
    Workbook excelWorkbook;
    int rowIndex;
    String sheetName;
    Sheet excelSheet;
    File excelFile;
    String sensorDataFile;
    ESenseConfig eSenseConfig;
    String dataDirPath;
    String activityName;
    int activityIndex;

    public SensorListenerManager(Context context){
        this.context = context;
        eSenseConfig = new ESenseConfig();
        rowIndex = 1;
        activityIndex = -1;
        activityName = "";
        sheetName = "";
        excelSheet = null;
        excelFile = null;
        dataDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ESenseData" + File.separator;
        Log.d(TAG, "Sensor Data Path : " + dataDirPath);
    }

    /**
     * Called when there is new sensor data available
     *
     * @param evt object containing the sensor samples received
     */
    @Override
    public void onSensorChanged(ESenseEvent evt) {
        //Log.d(TAG, "onSensorChanged()");

        if (dataCollecting){

            if(excelSheet != null){
                rowIndex++;

                timeStamp = evt.getTimestamp();
                accel = evt.convertAccToG(eSenseConfig);
                gyro = evt.convertGyroToDegPerSecond(eSenseConfig);

                Row dataRow = excelSheet.createRow(rowIndex);
                Cell dataCell = null;
                dataCell = dataRow.createCell(0);
                dataCell.setCellValue(timeStamp);

                dataCell = dataRow.createCell(1);
                dataCell.setCellValue(accel[0]);

                dataCell = dataRow.createCell(2);
                dataCell.setCellValue(accel[1]);

                dataCell = dataRow.createCell(3);
                dataCell.setCellValue(accel[2]);

                dataCell = dataRow.createCell(4);
                dataCell.setCellValue(gyro[0]);

                dataCell = dataRow.createCell(5);
                dataCell.setCellValue(gyro[1]);

                dataCell = dataRow.createCell(6);
                dataCell.setCellValue(gyro[2]);

                dataCell = dataRow.createCell(7);
                dataCell.setCellValue(String.valueOf(activityIndex));

                dataCell = dataRow.createCell(8);
                dataCell.setCellValue(activityName);

                String sensorData = "Index : " + activityIndex + " Activity : " + activityName + " Row : " + rowIndex + " Time : " + timeStamp
                        + " accel : " + accel[0] + " " + accel[1] + " " + accel[2] + " gyro : " + gyro[0] + " " + gyro[1] + " " + gyro[2];
                Log.d(TAG, sensorData);
            }
        }
    }

    public void setColumnWidth(Sheet sheet){
        sheet.setColumnWidth(0, (15 * 300));
        sheet.setColumnWidth(1, (15 * 300));
        sheet.setColumnWidth(2, (15 * 300));
        sheet.setColumnWidth(3, (15 * 300));
        sheet.setColumnWidth(4, (15 * 300));
        sheet.setColumnWidth(5, (15 * 300));
        sheet.setColumnWidth(6, (15 * 300));
        sheet.setColumnWidth(7, (15 * 300));
        sheet.setColumnWidth(8, (15 * 300));
    }

    public void startDataCollection(String activity) {

        this.activityName = activity;
        activityIndex = getActivityIndex(activityName);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a", Locale.getDefault());
        String currentDateTime = simpleDateFormat.format(new Date());

        sensorDataFile = activityName + "_" + currentDateTime + ".xls";
        excelFile = new File(dataDirPath, sensorDataFile);

        sheetName = activityName;
        excelWorkbook = new HSSFWorkbook();
        excelSheet = excelWorkbook.createSheet(sheetName);

        setColumnWidth(excelSheet);
        dataCollecting = true;
    }

    public void stopDataCollection(){

        rowIndex = 1;
        dataCollecting = false;
        FileOutputStream accelOutputStream = null;

        try {
            accelOutputStream = new FileOutputStream(excelFile);
            excelWorkbook.write(accelOutputStream);

            Log.w(TAG, "Writing excelFile : " + excelFile);
        } catch (IOException e) {
            Log.w(TAG, "Error writing : " + excelFile, e);
        } catch (Exception e) {
            Log.w(TAG, "Failed to save data file", e);
        } finally {
            try {
                if (null != accelOutputStream){
                    accelOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getActivityIndex(String activity){
        int index = -1;

        switch (activity){
            case "Head Shake":
                index = 1;
                break;
            case "Speaking":
                index = 2;
                break;
            case "Nodding":
                index = 3;
                break;
            case "Eating":
                index = 4;
                break;
            case "Walking":
                index = 5;
                break;
            case "Staying":
                index = 6;
                break;
        }

        return index;
    }

}
