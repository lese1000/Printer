package com.fanfei.printer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiUtils {
	
//	private static String reqHost = "http://api.gaticn.com:8090/";
//	private static String reqHost = "http://47.90.205.143:8001/printer/";
	private static String reqHost = "http://192.168.1.100/print/";
	
	public static Map<String,Object> getDataByTrackNum(String trackNum,String username){
		String	reqPath="api/v1/print/byTrackNum";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		formparams.add(new BasicNameValuePair("TrackNum", trackNum));
		formparams.add(new BasicNameValuePair("Username", username));
		return doPost(formparams,reqPath);
	}
	public static Map<String,Object> getDataBySequenceNum(String SequenceNum){
		String	reqPath="api/v1/print/bySequenceNum";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		formparams.add(new BasicNameValuePair("SequenceNum", SequenceNum));
		return doPost(formparams,reqPath);
	}
	
	public static Map<String,Object> userLogin(String loginAccount,String loginPassword){
		String	reqPath="api/v1/print/userLogin";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		formparams.add(new BasicNameValuePair("USERNAME", loginAccount));
		formparams.add(new BasicNameValuePair("PASSWORD", loginPassword));
		return doPost(formparams,reqPath);
	}
	
	public static Map<String,Object> getVersion(){
		String reqPath="api/v1/print/getVersion";
		return doGet(reqHost+reqPath);
	}
	
	public static Map<String,Object> doPost(List<NameValuePair> formparams,String reqPath){
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		Map<String,Object> rtn = new HashMap<>();
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);  
			HttpPost httppost = new HttpPost(reqHost+reqPath); 
			httppost.setEntity(entity); 
			CloseableHttpResponse response=httpclient.execute(httppost);
			HttpEntity resentity = response.getEntity();
			if(null!=resentity){
				String result=EntityUtils.toString(resentity);
				System.out.println(result);
				rtn = new ObjectMapper().readValue(result, HashMap.class);
			}
			response.close(); 
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public static Map<String,Object> doGet(String uri){
		Map<String,Object> rtn = new HashMap<>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		try {
			CloseableHttpResponse respone = httpclient.execute(httpGet);
			HttpEntity entity = respone.getEntity();
			String rtnResult = EntityUtils.toString(entity);
			rtn = new ObjectMapper().readValue(rtnResult, HashMap.class);
			System.out.println(rtnResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public static void main(String[] args){
		getDataByTrackNum("200039251","test_user");
	}

}
