package com.gss.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CheckUtil {
	private static final String token = "gssTest";

	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] {token,timestamp,nonce};
		// 排序
		Arrays.sort(arr);

		// 生产字符串
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}

		//SHA1加密
		String temp = getSha1(content.toString());

		return temp.equals(signature);
	}

	// SHA1加密
	private static String getSha1(String string) {
		if (string == null) {
			return null;
		}
		char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(string.getBytes());
			byte[] bytes = messageDigest.digest();
			int len = bytes.length;
			StringBuilder buf = new StringBuilder(len * 2);
			// 把密文转换成十六进制的字符串形式
			for (int j = 0; j < len; j++) {
				buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
				buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
			}
			return buf.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
