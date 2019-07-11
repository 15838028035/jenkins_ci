package com.thinkit.cloud.jenkinsci.util;

public class FreemarkerStringUtil {

	
	public Boolean isTrue(Object ob) {
		
		if(ob  instanceof Integer  && ((Integer) ob).intValue() ==1) {
			return true;
		}
		
		return false;
	}
}
