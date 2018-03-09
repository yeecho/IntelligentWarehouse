package com.greatwall.iws.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.greatwall.iws.bean.FDUBean;

/**
 * 数据转换工具类
 * @author yuanye
 *
 */
public class DataUtil {
	private static final String tag = "com.greatwall.iws.utils/.DataUtil";
	private static final String MainAddress = "32";
	
	/**
	 * 地址解析
	 * @param position 物料编号
	 * @return 物料虚拟地址
	 */
	public static String addressDecode(String kw) {
//		Log.d(tag, "地址解析"+kw);
		String[] strs = kw.split("-");
		String address = "";
		if (strs.length>=4) {
			address = strs[3];
		}else{
			Log.d(tag, "地址不规范，解析错误"+kw);
		}
		return address;
//		return "00";
	}

	/**
	 * 封装ADU
	 * @param virtualAddress 物料库虚拟地址
	 * @param fduBean 功能单元
	 * @return ADU
	 */
	public static String buildADU(String virtualAddress, FDUBean fduBean) {
		String ADU = "";
		ADU = virtualAddress + fduBean.getFDUCode() + MainAddress + fduBean.getFDULen() +fduBean.getFDU();
		return ADU;
	}
	
	/**
	 * 字符转换
	 * @param hexString 十六进制字符串
	 * @return	字节数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.replaceAll(" ", "");
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	/**
	 * 字符转换
	 * @param c 字符
	 * @return 字节
	 */
	private static byte charToByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	
	/**
	 * 整型转16进制小端数据
	 * @param count 整型
	 * @return 小端16进制字符
	 */
	public static String getHexCount(int count){
		String hexCount = "";
		String hex = Integer.toHexString(count);
		while (hex.length() < 8) {
			//长度不够需要补零
			hex = "0"+hex;
		}
		//大端转小端
		for (int i = hex.length() ; i > 0 ; i-=2) {
			hexCount += hex.substring(i-2, i);
		}
		return hexCount;
	}
	
	public static String getHexStringLength(String hex){
		int len = hex.length()/2;
		String strLen = "";
		if (len <= 15) {
			strLen = "0" + Integer.toHexString(len);
		}else{
			strLen = Integer.toHexString(len);
		}
		return strLen;
	}
	
	/**
	 * 字符串CRC16校验
	 * @param str 供校验的字符串
	 * @return	返回校验值（字符串）
	 */
	public static String getCRC16CheckCode(String str){
		str = str.replaceAll(" ", "");
		byte[] buf = hexStringToBytes(str);
		int len = hexStringToBytes(str).length;
		int checkResult = CRC16(buf, len);
		String result = Integer.toHexString(checkResult);
		while (result.length()<4) {
			result = "0"+result;
		}
		result = result.substring(2) + result.substring(0, 2);
		return result;
	}
	
	/**
	 * 字节数组CRC16校验
	 * @param Buf 供校验的字节数组
	 * @param Len 需要校验的数组长度
	 * @return 返回校验值（整型）
	 */
	public static int CRC16(byte[] Buf, int Len) {
		int CRC;
		int i, Temp;
		CRC = 0xffff;
		for (i = 0; i < Len; i++) {
			CRC = CRC ^ byteToInteger(Buf[i]);
			for (Temp = 0; Temp < 8; Temp++) {
				if ((CRC & 0x01) == 1)
					CRC = (CRC >> 1) ^ 0xA001;
				else
					CRC = CRC >> 1;
			}
		}
		return CRC;
	}
	
	/**
	 * 字节转整型
	 * @param b 字节
	 * @return 整型
	 */
	public static int byteToInteger(byte b) {
		int value;
		value = b & 0xff;
		return value;
	}
	//字节码转16进制字符串
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	/**
	 * 分析虚拟地址字符串来获取串口编号
	 * @param virtualAddress
	 * @return
	 */
	public static int serialNumDecode(String kw) {
		String[] strs = kw.split("-");
		if (strs.length>=4) {
			if (strs[2].equals("01")) {
				return 0;
			}else if (strs[2].equals("02")) {
				return 1;
			}else if (strs[2].equals("03")) {
				return 2;
			}
		}else{
			Log.d(tag, "地址不规范，解析错误"+kw);
		}
		return 0;
	}
	
