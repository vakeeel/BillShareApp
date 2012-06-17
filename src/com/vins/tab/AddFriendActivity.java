package com.vins.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);
    }
    
    public void displayMessage(String message)
    {
        Toast toast = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    
    private String friendName;
    private String friendID;
    public void onClickingSave(View v) {
        
        friendName = "";
        friendID = ((TextView)findViewById(R.id.addFreindsEmailIDTFID)).getText().toString();
        
        if(friendID.equals(""))
            displayMessage("Email id is empty.");
        else if(friendID.equals(UserInfo.userEmailId))
            displayMessage("You can't add yourself as your friend.");
        else
        {
            //check if the user is already present
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email",friendID);
        
            /* modified */
            
            Kumulos.call("getUser", params, new ResponseHandler()
                {
                    
                    @Override
                    @SuppressWarnings("unchecked")
                    public void didCompleteWithResult(Object result)
                    {
                        ArrayList<Object> list = (ArrayList<Object>)result;
                        
                        if(list.size()==0)
                        {
                            displayMessage("No such user present.");
                            return;
                        }
                        
                        LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) list.get(0);
                        friendName = record.get("username").toString(); 
                        
                /* end */        
                        //check if already his friend
                        params.clear();
                        
                        params.put("email", friendID);
                        params.put("friendof", UserInfo.userEmailId);
                        
                        Kumulos.call("checkIfAlreadyFriend", params, new ResponseHandler()
                            {
                                @Override
                                public void didCompleteWithResult(Object result)
                                {
                                    ArrayList<Object> list = (ArrayList<Object>)result;
                                    if(list.size()!=0)
                                    {
                                        displayMessage("Already your friend.");
                                        return;
                                    }
                                    
                                    params.clear();
                                    
                                    params.put("username", friendName);
                                    params.put("email", friendID);
                                    params.put("friendof", UserInfo.userEmailId);
                                    params.put("amount","0.0");
                                    
                                    Kumulos.call("addAFriend", params, new ResponseHandler()
                                        {
                                            @Override
                                            public void didCompleteWithResult(Object result)
                                            {
                                                params.clear();
                                                
                                                params.put("username", UserInfo.username);
                                                params.put("email", UserInfo.userEmailId);
                                                params.put("friendof", friendID);
                                                params.put("amount", "0.0");
                                                
                                                Kumulos.call("addAFriend", params, new ResponseHandler()
                                                    {
                                                        @Override
                                                        public void didCompleteWithResult(Object result)
                                                        {
                                                            displayMessage("Friend Add Successfully.");
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
                }
            );
        }
        finish();
    }
}