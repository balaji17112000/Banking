package mysql_util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public enum ConnectDB {
	INSTANCE;
	public static Object getDBConnect()  {
		String packageClass = null;
			try (BufferedReader file = new BufferedReader(new FileReader("/home/inc12/Eclipse/NetBanking/src/mysql_util/DBClass"))) {
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
					try {
						DBiInstance = dataBase.getDeclaredConstructor().newInstance();
					} catch (InvocationTargetException | NoSuchMethodException e) {
						e.printStackTrace();
					}
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
