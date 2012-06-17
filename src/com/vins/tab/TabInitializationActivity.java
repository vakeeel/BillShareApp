package com.vins.tab;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabInitializationActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        UserInfo.tabsActivity = this;
   
        
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, FriendsActivity.class);

        // Initialise a TabSpec for each tab and add it to the TabHost

        spec = tabHost.newTabSpec("friends").setIndicator("Friends").setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        
        // Do the same for the other tabs
        intent = new Intent().setClass(this, ViewBillsTabActivity.class);

        spec = tabHost.newTabSpec("report").setIndicator("Bills").setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);


    }
}