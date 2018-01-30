package com.kfgame.sdk.common;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	static final String key = "20199310";
	static final String iv = "19175027";

	public static String encryptDES(String encryptString) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
		SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skey, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

		return Base64.encodeToString(encryptedData, Base64.DEFAULT);
	}

	public static String decryptDES(String decryptString) throws Exception {
		byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
		IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
		SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skey, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}

	/**
	 * Encodes a string using MD5 hashing
	 * @param str
	 * @return Encoded String
	 * @author Rafael Steil
	 * @version $Id: MD5.java,v 1.7 2006/08/23 02:13:44 rafaelsteil Exp $
	 */
	public static String md5Crypt(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.toString());
		}
		return hexString.toString();
	}
}
