package com.vins.tab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ManageBillsActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		 TextView textview = new TextView(this);
	        textview.setText("This is the manage bills tab");
	        setContentView(textview);
	}
}
