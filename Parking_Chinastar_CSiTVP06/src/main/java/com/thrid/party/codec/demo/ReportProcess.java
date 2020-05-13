package com.thrid.party.codec.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author szp
 *对上行数据解码
 */
/**
 * @author zx
 *
 */
public class ReportProcess {
	
    /**
           * 固定值"deviceReq"，表示设备上报数据；
          * 固定值"deviceRsp"，表示设备的应答消息
     */
    private String msgType = "deviceReq";
    /**
     	* 表示设备是否还有后续消息，0表示没有，1
 		表示有，平台暂时不下发缓存命令，直到为
		0，下发缓存命令，不带按照0处理。
     */
    private int hasMore = 0;
    
    private int errcode = 0;
    /**
     	* 功能码
     */
    private byte bFunctionCode;
    
    /**
     * 被应答功能码
     */
    private static final byte returnFunctionCode=(byte) 0xAA;
    /**
     * 终端版本信息功能码
     */
    private static final byte DEVICE_VERSION_INFO = 0x01;
    /**
     * 设置地址功能码，回复功能码与此相同，方向相反
     */
    private static final byte SET_ADDRESS =  0x03;
    
    /**
     * 车位状态信息定时上报功能码
     */
    private static final byte TIMING_REPORT_PARKING = 0x02;
    
    /**
       	* 无后续数据
     */
    private static final int NO_MORE = 0;
    
    private static final String REQUEST ="deviceReq";
    
    private static final String RESPONSE ="deviceRsp";
    
    /**
     * ---终端基本开机信息属性定义开始---
     */
    
    /**
     	* 设备在应用协议里的标识（设备ID），平台从decode接
		口获取该参数，在encode接口传入此参数
     */
    private int identifier; 
    /**
      * 起始码，信息数据包包头
     */
    private int startCode; 
    
    
    
    /**
     * 协议版本号
     */
    private int PV;
    
    /**
     * 终端ID
     */
    private int ID;
    
    /**
     * 消息序号
     */
    private int serial;
    
    /**
     * 产品SN
     */
    private long SN;
    /**
     *设备类型SS 
     */
    private int SS;
    
    /**
     * 终端硬件版本
     */
    private String versionHW ;
    /**
     * 终端软件版本
     */
    private String versionSW;
    
    /**
     * 采样间隔时间
     */
    private int intervalTime=10;
    
    
    /**
     * IMEI
     */
    private String IMEI;
    
    /**
     * IMSI
     */
    private String IMSI;
    /**
     * ---终端基本开机信息属性定义结束---
     */  
    
    
    /**
     * ---车位状态信息定时上报属性定义开始---
     */  
    /**
     * 车位状态
     */
    private int parkingState;
    
    /**
     * 电池电压
     */
    private int batteryPercent;
    /**
     * 信号强度
     */
    private long RSSI;
    /**
     * 信号覆盖低级
     */
    private int SC;
    /**
     * 信噪比
     */
    private int SNR;
    /**
     * 小区PCI
     */
    private int PCI;
    /**
     * 小区所在id
     */
    private long cellId;
    /**
     * 背景磁场参数X
     */
    private int bMagneticField_X;
    /**
     * 背景磁场参数Y
     */
    private int bMagneticField_Y;
    /**
     * 背景磁场参数Z
     */
    private int bMagneticField_Z;
    /**
     * 当前磁场参数X
     */
    private int cMagneticField_X;
    /**
     * 当前磁场参数Y
     */
    private int cMagneticField_Y;
    /**
     * 当前磁场参数Z
     */
    private int cMagneticField_Z;
    /**
     * ---车位状态信息定时上报属性定义结束---
     */  
    private int DetectThresholdLevel;
 // 软件版本四个版本号
 	private Integer HH;
 	private Integer MM;
 	private Integer LL;
 	private Integer NN;
 	// 软件版本四个版本号结束
    /**
     * 设置地址（ID）,回复时返回的新ID
     */
    private int newID;
    
