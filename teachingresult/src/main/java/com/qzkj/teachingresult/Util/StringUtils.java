/**
 * ˵�����ı������ڴ�����
 * ��д�ߣ�лƽ
 * ���ڣ�Aug 8, 2007
 * �޸���־��
 *    лƽ Aug 8, 2007 �������з������Ե�����ע��
 * ����ǿ�ǿƼ���Ȩ���С�
 */
package com.qzkj.teachingresult.Util;

import java.security.MessageDigest;

/**
 * <p>
 * Title:�ı������ڴ�����
 * <p>
 * Description:ͨ�ù�����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: ����ǿ�ǿƼ���չ���޹�˾
 * </p>
 * 
 */
public class StringUtils {
	/**
	 * "����
	 */
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

	/**
	 * &����
	 */
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();

	/**
	 * <����
	 */
	private static final char[] LT_ENCODE = "&lt;".toCharArray();

	/**
	 * >����
	 */
	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * ժҪ�㷨
	 */
	private static MessageDigest digest = null;



	/**
	 * ���һ���ַ�����null���ǿյ�
	 *
	 * @param param
	 * @return boolean
	 */
	public static boolean nullOrBlank(String param) {
		return (param == null || param.length() == 0 || param.trim().equals("") || param.trim()
				.equalsIgnoreCase("null")) ? true : false;
	}



}