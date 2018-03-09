package com.greatwall.iws.constant;

/**
 * Э�鳣��������
 * @author yuanye
 *
 */
public class ProtocolConstant {
	
	public static final String FUCTIONCODE_SET = "41";							//�����豸����
	public static final String FUCTIONCODE_UPDATE = "46";						//�����豸����
	public static final String FUCTIONCODE_QUERY = "51";						//��ѯ�豸����
	
	public static final String INSTRUCTIONCODE_SET_ADDRESS = "73";				//�����豸��ַ
	public static final String INSTRUCTIONCODE_SET_RESET_REBOOT = "74";			//�ָ�����/�����豸
	public static final String INSTRUCTIONCODE_SET_SYNC_TIME = "75";			//ͬ��ʱ��
	public static final String INSTRUCTIONCODE_SET_ID = "76";					//�����豸ID
	public static final String INSTRUCTIONCODE_SET_NAME = "77";					//�����豸����
	
	public static final String INSTRUCTIONCODE_QUERY_ID = "56";					//��ѯ�豸ID
	public static final String INSTRUCTIONCODE_QUERY_PROTOCAL_VERSION = "50";	//��ѯЭ��汾
	public static final String INSTRUCTIONCODE_QUERY_NAME = "4e";				//��ѯ�豸����
	public static final String INSTRUCTIONCODE_QUERY_IMAGE_VERSION = "a3";		//��ѯ�̼��汾
	
	public static final String INSTRUCTIONCODE_UPDATE_SN_QRCODE = "53";			//���»��ִ洢��ƷSN QRcode
	public static final String INSTRUCTIONCODE_UPDATE_SN_BARCODE = "54";		//���»��ִ洢��ƷSN Barcode
	public static final String INSTRUCTIONCODE_UPDATE_COUNT = "55";				//���»��ִ洢��Ʒ����
	public static final String INSTRUCTIONCODE_UPDATE_TEMPERATURE = "56";		//���»����¶�
	public static final String INSTRUCTIONCODE_UPDATE_HUMIDITY = "57";			//���»���ʪ��
	public static final String INSTRUCTIONCODE_UPDATE_LIGHT = "58";				//LED�Ʋ���
	
	public static final String[] LIGHT_DOWN = {"00","20","10"};					//��ƣ���-��-��
//	public static final String[] LIGHT_UP = {"10","11","12"};					//����
	public static final String[] LIGHT_UP = {"01","21","11"};					//���ƣ���-��-��
	
}
