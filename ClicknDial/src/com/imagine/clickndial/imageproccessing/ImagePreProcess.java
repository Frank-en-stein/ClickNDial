package com.imagine.clickndial.imageproccessing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImagePreProcess {
	
	public Bitmap bitmap;
	
	public ImagePreProcess(Bitmap bt) {
		bitmap = bt;
		
		resizeImage(600, 600);
		bitmap = Bitmap.createBitmap(bitmap, 175, 0, bitmap.getWidth()-400, bitmap.getHeight());
		
		Matrix matrix = new Matrix();

		matrix.postRotate(90);
		bitmap = Bitmap.createBitmap(bitmap,0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
	}
	
	private void resizeImage(int IMG_WIDTH, int IMG_HEIGHT) {
		if(bitmap.getHeight() > bitmap.getWidth()){
			int temp = IMG_WIDTH;
    		IMG_WIDTH = IMG_HEIGHT;
    		IMG_HEIGHT = temp;
		}
	}

}
