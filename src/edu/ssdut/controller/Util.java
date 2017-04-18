package edu.ssdut.controller;

import java.io.File;
import java.security.MessageDigest;

public final class Util {
	public final static String MD5(String s) {  
        char hexDigits[] = { '0', '1', '2', '3', '4',  
                             '5', '6', '7', '8', '9',  
                             'A', 'B', 'C', 'D', 'E', 'F' };  
        try {  
            byte[] btInput = s.getBytes();  
     //获得MD5摘要算法的 MessageDigest 对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
     //使用指定的字节更新摘要  
            mdInst.update(btInput);  
     //获得密文  
            byte[] md = mdInst.digest();  
     //把密文转换成十六进制的字符串形式  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return new String(str);  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

	public final static void delDir(String path) {
		File file = new File(path);
		if (!file.isDirectory()) 
			return ;
		File[] files = file.listFiles();
		for (File f : files) {
			System.out.println(f.getAbsolutePath());
			if (f.isFile())
				f.delete();
			else
				delDir(f.getAbsolutePath());
		}
		file.delete();
	}
	
}
