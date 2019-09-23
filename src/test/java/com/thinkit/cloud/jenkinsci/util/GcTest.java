package com.thinkit.cloud.jenkinsci.util;

public class GcTest {

	public static void main(String []args) {
		
		GcTest GcTest = new GcTest();
		GcTest.testA();
		int c = 0;
		System.gc();
	}
	
	public byte []    testA() {
		byte [] bypte = null;
		{
			 bypte = new byte [64*1024*1024];
		}
		int i=0;
		System.gc();
		
		return bypte;
	}
}
