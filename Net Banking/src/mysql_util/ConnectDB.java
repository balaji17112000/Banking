package mysql_util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConnectDB {
	@SuppressWarnings("deprecation")
	public Object getDBConnect()  {
		String packageClass = null;
			try (BufferedReader file = new BufferedReader(new FileReader("/home/inc12/git/net_Banking/Net Banking/src/DBClass"))) {
				try {
					packageClass = file.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	try {
		Class<?> dataBase = Class.forName(packageClass);
		try {
			Object DBiInstance= null ;
				try {
					DBiInstance = dataBase.newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
				return DBiInstance;
		} catch ( SecurityException e) {
			e.printStackTrace();
		}
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	return null;
	}
}
