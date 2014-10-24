package com.github.znwabudike.androidpng2bmp;

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
		String[] pngStart = {"89","50","4E","47","OD","OA","1A","0A"};
		if (stream == null) return false; else
			for(int i = 0; i < pngStart.length; i+=2){
				if (i  % 2 == 0){
					String bytestring = pngStart[i/2];	
					byte[] comparebytes =  new byte[]{stream[i], stream[i+1]};
					if(bytesToHex(comparebytes).compareTo(bytestring) != 0) {
						return false;
					}
				}
			}
		return true;
	}


	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
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
		Log.i(Converter.class.getSimpleName(), text);
	}
}
