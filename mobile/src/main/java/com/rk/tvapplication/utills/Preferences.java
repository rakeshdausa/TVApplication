package com.rk.tvapplication.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rk.tvapplication.R;


/**
 * This class provides interface
 * to handle preferences like
 * saving user data to device preferences
 * Created by Rakesh on 28/04/15.
 */
public class Preferences {

    private SharedPreferences mPrefs;
    private Context context;
    private static final String TAG = Preferences.class.getCanonicalName();

    public static final String USER = "USER";
    public static final String USER_ID = "USER_ID";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String IS_REGISTERED = "IS_REGISTERED";
    public static final String ALERT_FREQUENCY = "ALERT_FREQUENCY";
    public static final String ALERT_SOUND = "ALERT_SOUND";




    private Preferences(){}

    public static Preferences getInstance(Context context){
        Preferences prefrences = new Preferences();
        prefrences.mPrefs = context.getSharedPreferences(context.getResources().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        prefrences.context = context;
        return prefrences;
    }

    public  void setString(String key,String value)
    {
        try{
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(key, value);
            editor.commit();
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
    }
    public String getString(String key){
        try{
            return mPrefs.getString(key, null);
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
        return null;
    }

    public  void setBoolean(String key, boolean value)
    {
        try{
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
    }

    public boolean getBoolean(String key){
        try{
            return mPrefs.getBoolean(key, false);
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
        return false;
    }

    public void clearAll(){
        try{
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.clear();
            editor.commit();
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
    }

}
