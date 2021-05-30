package util;

public class Os {

	/**
	 * Get operation system name 
	 * @return String
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	/**
	 * Get 
	 * @return
	 */
	public static boolean isWindows() {
		return getOSName().toLowerCase().contains("windows");		
	}
	
	public static String getOsSlash() {
		return isWindows()? "\\":"/";
	}
}