	public static boolean stringIsNull(String str){
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}
	
	public static boolean isCompleted(String str) {
		// TODO Auto-generated method stub
		byte[] bytes = hexStringToBytes(str);
		int len = bytes.length;
		Log.d(tag, "len : "+len);
		if (len == 0) {
			return false;
		}else if (len ==1) {
			return false;
		}else if (len == 2) {
			return false;
		}else if (len == 3) {
			return false;
		}else if (len >= 4) {
			int FUDLen = byteToInteger(bytes[3]);
			Log.d(tag, "FUDLen : "+FUDLen);
			return FUDLen == len-6;
		}
		
		return false;
	}
	
	public static boolean CRC16Check(String result) {
		// TODO Auto-generated method stub
		if (result.length()<=4) {
			Log.e(tag, "result:"+result);
			return false;
		}
		String crc = result.substring(result.length()-4);
		byte[] buf = hexStringToBytes(result.substring(0, result.length()-4));
		String tempCRC = Integer.toHexString(CRC16(buf, buf.length));
		String crcResult = tempCRC.substring(2) + tempCRC.substring(0, 2);
		if (crc.equals(crcResult)) {
			return true;
		}
		return false;
	}
	
	//条形码
		public static String generate_128C(String input){
			StringBuilder text = new StringBuilder();//text是输出的结果
	        text.append(code128[105][3]);//添加一个StartCodeC
	        int currentFlag = 2;//0:128A, 1:128B, 2:128C
	        int index = 1;//用于检验位的计算
	        int examine = 0;//检验位
//	        if (isNumeric(input.subSequence(0, 1).toString()) && isNumeric(input.subSequence(1, 2).toString())){
//	            examine += 105;//以128C开始
//	            currentFlag = 2;
//	        }else {
//	            //以128B开始
//	            examine += 104;
//	            currentFlag = 1;
//	        }
	        examine += 105;

	        while (input.length() != 0) {
	            if (input.length() != 1 && isNumeric(input.subSequence(0, 1).toString()) && isNumeric(input.subSequence(1, 2).toString())) {
	                if (currentFlag == 2) {
	                    text.append(transformC(input.subSequence(0, 2).toString()));
	                    examine += (Integer.parseInt(input.subSequence(0, 2).toString()) * index);
	                    index += 1;
	                    input = input.substring(2);
	                } else if (currentFlag == 1) {
	                	examine += (99 * index);
	                    index += 1;
	                    examine += (Integer.parseInt(input.subSequence(0, 2).toString()) * index);
	                    index += 1;
	                    text.append(code128[99][3]);//128B转128C
	                    text.append(transformC(input.subSequence(0, 2).toString()));
	                    input = input.substring(2);
	                    currentFlag = 2;
	                }
	            } else {
	                Log.e(tag, "进入CODEB");
	                String[] strings = transformB(input.subSequence(0, 1).toString());
	                if (currentFlag == 1) {
	                    examine += (Integer.parseInt(strings[0]) * index);
	                    index += 1;
	                    text.append(strings[1]);
	                    input = input.substring(1);

	                } else if (currentFlag == 2) {
	                    currentFlag = 1;
	                    examine += (100 * index);//加上codeC转codeB对应的ID
	                    index += 1;
	                    examine += (Integer.parseInt(strings[0]) * index);
	                    index += 1;
	                    text.append(code128[100][3]);//128C转128B
	                    text.append(strings[1]);
	                    input = input.substring(1);

	                }
	            }
	        }
	        examine %= 103;
	        text.append(code128[examine][3]);
	        text.append(code128[106][3]);//添加Stop
	        Log.e(tag, "examine = " + String.valueOf(examine));
	        Log.e(tag, "text = " + text.toString());
	        return bytesToHexString(text.toString().getBytes());
	    }
	    private static boolean isNumeric(String str){
	        Pattern pattern = Pattern.compile("[0-9]*");
	        Matcher isNum = pattern.matcher(str);
	        if( !isNum.matches() ){
	            return false;
	        }
	        return true;
	    }

