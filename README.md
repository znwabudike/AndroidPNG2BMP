AndroidPNG2BMP
==============

Convert Android PNG to Windows Bitmap

Usage
    //get your png byte array...
    byte[] pngBytes = however();
    //new instance of Converter
    Converter converter = new Converter();
    //convert the bytes
    byte[] bmpBytes = converter.convert(pngBytes);
    //do whatever you want with them
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pathTo_yourFile));
    bos.write(bmpBytes);
    bos.flush();
    bos.close();
