package com.ksy.firstkiss.core;
import android.annotation.SuppressLint;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

public class DataService {
	private static Context text;
	@SuppressLint("SdCardPath")
	//directory should be created automatically
	String FILEROOT = "/sdcard/Android/data/com.ksy.firstkiss/";
	String FILENAME ;
	FileOutputStream fos;
//	File mfile;
	
	public DataService(Context text,String filename){
		this.text=text;
		FILENAME=filename;
	}
	public void saveFile(String data) throws Exception{
		//context.getFilesDir();// �õ�����ļ���ϵͳĿ¼ /data/data/<package name>/files  
	    //context.getCacheDir(); //����Ŀ¼  /data/data/<package name>/cache  
	    FileOutputStream outputStream=text.openFileOutput(FILEROOT+FILENAME, Context.MODE_APPEND);  
	    outputStream.write(data.getBytes());  
	    outputStream.close();
	}
	//�洢���ݵ�sdcard  
    @SuppressLint("SdCardPath")
	public void saveFileToSDCard(String name) throws Exception{  
        Environment.getExternalStorageDirectory();  
        File file=new File("/sdcard",FILENAME);  
        FileOutputStream outputStream=new FileOutputStream(file);  
        outputStream.write(name.getBytes());  
        outputStream.close();  
    }  
	
	
	public void startFileStream() throws FileNotFoundException{
//		fos = text.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		//TODO need to check the dir or file exist
		Environment.getExternalStorageDirectory();  
        File mfile=new File(FILEROOT,FILENAME);
        fos = new FileOutputStream(mfile);  
	}
	public void saveFileStream(String data) throws Exception{
		fos.write(data.getBytes());
	}
	 
	public void closeFileStream() throws IOException{
		fos.close();
	}
	
	
	
	public String getFile(String filename) throws Exception{  
        FileInputStream inputStream=text.openFileInput(FILEROOT+FILENAME);  
        ByteArrayOutputStream outStream=new ByteArrayOutputStream();  
        byte[] buffer=new byte[1024];  
        int len=0;  
        while ((len=inputStream.read(buffer))!=-1){  
            outStream.write(buffer, 0, len);  
        }  
        outStream.close();  
        byte[] data=outStream.toByteArray();  
        
        return new String(data); 
    }
}
