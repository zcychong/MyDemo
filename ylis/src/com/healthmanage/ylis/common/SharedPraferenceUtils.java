package com.healthmanage.ylis.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPraferenceUtils {
	private static String FILE_NAME = "ecare";
	private static Context mContext;
	public static void save(Context context, String key,Object obj){
		mContext = context;
		if(key != null && obj != null){
			SharedPreferences mySharedPreferences = mContext.getSharedPreferences(FILE_NAME, 
					Activity.MODE_PRIVATE); 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			if(obj.getClass().equals(Integer.class)){
				editor.putInt(key, (Integer)obj);
				editor.commit();
			}else if(obj.getClass().equals(String.class)){
				editor.putString(key, (String)obj);
				editor.commit();
			}else{
				saveOAuth(key, obj);
			}
		}
	}
	
	public static Object read(Context context, String key,Object obj){
		SharedPreferences mySharedPreferences = mContext.getSharedPreferences(FILE_NAME, 
				Activity.MODE_PRIVATE);
		if(obj.getClass().equals(Integer.class)){
			return mySharedPreferences.getInt(key, 0);
		}else if(obj.getClass().equals(String.class)){
			return mySharedPreferences.getString(key, "");
		}else{
			return readOAuth(key);
		}
	}
	
	public static void saveOAuth(String key, Object obj) {  
		SharedPreferences mySharedPreferences = mContext.getSharedPreferences(FILE_NAME,  
	    		Activity.MODE_PRIVATE);  
	    // 创建字节输出流  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    try {  
	        // 创建对象输出流，并封装字节流  
	        ObjectOutputStream oos = new ObjectOutputStream(baos);  
	        // 将对象写入字节流  
	        oos.writeObject(obj);  
	        // 将字节流编码成base64的字符窜  
	        String oAuth_Base64 = new String(Base64.encodeBase64(baos  
	                .toByteArray()));  
	        Editor editor = mySharedPreferences.edit();  
	        editor.putString(key, oAuth_Base64);  
	        editor.commit();  
	    } catch (IOException e) {  
	        // TODO Auto-generated  
	    }  
	}  
	
	public static Object readOAuth(String key) {  
		Object obj = null;  
	    SharedPreferences preferences = mContext.getSharedPreferences(FILE_NAME,  
	    		Activity.MODE_PRIVATE);  
	    String productBase64 = preferences.getString("oAuth_1", "");  
	              
	    //读取字节  
	    byte[] base64 = Base64.decodeBase64(productBase64.getBytes());  
	      
	    //封装到字节流  
	    ByteArrayInputStream bais = new ByteArrayInputStream(base64);  
	    try {  
	        //再次封装  
	        ObjectInputStream bis = new ObjectInputStream(bais);  
	        try {  
	            //读取对象  
	        	obj = (Object) bis.readObject();  
	        } catch (ClassNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	    } catch (StreamCorruptedException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return obj;  
	}
	
}
