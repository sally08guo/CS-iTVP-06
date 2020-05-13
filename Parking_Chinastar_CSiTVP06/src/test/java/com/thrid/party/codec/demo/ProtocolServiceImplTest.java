package com.thrid.party.codec.demo;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;

/**
 * Unit test for simple App.
 */
public class ProtocolServiceImplTest {

    private IProtocolAdapter protocolAdapter;

    @Before
    public void setProtocolAdapter() {
        this.protocolAdapter = new ProtocolAdapterImpl();
    }

    /**
     * 测试用例1：设备向平台上报数据。
     * <p>
     * <pre>
     * 设备上报数据:AA72000032088D0320623399
     * </pre>
     *
     * @throws Exception
     */
    
    
    @Test
    public void testDecodeDeviceReportData() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceReqByte);
        String str = objectNode.toString();
        System.out.println(Utilty.getInstance().isValid(deviceReqByte));
        System.out.println(str);
    }
@Test
    public void testDecodeDevicePowerOn()throws Exception {
    	byte[] deviceReqByte = initDevicePowerOnByte();
    	ObjectNode objectNode = protocolAdapter.decode(deviceReqByte);
        String str = objectNode.toString();
        System.out.println(Utilty.getInstance().parseByte2HexStr(deviceReqByte));
        System.out.println(Utilty.getInstance().isValid(deviceReqByte));
        System.out.println(str);
    }
    
    /**
     * 测试用例2：平台向设备下发控制命令:
     * <p>
     * <pre>
     * {
     * //"identifier": "123",
     * "msgType": "cloudReq",
     * "cmd": "SET_DEVICE_LEVEL",
     * "mid": 2016,
     * "paras": { "value": "10" },
     * "hasMore": 0
     * }
     * </pre>
     */
   @Test
    public void testEncodeIoTSendCommand() throws Exception {
        ObjectNode CloudReqObjectNode = initCloudReqObjectNode();
        byte[] outputByte = protocolAdapter.encode(CloudReqObjectNode);
        System.out.println("cloudReq output:" + parseByte2HexStr(outputByte));
    }

    /**
     * 测试用例3：设备对平台命令的应答消息 有命令短id
     * <p>
     * <pre>
     * 设备应答消息:AA7201000107E0
     *
     * <pre>
     *
     * @throws Exception
     */
