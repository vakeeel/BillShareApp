package com.vins.tab;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomePageAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
    private List<FriendsInfo> friendsInfo;
    
	public HomePageAdapter(Context context, List<FriendsInfo> friendsInfo) {
		this.context = context;
		this.friendsInfo = friendsInfo;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		FriendsInfo info = friendsInfo.get(position);
		
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.homepage_lv, null);
        }
        
        TextView userName = (TextView) convertView.findViewById(R.id.homePageFriendsTVID);
        userName.setText(info.getUserEmail());

        TextView owesYou = (TextView) convertView.findViewById(R.id.homePageOwesYouId);
        String owesYouInText = new Double(Math.round(info.getHowMuchHeOwesYou()*100.0)/100.0).toString();
        owesYou.setText(owesYouInText);
        
        TextView youOwe = (TextView) convertView.findViewById(R.id.homePageYouOweID);
        String youOweInText = new Double(Math.round(info.getHowMuchYouOwe()*100.0)/100.0).toString();
        youOwe.setText(youOweInText);
        
        return convertView;
	}

	@Override
	public void onClick(View arg0) {
		
	}

}
