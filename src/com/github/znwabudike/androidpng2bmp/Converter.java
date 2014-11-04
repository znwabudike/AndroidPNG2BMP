package com.github.znwabudike.androidpng2bmp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.znwabudike.alogger.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;


/**
 * Created by znwabudike on 10/22/14.
 *
 * This class houses the methods used to convert PNG to BMP
 */
public class Converter {

	//sizes (in bytes)
	int dwordSize = 4;

	private static final int BMP_WIDTH_OF_TIMES = 4;
	private static final int BYTE_PER_PIXEL = 3;

	public byte[] convert(byte[] stream){
		//test
		try {
			Bitmap bmp = makeAndroidBitmapFromPng(stream);
			byte[] bmpBytes = save(bmp);
			return bmpBytes;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if(isPNGHeader(stream)){
//			return true;
//		}
		return null;
	}

	public boolean isBMPHeader(byte[] stream) {
		System.out.println(new String(stream));
		byte a = fromHexString("42")[0];
		byte b = fromHexString("4D")[0];
		return ((stream[0] == a) && (stream[1] == b));
	}

	public boolean isPNGHeader(byte[] stream){
		String pngStart = "89504E470D0A1A0A";
		log(pngStart);
		int length = pngStart.length() / 2;

		String compareStr = bytesToHex(Arrays.copyOfRange(stream, 0, length));
		log(compareStr);
		return compareStr.compareTo(pngStart) == 0 ? true : false;
	}

	public byte[] getArrayFromResource(Context context, int resource) throws IOException{
		byte[] byteArray;
		Resources testRes = context.getResources();
		InputStream streamIn = (InputStream) testRes.openRawResource(resource);
		byteArray = new byte[streamIn.available()];
		for(int i = 0; i < byteArray.length; i++){
			byteArray[i] = (byte) streamIn.read();
		}
		return byteArray;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public Bitmap makeAndroidBitmapFromPng(byte[] bytes) throws IOException{
		
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	private static byte[] fromHexString(final String encoded) {
		if ((encoded.length() % 2) != 0)
			throw new IllegalArgumentException("Input string must contain an even number of characters");

		final byte result[] = new byte[encoded.length()/2];
		final char enc[] = encoded.toCharArray();
		for (int i = 0; i < enc.length; i += 2) {
			StringBuilder curr = new StringBuilder(2);
			curr.append(enc[i]).append(enc[i + 1]);
			result[i/2] = (byte) Integer.parseInt(curr.toString(), 16);
		}
		return result;
	}


	/**
	 *  derived from this source : https://github.com/ultrakain/AndroidBitmapUtil
	 *  and the answer to this SO question: http://stackoverflow.com/questions/22909429/android-save-a-bitmap-to-bmp-file-format
	 *  by ben75
	 * Android Bitmap Object to Window's v3 24bit Bmp Format File
	 * @param orgBitmap
	 * @param filePath
	 * @return file saved result
	 */
	public static byte[] save(Bitmap orgBitmap) throws IOException {
		long start = System.currentTimeMillis();
		if(orgBitmap == null){
			return null;
		}

		boolean isSaveSuccess = true;

		//image size
		int width = orgBitmap.getWidth();
		int height = orgBitmap.getHeight();

		//image dummy data size
		//reason : the amount of bytes per image row must be a multiple of 4 (requirements of bmp format)
		byte[] dummyBytesPerRow = null;
		boolean hasDummy = false;
		int rowWidthInBytes = BYTE_PER_PIXEL * width; //source image width * number of bytes to encode one pixel.
		if(rowWidthInBytes%BMP_WIDTH_OF_TIMES>0){
			hasDummy=true;
			//the number of dummy bytes we need to add on each row
			dummyBytesPerRow = new byte[(BMP_WIDTH_OF_TIMES-(rowWidthInBytes%BMP_WIDTH_OF_TIMES))];
			//just fill an array with the dummy bytes we need to append at the end of each row
			for(int i = 0; i < dummyBytesPerRow.length; i++){
				dummyBytesPerRow[i] = (byte)0xFF;
			}
		}

		//an array to receive the pixels from the source image
		int[] pixels = new int[width * height];

		//the number of bytes used in the file to store raw image data (excluding file headers)
		int imageSize = (rowWidthInBytes+(hasDummy?dummyBytesPerRow.length:0)) * height;
		//file headers size
		int imageDataOffset = 0x36; 

		//final size of the file
		int fileSize = imageSize + imageDataOffset;

		//Android Bitmap Image Data
		orgBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		//ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
		ByteBuffer buffer = ByteBuffer.allocate(fileSize);

		/**
		 * BITMAP FILE HEADER Write Start
		 **/
		buffer.put((byte)0x42);
		buffer.put((byte)0x4D);

		//size
		buffer.put(writeInt(fileSize));

		//reserved
		buffer.put(writeShort((short)0));
		buffer.put(writeShort((short)0));

		//image data start offset
		buffer.put(writeInt(imageDataOffset));

		/** BITMAP FILE HEADER Write End */

		//*******************************************

		/** BITMAP INFO HEADER Write Start */
		//size
		buffer.put(writeInt(0x28));

		//width, height
		//if we add 3 dummy bytes per row : it means we add a pixel (and the image width is modified.
		buffer.put(writeInt(width+(hasDummy?(dummyBytesPerRow.length==3?1:0):0)));
		buffer.put(writeInt(height));

		//planes
		buffer.put(writeShort((short)1));

		//bit count
		buffer.put(writeShort((short)24));

		//bit compression
		buffer.put(writeInt(0));

		//image data size
		buffer.put(writeInt(imageSize));

		//horizontal resolution in pixels per meter
		buffer.put(writeInt(0));

		//vertical resolution in pixels per meter (unreliable)
		buffer.put(writeInt(0));

		buffer.put(writeInt(0));

		buffer.put(writeInt(0));

		/** BITMAP INFO HEADER Write End */

		int row = height;
		int col = width;
		int startPosition = (row - 1) * col;
		int endPosition = row * col;
		while( row > 0 ){
			for(int i = startPosition; i < endPosition; i++ ){
				buffer.put((byte)(pixels[i] & 0x000000FF));
				buffer.put((byte)((pixels[i] & 0x0000FF00) >> 8));
				buffer.put((byte)((pixels[i] & 0x00FF0000) >> 16));
			}
			if(hasDummy){
				buffer.put(dummyBytesPerRow);
			}
			row--;
			endPosition = startPosition;
			startPosition = startPosition - col;
		}

//		FileOutputStream fos = new FileOutputStream(filePath);
	
//		fos.write(buffer.array());
//		fos.close();
//		Log.v("AndroidBmpUtil" ,System.currentTimeMillis()-start+" ms");

//		return isSaveSuccess;
		return buffer.array();
	}

	/**
	 * Write integer to little-endian
	 * @param value
	 * @return
	 * @throws IOException
	 */
	private static byte[] writeInt(int value) throws IOException {
		byte[] b = new byte[4];

		b[0] = (byte)(value & 0x000000FF);
		b[1] = (byte)((value & 0x0000FF00) >> 8);
		b[2] = (byte)((value & 0x00FF0000) >> 16);
		b[3] = (byte)((value & 0xFF000000) >> 24);

		return b;
	}

	/**
	 * Write short to little-endian byte array
	 * @param value
	 * @return
	 * @throws IOException
	 */
	private static byte[] writeShort(short value) throws IOException {
		byte[] b = new byte[2];

		b[0] = (byte)(value & 0x00FF);
		b[1] = (byte)((value & 0xFF00) >> 8);

		return b;
	}


	public void log(String text){
		Log.v("Converter", text);
	}

	public static String str2Hex(String ascii){
		StringBuilder hex = new StringBuilder();

		for (int i=0; i < ascii.length(); i++) {
			hex.append(Integer.toHexString(ascii.charAt(i)));
		}       
		return hex.toString().toUpperCase();
	} 

	public int hasEndPNG(byte[] pngByteArray) {
		String imageStr = bytesToHex(pngByteArray);
		String IEND = str2Hex("IEND");
		log(IEND);
		if (imageStr.contains(IEND)){
			return imageStr.indexOf(IEND);
		}
		return 0;
	}
}
