package com.thrid.party.codec.demo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;

public class Utilty {

    private static Utilty instance = new Utilty();

    public static Utilty getInstance() {
        return instance;
    }

    public static final int MIN_MID_VALUE = 1;
    public int mid;
    public int functioncode;

    public static final int MAX_MID_VALUE = 65535;
    private static final int[] CRC16Table = { 0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241, 0xC601,
			0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440, 0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1,
			0xCE81, 0x0E40, 0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841, 0xD801, 0x18C0, 0x1980,
			0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
			0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341, 0x1100,
			0xD1C1, 0xD081, 0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240, 0x3600, 0xF6C1,
			0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441, 0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80,
			0xFE41, 0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981, 0x2940,
			0xEB01, 0x2BC0, 0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40, 0xE401,
			0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0,
			0x2080, 0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240, 0x6600, 0xA6C1, 0xA781,
			0x6740, 0xA501, 0x65C0, 0x6480, 0xA441, 0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
			0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840, 0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01,
			0x7BC0, 0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40, 0xB401, 0x74C0,
			0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080,
			0xB041, 0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741,
			0x5500, 0x95C1, 0x9481, 0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40, 0x5A00,
			0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841, 0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1,
			0x8A81, 0x4A40, 0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581,
			0x4540, 0x8701, 0x47C0, 0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040 };
    /**long转byte数组
	 * @param num
	 * @return
	 */
	public  byte[] long2bytes(long number) {  
		 long temp = number;   
	        byte[] b = new byte[8];   
	        for (int i = 0; i < b.length; i++) {   
	            b[i] = new Long(temp & 0xff).byteValue();//   
	            temp = temp >> 8; // 向右移8位   
	        }   
	        return b;   
	}  
    /**
     * 截取byte数组
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public  byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
    /**
     * byte转有符号整型
     * @param binaryData
     * @return
     */
    public  int byteArray2Int(byte b1, byte b2) {
    	    return (b2 << 8) | (b1 & 0xFF);
    }
    
