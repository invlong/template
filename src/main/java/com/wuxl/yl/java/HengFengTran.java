package com.wuxl.yl.java;

import com.alibaba.fastjson.JSONObject;
import com.wuxl.yl.util.Base64;
import com.wuxl.yl.util.RsaKey;
import com.wuxl.yl.util.RsaUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class HengFengTran {


    /**
     * 注册不需要进行加密处理，直接封装好发送请求即可。
     */
	public void registerDemo(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account", "");
		param.put("password", "");//不要进行加密
		param.put("channel", "HF001");
		param.put("accessPart", "LS001");
		//直接将param转为json，http请求即可。
	}



	/**
	 * 下载秘钥/验卡/修改费率一码付
	 * @throws UnsupportedEncodingException
	 */
	public void demo () throws UnsupportedEncodingException {
		Map<String,Object> valiCardMap = new HashMap<String,Object>();
		//存放加密数据的json对象
		JSONObject jsonObject = new JSONObject();
		//注册手机号
		jsonObject.put("account", "13566678871");
		//放入私钥
		jsonObject.put("privatekey", "xxxx");
		jsonObject.put("real_name", "xx");
		jsonObject.put("cmer", "大润发超市");
		jsonObject.put("cmer_sort", "大润发超市");
		jsonObject.put("phone", "13566678871");
		jsonObject.put("business_id", "270");
		jsonObject.put("channel_code", "ALIPAY");
		jsonObject.put("card_type", "1");
		jsonObject.put("card_no", "xxxxxxxx");
		jsonObject.put("cert_type", "00");
		jsonObject.put("cert_no", "xxxxxxx");

		jsonObject.put("mobile", "13566678871");
		jsonObject.put("location", "烟台");
		jsonObject.put("channel", "HF001");
		jsonObject.put("accessPart", "LS001");


		jsonObject.put("cert_correct", "");
		jsonObject.put("cert_opposite", "");
		jsonObject.put("cert_meet", "");
		jsonObject.put("card_correct", "");
		jsonObject.put("card_opposite", "");

		//base64编码时，对中文会产生乱码，为解决乱码，进行指定utf-8的编码方式
		String jsonObject64 = new String(Base64.encodeToByte(jsonObject.toJSONString().getBytes("UTF-8")),"utf-8");
		//开始加密，加密完后，为base64编码数据
		String jsonObjectRsa = RsaUtil.encryptByPublicKey(RsaKey.PUBLIC_KEY,jsonObject64);
		//最终传输数据的map包含两个参数，一个为加密后的数据data，另一个为来自的渠道channel
		// '+' 在 URL 传递时会被当成空格，因此必须要将 base64 编码后的字符串中的加号替换成 %2B 才能当作 URL 参数进行传递
		jsonObjectRsa = jsonObjectRsa != null ? jsonObjectRsa.replaceAll("\\+", "%2B") : null;
		valiCardMap.put("data", jsonObjectRsa);
		valiCardMap.put("channel", "HF001");

	}

	
}
