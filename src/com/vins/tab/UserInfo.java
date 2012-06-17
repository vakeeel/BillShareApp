package com.vins.tab;

import android.app.Activity;
import android.content.Intent;

public class UserInfo {
	
	public static String userEmailId;
	public static String username;
	
	public static HomePageActivity homeActivity;
	public static TabInitializationActivity tabsActivity;
	public static ViewBillsTabActivity billsActivity;
	
	public static void restartActivity(Activity act){		
		Intent intent = act.getIntent();
		act.finish();
		act.startActivity(intent);
	}
}
