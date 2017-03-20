package com.wuxl.yl.util;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;  
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.SecureRandom;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  
import java.util.HashMap;  
import java.util.Map;  
  


import javax.crypto.Cipher;
  
/** 
 * RSA加密解密工具 
 *  
  
 */  
public class RsaUtil {  
	
	private static final int MAX_ENCRYPT_BLOCK = 117;
	
	private static final int MAX_DECRYPT_BLOCK = 128;
	
      

    /** 
     * 公钥加密 
     *  
     * @param key 公钥 
     * @param data 明文 
     * @return <b>String</b> 密文<b><br/>null</b> 加密失败 
     */  
    public static String encryptByPublicKey(String key, String data) {  
        try {  
            byte[] bdata = data.getBytes("utf-8");
            RSAPublicKey publicKey = getPublicKey(key);  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = bdata.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(bdata, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(bdata, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return new String(Base64.encodeToByte(encryptedData),"utf-8");
        } catch (Exception e) {
        	e.printStackTrace();
        }  
        return null;  
    }  
  
    /** 
     * 私钥解密 
     *  
     * @param key 私钥 
     * @param data 密文 
     * @return <b>String</b> 明文<b><br/>null</b> 解密失败 
     */  
    public static String decryptByPrivateKey(String key, byte[] data) {  
        try {  
            RSAPrivateKey privateKey = getPrivateKey(key);  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet,
							MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen
							- offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return new String(decryptedData, "UTF-8");
        } catch (Exception e) {  
        	e.printStackTrace();
        }  
        return null;  
    }

    private static RSAPrivateKey getPrivateKey(String key) {
        try {
            byte[] keyBytes = Base64Util.base64ToByteArray(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) factory.generatePrivate(spec);
        } catch (Exception e) {
        }
        return null;
    }

    private static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes = Base64Util.base64ToByteArray(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) factory.generatePublic(spec);
        } catch (Exception e) {
        }
        return null;
    }

}  