package com.example.weixin.wxapi;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
	private IWXAPI api;
	private static final String App_ID = "wx1e49f29bc1e0a666";
	private static final String TAG = "sd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, App_ID, false);
		api.handleIntent(getIntent(), this);
		api.registerApp(App_ID);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// LogManager.show("resp.errCode:" + resp.errCode + ",resp.errStr:" +
		// resp.errStr, 1);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// �����
			Toast.makeText(WXEntryActivity.this, "success", Toast.LENGTH_SHORT)
					.show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// ����ȡ��
			Toast.makeText(WXEntryActivity.this, "cancel", Toast.LENGTH_SHORT)
					.show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// ����ܾ�
			Toast.makeText(WXEntryActivity.this, "fail", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
}