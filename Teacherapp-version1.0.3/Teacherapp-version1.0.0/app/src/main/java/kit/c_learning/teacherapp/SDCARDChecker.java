package kit.c_learning.teacherapp;

import android.content.Context;
import android.os.Environment;

/**
 * Created by sokrim on 3/29/2018.
 */

public class SDCARDChecker {
    public static void checkWeatherExternalStorageAvailableOrNot(Context context){
        boolean isExternalStorageAvailable = false;
        boolean isExternalStorageWritable = false;

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            isExternalStorageAvailable = isExternalStorageWritable = true;
            //  Toast.makeText(context, "read and write", Toast.LENGTH_SHORT).show();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            isExternalStorageAvailable = true;
            isExternalStorageWritable = false;
            //  Toast.makeText(context, "read only", Toast.LENGTH_SHORT).show();
        } else {
            isExternalStorageAvailable = isExternalStorageWritable = false;
            //  Toast.makeText(context, "neither read nor write", Toast.LENGTH_SHORT).show();
        }
    }
}
