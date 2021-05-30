package PhotoVideoCatalog;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoVideoCatalog {

	private String _fileRegex;
	
	private String _folderSlash; 	
	
	private File folder;

	private String getFolderSlash() {
		return _folderSlash;
	}

	public void setFolderSlash(String _folderSlash) {
		this._folderSlash = _folderSlash;
	}

	private String _PhotoVideoFolder;

	private String getPhotoVideoFolder() {
		return _PhotoVideoFolder;
	}

	public void setPhotoVideoFolder(String _PhotoVideoFolder) {
		this._PhotoVideoFolder = _PhotoVideoFolder;
	}

	public PhotoVideoCatalog(String folderPath, String folderSlash) {
		this.setPhotoVideoFolder(folderPath);
		this.setFolderSlash(folderSlash);
		this.setFolder(new File(this.getPhotoVideoFolder()));
	}

	public void listFilesForFolder(boolean createDirectories, boolean moveFiles, File actualFolder) {				 		
		for (File fileEntry : actualFolder.listFiles()) {
			if (! fileEntry.isDirectory())
				doTheFile(createDirectories, moveFiles, fileEntry);
			else {				
				listFilesForFolder(createDirectories, moveFiles, fileEntry);				
			}
		}
	}
	
	/*
	 * ref: https://howtodoinjava.com/java/io/sha-md5-file-checksum-hash/
	 * */
	private String getFileChecksum(MessageDigest digest, File file) throws IOException
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

	private void doTheFile(boolean createDirectories, boolean moveFiles, final File fileEntry) {
		String dstFolder;
		File dstFolderFile;
		MessageDigest md5Digest = null;
		try {
			md5Digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("MD5 Hash alg not found.");
			e.printStackTrace();
		}
		String checksumSrc, checksumDst;
		{
			if (fileEntry.getName().toUpperCase().matches("(.*(JPEG|JPG|AVI|MP4|PNG|HEIC|MKV))")) {
				//Directory exists?				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				Date filedate = ExifMetadata.extractDate(fileEntry);
				String newDateFolder = "";
				boolean skip = false;
				
				if (filedate != null)
					//exif metadata
					newDateFolder = sdf.format(filedate);					
				else {
					//only changes files with exif info
					skip = true;
					// Modifie entry
					//newDateFolder = sdf.format(new Date(fileEntry.lastModified()));
				}
				
				if (! skip) {
					//source stuff
					Path srcPath = Paths.get(fileEntry.getAbsolutePath());
					//destiny stuff
					//forced for while... #TODO fix this
					dstFolder = "d:\\fotos2" + getFolderSlash() + newDateFolder + getFolderSlash();														
					String dstFileStr = dstFolder + getFolderSlash() + fileEntry.getName();
					Path dstPath = Paths.get(dstFileStr);
	
					if (moveFiles || createDirectories) { //create if movefile is true 
						dstFolderFile = new File(dstFolder);		
						if (! dstFolderFile.isDirectory()) dstFolderFile.mkdir();
					}

					//System.out.println(fileEntry.getPath() + "  -->  " + destination);
					
					try {
//						boolean sameFileNDir = dstPath.toString().equals(fileEntry.getAbsolutePath().toString());
//						if (! sameFileNDir) { 							
//							System.out.println("Destino: " + dstPath + " / origem: " + fileEntry.getAbsolutePath());
//							moveFile(moveFiles, srcPath, dstPath);
							
//						} else { // do something if didn't exists in the destination							
							File dstFile = new File(dstFileStr);							
							if (dstFile.exists()){
								if (dstFile.length() != fileEntry.length()) {									
									System.out.println(dstFile.length() + " / " + fileEntry.length());
									moveFile(moveFiles, srcPath, getNewPath(dstFile));									
								} else {
									checksumSrc = getFileChecksum(md5Digest, fileEntry);
									checksumDst = getFileChecksum(md5Digest, dstFile);
									if (! checksumSrc.equals(checksumDst))
										moveFile(moveFiles, srcPath, getNewPath(dstFile));							
									else
										//removing the src file 
										rmFile(moveFiles, srcPath);
								}
									
							} else {
								moveFile(moveFiles, srcPath, dstPath);
							}
//						}
					} catch (Exception ex){}
				}
			}
		}
	}
	
	private Path getNewPath(File dstFile, int fileIndex) {
		// TODO Auto-generated method stub
		if (dstFile.exists())
		{			
			String oldFileName = dstFile.getAbsoluteFile().toString();
			int dotPos = oldFileName.lastIndexOf(".");			
			String prefix = oldFileName.substring(0, dotPos-1);
			String sufix = oldFileName.substring(dotPos+1, oldFileName.length());
			String newFileName = prefix + "-" + String.valueOf(fileIndex) + "-." + sufix;
			File newDstFile = new File(newFileName);
			return getNewPath(newDstFile, fileIndex+1);
		}
		return dstFile.toPath();		
	}

	private Path getNewPath(File dstFile) {
		// TODO Auto-generated method stub
		return getNewPath(dstFile, 0);
	}

	public void moveFile(boolean moveFiles, Path srcPath, Path dstPath) {
		if (moveFiles)
		{								
			//move file to the new folder					
			try {
				Files.move(srcPath, dstPath, REPLACE_EXISTING);
			} catch (Exception e) {
				System.out.println(srcPath + " > " + dstPath);
				e.printStackTrace();
			}
		}
	}
	
	public void rmFile(boolean moveFiles, Path srcPath) {
		if (moveFiles)
		{								
			//move file to the new folder					
			try {
				Files.delete(srcPath);
			} catch (Exception e) {
				System.out.println("Fail to delete: " + srcPath);
				e.printStackTrace();
			}
		}
	}

	private File getFolder() {
		return folder;
	}

	private void setFolder(File folder) {
		this.folder = folder;
	}
}
