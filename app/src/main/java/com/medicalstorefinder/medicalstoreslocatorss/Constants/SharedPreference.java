package com.medicalstorefinder.medicalstoreslocatorss.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class SharedPreference {

	public SharedPreference() {
		super();
	}

    public boolean isSPKeyExits(Activity context,String PREFS_NAME,String PREFS_KEY){
		SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		if(sharedPreference.contains(PREFS_KEY)) {
			return true;
		}
		return false;
    }

	public void createSharedPreference(Activity context,String PREFS_NAME ) {
		SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreference.edit();
		editor.commit();
	}

	public void putValue(Context context,String PREFS_NAME,String PREFS_KEY, String PREFS_VALUE) {
		SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreference.edit();
		editor.putString(PREFS_KEY, PREFS_VALUE);
		editor.commit();
	}

	public String getValue(Context context, String PREFS_NAME, String PREFS_KEY) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getString(String.valueOf(PREFS_KEY), "");
	}
	
	public void clearSharedPreference(Context context,String PREFS_NAME) {
		SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreference.edit();
		editor.clear();
		editor.commit();
	}

	public void removeValue(Context context, String PREFS_NAME,String PREFS_KEY) {
		SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreference.edit();
		editor.remove(PREFS_KEY);
		editor.commit();
	}	
}
