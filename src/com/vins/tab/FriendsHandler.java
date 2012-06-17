package com.vins.tab;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.kumulos.android.jsonclient.ResponseHandler;

public class FriendsHandler extends ResponseHandler {

	FriendsActivity friendsActivity;
	
	public FriendsHandler(FriendsActivity obj)
	{
		friendsActivity = obj;
	}
	
	@Override
	@SuppressWarnings("unchecked")	
	public void didCompleteWithResult(Object result) {
		
		//populating the friendsInfo List
		ArrayList<Object> friends = (ArrayList<Object>) result;

		for(int i=0;i<friends.size();i++)
		{
			LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) friends.get(i);						
			friendsActivity.friendsInfo.add(new FriendsInfo(record.get("username").toString(), record.get("email").toString()));
		}
		
		// adding them to ListView
		ListView list = (ListView) friendsActivity.findViewById(R.id.listView1);
		list.setClickable(true);
		
		FriendsAdapter adapter = new FriendsAdapter(friendsActivity, friendsActivity.friendsInfo);
		list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long index) {
				//do nothing...
			}
		});

		list.setAdapter(adapter);
	}	
}
