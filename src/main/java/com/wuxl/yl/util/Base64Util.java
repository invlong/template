package com.wuxl.yl.util;

import org.apache.commons.lang.StringUtils;

public class Base64Util {
    private static final char intToBase64[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',   
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',   
                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',   
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};  
    private static final byte base64ToInt[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,   
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,   
                -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,   
                -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,   
                17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32,   
                33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };  
      
    /** 
     * byte数组转换成BASE64字符串 
     *  
     * @param data byte数组 
     * @return <b>String</b> BASE64字符串<b><br/>null</b> 转换失败 
     */  
    public static String byteArrayToBase64(byte[] data) {  
        if(data==null || data.length==0){  
            return null;  
        }  
        int len = data.length;  
        int groups = len/3;  
        int nogroups = len-3*groups;  
        int resultLen = (len+2)/3*4;  
        StringBuffer result = new StringBuffer(resultLen);  
        int cursor = 0;  
        for(int i=0;i<groups;i++){  
            int byte0 = data[cursor++]&0xff;  
            int byte1 = data[cursor++]&0xff;  
            int byte2 = data[cursor++]&0xff;  
            result.append(intToBase64[byte0>>2]);  
            result.append(intToBase64[(byte0<<4)&0x3f|(byte1>>4)]);  
            result.append(intToBase64[(byte1<<2)&0x3f|(byte2>>6)]);  
            result.append(intToBase64[byte2&0x3f]);  
        }  
        if(nogroups!=0){  
            int byte0 = data[cursor++]&0xff;  
            result.append(intToBase64[byte0>>2]);  
            if(nogroups==1){  
                result.append(intToBase64[(byte0<<4)&0x3f]);  
                result.append("==");  
            }else{  
                int byte1 = data[cursor++]&0xff;  
                result.append(intToBase64[(byte0<<4)&0x3f|(byte1>>4)]);  
                result.append(intToBase64[(byte1<<2)&0x3f]);  
                result.append('=');  
            }  
        }  
        return result.toString();  
    }  
      
    /** 
     * BASE64字符串转换成byte数组 
     *  
     * @param data BASE64字符串 
     * @return <b>String</b> byte数组<b><br/>null</b> 转换失败 
     */  
    public static byte[] base64ToByteArray(String data) {

        if(StringUtils.isEmpty(data)){
            return null;  
        }  
        int len = data.length();  
        int groups = len/4;  
        if(groups*4!=len){  
            return null;  
        }  
        int nogroups = 0;  
        int fullGroups = groups;  
        if(len!=0){  
            if(data.charAt(len-1)=='='){  
                nogroups++;  
                fullGroups--;  
            }  
            if(data.charAt(len-2)=='='){  
                nogroups++;  
            }  
        }  
        byte[] result = new byte[groups*3-nogroups];  
        int inCursor = 0;  
        int outCursor = 0;  
        try {  
            for(int i=0;i<fullGroups;i++){  
                int ch0 = base64toInt(data.charAt(inCursor++));  
                int ch1 = base64toInt(data.charAt(inCursor++));  
                int ch2 = base64toInt(data.charAt(inCursor++));  
                int ch3 = base64toInt(data.charAt(inCursor++));  
                result[outCursor++] = (byte) ((ch0<<2)|(ch1>>4));  
                result[outCursor++] = (byte) ((ch1<<4)|(ch2>>2));  
                result[outCursor++] = (byte) ((ch2<<6)|ch3);  
            }  
            if(nogroups!=0){  
                int ch0 = base64toInt(data.charAt(inCursor++));  
                int ch1 = base64toInt(data.charAt(inCursor++));  
                result[outCursor++] = (byte) ((ch0<<2)|(ch1>>4));  
                if(nogroups==1){  
                    int ch2 = base64toInt(data.charAt(inCursor++));  
                    result[outCursor++] = (byte) ((ch1<<4)|(ch2>>2));  
                }  
            }  
        } catch (Exception e) {  
            return null;  
        }  
        return result;  
    }  
      
    private static int base64toInt(char c) {  
        int result = base64ToInt[c];  
        if(result<0){  
            throw new RuntimeException();  
        }  
        return result;  
    }  
      
}  