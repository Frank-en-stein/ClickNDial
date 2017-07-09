package com.imagine.clickndial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Tutorial extends Activity implements OnClickListener {
	
	ImageView iv;
	private static int counter;
	public static boolean is_opening_tutorial_1st_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		
		
		
		iv = (ImageView) findViewById(R.id.iv_tutorial);
		
		iv.setOnClickListener(this);
		
		if(is_opening_tutorial_1st_time==true){
			Toast.makeText(this, "Lets take a tour of Click n Dial.", Toast.LENGTH_LONG).show();
			counter = 1;
			iv.setImageResource(R.drawable.t1);
			is_opening_tutorial_1st_time = false;
		}
		else {
			if(counter == 1)
				iv.setImageResource(R.drawable.t1);
			else if(counter == 2)
				iv.setImageResource(R.drawable.t2);
			else if(counter == 3)
				iv.setImageResource(R.drawable.t3);
			else if(counter == 4)
				iv.setImageResource(R.drawable.t4);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		counter++;
		if(counter == 2)
			iv.setImageResource(R.drawable.t2);
		else if(counter == 3)
			iv.setImageResource(R.drawable.t3);
		else if(counter == 4)
			iv.setImageResource(R.drawable.t4);
		else
			finish();
		
	}

}
