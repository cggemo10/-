package com.rrja.carja.utils;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * 
 * description: 动态验证码生成<br/>
 * 
 * author: fanruifa <br/>
 * 
 * date: 2013-4-16 <br/>
 * 
 */
public class SecurityCodeUtil {

	private static final int DEFAULT_CODE_LENGTH = 4;
	private static final int DEFAULT_FONT_SIZE = 30;
	private static final int DEFAULT_LINE_NUMBER = 3;
	private static final int BASE_PADDING_LEFT = 5;
	private static final int RANGE_PADDING_LEFT = 10;
	private static final int BASE_PADDING_TOP = 15;
	private static final int RANGE_PADDING_TOP = 10;
	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 39;

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	private static SecurityCodeUtil securityCodeUtil = null;

	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;
	private int base_padding_left = BASE_PADDING_LEFT;
	private int base_padding_top = BASE_PADDING_TOP;
	private int range_padding_left = RANGE_PADDING_LEFT;
	private int range_padding_top = RANGE_PADDING_TOP;
	private int codeLength = DEFAULT_CODE_LENGTH;
	private int lineNumber = DEFAULT_LINE_NUMBER;
	private int fontSize = DEFAULT_FONT_SIZE;

	private String code;
	private int padding_left, padding_top;
	private Random random = new Random();

	private SecurityCodeUtil() {
	}

	public static SecurityCodeUtil getInstance() {
		if (securityCodeUtil == null) {
			securityCodeUtil = new SecurityCodeUtil();
		}
		return securityCodeUtil;
	}

	public String createCode() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}

	public Bitmap createCodeBitmap() {
		padding_left = 0;
		base_padding_left = width / codeLength;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		code = createCode();
		canvas.drawColor(randomColor());
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		paint.setColor(Color.BLUE);
		for (int i = 0; i < code.length(); i++) {
			randomTextStyle(paint);
			randomPadding(i);
			canvas.drawText(String.valueOf(code.charAt(i)), padding_left,
					base_padding_top + range_padding_top, paint);
		}
		for (int i = 0; i < lineNumber; i++) {
			drawLine(canvas, paint);
		}
		for (int i = 0; i < 255; i++) {
			drawPoints(canvas, paint);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bitmap;
	}

	public String getCode() {
		return code;
	}

	private int randomColor(int rate) {
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}

	private int randomColor() {
		return randomColor(1);
	}

	private void randomTextStyle(Paint paint) {
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean());
		float skewX = random.nextInt(11) / 10;
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX);
	}

	private void randomPadding() {
		padding_left += base_padding_left + random.nextInt(range_padding_left);
		// padding_top += base_padding_top + random.nextInt(range_padding_top);
	}

	private void randomPadding(int i) {
		padding_left = base_padding_left * i
				+ random.nextInt(range_padding_left);
	}

	private void drawLine(Canvas canvas, Paint paint) {
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}
	
	private void drawPoints(Canvas canvas, Paint paint){
		int color = randomColor();
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawPoint(stopX, stopY, paint);
	}

}
