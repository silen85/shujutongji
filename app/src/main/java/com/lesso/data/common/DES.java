package com.lesso.data.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DES {
	   private static byte[] iv = {2,8,9,7,7,0,3,6};
	   private static String encryptKey = "oP20Mt13";
	   
	   public static String encryptDES(String encryptString)
	             throws Exception 
	   {
	         IvParameterSpec zeroIv = new IvParameterSpec(iv);
	         SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
	         Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	         cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
	         byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
	         return Base64.encode(encryptedData);
	     }
	   
	   public static void main(String[] args) {
		
		   try {
			System.out.println(encryptDES("123456"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	   
	   
}
