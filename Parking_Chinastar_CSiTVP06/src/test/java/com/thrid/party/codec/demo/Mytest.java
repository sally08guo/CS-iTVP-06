package com.thrid.party.codec.demo;

import java.util.Arrays;

public class Mytest {

	
	public static void main_() {
//		System.out.println(Utilty.getInstance().bytes2Int(intToByteArray(59999), 2, 2));
//		System.out.println(Arrays.toString(intToByteArray(59999)) );
//		System.out.println( Utilty.getInstance().parseByte2HexStr(new byte[]{(byte) 0xAA}));
//		byte[] ba = {120,89};
		byte inte = -127;
		System.out.println(Integer.toBinaryString(inte));
		
		int n = 500;
		int[] binaryArray = new int[8];
		int j = -1;
		for(int i = 7;i >= 0; i--){
//			>>> i 表示向右移动i位，高位补0
			binaryArray[++j] = n >>> i & 1;
			System.out.print(n >>> i & 1);
		}
		System.out.println();
		System.out.println(Arrays.toString(binaryArray));
	}
	
	
	
	public static byte[] intToByteArray(int a) {  
	    return new byte[] {  
	        (byte) ((a >> 24) & 0xFF),  
	        (byte) ((a >> 16) & 0xFF),     
	        (byte) ((a >> 8) & 0xFF),     
	        (byte) (a & 0xFF)  
	    };  
	}
	public static void main(String[] args) {
//		main_();30 000000000440

	/*	byte[] beforevarify = new byte[5]; 
		beforevarify[0] = 0x30;
		beforevarify[1] = 0x00;
		beforevarify[2] = 0x00;
		beforevarify[3] = 0x00;
		beforevarify[4] = 0x00;
		byte[] varify = Utilty.getInstance().CRC16(beforevarify);
		byte[] aftervarify = new byte[7];
		aftervarify = Arrays.copyOf(beforevarify,7);
		aftervarify[5]=varify[0];
		aftervarify[6]=varify[1];
		
		System.out.println( Utilty.getInstance().parseByte2HexStr(aftervarify));*/
		
		
		
	}
}
