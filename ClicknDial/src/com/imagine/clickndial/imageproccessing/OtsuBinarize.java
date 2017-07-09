package com.imagine.clickndial.imageproccessing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

public class OtsuBinarize {
	
	
	
	ImageView iv;
	
	Bitmap bitmap;
	
	public OtsuBinarize(Bitmap bt) {
		
		
		bitmap = bt;
		toGray();
		binarize();
		
		
	}
	
	
	private void toGray() {
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
	             
	             R = (int) (0.21 * R + 0.71 * G + 0.07 * B);
	             
	             bitmap.setPixel(x, y, Color.rgb(R, R, R));
	         }
        }

	}
	
	private int[] imageHistogram() {
		int[] histogram = new int[256];
		 
        for(int i=0; i<histogram.length; i++) 
        	histogram[i] = 0;
        
        int R;
		int pich = bitmap.getHeight();
		int picw = bitmap.getWidth();
		 
		int[] pix = new int[picw * pich];
        bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
        
        for (int y = 0; y < pich; y++){
	         for (int x = 0; x < picw; x++){
	        	 int index = y * picw + x;
	             R = (pix[index] >> 16) & 0xff;     //bitwise shifting
	             
	             histogram[R]++ ;
	         }
        }
        return histogram;
	}
	
	
	private int otsuTreshold() {
		int[] histogram = imageHistogram();
		int total = bitmap.getHeight() * bitmap.getWidth();
		
		float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
 
        return threshold;
	}
	
	
	
	private void binarize() {
        int newPixel;
 
        int threshold = otsuTreshold();
        
        int R;
		int pich = bitmap.getHeight();
		int picw = bitmap.getWidth();
		 
		int[] pix = new int[picw * pich];
        bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
        
        for (int y = 0; y < pich; y++){
	         for (int x = 0; x < picw; x++){
	        	 int index = y * picw + x;
	             R = (pix[index] >> 16) & 0xff;     //bitwise shifting
	             
	             if(R > threshold) {
	                    newPixel = 255;
	                }
	                else {
	                    newPixel = 0;
	                }
	             
	             bitmap.setPixel(x, y, Color.rgb(newPixel, newPixel, newPixel));
	         }
        }

	}

}
