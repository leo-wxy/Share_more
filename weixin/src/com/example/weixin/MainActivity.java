package com.example.weixin;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class MainActivity extends Activity {
	private static final String App_ID = "wx1e49f29bc1e0a666";
	private IWXAPI apiIwxapi;
	TextView xTextView;
	TelephonyManager phoneMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		phoneMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		regToWx();

	}

	private void regToWx() {
		apiIwxapi = WXAPIFactory.createWXAPI(this, App_ID, true);
		apiIwxapi.registerApp(App_ID);
		Button button = (Button) findViewById(R.id.share_haoyou);
		Button button2 = (Button) findViewById(R.id.share_pengyouquan);
		Button button3 = (Button) findViewById(R.id.share_sina);
		Button button4 = (Button) findViewById(R.id.ipaddr);
		Button button5 = (Button) findViewById(R.id.phoneIn);
		Button button6 = (Button) findViewById(R.id.facebook);
		Button button7 = (Button) findViewById(R.id.change);
		xTextView = (TextView) findViewById(R.id.xinxi);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(0);// ���?΢������Ȧ
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wechatShare(1);// ��������
			}
		});
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, SinaActivity.class));
			}
		});
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				xTextView.setText(getLocalIpAddress().toString());
			}
		});
		button5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				xTextView.setText("�ֻ��ͺ�" + Build.MODEL + "�ֻ����"
						+ phoneMgr.getLine1Number());
			}

		});
		button6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						FaceBookActivity.class));
			}
		});
		button7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Locale.setDefault(Locale.ENGLISH); 
				Configuration config = getBaseContext().getResources().getConfiguration(); 
				        config.locale = Locale.ENGLISH; 
				        getBaseContext().getResources().updateConfiguration(config
				            , getBaseContext().getResources().getDisplayMetrics());
		
			}
		});
	}

	private void wechatShare(int flag) {
		WXTextObject textObject=new WXTextObject();
		textObject.text="我发现一个很好用的app哦，下载地址是 www.baidu.com";
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject=textObject;
		msg.description="分享到朋友圈";


		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		apiIwxapi.sendReq(req);
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						System.out.println(inetAddress.getHostAddress()
								.toString());
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("errror", ex.toString());
		}
		return null;
	}
}