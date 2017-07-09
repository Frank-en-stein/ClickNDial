package com.imagine.clickndial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.imagine.clickndial.camera.MainCamera;
import com.imagine.clickndial.imageproccessing.ImagePreProcess;
import com.imagine.clickndial.imageproccessing.ImageProcess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.hardware.camera2.*;


public class MainOCR extends Activity {
	public static final String PACKAGE_NAME = "com.imagine.clickndial";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/ClicknDial/";
	
	public static final String lang = "eng";
	protected String _path;
	protected boolean _taken;
	
	protected static final String PHOTO_TAKEN = "photo_taken";
	
	Bitmap bitmap;
	
	int Red, Green, Blue;
	
	public static boolean is_opning_mainocr_1st_time;
	private boolean is_calling;
	private int op_num;

   
	public static void textFile(String str)
	{
		String fileName = DATA_PATH;
		
		File file = new File( fileName, "output.txt");
		String content = str;
		 
		//OutputStream outputStream = null;
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(content.getBytes(), 0, content.length());
			out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return;
				} else {
				}
			}
		}
		
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
			} catch (IOException e) {
			}
		}
//		String PC_PATH = "C:/Users/Avash/Desktop/java codes/CND/tess-two/assets/tesseract-ocr/";
//		if (!(new File(DATA_PATH + "tessdata/")).exists()) {
//			copyFile(PC_PATH+"eng.cube.bigrams", DATA_PATH+"tessdata/"+"eng.cube.bigrams");
//			copyFile(PC_PATH+"eng.cube.fold", DATA_PATH+"tessdata/"+"eng.cube.fold");
//			copyFile(PC_PATH+"eng.cube.lm", DATA_PATH+"tessdata/"+"eng.cube.lm");
//			copyFile(PC_PATH+"eng.cube.nn", DATA_PATH+"tessdata/"+"eng.cube.nn");
//			copyFile(PC_PATH+"eng.cube.params", DATA_PATH+"tessdata/"+"eng.cube.params");
//			copyFile(PC_PATH+"eng.cube.size", DATA_PATH+"tessdata/"+"eng.cube.size");
//			copyFile(PC_PATH+"eng.cube.word-freq", DATA_PATH+"tessdata/"+"eng.cube.word-freq");
//			copyFile(PC_PATH+"eng.tesseract_cube.nn", DATA_PATH+"tessdata/"+"eng.tesseract_cube.nn");
//			copyFile(PC_PATH+"eng.traineddata", DATA_PATH+"tessdata/"+"eng.traineddata");
//		}
		
		//CopyDirectory cpDir = new CopyDirectory("C:/Users/Avash/Desktop/tessdata", DATA_PATH);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainocr);

		_path = DATA_PATH + "/ocr.jpg";
		
		if(is_opning_mainocr_1st_time == true){
			startCameraActivity();
			is_opning_mainocr_1st_time = false;
		}
		is_calling = getIntent().getExtras().getBoolean("isCalling");
		
		if(is_calling == false)
			op_num = getIntent().getExtras().getInt("opNum");
	}
	
	
	
	protected void startCameraActivity() {
		//File file = new File(_path);
		//Uri outputFileUri = Uri.fromFile(file);

		//final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		

		Intent intent = new Intent(MainOCR.this, MainCamera.class);
		startActivityForResult(intent, 0);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == -1) {
			onPhotoTaken();
		} else {
			finish();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(MainOCR.PHOTO_TAKEN,_taken);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.getBoolean(MainOCR.PHOTO_TAKEN)){
			new Task().execute();
		}
		else recognizing_error();
	}
	
	

//////////////////image processing & tessaract handling////////////////////////// 	
	protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		bitmap = BitmapFactory.decodeFile(_path, options);
		
		try {
			ImagePreProcess ipp = new ImagePreProcess(bitmap);
			bitmap = ipp.bitmap;
			
			//image_process();
				ImageProcess ip = new ImageProcess(bitmap);
				bitmap = ip.bitmap;
			
		} catch (Exception e1) {
			recognizing_error();
		}

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
		}

		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		//String whiteList = "1234567890";
		//baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, whiteList);
		
		 String recognizedText = baseApi.getUTF8Text();
		textFile(recognizedText+"asd");
		baseApi.end();
		
		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^0-9]+", "");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			if(is_calling==true)
				calling(recognizedText);
			else
				recharging(recognizedText);
		}
		
		
		else {
			recognizing_error();
		}
		
		
		// Cycle done.
		//finish();
	}
	
	
///////////////////making call/////////////////////////////////////	
	private void calling(final String num_str){
		
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainOCR.this);
			alert_builder.setMessage("Find "+ num_str + " \nMake call?");
			alert_builder.setCancelable(false);
			alert_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+ num_str));
					startActivity(intent);
					finish();
				}
			});
			
			alert_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();	
				}
			});
			AlertDialog alertDialog = alert_builder.create();
			alertDialog.show();	
	}
	
	
	
///////////////////making call/////////////////////////////////////	
	private void recharging(String st){
	
		final String num_str;
		
		if(st.length() < 16){
			recognizing_error();
			return;
		}
		else if(st.length() > 16){
			st = st.substring(st.length()-16);
		}
		
		num_str = st;
		
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainOCR.this);
		alert_builder.setMessage("Find "+ num_str + " \nMake call?");
		alert_builder.setCancelable(false);
		alert_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String front_str = null;
				if(op_num == 0)
					front_str = "*555*";
				else if(op_num==1)
					front_str = "*123*";
				else if(op_num==2)
					front_str = "*787*";
				else if(op_num==3)
					front_str = "*222*";
				else if(op_num==4)
					front_str = "*151*";
				
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+ front_str + num_str + Uri.encode("#")));
				startActivity(intent);
				finish();
			}
		});
		
		alert_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();	
			}
		});
		AlertDialog alertDialog = alert_builder.create();
		alertDialog.show();	
	}	
	
//////////////////////////Retiring photo taking/////////////////////
	private void recognizing_error() {
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainOCR.this);
		alert_builder.setMessage("Something goes wrong.\nLets try again");
		alert_builder.setCancelable(false);
		alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(MainOCR.this, MainCamera.class);
				startActivityForResult(intent, 0);
				//finish();
			}
		});
		
		alert_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();	
			}
		});
		AlertDialog alertDialog = alert_builder.create();
		alertDialog.show();
	}
	
	private class Task extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			onPhotoTaken();
			return null;
		}
		
	}
	
}
