
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import PhotoVideoCatalog.ExifMetadata;
import PhotoVideoCatalog.PhotoVideoCatalog;
import util.Internationalization;
import util.Os;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
//		String filestr; 
//		filestr = "E:\\Nextcloud\\fotos\\201608\\2016-08-21 21.58.18.jpg";
//		filestr = "E:\\Nextcloud\\fotos\\201801\\IMG_20180101_004135149.jpg";
//		filestr = "D:\\fotos\\2018\\VID_20180813_190214425.mp4";
//		filestr = "D:\\fotos\\201001FeriasBahiaDaTraicao\\11012010475.jpg";
//		filestr = "E:\\OneDrive\\fotos\\201601\\2016-01-17 11.09.58.jpg";
//		filestr = "E:\\On4eDrive\\fotos\\2019\\20190709_224201.jpg";
//		File file = new File(filestr);
//		System.out.println(ExifMetadata.extractDate(file));
		
		/*
		System.out.println("Before Format : " + file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		System.out.println("After Format : " + sdf.format(new Date(file.lastModified())));
		 */
		
		processDirectory(args);
	}

	

	private static void processDirectory(String[] args) {
		Internationalization internationalization = new Internationalization(args);
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", internationalization.getCurrentLocale(args));
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setDialogTitle(messages.getString("folderselection"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(null);
		
		String path = chooser.getSelectedFile().getPath();
		PhotoVideoCatalog pvc = new PhotoVideoCatalog(path, Os.getOsSlash());
		pvc.listFilesForFolder(true, true, new File(path));
	}
}