//    @Test
    public void testDecodeDeviceResponseIoT() throws Exception {
        byte[] deviceRspByte = initDeviceRspByte();
        ObjectNode objectNode = protocolAdapter.decode(deviceRspByte);
        String str = objectNode.toString();
        System.out.println(str);
    }

    /**
     * 测试用例4：平台收到设备的上报数据后对设备的应答，如果不需要应答则返回null即可
     * <pre>
     * {
     * "identifier": "0",
     * "msgType": "cloudRsp",
     * "request": [AA,72,00,00,32,08,8D,03,20,62,33,99],
     * "errcode": 0,
     * "hasMore": 0
     * }
     *
     * <pre>
     *
     * @throws Exception
     */
    @Test
    public void testEncodeIoTResponseDevice() throws Exception {
        byte[] deviceReqByte = initDeviceReqByte();
        ObjectNode cloudRspObjectNode = initCloudRspObjectNode(deviceReqByte);
        byte[] outputByte2 = protocolAdapter.encode(cloudRspObjectNode);
        System.out.println("cloudRsp output:" + parseByte2HexStr(outputByte2));
    }

    public static String parseByte2HexStr(byte[] buf) {
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
     * 初始化：设备数据上报码流
     */
    private static byte[] initDeviceReqByte() {
    	
    	byte[] idBytes = Utilty.getInstance().int2Bytes(65511, 2);
        byte[] byteDeviceReq = new byte[34];
        
        byteDeviceReq[0] = (byte) 0xFA;//version
	    byteDeviceReq[1] = (byte) 0x00; //function code
	    byteDeviceReq[2] = (byte) 0x01; //id
        byteDeviceReq[3] = (byte) 0x01; //id
        byteDeviceReq[4] = (byte) 0x27;
        
        
        byteDeviceReq[5] = (byte) 0x43 ;
        byteDeviceReq[6] = (byte) 0x53 ;
        byteDeviceReq[7] = (byte) 0x2D ;
        byteDeviceReq[8] = (byte) 0x69 ;
        byteDeviceReq[9] = (byte) 0x54 ;
        byteDeviceReq[10] = (byte) 0x56 ;
        byteDeviceReq[11] = (byte) 0x50 ;
        byteDeviceReq[12] = (byte) 0x2D ;
        byteDeviceReq[13] = (byte) 0x30 ;
        byteDeviceReq[14] = (byte) 0x32 ;
        byteDeviceReq[15] = (byte) 0x4E ;
        byteDeviceReq[16] = (byte) 0x42 ;
        byteDeviceReq[17] = (byte) 0x35 ;
        byteDeviceReq[18] = (byte) 0x39 ;
        byteDeviceReq[19] = (byte) 0x35 ;
        byteDeviceReq[20] = (byte) 0xFF ;
//        byte[] mVersion = "CS-ITVP-02LW".getBytes();
//        for (int i = 0; i < mVersion.length; i++) {
//        	byteDeviceReq[5+i] = mVersion[i];
//        
//		}
//        System.out.println("hh:"+Arrays.toString(mVersion));
        byteDeviceReq[21] = (byte) 0x02 ;
        byteDeviceReq[22] = (byte) 0x28;
        byteDeviceReq[23] = (byte) 0x02 ;
        byteDeviceReq[24] = (byte) 0x23 ;
      
        byteDeviceReq[25] = (byte)10; //sn
        byteDeviceReq[26] = (byte)0x01; //sn
        
       
        byte[] crcNo=Arrays.copyOf(byteDeviceReq, byteDeviceReq.length - 2);
        byte[] crc = Utilty.getInstance().CRC16(crcNo);
        
        byteDeviceReq[27] = crc[1];
        byteDeviceReq[28] = crc[0];
        System.out.println(byteDeviceReq.length);
        System.out.println(Utilty.getInstance().parseByte2HexStr(byteDeviceReq));
        return byteDeviceReq;
    }
    
    
    private static byte[] initDevicePowerOnByte() {
    	//byte[] idBytes = Utilty.getInstance().int2Bytes(65530, 2);
    	byte[] byteDeviceReq = new byte[64];
		byteDeviceReq[0] = (byte) 0x01;//version
	    byteDeviceReq[1] = (byte) 0x01; //function code
	    byteDeviceReq[2] = (byte) 0x22; //id
        byteDeviceReq[3] = (byte) 0x22; //id
        byteDeviceReq[4] = (byte) 0x33;
        byteDeviceReq[5] = (byte) 0x44;
        byteDeviceReq[6] = (byte)54;
        byteDeviceReq[7] = (byte)0;
        byteDeviceReq[8] = (byte)0x11; //sn
        byteDeviceReq[9] = (byte)0x22; //sn
        byteDeviceReq[10] = (byte)0x33; //sn
        byteDeviceReq[11] = (byte)0x44; //sn
        byteDeviceReq[12] = (byte)0x04;
        byteDeviceReq[13] = (byte)'A';
//        byteDeviceReq[5] = (byte) 0x00;
        byte[] sn = Utilty.getInstance().int2Bytes(3512, 4);
        byteDeviceReq[14] = (byte) 0x01;
        byteDeviceReq[15] = (byte) 0x02;
        byteDeviceReq[16] = (byte) 0x03;
        byteDeviceReq[17] = (byte) 0x00;
        byteDeviceReq[20] = (byte) 0x11;
        byteDeviceReq[21] = (byte) 0x12;
        byteDeviceReq[22] = (byte) 0x11;
        byteDeviceReq[23] = (byte) 0x12;
        byteDeviceReq[24] = (byte) 0x11;
        byteDeviceReq[25] = (byte) 0x12;
        
        
        byte[] mVersion = "ABCDEFGHIJKLMN".getBytes();
        for (int i = 0; i < mVersion.length; i++) {
        	byteDeviceReq[26+i] = mVersion[i];
		}
        byte[] mVersionm = "A5656765HIJKLMN".getBytes();
        for (int i = 0; i < mVersionm.length; i++) {
        	byteDeviceReq[42+i] = mVersionm[i];
		}
        
        byteDeviceReq[58] = (byte)30;
        byteDeviceReq[59] = (byte)0;
        System.out.println(byteDeviceReq.length);
        
        byte[] crcNo=Arrays.copyOf(byteDeviceReq, byteDeviceReq.length - 2);
        byte[] crc = Utilty.getInstance().CRC16(crcNo);
        byteDeviceReq[62] = crc[1];
        byteDeviceReq[63] = crc[0];
        
    	return byteDeviceReq;
    }
    
    /*
     * 初始化：平台向设备命令下发数据
     */
    private static ObjectNode initCloudReqObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudReqObjectNode = mapper.createObjectNode();
        ObjectNode paras = mapper.createObjectNode();
//        paras.put("NewID", 65535);
//        paras.put("TimelyReportInterval", 65535);
//        paras.put("IPAddress", 1);
//        paras.put("Port", 65535);
//        paras.put("DetectThresholdLevel", "4");
//        paras.put("VarOfMagneticFieldWithoutCar", 4);
//        paras.put("VarOfMagneticFieldWithCar", 4);
        	        
        cloudReqObjectNode.put("identifier", "0001");
        cloudReqObjectNode.put("msgType", "cloudReq");
        cloudReqObjectNode.put("cmd", "SampleBGMagneticField");
        cloudReqObjectNode.put("paras", paras);
        cloudReqObjectNode.put("hasMore", 0);
        cloudReqObjectNode.put("mid", 1);
        return cloudReqObjectNode;
    }

    /*
     * 初始化：设备对平台的响应码流
     */
    private static byte[] initDeviceRspByte() {
        /*
         * 测试用例：有命令短mid 设备应答消息:AA7201000107E0
         */
        byte[] byteDeviceRsp = new byte[12];
        byteDeviceRsp[0] = (byte) 0xAA;
        byteDeviceRsp[1] = (byte) 0x72;
        byteDeviceRsp[2] = (byte) 0x01;
        byteDeviceRsp[3] = (byte) 0x00;
        byteDeviceRsp[4] = (byte) 0x01;
        byteDeviceRsp[5] = (byte) 0x07;
        byteDeviceRsp[6] = (byte) 0xE0;
        return byteDeviceRsp;
    }

    /*
     * 初始化：平台对设备的应答数据
     */
    private static ObjectNode initCloudRspObjectNode(byte[] device2CloudByte) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudRspObjectNode = mapper.createObjectNode();
        cloudRspObjectNode.put("identifier", "123");
        cloudRspObjectNode.put("msgType", "cloudRsp");
        // 设备上报的码流
        cloudRspObjectNode.put("request", device2CloudByte);
        cloudRspObjectNode.put("errcode", 0);
        cloudRspObjectNode.put("hasMore", 0);
        return cloudRspObjectNode;
    }
}
