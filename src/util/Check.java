package util;

import java.util.Objects;
public class Check {
	public static void nullCheck(Object obj) throws KeyException{
		if(Objects.isNull(obj)){
			throw new KeyException("Given value is null");
		}
	}
	public static void emptyCheck(String obj) throws KeyException{
		if(obj.isEmpty()){
			throw new KeyException("Empty value is given");
		}
	}
}