    private int magneticValue;
    
//  	终端回应设置信息时上发的错误码
    private byte[] iResult ;
    /**
     * 2字节无符号的命令id，根据平台下发命令
		时的mid返回给平台。
     */
    private int mid= 0;
	private int register;
	private Integer TimelySampleInterval;
	private int WithoutCarThreshold;
	private int WithCarThreshold;
    /**
     * @param binaryData 设备发送给平台coap报文
     * @return
     */
    public ReportProcess(byte[] binaryData) {
    	
//    	binaryData = Utilty.getInstance().positionFormat(binaryData);
    	
    	PV = binaryData[0];  
    	identifier = Utilty.getInstance().bytes2Int(binaryData, 2, 2);
    	bFunctionCode = binaryData[1];
    	serial =     Utilty.getInstance().bytes2Int(binaryData, 4, 2);
    	SN = Utilty.getInstance().bytesToLong(binaryData, 8, 4,true);
    	ID=Utilty.getInstance().bytes2Int(binaryData, 2, 2);
    	mid = Utilty.getInstance().bytes2Int(binaryData, 4, 2);   
    	
    	//将mid和功能码赋值给Utilty单例
    	Utilty.getInstance().mid = mid;
    	Utilty.getInstance().functioncode = bFunctionCode;
    	
    	 switch (bFunctionCode) {//发送过来的终端开机信息
			case DEVICE_VERSION_INFO:
				deviceVersionInfo(binaryData);
				break;
			case TIMING_REPORT_PARKING:
				parkingStateInfo(binaryData);
				break;
			case returnFunctionCode:
				deviceReply(binaryData);
				break;
			default:
				break;
			}
    	 


    }


	/**
	 *定时上报
	 * @param binaryData
	 */
	private void parkingStateInfo(byte[] binaryData) {
    	msgType = REQUEST;
        hasMore = NO_MORE;
        parkingState = Utilty.getInstance().bytes2Int(binaryData, 12, 2);
        batteryPercent =binaryData[14];
        RSSI = Utilty.getInstance().bytes2Int(binaryData, 16, 4);
        SC=binaryData[20];
        SNR=binaryData[21];
        PCI = Utilty.getInstance().bytes2Int(binaryData, 22, 2);
        cellId =Utilty.getInstance().bytesToLong(binaryData, 24, 4,true);
        bMagneticField_X =  Utilty.getInstance().byteArray2Int(binaryData[28], binaryData[29]);
        bMagneticField_Y =  Utilty.getInstance().byteArray2Int(binaryData[30], binaryData[31]);
        bMagneticField_Z = Utilty.getInstance().byteArray2Int(binaryData[32], binaryData[33]);
        cMagneticField_X = Utilty.getInstance().byteArray2Int(binaryData[34], binaryData[35]);
        cMagneticField_Y = Utilty.getInstance().byteArray2Int(binaryData[36], binaryData[37]);
        cMagneticField_Z = Utilty.getInstance().byteArray2Int(binaryData[38], binaryData[39]);
  
	}

	/**统一的响应回复
	 * @param binaryData
	 */
	private void deviceReply(byte[] binaryData) {
		msgType = RESPONSE;
		// 在华为的API中规定 ： errcode 为0表示成功，1表示失败 ；但下位机协议中规定 错误码为
		// 0无错误，1超范围，2检验错误；在profile文件中终端的应答应当包含一个int型的result参数,
		// 为了避免数据损失，在这里
		// 将终端的错误码放在result中，而errcode按照华为的API规定，只要下位机的错误码不为零，就认为失败，即在error中装入1.
		errcode = binaryData[8] == 0 ? 0 : 1;
		iResult = binaryData;	
	}

	/**
     * @param binaryData
     	* 读取终端版本信息
     */
    private void deviceVersionInfo(byte[] binaryData) {
    	msgType = REQUEST;
       hasMore = NO_MORE;
       SS = binaryData[12];
       versionHW =  new String(binaryData,13,1);
       	NN = (int) binaryData[14];
		LL = (int) binaryData[15];
		MM = (int) binaryData[16];
		HH = (int) binaryData[17];
		versionSW = HH + "." + MM + "." + LL + "." + NN;
		register =  Utilty.getInstance().bytes2Int(binaryData,18,1);
       intervalTime = Utilty.getInstance().bytes2Int(binaryData,20,2);
       TimelySampleInterval = Utilty.getInstance().bytes2Int(binaryData,24,2);
       IMEI = new String(binaryData,26,16);  
       IMSI = new String(binaryData,42,16);  
       DetectThresholdLevel = binaryData[58];
       WithoutCarThreshold = Utilty.getInstance().bytes2Int(binaryData,59,1);
       WithCarThreshold = Utilty.getInstance().bytes2Int(binaryData,60,1);
	}

	

