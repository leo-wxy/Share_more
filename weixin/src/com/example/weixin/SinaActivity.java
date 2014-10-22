package com.example.weixin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.weixin.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SinaActivity extends Activity {
	WebView webView;
	String url2;
	HttpPost httpPost;
	HttpResponse response;
	HttpEntity entity;
	HttpClient httpClient;
	WebSettings webSettings;
	String access_token;
	Button button;
	String App_Key = "申请的AppKey";
	String Rediert_url = "https://api.weibo.com/oauth2/default.html";
	String App_Secret = "生成的Secret";
	String code;
	public static final String authUrl = "https://open.weibo.cn/oauth2/authorize";// ����΢����Ȩ��ַ
	public static final String authUrl2 = "https://open.weibo.cn/oauth2/access_token";// ����΢����Ȩ��ַ
	private String url1 = authUrl
			+ "?client_id="
			+ App_Key
			+ "&response_type=code&forcelogin=true&display=mobile&redirect_uri="
			+ Rediert_url;

	private static final String requesturl = "https://api.weibo.com/2/statuses/update.json";// ���˷�����Ϣ�ӿ�

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina);
		int version = 0;
		version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (version >= 11.0) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedSqlLiteObjects()
					.penaltyLog().penaltyDeath().build());
		} else {
		}
		button = (Button) findViewById(R.id.Button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button.setText(share());
				System.out.println(share());
			}
		});
		webView = (WebView) findViewById(R.id.sina);
		webView.getSettings().setJavaScriptEnabled(true);
		webSettings=webView.getSettings();
		webSettings.setTextZoom(10);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				Map<String, String> data = pareseUrl(url);
				if (data.containsKey("error")) {
					finish();
				} else {
					code = data.get("code");
					url2 = authUrl2 + "?client_id=" + App_Key
							+ "&client_secret=" + App_Secret
							+ "&grant_type=authorization_code&redirect_uri="
							+ Rediert_url + "&code=" + code;
					login();
				}
				return true;
			}
		});
		webView.loadUrl(url1);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	private Map<String, String> pareseUrl(String url) {
		Map<String, String> dataMap = new HashMap<String, String>();
		try {
			URL mURL = new URL(url);
			String s = mURL.getQuery();
			if (s != null) {
				String array[] = s.split("&");
				for (String parameter : array) {
					String v[] = parameter.split("=");
					if (v.length >= 2)
						dataMap.put(URLDecoder.decode(v[0]),
								URLDecoder.decode(v[1]));
					else
						dataMap.put(URLDecoder.decode(v[0]), "");
				}
			}
			s = mURL.getRef();
			if (s != null) {
				String array[] = s.split("&");
				for (String parameter : array) {
					String v[] = parameter.split("=");
					if (v.length >= 2)
						dataMap.put(URLDecoder.decode(v[0]),
								URLDecoder.decode(v[1]));
					else
						dataMap.put(URLDecoder.decode(v[0]), "");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	public void login() {
		httpClient = new DefaultHttpClient();
		String result = null;
		try {
			httpPost = new HttpPost(url2);
			List<NameValuePair> data = new ArrayList<NameValuePair>();

			httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
			response = httpClient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject datas;
			try {
				datas = new JSONObject(result.toString());

				access_token = datas.getString("access_token");// �һ���

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	public String share() {

		httpClient = new DefaultHttpClient();
		String result = null;
		try {
			httpPost = new HttpPost(requesturl);
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			try {
				data.add(new BasicNameValuePair("status", "a"));
				data.add(new BasicNameValuePair("access_token", access_token));
			} catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			}
			httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
			response = httpClient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}
		return result;
	}
}
