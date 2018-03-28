package com.example.sudantribune;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs); 
		
		 LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
	        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
	        bar.setTitleTextColor(0xFFFFFFFF);
	        root.addView(bar, 0); // insert at top
	        bar.setNavigationOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	startActivity(new Intent(Preferences.this,MainActivity.class));
	            	finish();
	            }
	        });
	    }
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(Preferences.this,MainActivity.class));
    	finish();
	}
		 
}
