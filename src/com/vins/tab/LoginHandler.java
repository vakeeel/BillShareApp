package com.vins.tab;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Intent;

import com.kumulos.android.jsonclient.ResponseHandler;

public class LoginHandler extends ResponseHandler {

	LoginPageActivity loginactivity;
	
	public LoginHandler(LoginPageActivity obj)
	{
		loginactivity = obj;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void didCompleteWithResult(Object result)
	{
		if(result == null)
		{
			loginactivity.errorMsg.setText("Login Failed !");
			return;
		}
		
		ArrayList<Object> list = (ArrayList<Object>) result;

		LinkedHashMap<String, Object> record = (LinkedHashMap<String, Object>) list.get(0);
		
		String password = record.get("password").toString();
		
		if(loginactivity.password.equals(password))
		{
			loginactivity.errorMsg.setText("");
			UserInfo.userEmailId = loginactivity.loginname;
			UserInfo.username = record.get("username").toString();
			loginactivity.startActivity(new Intent(loginactivity, HomePageActivity.class));
		}
		else
			loginactivity.errorMsg.setText("Login Failed !");
	}
	
}
