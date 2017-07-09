package com.imagine.clickndial.imageproccessing;

import android.graphics.Bitmap;

public class ImageProcess {
	 public Bitmap bitmap;
	 
	 
	 public ImageProcess(Bitmap bt) {
		bitmap = bt;
		
		//int offset = 10;
		//int blackMargin = 2;
		//float setBrightness = (float).5; //.01~.99
		
		Filter filter = new Filter(bitmap);
		filter.convertToMonochromatic();
		//filter.resizeImage(600, 400);
		float maxBrightness = filter.analyze();
		
		if(maxBrightness >= .95) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.16));
		else if(maxBrightness >= .9) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.14));
		else if(maxBrightness >= .85) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.157));
		else if(maxBrightness >= .8) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.192));
		else if(maxBrightness >= .75) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.197));
		else if(maxBrightness >= .7) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.2));
		else if(maxBrightness >= .65) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.18));
		else if(maxBrightness >= .6) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.2));
		else if(maxBrightness >= .55) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.2));
		else if(maxBrightness >= .5) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.2));
		else if(maxBrightness >= .45) filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.19));
		else filter.analyze((float)((float)((int)(filter.setBrightness*100))/100.0-.2));

		
		bitmap = filter.bitmap;
		
		OtsuBinarize otsu = new OtsuBinarize(bitmap);
		
		bitmap = otsu.bitmap;
	}

}
