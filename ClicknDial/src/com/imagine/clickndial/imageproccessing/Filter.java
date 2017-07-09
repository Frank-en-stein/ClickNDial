package com.imagine.clickndial.imageproccessing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class Filter {

	public Bitmap bitmap;
	int maxRed=0,maxGreen=0,maxBlue=0,minRed=300,minGreen=300,minBlue=300;
	int avgR=0,avgG=0,avgB=0;
	public float sumBrightness=0, setBrightness=0;
	public int pixelCount=0;
	
	public Filter(Bitmap bt) {
		bitmap = bt;
	}
	
	public void convertToMonochromatic() {
		int width, height;
	    height = bitmap.getHeight();
	    width = bitmap.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bitmap, 0, 0, paint);
	    bitmap = bmpGrayscale;
	    
	}
	
	
	public void resizeImage(int IMG_WIDTH, int IMG_HEIGHT) {
		if(bitmap.getHeight() > bitmap.getWidth()){
			int temp = IMG_WIDTH;
    		IMG_WIDTH = IMG_HEIGHT;
    		IMG_HEIGHT = temp;
		}
		
		bitmap = Bitmap.createScaledBitmap(bitmap, IMG_WIDTH, IMG_HEIGHT, true);
	}
	
	
	public void analyze(float setBrightness){
		int R,G,B;
		int pich = bitmap.getHeight();
		int picw = bitmap.getWidth();
		 
		int[] pix = new int[picw * pich];
        bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
        
        for (int y = 0; y < pich; y++){
	         for (int x = 0; x < picw; x++){
	        	 int index = y * picw + x;
	             R = (pix[index] >> 16) & 0xff;     //bitwise shifting
	             G = (pix[index] >> 8) & 0xff;
	             B = pix[index] & 0xff;
	             
	             maxRed = R>maxRed ? R : maxRed;
	             minRed = minRed>R ? R : minRed;
	             maxGreen = G>maxGreen ? G : maxGreen;
	             minGreen = minGreen>G ? G : minGreen;
	             maxBlue = B>maxBlue ? B : maxBlue;
	             minBlue = minBlue>B ? B : minBlue;
	             
	             float[] hsv = new float[3];
	             Color.RGBToHSV(R , G, B, hsv);
	             hsv[2]=(float)(hsv[2]>setBrightness ? setBrightness : hsv[2]);
	             bitmap.setPixel(x, y, Color.HSVToColor(hsv));
	         }
        }
	}
	
	
	public float analyze(){
		int R,G,B;
		float minBr=100, maxBr=0;
		int pich = bitmap.getHeight();
		int picw = bitmap.getWidth();
		 
		int[] pix = new int[picw * pich];
        bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
        
        for (int y = 0; y < pich; y++){
	         for (int x = 0; x < picw; x++){
	        	 int index = y * picw + x;
	             R = (pix[index] >> 16) & 0xff;     //bitwise shifting
	             G = (pix[index] >> 8) & 0xff;
	             B = pix[index] & 0xff;
	             
	             avgR+=R;
	             avgG+=G;
	             avgB+=B;
	             
	             float[] hsv = new float[3];
	             Color.RGBToHSV(R , G, B, hsv);
	             sumBrightness += hsv[2];
	             pixelCount++;
	             minBr = minBr > hsv[2] ? hsv[2] : minBr;
	             maxBr = maxBr < hsv[2] ? hsv[2] : maxBr;
	         }
        }
        
        avgR/=pixelCount;
		avgG/=pixelCount;
		avgB/=pixelCount;
		
		
		setBrightness = (float)(sumBrightness/pixelCount);
		
		return maxBr;
	}
}