	public ObjectNode toJsonNode() {
        try {
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();

            String s = Integer.toString(this.identifier);
			root.put("identifier", s);
            root.put("msgType", this.msgType);
            root.put("hasMore", this.hasMore);
            //根据msgType字段组装消息体
           if(this.msgType.equals(REQUEST) && bFunctionCode == DEVICE_VERSION_INFO){
            	 root.put("hasMore", this.hasMore);
                 ArrayNode arrynode = mapper.createArrayNode();
            	
                ObjectNode deviceInfoNode = mapper.createObjectNode();
                deviceInfoNode.put("serviceId", "BootMessage");
                
                ObjectNode deviceInfoData = mapper.createObjectNode();                
                deviceInfoData.put("ProtocolVersion", this.PV);
                deviceInfoData.put("DeviceID", this.ID);
                deviceInfoData.put("MessageID", this.serial);
                deviceInfoData.put("SN", this.SN);
                deviceInfoData.put("DeviceType", this.SS);
                deviceInfoData.put("versionHW", this.versionHW);
                deviceInfoData.put("versionSW", this.versionSW.trim());
                deviceInfoData.put("register", this.register);
                deviceInfoData.put("TimelyReportInterval", this.intervalTime);
                deviceInfoData.put("TimelySampleInterval", this.TimelySampleInterval);
                deviceInfoData.put("IMEI", this.IMEI.trim());
                deviceInfoData.put("IMSI", this.IMSI.trim());
                deviceInfoData.put("DetectThresholdLevel", this.DetectThresholdLevel);
                deviceInfoData.put("WithoutCarThreshold", this.WithoutCarThreshold);
                deviceInfoData.put("WithCarThreshold", this.WithCarThreshold);
                
                deviceInfoNode.put("serviceData",deviceInfoData);
                arrynode.add(deviceInfoNode);            	
                
               root.put("data", arrynode);
                
            } 
        else if(this.msgType.equals(REQUEST) && bFunctionCode == TIMING_REPORT_PARKING) {
        	root.put("hasMore", this.hasMore);
            ArrayNode arrynode = mapper.createArrayNode();
       	
           ObjectNode parkingTimingReportNode = mapper.createObjectNode();
           parkingTimingReportNode.put("serviceId", "TimelyReportMessage");
           ObjectNode parkingTimingReportData = mapper.createObjectNode();                
           parkingTimingReportData.put("ProtocolVersion", this.PV);
           parkingTimingReportData.put("DeviceID", this.ID);
           parkingTimingReportData.put("MessageID", this.serial);
           parkingTimingReportData.put("SN", this.SN);
           parkingTimingReportData.put("DeviceStatus", this.parkingState);
           parkingTimingReportData.put("BatteryCapacity", this.batteryPercent);
           parkingTimingReportData.put("SignalStrength", this.RSSI);
           parkingTimingReportData.put("SignalCoverageLevel", this.SC);
           parkingTimingReportData.put("SNR", this.SNR);
           parkingTimingReportData.put("PCI", this.PCI);
           parkingTimingReportData.put("CellId", this.cellId);
           parkingTimingReportData.put("BGMagneticField_X", this.bMagneticField_X);
           parkingTimingReportData.put("BGMagneticField_Y", this.bMagneticField_Y);
           parkingTimingReportData.put("BGMagneticField_Z", this.bMagneticField_Z);
           parkingTimingReportData.put("CurrentMagneticField_X", this.cMagneticField_X);
           parkingTimingReportData.put("CurrentMagneticField_Y", this.cMagneticField_Y);
           parkingTimingReportData.put("CurrentMagneticField_Z", this.cMagneticField_Z);
           
           parkingTimingReportNode.put("serviceData",parkingTimingReportData);
           arrynode.add(parkingTimingReportNode);            	
           
          root.put("data", arrynode);
        }
        else {            	
        root.put("mid", this.mid);            	
           root.put("errcode", this.errcode);                
           //组装body体，只能为ObjectNode对象
           ObjectNode body = mapper.createObjectNode();
           body.put("result",  Utilty.getInstance().parseByte2HexStr(iResult));
           root.put("body", body);
        }
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}