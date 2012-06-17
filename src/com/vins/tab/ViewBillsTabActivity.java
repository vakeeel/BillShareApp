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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewBillsTabActivity extends Activity {

	List<BillInfo> billInfo = new ArrayList<BillInfo>();
	ListView list;
	ViewBillsTabActivity activity;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewbills);
		
		UserInfo.billsActivity = this;
		activity = this;
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
		
		list = (ListView) findViewById(R.id.viewBillsLVID);
		list.setClickable(true);
		
		billInfo.clear();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("whopaid", UserInfo.userEmailId);		
		
		Kumulos.call("getListOfBills", params, new ResponseHandler()
			{
				@Override
				@SuppressWarnings("unchecked")
				public void didCompleteWithResult(Object result) 
				{
					ArrayList<Object> bills = (ArrayList<Object>) result;
					
					for(int i=0;i<bills.size();i++)
					{
						LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) bills.get(i);						
						billInfo.add(new BillInfo(record.get("billdescription").toString(),record.get("billamount").toString()));
					}
					
					ViewBillsTabAdapter adapter = new ViewBillsTabAdapter(activity, billInfo);
					list.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long index) {
							// do nothing...
						}
					});
					
					list.setAdapter(adapter);
					
				}
			}
		);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.viewbillsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.viewBillsReportBill:
			startActivity(new Intent(this, ReportBillsActivity.class));
			break;
		case R.id.viewBillsReportPayment:
			startActivity(new Intent(this, ReportPaymentsActivity.class));
			break;
		case R.id.viewBillsHomePageID:
			UserInfo.restartActivity(UserInfo.homeActivity);
			break;
		case R.id.viewBillsLogOut:
			Intent intent = new Intent(this, LoginPageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}

}
