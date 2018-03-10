package com.moemc.login;

import java.security.Key; 
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec; 
 
import org.apache.commons.codec.binary.Base64;
  
public class AES { 
  
    private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
 
    public static String encrypt(String keyStr, String plainText) { 
        byte[] encrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            encrypt = cipher.doFinal(plainText.getBytes());     
        }catch(Exception e){ 
            e.printStackTrace(); 
        }
        return new String(Base64.encodeBase64(encrypt)); 
    } 
 
    public static String decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.DECRYPT_MODE, key); 
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData)); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
        return new String(decrypt).trim(); 
    } 
 
    private static Key generateKey(String key)throws Exception{ 
        try{            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            return keySpec; 
        }catch(Exception e){ 
            e.printStackTrace(); 
            throw e; 
        } 
 
    } 
}