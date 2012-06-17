package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class HomePageActivity extends Activity implements OnClickListener {

	Button goBtn;
	List<FriendsInfo> paymentInfo = new ArrayList<FriendsInfo>();
	ListView list;
	HomePageActivity activity; 	
	
	float amount = 0.0f;
	boolean isDone = true;
	int i=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		
		activity = this;
		UserInfo.homeActivity = this;
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
		
		goBtn = (Button) findViewById(R.id.goBtn);
		goBtn.setOnClickListener(this);

		list = (ListView)findViewById(R.id.homePageListViewID);
		list.setClickable(true);

		paymentInfo.clear();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendof", UserInfo.userEmailId);		
		
		Kumulos.call("getFriendsList", params, new ResponseHandler()
			{
				@Override
				@SuppressWarnings("unchecked")
				public void didCompleteWithResult(Object result) {
				
					//populating the friendsInfo List
					ArrayList<Object> friends = (ArrayList<Object>) result;
					
					float amount = 0.0f;
					
					for(int i=0;i<friends.size();i++)
					{
						LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) friends.get(i);						
						paymentInfo.add(new FriendsInfo(record.get("username").toString(), record.get("email").toString()));
						amount = Float.parseFloat(record.get("amount").toString());
						if(amount < 0.0f)
							paymentInfo.get(i).youOwe = (-1.0f) * amount;
						else
							paymentInfo.get(i).owesYou = amount;
					}
					
					HomePageAdapter adapter = new HomePageAdapter(activity, paymentInfo);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long index) {
							
						}
					});
					
					list.setAdapter(adapter);
				}
			}
		);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.homelogout, 	menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homePageLogOutMenuID:
			Intent intent = new Intent(this, LoginPageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}

	public void onClick(View v) {
		startActivity(new Intent(this, TabInitializationActivity.class));
	}	
	
	@Override
	public void onBackPressed() {
	}
}
