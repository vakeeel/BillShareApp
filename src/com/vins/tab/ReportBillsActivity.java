package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ReportBillsActivity extends Activity {

	List<FriendsInfo> paymentInfo = new ArrayList<FriendsInfo>();

	EditText amountTV;
	TextView sharedAmount;
	int friendsParticipated = 0;
	
	ListView list;
	ArrayAdapter<String> adapter;
	ReportBillsActivity activity;
	String selectedItem;
	
	ReportBillsAdapter reportBillsAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reportbills);
		
		activity = this;
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
		
		amountTV =  (EditText) findViewById(R.id.AmountPaid);
		sharedAmount = (TextView) findViewById(R.id.sharedAmountID);
		
		Spinner spinner = (Spinner) findViewById(R.id.SelectWhoPaidID);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
//			{
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				selectedItem = adapter.getItem(arg2).toString();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				selectedItem = adapter.getItem(0).toString();
//			}
//			
//			}
//		);		
		
		adapter.add(UserInfo.userEmailId);
		
		selectedItem = UserInfo.userEmailId;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendof", UserInfo.userEmailId);		
		
		paymentInfo.clear();
		paymentInfo.add(new FriendsInfo("You", UserInfo.userEmailId));
		
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
						//adapter.add(record.get("email").toString());
						paymentInfo.add(new FriendsInfo(record.get("username").toString(), record.get("email").toString()));
						float amount = Float.parseFloat(record.get("amount").toString());
						if(amount < 0.0f)
							paymentInfo.get(i).youOwe = (-1.0f) * amount;
						else
							paymentInfo.get(i).owesYou = amount;						
					}
					
					list = (ListView) findViewById(R.id.whoParticipatedListViewID);
					list.setClickable(true);

					//get all bills
					//paymentInfo = getData();
					
					reportBillsAdapter = new ReportBillsAdapter(activity, paymentInfo, amountTV, sharedAmount);
//					list.setOnItemClickListener(new OnItemClickListener() {
//
//						public void onItemClick(AdapterView<?> arg0, View view,
//								int position, long index) {
//							System.out.println("User Email : " + paymentInfo.get(position).userEmail);
//						}
//					});

					list.setAdapter(reportBillsAdapter);
				}
			}
		);
	}
	
	private int i=0;
//	private boolean isDone = true;
	private float originalAmount = 0.0f;
	
	public void saveReportBills(View v) {
		
		System.out.println("bill description : " + ((EditText)findViewById(R.id.Description)).getText().toString());
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("whopaid", selectedItem);
		params.put("billdescription", ((EditText)findViewById(R.id.Description)).getText().toString());		
		params.put("billamount", amountTV.getText().toString());
		
		Kumulos.call("reportBill", params, new ResponseHandler()
			{
				@Override
				public void didCompleteWithResult(Object result) 
				{
					params.clear();
					params.put("whopaid", selectedItem);
					
					Kumulos.call("getMaxBillId", params, new ResponseHandler()
						{
							@Override
							public void didCompleteWithResult(Object result) 
							{
								params.clear();
								//update all bills
								params.put("amount", sharedAmount.getText().toString());
								params.put("billid", result.toString());
								params.put("whopaid", selectedItem);
								params.put("oldid", "xxx");
								
								Kumulos.call("updateSharedBill", params, new ResponseHandler()
									{
										@Override
										public void didCompleteWithResult(Object result) 
										{
//											while(true)
//											{
//												if(i == reportBillsAdapter.sharedPeople.size()) 
//													break;
//												if(isDone)
//												{
//													isDone = false;
													
													if(reportBillsAdapter.sharedPeople.get(i).equals(UserInfo.userEmailId)) i++;
													
													if(i >= reportBillsAdapter.sharedPeople.size()) return;
													
													int j=0;
													for(;j<paymentInfo.size();j++)
														if(paymentInfo.get(j).userEmail.equals(reportBillsAdapter.sharedPeople.get(i)))
															break;
													
													originalAmount = (paymentInfo.get(j).youOwe != 0.0f) ? (-1.0f)*paymentInfo.get(j).youOwe : paymentInfo.get(j).owesYou;
													originalAmount += Float.parseFloat(sharedAmount.getText().toString());
													
													//update amount to database
													params.clear();
													
													params.put("email", reportBillsAdapter.sharedPeople.get(i));
													params.put("friendof", selectedItem);
													params.put("amount", String.valueOf(originalAmount));
													
													Kumulos.call("updateAmount", params, new ResponseHandler()
														{
															@Override
															public void didCompleteWithResult(Object result)
															{
																params.clear();
																params.put("email", selectedItem);
																params.put("friendof", reportBillsAdapter.sharedPeople.get(i));
																params.put("amount", String.valueOf((-1.0f)*originalAmount));
																
																Kumulos.call("updateAmount", params, new ResponseHandler()
																	{
																		@Override
																		public void didCompleteWithResult(Object result)
																		{
																			UserInfo.restartActivity(UserInfo.tabsActivity);
																		}
																	}
																);									
															}
														}
													);
//												}
//											}
											
//											finish();
										}	
									}
								);								
							}
						}	
					);
				}
			}
		);
	}
	
	public void onClickingCancel(View v) {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billid", "xxx");
		params.put("whopaid", selectedItem);
		
		Kumulos.call("deleteAllUnsavedEntries", params, new ResponseHandler()
			{
				@Override
				public void didCompleteWithResult(Object result) 
				{
					finish();
				}
			}
		);
	}
	
//	private List<FriendsInfo> getData() {
//		paymentInfo.add(new FriendsInfo("Vineeth", 20.0, 10.0));
//		paymentInfo.add(new FriendsInfo("Ananth", 0.0, 50.0));
//		paymentInfo.add(new FriendsInfo("Sai Yudhi", 0.0, 0.0));
//		paymentInfo.add(new FriendsInfo("Swatantra", 100.0, 0.0));
//		paymentInfo.add(new FriendsInfo("Kowshik", 5.41, 60.90));
//		return paymentInfo;
//	}
}
