package com.github.znwabudike.androidpng2bmp;

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
    	int headerLength = isPNGHeader(stream);
    	if(headerLength == 8){
    		
    	}
        return false;
    }
    
    public boolean isBM(byte[] stream) {
    	
    	byte a = fromHexString("42")[0];
    	byte b = fromHexString("4D")[0];
		return ((stream[0] == a) && (stream[1] == b));
	}
    
    public int isPNGHeader(byte[] stream){
    	String[] pngStart = {"89","50","4E","47","OD","OA","1A","0A"};
    	
    	for(int i = 0; i < pngStart.length; i++){
    		if((stream[i]+"").compareTo(pngStart[i]) != 0){
    			return 0;
    		}
    	}
    	return pngStart.length;
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
}
