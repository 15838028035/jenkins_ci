package com.thinkit.cloud.jenkinsci.util;

import org.junit.Test;

public class JwtUtilTest {

	@Test
	public void testGenerateToken() {
		
		String token = JwtUtil.generateToken("malingbing", "22", 100000L);
		System.out.println(token);
	}

}
