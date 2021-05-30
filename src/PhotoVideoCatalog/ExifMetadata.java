package PhotoVideoCatalog;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class ExifMetadata {	
	
	public static Date extractDate(File file)  {		
		Date date = null;
		DateFormat dateFormatVideos = 
				new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		SimpleDateFormat dateFormatPics = 
				new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
//		processDirectory();
//		MetadataReading readm = new MetadataReading();
//		readm.readAndDisplayMetadata("E:\\Nextcloud\\fotos\\201801\\IMG_20180101_004135149.jpg");
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			
			for (Directory directory : metadata.getDirectories()) {
//				System.out.println(directory.getName() + "BEGIN=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	        //
	        // Each Directory stores values in Tag objects
	        //				
		        for (Tag tag : directory.getTags()) {
		        	// 306 == datetime
		        	// 256 == datetime
		        	switch (tag.getTagType()){
		        		case 306:
		        			try {						
								date = dateFormatPics.parse(tag.getDescription());
		//						System.out.println(date.toLocaleString() + " \\ " + tag.getDescription());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Problema:" + file.getAbsolutePath());
							}
		        			break;
		        		case 36867:
		        			try {						
								date = dateFormatPics.parse(tag.getDescription());
		//						System.out.println(date.toLocaleString() + " \\ " + tag.getDescription());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Problema:" + file.getAbsolutePath());
							}
		        			break;		        			
		        		case 256:
		//        			Format
		//        			Mon Aug 13 19:02:43 GFT 2018
		        			try {
		        				date = dateFormatVideos.parse(tag.getDescription());
		        			} catch (ParseException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Problema:" + file.getAbsolutePath());
							}
		//	        		System.out.println(dateFormat.parse(tag.getDescription()));
		        			break;
		        	}
//		       		System.out.printf("%s %s %s\n", tag.getTagType(), tag.getTagName(), tag.getDescription());
		        }
	//        System.out.println(directory.getName() + "END=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Problema: " + file.getAbsolutePath());
		}
		return date;
	}
}
