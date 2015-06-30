package com.dsh.m.util;

import java.util.Random;

import jodd.datetime.JDateTime;

public class OrderUtil {
	
	public static String generateOrderNo() {
		return "md"+basegenerate();
	}
	
	public static String generateSettleNo() {
		return "js"+basegenerate();
	}
	
	public static String generatePeriodNo() {
		return "zq"+basegenerate();
	}
	
	public static String basegenerate() {
		return new JDateTime().toString("YYYYMMDDhhmmss")+new Random().nextInt(100);
	}
	
	public static void main(String[] args) {
		System.out.println(generateOrderNo());
	}

}