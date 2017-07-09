package com.imagine.clickndial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		
		SharedPreferences tutorial_prep = getSharedPreferences("clickndialprep", 0);
		if(tutorial_prep.getBoolean("firstTime", true)==true){
			Tutorial.is_opening_tutorial_1st_time = true;
			Intent intent = new Intent(MainActivity.this, Tutorial.class);
			startActivity(intent);
			
			SharedPreferences.Editor editor = tutorial_prep.edit();
			editor.putBoolean("firstTime", false);
			editor.commit();
		}
		
		Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
		
		String[] items_main_list = {
				"Make Call",
				"Recharge"
		};
		
		ListView lv = (ListView) findViewById(R.id.lv_main);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items_main_list));
		lv.setOnItemClickListener(this);
	}

	
/////////////////////////options menu//////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.menu_tutorial){
			Tutorial.is_opening_tutorial_1st_time = true;
			Intent intent = new Intent(MainActivity.this, Tutorial.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
////////////////////on click list item///////////////////////////
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg3 == 0){
			MainOCR.is_opning_mainocr_1st_time = true;
			Intent intent = new Intent(MainActivity.this, MainOCR.class);
			intent.putExtra("isCalling", true);
			startActivity(intent);
		}
		else if(arg3 == 1){
			Intent intent = new Intent(MainActivity.this, Recharge.class);
			startActivity(intent);
		}
		
	}

}
