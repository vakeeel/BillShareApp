package com.vins.tab;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

public class FriendsAdapter extends BaseAdapter implements OnClickListener{

	private Context context;
    private List<FriendsInfo> friendsInfo;
    
	public FriendsAdapter(Context context, List<FriendsInfo> friendsInfo) {
		this.context = context;
		this.friendsInfo = friendsInfo;
	}

	public int getCount() {
		return friendsInfo.size();
	}

	public Object getItem(int position) {
		return friendsInfo.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		 
		FriendsInfo info = friendsInfo.get(position);
	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = inflater.inflate(R.layout.friends, null);
	        }
	        TextView userName = (TextView) convertView.findViewById(R.id.listViewNameTF);
	        userName.setText(info.getUserName());

	        TextView userEmail = (TextView) convertView.findViewById(R.id.listViewEmailTF);
	        userEmail.setText(info.getUserEmail());

	        // Set the onClick Listener on this button
	        Button btnRemove = (Button) convertView.findViewById(R.id.listViewBtn);
	        btnRemove.setFocusableInTouchMode(false);
	        btnRemove.setFocusable(false);
	        btnRemove.setOnClickListener(this);
	        btnRemove.setTag(info);
	        return convertView;
	}

	 private FriendsInfo entry;
	    
	    public void onClick(View view) {
	        entry = (FriendsInfo) view.getTag();
	        
	        HashMap<String, String> params = new HashMap<String, String>();
	        params.put("email", entry.userEmail);
	        params.put("friendof", UserInfo.userEmailId);
	        
	        //write code to delete from database
	        Kumulos.call("deleteFriend",params, new ResponseHandler()
	            {
	                @Override
	                public void didCompleteWithResult(Object result)
	                {
	                    friendsInfo.remove(entry);
	                    params.clear();
                        
                        params.put("email", UserInfo.userEmailId);
                        params.put("friendof", entry.userEmail);
                        
                        Kumulos.call("deleteFriend", params, new ResponseHandler()
                            {
                                @Override
                                public void didCompleteWithResult(Object result)
                                {
                                    notifyDataSetChanged();
                                }
                            }
                        );
	                }
	            }
	        );
	    }
}
