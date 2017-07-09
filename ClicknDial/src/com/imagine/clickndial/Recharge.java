package com.imagine.clickndial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Recharge extends Activity implements OnItemClickListener {
	
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		
		String[] items_operators_list = {
				"Grameen Phone",
				"Banglalink",
				"Airtel",
				"Robi",
				"Teletalk"
		};
		
		lv = (ListView) findViewById(R.id.lv_oparators);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items_operators_list));
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MainOCR.is_opning_mainocr_1st_time = true;
		Intent intent = new Intent(Recharge.this, MainOCR.class);
		intent.putExtra("isCalling", false);
		intent.putExtra("opNum", (int)arg3);
		startActivity(intent);
	}

}
