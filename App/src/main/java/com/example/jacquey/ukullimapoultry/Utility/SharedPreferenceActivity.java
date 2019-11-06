package com.example.jacquey.ukullimapoultry.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jacquey.ukullimapoultry.MyApp;

public class SharedPreferenceActivity {


    private static String PREFERENCE_NAME = "ukulimapoultry";
    private static SharedPreferenceActivity sharedPreferenceActivity;

    private SharedPreferences sharedPreferences;

    private SharedPreferenceActivity(Context context) {
        PREFERENCE_NAME = PREFERENCE_NAME + context.getPackageName();
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceActivity getInstance(){
        if (sharedPreferenceActivity == null){
            sharedPreferenceActivity = new SharedPreferenceActivity(MyApp.getContext());
        }
        return sharedPreferenceActivity;
    }

    // login response user_id 1234
    public void saveString(String key,  String Val ){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, Val);
        editor.commit();
    }

    public String getString(String key, String defVal){
        return sharedPreferences.getString(key, defVal);
    }


    public String getString(String key){
        return sharedPreferences.getString(key, "");
    }

    public void saveInt(String key,  int Val ){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, Val);
        editor.commit();
    }


    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Clear value of given key from this preference
     *
     * @param key name of the key whose value to be removed
     */
    public void clearString(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public int getInteger(String key){ return sharedPreferences.getInt(key, 0 ); }


}
