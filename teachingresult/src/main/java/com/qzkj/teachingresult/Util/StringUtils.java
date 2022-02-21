/**
 * 说明：文本及日期处理方法
 * 编写者：谢平
 * 日期：Aug 8, 2007
 * 修改日志：
 *    谢平 Aug 8, 2007 增加所有方法属性的中文注释
 * 湖南强智科技版权所有。
 */
package com.qzkj.teachingresult.Util;

import java.security.MessageDigest;

/**
 * <p>
 * Title:文本及日期处理方法
 * <p>
 * Description:通用工具类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: 湖南强智科技发展有限公司
 * </p>
 * 
 */
public class StringUtils {
	/**
	 * "符号
	 */
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

	/**
	 * &符号
	 */
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();

	/**
	 * <符号
	 */
	private static final char[] LT_ENCODE = "&lt;".toCharArray();

	/**
	 * >符号
	 */
	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * 摘要算法
	 */
	private static MessageDigest digest = null;



	/**
	 * 检查一个字符串是null还是空的
	 *
	 * @param param
	 * @return boolean
	 */
	public static boolean nullOrBlank(String param) {
		return (param == null || param.length() == 0 || param.trim().equals("") || param.trim()
				.equalsIgnoreCase("null")) ? true : false;
	}



}