package com.ksy.firstkiss.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.ksy.firstkiss.common.utils.TimeUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


/**
 * Singleton helper: install a default unhandled exception handler which shows
 * an informative dialog and kills the app.  Useful for apps whose
 * error-handling consists of throwing RuntimeExceptions.
 * NOTE: almost always more useful to
 * Thread.setDefaultUncaughtExceptionHandler() rather than
 * Thread.setUncaughtExceptionHandler(), to apply to background threads as well.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Context mContext; 
    private Thread.UncaughtExceptionHandler mDefaultHandler;      
    //CrashHandler instance    
    private static CrashHandler instance;  
         
    //store device's info and exception info   
    private Map<String, String> infos = new HashMap<String, String>();      
            
    private CrashHandler() {}      
      
    /** get single instance */      
    public static CrashHandler getInstance() {      
        if(instance == null)  
            instance = new CrashHandler();     
        return instance;      
    }      
    
    /**   
     *    initialize
     */      
    public void init(Context context) {      
        mContext = context.getApplicationContext();      
        //get system's default UncaughtException handler      
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();      
        //set this CrashHandler as default     
        Thread.setDefaultUncaughtExceptionHandler(this);      
    }    
    
    /**   
     * enter when UncaughtException occurs   
     */      
    @Override      
    public void uncaughtException(Thread thread, Throwable ex) {      
        if (!handleException(ex) && mDefaultHandler != null) {      
            //if user doesn't handle it ,let  out crash handler do it     
            mDefaultHandler.uncaughtException(thread, ex);      
        } else {      
            try {      
                Thread.sleep(2000);      
            } catch (InterruptedException e) {      
                Log.e(TAG, "error : ", e);      
            }      
            //exit current process
            android.os.Process.killProcess(android.os.Process.myPid());      
            System.exit(1);   
             
        } 
      
    }  
    
    /**   
     * define how to handle exception, collect error info, send error report, ... etc   
     *    
     * @param ex   
     * @return true: if exception is handled or return false.   
     */      
    private boolean handleException(Throwable ex) {      
        if (ex == null) {      
            return false;      
        }      
        //collect device info
        collectDeviceInfo(mContext);      
          
        //show error in toast      
        new Thread() {      
            @Override      
            public void run() {      
                Looper.prepare();      
                Toast.makeText(mContext, "出现错误！正在尝试重启，请稍后", Toast.LENGTH_SHORT).show();      
                Looper.loop();      
            }      
        }.start();      
        //save to log files
        saveCatchInfo2File(ex);    
        return true;      
    }      
          
    /**   
     * collect device info
     * @param ctx   
     */      
    public void collectDeviceInfo(Context ctx) {      
        try {      
            PackageManager pm = ctx.getPackageManager();      
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);      
            if (pi != null) {      
                String versionName = pi.versionName == null ? "null" : pi.versionName;      
                String versionCode = pi.versionCode + "";      
                infos.put("versionName", versionName);      
                infos.put("versionCode", versionCode);      
            }      
        } catch (NameNotFoundException e) {      
            Log.e(TAG, "an error occured when collect package info", e);      
        }      
        Field[] fields = Build.class.getDeclaredFields();      
        for (Field field : fields) {      
            try {      
                field.setAccessible(true);      
                infos.put(field.getName(), field.get(null).toString());      
                Log.d(TAG, field.getName() + " : " + field.get(null));      
            } catch (Exception e) {      
                Log.e(TAG, "an error occured when collect crash info", e);      
            }      
        }      
    }      
      
    /**   
     * save error info into log file 
     *    
     * @param ex   
     * @return  file name  
     */      
    @SuppressLint("SdCardPath")
	private String saveCatchInfo2File(Throwable ex) {      
              
        StringBuffer sb = new StringBuffer();      
        for (Map.Entry<String, String> entry : infos.entrySet()) {      
            String key = entry.getKey();      
            String value = entry.getValue();      
            sb.append(key + "=" + value + "\n");      
        }      
        sb.append(getRecursiveStackTrace(ex));
        sb.append(getTopLevelCauseMessage(ex));
        
        try {      
            long timestamp = System.currentTimeMillis();      
            String time = TimeUtils.getCurrentTimeInString();     
            String fileName = "crash-" + time + "-" + timestamp + ".log";      
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {      
                String path = "/mnt/sdcard/Android/data/com.sjtu.icarer/crash/";      
                File dir = new File(path);      
                if (!dir.exists()) {      
                    dir.mkdirs();      
                }      
                FileOutputStream fos = new FileOutputStream(path + fileName);      
                fos.write(sb.toString().getBytes());    
                // send to developer
                sendCrashLog2PM(path+fileName);  
                fos.close();      
            }      
            return fileName;      
        } catch (Exception e) {      
            Log.e(TAG, "an error occured while writing file...", e);      
        }      
        return null;      
    }      
      
    /** 
     * send the captured crash message and error info to developers
     *  
     *  Now only save in sdcard and print in logCat
     *  TODO send to server
     */  
    private void sendCrashLog2PM(String fileName){  
        if(!new File(fileName).exists()){  
            Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();  
            return;  
        }  
        FileInputStream fis = null;  
        BufferedReader reader = null;  
        String s = null;  
        try {  
            fis = new FileInputStream(fileName);  
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));  
            while(true){  
                s = reader.readLine();  
                if(s == null) break;  
                //TODO send it to server  
                Log.i("info", s.toString());  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{
            try {  
                reader.close();  
                fis.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    // Returns the Message attached to the original Cause of |t|.
    private static String getTopLevelCauseMessage(Throwable t) {
        Throwable topLevelCause = t;
        while (topLevelCause.getCause() != null) {
            topLevelCause = topLevelCause.getCause();
        }
        return topLevelCause.getMessage();
    }

    // Returns a human-readable String of the stacktrace in |t|, recursively
    // through all Causes that led to |t|.
    private static String getRecursiveStackTrace(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
