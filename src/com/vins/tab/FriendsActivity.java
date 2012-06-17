package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kumulos.android.jsonclient.Kumulos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FriendsActivity extends Activity {

	List<FriendsInfo> friendsInfo = new ArrayList<FriendsInfo>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_lv);	
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendof", UserInfo.userEmailId);
		
		Kumulos.call("getFriendsList",params, new FriendsHandler(this));
	}
	
	//Menu ActionBar
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.friends_menu, 	menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pendingFriendsReq:
			startActivity(new Intent(this, AddFriendActivity.class));
			break;
		case R.id.goHomeFromFriendsActivityMenu:
			UserInfo.restartActivity(UserInfo.homeActivity);
			break;
		case R.id.friendsLogOut:
			Intent intent = new Intent(this, LoginPageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		default:
			break;
		}
		return true;
	}
}