package com.github.znwabudike.androidpng2bmp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.content.Context;
import android.content.res.Resources;

import com.github.znwabudike.alogger.Log;






/**
 * Created by znwabudike on 10/22/14.
 *
 * This class houses the methods used to convert PNG to BMP
 */
public class Converter {

	//sizes (in bytes)
	int dwordSize = 4;

	public boolean convert(byte[] stream){
		//test

		if(isPNGHeader(stream)){
			return true;
		}
		return false;
	}

	public boolean isBM(byte[] stream) {
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

	public byte[] getHeader(byte[] stream){
		byte[] bytes;

		for(byte b : stream){

		}
		return null;
	}

	public void getIdentifier(){
		int offset = 0;

	}

	private static byte fromHexPair(final String pair){
		if (pair.length() / 2. != 0) 
			throw new IllegalArgumentException("Input string pair must be 2 characters");


		return 0;
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

	public void log(String text){
		Log.v("frump", text);
	}
}
