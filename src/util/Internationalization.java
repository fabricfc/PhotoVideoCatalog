package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Internationalization {

	ResourceBundle _messages;
	String _language;
	String _country;

	public Internationalization(String[] args)
	{
		Locale currentLocale = getCurrentLocale(args);
		_messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
	}
	
	public void setLanguage(String language) {
		this._language = language;
	}
	
	public void setCountry(String country) {
		this._country = country;
	}

	public Locale getCurrentLocale(String[] args) {
		if (args.length != 2) {
			setLanguage(new String("en"));
			setCountry(new String("US"));
		} else {
			setLanguage(new String(args[0]));
			setCountry(new String(args[1]));
		}

		Locale currentLocale;
		currentLocale = new Locale(this._language, this._country);
		return currentLocale;
	}
}
