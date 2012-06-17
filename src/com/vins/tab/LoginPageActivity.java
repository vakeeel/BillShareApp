package com.vins.tab;

import java.util.HashMap;

import com.kumulos.android.jsonclient.Kumulos;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPageActivity extends Activity {

	TextView errorMsg;
	String password;
	String loginname;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		errorMsg = (TextView) findViewById(R.id.textView2);
		
		Kumulos.initWithAPIKeyAndSecretKey("0xvbx9d144ctx14f99gmtmmmrnzq3nfq", "00bvy5cd", this);
	}

	public void onClickingLogin(View v) {
		
		loginname = ((EditText) findViewById(R.id.editText1)).getText().toString();
		password = ((EditText) findViewById(R.id.editText2)).getText().toString();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", loginname);
		
		Kumulos.call("getPassword", params, new LoginHandler(this));
	}

	public void onClickingRegister(View v) {
		startActivity(new Intent(this, RegisterPageActivity.class));
	}
}
