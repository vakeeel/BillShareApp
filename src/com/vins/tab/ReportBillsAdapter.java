package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportBillsAdapter extends BaseAdapter {

	private Context context;
	private List<FriendsInfo> friendsInfo;
	private EditText amount;
	private TextView sharedAmount;
	
	public ArrayList<String> sharedPeople;
	
	int friendsParticipated = 0;

	public ReportBillsAdapter(Context context, List<FriendsInfo> friendsInfo, EditText amountTV, TextView sharedAmount) {
		this.context = context;
		this.friendsInfo = friendsInfo;
		this.amount = amountTV;
		this.sharedAmount = sharedAmount;
		
		sharedPeople = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return friendsInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return friendsInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public int pos = 0;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FriendsInfo info = friendsInfo.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.reportbills_lv, null);
		}
		TextView userName = (TextView) convertView.findViewById(R.id.selectWhoParticipatedTVID);
		userName.setText(info.getUserEmail());
		
		// Set the onClick Listener on this button
		final Button btnRemove = (Button) convertView.findViewById(R.id.addRemoveButton);
		btnRemove.setFocusableInTouchMode(false);
		btnRemove.setFocusable(false);
		btnRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("on click");
				System.out.println("amount on clicking = " + amount.getText().toString());
				
				LinearLayout layout = (LinearLayout)v.getParent();
				TextView tv = (TextView)layout.getChildAt(0);

				if(btnRemove.getText().toString().equalsIgnoreCase("add")) {					
					friendsParticipated++;
					double sharedAmountValue = Double.valueOf(amount.getText().toString())/(double)friendsParticipated;
					sharedAmount.setText(new Double(Math.round(sharedAmountValue*100.0)/100.0).toString());
					System.out.println("Add : " + tv.getText().toString());
					System.out.println("selected item : " + ((ReportBillsActivity)context).selectedItem);
					
					sharedPeople.add(tv.getText().toString());
					
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("billid", "xxx");
					params.put("email", tv.getText().toString());
					params.put("whopaid", ((ReportBillsActivity)context).selectedItem);
					params.put("amount", "0.0");
					
					Kumulos.call("createShareRecord", params, new ResponseHandler()
						{
							@Override
							public void didCompleteWithResult(Object result) 
							{
								btnRemove.setText("Remove");
							}
						}
					);
					
				}
				else {
					friendsParticipated--;
					btnRemove.setText("Add");
					double sharedAmountValue = (friendsParticipated <= 0)? 0.0 : Double.valueOf(amount.getText().toString())/(double)friendsParticipated;
					sharedAmount.setText(new Double(Math.round(sharedAmountValue*100.0)/100.0).toString());
					System.out.println("Remove : " + tv.getText().toString());
					System.out.println("selected item : " + ((ReportBillsActivity)context).selectedItem);
					
					sharedPeople.remove(tv.getText().toString());
					
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("billid", "xxx");
					params.put("email", tv.getText().toString());
					params.put("whopaid", ((ReportBillsActivity)context).selectedItem);
					
					Kumulos.call("deleteShareRecord", params, new ResponseHandler()
						{
							@Override
							public void didCompleteWithResult(Object result) 
							{
								btnRemove.setText("Add");
							}
						}
					);					
					
				}
			}
		});
		btnRemove.setTag(info);
		return convertView;

	}

}
