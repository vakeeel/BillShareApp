package com.vins.tab;

import java.util.HashMap;

import com.kumulos.android.jsonclient.Kumulos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPageActivity extends Activity implements OnClickListener{

	Button createAccountBtn;
	EditText name_TF;
	EditText email_TF;
	EditText passwordTF;
	EditText verifyPwdTF;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		name_TF = (EditText) findViewById(R.id.regPageNameTF);
		email_TF = (EditText) findViewById(R.id.regPageEmailTF);
		passwordTF = (EditText) findViewById(R.id.regPagePwdTF);
		verifyPwdTF = (EditText) findViewById(R.id.regPageVerifyPwdTF);
		
		createAccountBtn = (Button) findViewById(R.id.regPageIAgreeBtn);
		createAccountBtn.setOnClickListener(this);
		
	}
	
	public void onClick(View v){
		if(!(name_TF.getText().toString().equals("") || email_TF.getText().toString().equals("") || passwordTF.getText().toString().equals("") || verifyPwdTF.getText().toString().equals(""))) {
			if(passwordTF.getText().toString().equals(verifyPwdTF.getText().toString())){
				
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("email",email_TF.getText().toString());
				
				Kumulos.call("getNumberOfUsers", params, new RegistrationHandler(this, "getNumberOfUsers"));
			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "Password Does Not Match", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
//  )