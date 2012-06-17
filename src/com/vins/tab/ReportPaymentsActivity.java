package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ReportPaymentsActivity extends Activity {

	List<FriendsInfo> paymentInfo = new ArrayList<FriendsInfo>();

	ArrayAdapter<String> adapter;
	EditText amountEV;
	String selectedItem;
	
//	private ReportPaymentsActivity activity;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reportpayments);

//		activity = this;
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
		
		amountEV =  (EditText) findViewById(R.id.AmountYouArePaying);
		
		Spinner spinner = (Spinner) findViewById(R.id.SelectWhomYourArePayingToID);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendof", UserInfo.userEmailId);		
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					selectedItem = adapter.getItem(arg2).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					selectedItem = adapter.getItem(0).toString();
				}
				
			}
		);
		
		//get all the users
		Kumulos.call("getFriendsList", params, new ResponseHandler()
			{
				@Override
				@SuppressWarnings("unchecked")	
				public void didCompleteWithResult(Object result) 
				{
					ArrayList<Object>friends = (ArrayList<Object>) result;
					for(int i=0;i<friends.size();i++)
					{
						LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) friends.get(i);						
						adapter.add(record.get("email").toString());
						paymentInfo.add(new FriendsInfo(record.get("username").toString(), record.get("email").toString()));
						float amount = Float.parseFloat(record.get("amount").toString());
						if(amount < 0.0f)
							paymentInfo.get(i).youOwe = (-1.0f) * amount;
						else
							paymentInfo.get(i).owesYou = amount;
					}
				}
			}
		);
	}
	
	private boolean checkIfNumber(String str)
	{
		try{
			Float.parseFloat(str);
			return true;
		}catch(NumberFormatException ex)
		{
			return false;
		}
	}
	
	private void displayMessage(String message)
	{
		Toast toast = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private String amount;
	private float originalAmount = 0.0f;
	public void savePayment(View v) {

		amount = ((EditText)findViewById(R.id.AmountYouArePaying)).getText().toString();
		String description = "this is test.";		
		
		if(!checkIfNumber(amount))
		{
			displayMessage("amount is not a number.");
			return;
		}
		
		if(description.equals(""))
		{
			displayMessage("description is empty.");
			return;
		}
		
		//insert it to database
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("whopaid", UserInfo.userEmailId);		
		params.put("towhom", selectedItem);
		params.put("description", description);
		params.put("amount", amount);
		
		Kumulos.call("reportPayment", params, new ResponseHandler()
			{
				@Override
				public void didCompleteWithResult(Object result)
				{
					if(result != null)
					{
						//get amount from friends table
						params.clear();
						
						int i=0;
						for(;i<paymentInfo.size();i++)
							if(paymentInfo.get(i).userEmail.equals(selectedItem)) 
								break;
						
						originalAmount = (paymentInfo.get(i).youOwe != 0.0f) ? (-1.0f)*paymentInfo.get(i).youOwe : paymentInfo.get(i).owesYou;
						originalAmount += Float.parseFloat(amount);
						
						//update amount to database
						params.clear();
						
						params.put("email", selectedItem);
						params.put("friendof", UserInfo.userEmailId);
						params.put("amount", String.valueOf(originalAmount));
						
						Kumulos.call("updateAmount", params, new ResponseHandler()
							{
								@Override
								public void didCompleteWithResult(Object result)
								{
									params.clear();
									params.put("email", UserInfo.userEmailId);
									params.put("friendof", selectedItem);
									params.put("amount", String.valueOf((-1.0f)*originalAmount));
									
									Kumulos.call("updateAmount", params, new ResponseHandler()
										{
											@Override
											public void didCompleteWithResult(Object result)
											{
												displayMessage("reported successfully!");
												UserInfo.restartActivity(UserInfo.homeActivity);
											}
										}		
									);									
								}
							}
						);						
					}
				}
			}
		);
	}	
}
