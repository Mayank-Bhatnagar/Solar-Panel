package com.setup.solarpanel.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Mayank on 9/5/2015.
 */
public class CommonMethods {

    public static void showToast(String message, Context context){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void copyDatabaseToSDCard(Context context){
        try{

            java.io.File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if(sd.canWrite()) {
                String currentDBPath = "//data//"+context.getPackageName()+"//databases//"+"solar_panel.db";
                String backupDBPath = "solarpanel.db";
                java.io.File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if(currentDB.exists()){
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();

                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }


        }catch(Exception e){

        }

    }

    public static void switchToNextActivity(Class<?> activity, Context context) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static String getTime(int laterTime)
    {
        Calendar calendar = Calendar.getInstance();

        if(laterTime != 0)
            calendar.add(Calendar.HOUR_OF_DAY, 1);

        return new SimpleDateFormat("hh:mm a").format(calendar.getTime());
    }
}