	    private static String transformC(String str){
	        int tempValue = Integer.valueOf(str);
	        return code128[tempValue][3];
	    }
	    private static String[] transformB(String str){
	        String[] strings = new String[2];
	        for (int i = 0; i < code128.length; i++){
	            if (code128[i][1].equals(str)){
	                strings[0] = code128[i][2];
	                strings[1] = code128[i][3];
	                return strings;
	            }
	        }
	        return null;
	    }
//	    public static String getValue(int i, int j){
//	        return code128[i][j];
//	    }


	    private static String[][] code128 = { { " ", " ", "00", "212222", "11011001100" },
	            { "!", "!", "01", "222122", "11001101100" },
	            { "\"", "\"", "02", "222221", "11001100110" },
	            { "#", "#", "03", "121223", "10010011000" },
	            { "$", "$", "04", "121322", "10010001100" },
	            { "%", "%", "05", "131222", "10001001100" },
	            { "&", "&", "06", "122213", "10011001000" },
	            { "'", "'", "07", "122312", "10011000100", },
	            { "(", "(", "08", "132212", "10001100100" },
	            { ")", ")", "09", "221213", "11001001000" },
	            { "*", "*", "10", "221312", "11001000100" },
	            { "+", "+", "11", "231212", "11000100100" },
	            { ",", ",", "12", "112232", "10110011100" },
	            { "-", "-", "13", "122132", "10011011100" },
	            { ".", ".", "14", "122231", "10011001110" },
	            { "/", "/", "15", "113222", "10111001100" },
	            { "0", "0", "16", "123122", "10011101100" },
	            { "1", "1", "17", "123221", "10011100110" },
	            { "2", "2", "18", "223211", "11001110010", },
	            { "3", "3", "19", "221132", "11001011100" },
	            { "4", "4", "20", "221231", "11001001110", },
	            { "5", "5", "21", "213212", "11011100100" },
	            { "6", "6", "22", "223112", "11001110100" },
	            { "7", "7", "23", "312131", "11101101110" },
	            { "8", "8", "24", "311222", "11101001100" },
	            { "9", "9", "25", "321122", "11100101100" },
	            { ":", ":", "26", "321221", "11100100110" },
	            { ";", ";", "27", "312212", "11101100100" },
	            { "<", "<", "28", "322112", "11100110100" },
	            { "=", "=", "29", "322211", "11100110010" },
	            { ">", ">", "30", "212123", "11011011000" },
	            { "?", "?", "31", "212321", "11011000110" },
	            { "@", "@", "32", "232121", "11000110110" },
	            { "A", "A", "33", "111323", "10100011000" },
	            { "B", "B", "34", "131123", "10001011000" },
	            { "C", "C", "35", "131321", "10001000110" },
	            { "D", "D", "36", "112313", "10110001000" },
	            { "E", "E", "37", "132113", "10001101000" },
	            { "F", "F", "38", "132311", "10001100010" },
	            { "G", "G", "39", "211313", "11010001000" },
	            { "H", "H", "40", "231113", "11000101000" },
	            { "I", "I", "41", "231311", "11000100010" },
	            { "J", "J", "42", "112133", "10110111000" },
	            { "K", "K", "43", "112331", "10110001110" },
	            { "L", "L", "44", "132131", "10001101110" },
	            { "M", "M", "45", "113123", "10111011000" },
	            { "N", "N", "46", "113321", "10111000110" },
	            { "O", "O", "47", "133121", "10001110110" },
	            { "P", "P", "48", "313121", "11101110110" },
	            { "Q", "Q", "49", "211331", "11010001110" },
	            { "R", "R", "50", "231131", "11000101110" },
	            { "S", "S", "51", "213113", "11011101000" },
	            { "T", "T", "52", "213311", "11011100010" },
	            { "U", "U", "53", "213131", "11011101110" },
	            { "V", "V", "54", "311123", "11101011000" },
	            { "W", "W", "55", "311321", "11101000110" },
	            { "X", "X", "56", "331121", "11100010110" },
	            { "Y", "Y", "57", "312113", "11101101000" },
	            { "Z", "Z", "58", "312311", "11101100010" },
	            { "[", "[", "59", "332111", "11100011010" },
	            { "\\", "\\", "60", "314111", "11101111010" },
	    { "]", "]", "61", "221411", "11001000010" },
	    { "^", "^", "62", "431111", "11110001010" },
	    { "_", "_", "63", "111224", "10100110000" },
	    { "NUL", "`", "64", "111422", "10100001100" },
	    { "SOH", "a", "65", "121124", "10010110000" },
	    { "STX", "b", "66", "121421", "10010000110" },
	    { "ETX", "c", "67", "141122", "10000101100" },
	    { "EOT", "d", "68", "141221", "10000100110" },
	    { "ENQ", "e", "69", "112214", "10110010000" },
	    { "ACK", "f", "70", "112412", "10110000100" },
	    { "BEL", "g", "71", "122114", "10011010000" },
	    { "BS", "h", "72", "122411", "10011000010" },
	    { "HT", "i", "73", "142112", "10000110100" },
	    { "LF", "j", "74", "142211", "10000110010" },
	    { "VT", "k", "75", "241211", "11000010010" },
	    { "FF", "I", "76", "221114", "11001010000" },
	    { "CR", "m", "77", "413111", "11110111010" },
	    { "SO", "n", "78", "241112", "11000010100" },
	    { "SI", "o", "79", "134111", "10001111010" },
	    { "DLE", "p", "80", "111242", "10100111100" },
	    { "DC1", "q", "81", "121142", "10010111100" },
	    { "DC2", "r", "82", "121241", "10010011110" },
	    { "DC3", "s", "83", "114212", "10111100100" },
	    { "DC4", "t", "84", "124112", "10011110100" },
	    { "NAK", "u", "85", "124211", "10011110010" },
	    { "SYN", "v", "86", "411212", "11110100100" },
	    { "ETB", "w", "87", "421112", "11110010100" },
	    { "CAN", "x", "88", "421211", "11110010010" },
	    { "EM", "y", "89", "212141", "11011011110" },
	    { "SUB", "z", "90", "214121", "11011110110" },
	    { "ESC", "{", "91", "412121", "11110110110" },
	    { "FS", "|", "92", "111143", "10101111000" },
	    { "GS", "},", "93", "111341", "10100011110" },
	    { "RS", "~", "94", "131141", "10001011110" },
	    { "US", "DEL", "95", "114113", "10111101000" },
	    { "FNC3", "FNC3", "96", "114311", "10111100010" },
	    { "FNC2", "FNC2", "97", "411113", "11110101000" },
	    { "SHIFT", "SHIFT", "98", "411311", "11110100010" },
	    { "CODEC", "CODEC", "99", "113141", "10111011110" },
	    { "CODEB", "FNC4", "CODEB", "114131", "10111101110" },
	    { "FNC4", "CODEA", "CODEA", "311141", "11101011110" },
	    { "FNC1", "FNC1", "FNC1", "411131", "11110101110" },
	    { "StartA", "StartA", "StartA", "211412", "11010000100" },
	    { "StartB", "StartB", "StartB", "211214", "11010010000" },
	    { "StartC", "StartC", "StartC", "211232", "11010011100" },
	    { "Stop", "Stop", "Stop", "2331112", "1100011101011" }, };
}
