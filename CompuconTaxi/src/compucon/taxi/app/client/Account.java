package compucon.taxi.app.client;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Account extends Activity{

	String fname = "", lname = "", city = "", address = "", address_num = "",
			municipality = "", number = "", lang = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	   
		
		Button clear = (Button) findViewById(R.id.clear);
		Button submit = (Button) findViewById(R.id.Submit);
	    
	    final SharedPreferences getData = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
	    
	     fname = getData.getString("fname", fname);
	     lname = getData.getString("lname", lname);
	     city = getData.getString("city", city);
	     address = getData.getString("address", address);
	     address_num = getData.getString("address_num", address_num);
	     municipality = getData.getString("municipality", municipality);
	     number = getData.getString("number", number);
	     
	     	((EditText) findViewById(R.id.fname)).setText(fname);
			((EditText) findViewById(R.id.lname)).setText(lname);
			((EditText) findViewById(R.id.city)).setText(city);
			((EditText) findViewById(R.id.address)).setText(address);
			((EditText) findViewById(R.id.number)).setText(address_num);
			((EditText) findViewById(R.id.municipality)).setText(municipality);
			((EditText) findViewById(R.id.mobile)).setText(number);
	    
	    submit.setOnClickListener(new View.OnClickListener() {
	    	
			@Override
			public void onClick(View v) {
				getData.edit().putString("fname", ((EditText) findViewById(R.id.fname)).getText().toString()).commit();
				getData.edit().putString("lname", ((EditText) findViewById(R.id.lname)).getText().toString()).commit();
				getData.edit().putString("city", ((EditText) findViewById(R.id.city)).getText().toString()).commit();
				getData.edit().putString("address", ((EditText) findViewById(R.id.address)).getText().toString()).commit();
				getData.edit().putString("address_num", ((EditText) findViewById(R.id.number)).getText().toString()).commit();
				getData.edit().putString("municipality", ((EditText) findViewById(R.id.municipality)).getText().toString()).commit();
				getData.edit().putString("number", ((EditText) findViewById(R.id.mobile)).getText().toString()).commit();
				
				Toast.makeText(Account.this, "Saved!", Toast.LENGTH_LONG).show();
				finish();
				
			}
		});
	    
	    clear.setOnClickListener(new View.OnClickListener() {
	    	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((EditText) findViewById(R.id.fname)).setText("");
				((EditText) findViewById(R.id.lname)).setText("");
				((EditText) findViewById(R.id.city)).setText("");
				((EditText) findViewById(R.id.address)).setText("");
				((EditText) findViewById(R.id.number)).setText("");
				((EditText) findViewById(R.id.municipality)).setText("");
				((EditText) findViewById(R.id.mobile)).setText("");
				
			}
		});

	}
	

}
