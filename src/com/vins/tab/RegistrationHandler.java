package com.vins.tab;

import java.util.HashMap;

import android.content.Intent;
import android.widget.Toast;

import com.kumulos.android.jsonclient.Kumulos;
import com.kumulos.android.jsonclient.ResponseHandler;

public class RegistrationHandler extends ResponseHandler {

	RegisterPageActivity registerActivity;
	String functionName;
	
	public RegistrationHandler(RegisterPageActivity obj, String fname)
	{
		registerActivity = obj;
		functionName = fname;
	}
	
	public void displayMessage(String message)
	{
		Toast toast = Toast.makeText(registerActivity.getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	@Override
	public void didCompleteWithResult(Object result)
	{
		if(functionName.equals("registerUser"))
		{
			if(result != null)
				registerActivity.startActivity(new Intent(registerActivity, HomePageActivity.class));
			else
				displayMessage("Registration Failed !");
		}
		else if(functionName.equals("getNumberOfUsers"))
		{
			if(result != null)
			{
				int n = Integer.parseInt(result.toString());
				
				System.out.println("value of n = " + n);
				
				if(n!=0)
				{
					displayMessage("Email already registered.");
					return;
				}
				
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("email",registerActivity.email_TF.getText().toString());
				params.put("username",registerActivity.name_TF.getText().toString());
				params.put("password",registerActivity.passwordTF.getText().toString());
				
				Kumulos.call("registerUser", params, new RegistrationHandler(registerActivity, "registerUser"));
			}
			else
				System.out.println("result is null.");
		}
		else
		{
			displayMessage("Invalid Function name.");
		}
	}	
}