    /**
     * 把16进制字符串转换成字节数组
     * @param hexString
     * @return byte[]
     */
    public  byte[] hexStringToByte(String hex) {
     int len = (hex.length() / 2);
     byte[] result = new byte[len];
     char[] achar = hex.toCharArray();
     for (int i = 0; i < len; i++) {
      int pos = i * 2;
      result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
     }
     return result;
    }
    private  int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
     }
    /**
     * 利用 {@link java.nio.ByteBuffer}实现byte[]转long
     * @param input
     * @param offset 
     * @param littleEndian 输入数组是否小端模式
     * @return
     */
    public long bytesToLong(byte[] binaryData, int offset,int length, boolean littleEndian) {
    	byte[] input = new byte[8];
    	System.arraycopy(binaryData,offset,input,0, length);
		while(length<8) {
			input[length++]=(byte)0x00;
		}
        // 将byte[] 封装为 ByteBuffer 
        ByteBuffer buffer = ByteBuffer.wrap(input,0,8);
        if(littleEndian){
            // ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
            // ByteBuffer 默认为大端(BIG_ENDIAN)模式 
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getLong();
    }
    //byte转无符号整型,小端对齐（低位在前）
    public Integer bytes2Int(byte[] b, int start, int length) {
        int sum = 0;
        int end = start + length;
        for (int k = start; k < end; k++) {
            int n = ((int) b[k]) & 0xff;
//            n <<= (--length) * 8;
           n <<= (k - start) * 8;
            sum += n;
        }
        return sum;
    }

    //����תΪ�ֽ���
    public  byte[] int2Bytes(int value, int length) {
        byte[] b = new byte[length];
        for (int k = 0; k < length; k++) {
            b[length - k - 1] = (byte) ((value >> 8 * k) & 0xff);
        }
        return b;
    }
    //�ж�mid�Ƿ���Ч
    public boolean isValidofMid(int mId) {
        if (mId < MIN_MID_VALUE || mId > MAX_MID_VALUE) {
            return false;
        }
        return true;
    }

    /***
     * ���ֽ���ת��Ϊ16�����ַ���
     */
    public String parseByte2HexStr(byte[] buf) {
        if (null == buf) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    /*
     * 将byte型数字转换为用int[]表示的2进制
     */
    public  int[] byte2Binary(byte n){
    	
    	int[] binaryArray = new int[8];
		int j = -1;
		for(int i = 7;i >= 0; i--){
//			>>> i 表示向右移动i位，高位补0
			binaryArray[++j] = n >>> i & 1;
		}
		return binaryArray;
    }
    /*
    /*
	 * CRC16查表算法 https://www.cnblogs.com/lvdongjie/p/4444494.html 由c翻过来的 经验证可用
	 */
	public byte[] CRC16(byte[] toVerify) {

		int result = 0xFFFF;
		int tableNo = 0;

		for (int i = 0; i < toVerify.length; i++) {
			tableNo = ((result & 0xff) ^ (toVerify[i] & 0xff));
			result = ((result >> 8) & 0xff) ^ CRC16Table[tableNo];
		}

		return int2Bytes(result, 2);
	}
    
    public byte[] str2Bytes(String str){
    	byte[] bytes = new byte[2];
    	try {
        	byte[] fullStr = str.getBytes();
        	bytes[0] = fullStr[0];
        	bytes[1] = fullStr[1];
		} catch (Exception e) {
			// TODO: handle exception
			return bytes;
		}
    
    	return bytes;
    }
    
    
    public boolean isValid(byte[] requestBytes){
    	
    	if(requestBytes == null || requestBytes.length <= 2){
    		return false;
    	}
    	byte[] requestNoCrc = Arrays.copyOf(requestBytes, requestBytes.length - 2);
    	byte[] requestCrc = Arrays.copyOfRange(requestBytes, requestBytes.length - 2, requestBytes.length);
    	byte[] valideCrc = CRC16(requestNoCrc);
    	if(Arrays.equals(requestCrc, valideCrc)){
    		return true;
    	}
    	return false;
    }
    
     public byte[]  invert( byte[] origin,int from ,int length){
    	 byte temp;
    	 for (int i = 0; i < length/2 ; i++) {
    		 temp = origin[from+i];
    		 origin[from+i] =  origin[from+length-i-1];
    		 origin[from+length-i-1] = temp;
		}
		return origin;
     }
     
     
     
     public  byte[] positionFormat(byte[] origin){
    		
     	if(origin == null || origin.length ==0){
     		return new byte[0];
     	}
     	
     	if (origin.length == 26) {
     		byte temp;
     		
     		temp = origin[0];
     		origin[0] = origin[1];
     		origin[1] = temp;
     		
     		temp = origin[5];
     		origin[5] = origin[6];
     		origin[6] = temp;
     		
     		temp = origin[8];
     		origin[8] = origin[9];
     		origin[9] = temp;
     		
     		temp = origin[10];
     		origin[10] = origin[11];
     		origin[11] = temp;
     		
//     		15 16 17 18
     		temp = origin[15];
     		origin[15] = origin[18];
     		origin[18] = temp;
     		temp = origin[16];
     		origin[16] = origin[17];
     		origin[17] = temp;
     		
//     		19 20
     		temp = origin[19];
     		origin[19] = origin[20];
     		origin[20] = temp;
//     		22 23
     		temp = origin[22];
     		origin[22] = origin[23];
     		origin[23] = temp;
//     		24 25
     		temp = origin[24];
     		origin[24] = origin[25];
     		origin[25] = temp;
     		
     		
 		} else if(origin.length == 64){
 			
 			byte temp;
//     		0 1
     		temp = origin[2];
     		origin[2] = origin[3];
     		origin[3] = temp;
// 			5 6 
     		temp = origin[4];
     		origin[4] = origin[5];
     		origin[5] = temp;
     		
     		temp = origin[6];
     		origin[6] = origin[7];
     		origin[7] = temp;
     		
     		temp = origin[8];
     		origin[8] = origin[11];
     		origin[11] = temp;
     		
//     		13 14
     		temp = origin[9];
     		origin[9] = origin[10];
     		origin[10] = temp;
     		
     		
     		
//     		13 14
     		temp = origin[14];
     		origin[14] = origin[17];
     		origin[17] = temp;
//     		15 16 17 18
     		temp = origin[15];
     		origin[15] = origin[16];
     		origin[16] = temp;
     		
     		
     		temp = origin[18];
     		origin[18] = origin[19];
     		origin[19] = temp;
//     		19 20
     		temp = origin[20];
     		origin[20] = origin[21];
     		origin[21] = temp;
//     		/40 22 23 24 25 26 27 28 29 30
//     		31 32 33 34 35 36 37 37 39 /21
     		
     		 for (int start = 26, end = 41; start < end; start++, end--) {
     	        byte t = origin[end];
     	        origin[end] = origin[start];
     	        origin[start] = t;
     	    }
     		for (int start = 42, end = 57; start < end; start++, end--) {
     	        byte t = origin[end];
     	        origin[end] = origin[start];
     	        origin[start] = t;
     	    }
     		temp = origin[62];
     		origin[62] = origin[63];
     		origin[63] = temp;
 		}
 		else if(origin.length == 46){
 			
 			byte temp;
//     		0 1
     		temp = origin[2];
     		origin[2] = origin[3];
     		origin[3] = temp;
// 			5 6 
     		temp = origin[4];
     		origin[4] = origin[5];
     		origin[5] = temp;
     		
     		temp = origin[6];
     		origin[6] = origin[7];
     		origin[7] = temp;
     		
     		temp = origin[8];
     		origin[8] = origin[11];
     		origin[11] = temp;
     		
//     		13 14
     		temp = origin[9];
     		origin[9] = origin[10];
     		origin[10] = temp;
//     		15 16 17 18
     		temp = origin[12];
     		origin[12] = origin[13];
     		origin[13] = temp;
     		temp = origin[16];
     		origin[16] = origin[19];
     		origin[19] = temp;
//     		19 20
     		temp = origin[17];
     		origin[17] = origin[18];
     		origin[18] = temp;
//     		/40 22 23 24 25 26 27 28 29 30
//     		31 32 33 34 35 36 37 37 39 /21
     		temp = origin[22];
     		origin[22] = origin[23];
     		origin[23] = temp;
     		
     		temp = origin[24];
     		origin[24] = origin[27];
     		origin[27] = temp;
//     		origin = Utilty.getInstance().invert(origin, 21, 20);
//     		42 43
     		temp = origin[25];
     		origin[25] = origin[26];
     		origin[26] = temp;
     		
     		temp = origin[28];
     		origin[28] = origin[33];
     		origin[33] = temp;
     		
     		temp = origin[29];
     		origin[29] = origin[32];
     		origin[32] = temp;
     		
     		temp = origin[30];
     		origin[30] = origin[31];
     		origin[31] = temp;
     		
     		temp = origin[34];
     		origin[34] = origin[39];
     		origin[39] = temp;
     		
     		temp = origin[35];
     		origin[35] = origin[38];
     		origin[38] = temp;
     		
     		temp = origin[36];
     		origin[36] = origin[37];
     		origin[37] = temp;
     		
     		temp = origin[44];
     		origin[44] = origin[45];
     		origin[45] = temp;
 		}
		return origin;
    	 
     }
     /**
      * 车位检测CRC校验算法
     * @param ucCRC_Buf
     * @param ucBufLength
     * @return
     */
//    public byte[] CRC_Check(byte[] ucCRC_Buf,int ucBufLength){
//	     int uiX,uiY,uiCRC;
//	     char ucStart= 0;
//	     uiCRC = 0xFFFF;
//			if (ucBufLength <= 0 || ucStart > ucBufLength)
//				uiCRC = 0;
//			else {
//	
//				ucBufLength += ucStart;
//				for (uiX = (int) ucStart; uiX < ucBufLength; uiX++) {
//	
//					uiCRC = (int) (uiCRC ^ ucCRC_Buf[uiX]);
//					for (uiY = 0; uiY <= 7; uiY++) {
//						if ((uiCRC & 1) != 0)
//							uiCRC = (int) ((uiCRC>>1)^0xA001);
//						else
//							uiCRC = (int) (uiCRC >> 1);
//					}
//				}
//			}
//			return int2Bytes(uiCRC,2);
//    }
    public static void main(String[] args) throws Exception {
//    	System.out.println(Arrays.toString(Utilty.getInstance().byte2Binary((byte)0xF1)));
//    	byte[] a={0x01,0x00,0x01,0x2A,0x08,00,00,00,00,00,0x78,0x56,0x34,0x12,0x4E,0x61,0x74,0x75,0x72,0x61,0x6C,0x47,0x61,0x73,0x20,0x50,0x72,0x65,0x73,0x73,0x18,00,05,00,0x0F,00,0x0A,00,(byte) 0xF4,0x01,0x64,00,00,00,00,00};
//	a[29] =0;
//    	byte[] b;
//
//	b = Utilty.getInstance().CRC_Check(a,a.length);
//	System.out.println(Utilty.getInstance().parseByte2HexStr(b));
//	System.out.println(a.length);00
//    	 byte b1 = (byte) 0xFE;
//    	 byte b2 = (byte) 0x7F;
//    	byte[] a={0x08,0x00};
//    	Integer num=-1;
//    	String s=Integer.toBinaryString(num);	
    	//负数转十进制
//    	BigInteger bi = new BigInteger((long)num);
//		bi.toString(2);
//    	System.out.println(s);
//    	int a=Integer.valueOf(s,2);
    	//当二进制位负数时
//    	BigInteger qbi = new BigInteger(2, num.byteValue());
//    	  BigInteger qbi = new BigInteger(Byte.to, 2);
    	byte[] b = {(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,0x00,0x00,0x00,0x00};
    	byte[] a = { (byte) 0x13, (byte) 0x26, (byte) 0x27, (byte) 0x7d};
    	System.out.println(Utilty.getInstance().bytesToLong(a, 0, 4, true));
            BigDecimal data = new BigDecimal("2308783221‬");
            long l = data.subtract(new BigDecimal(Long.MAX_VALUE)).subtract(BigDecimal.valueOf(1)).longValue();
            System.out.println();
//		System.out.println(Utilty.getInstance().parseByte2HexStr(Utilty.getInstance().CRC16(a)));
    }
}
