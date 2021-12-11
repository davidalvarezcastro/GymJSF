package es.ubu.asi.utils;

import java.util.ResourceBundle;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class Properties {
	private ResourceBundle properties;

	public Properties() {
		super();
		properties = ResourceBundle.getBundle("es.ubu.asi.msgs");
	}
	
	public String getMsg(String key) {
		return properties.getString(key);
	}
}
