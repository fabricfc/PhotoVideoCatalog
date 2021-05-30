package PhotoVideoCatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class testMD5 {
	
	public static void main(String[] args){
		String str1 = "E:\\OneDrive\\fotos\\2019\\IMG-20190714-WA0003.jpg";
		String str2 = "D:\\fotos2\\201907\\IMG-20190714-WA0003.jpg";
		File fileEntry = new File(str1);
		File dstFile = new File(str2);
		
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("MD5 Hash alg not found.");
			e.printStackTrace();
		}
				
		
		String checksumSrc = "noini";
		String checksumDst = "noini";
		try {
			checksumSrc = getFileChecksum(md5Digest, fileEntry);
			checksumDst = getFileChecksum(md5Digest, dstFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(checksumSrc + "\n" + checksumDst);
	}
	
	
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(file);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0; 
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    };
	     
	    //close the stream; We don't need it now.
	    fis.close();
	     
	    //Get the hash's bytes
	    byte[] bytes = digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    //return complete hash
	   return sb.toString();
	}

}
