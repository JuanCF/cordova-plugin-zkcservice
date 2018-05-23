package ls.co.apos;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.smartdevice.aidl.IZKCService;

public class ZKCServicePlugin extends CordovaPlugin {
	public static final String TAG = "ZKCServicePlugin";
	
	public static IZKCService mIzkcService;
	
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
		if ("echo".equals(action)) {
		  echo(args.getString(0), callbackContext);
		  return true;
		}
		return false;
	}
	
	
	private final ServiceConnection mServiceConnection = new ServiceConnection () {
		@Override 
		public void onServiceDisconnected ( ComponentName name ) { 
		// this callback will be invoked when the remote service is dead 		
			mIsBound = false ; 
		} 
		@Override 
		public void onServiceConnected ( ComponentName name , IBinder service ) { 
			mIsBound = true ; 
			mService = IRemoteService .  Stub .  asInterface (service); 
			try { 
				mService.registerCallback (mCallback); 
			} catch ( RemoteException e) { 
				// TODO Auto-generated catch block 
				e .  printStackTrace (); 
			} 
			try { 
				mService.createBitmap (); 
			} catch ( RemoteException e) { 
				// TODO Auto-generated catch block 
				e .  printStackTrace (); 
			} 
		} 
	};
	
	private ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e("client", "onServiceDisconnected");
			mIzkcService = null;
			Toast.makeText(BaseActivity.this, getString(R.string.service_bind_fail), Toast.LENGTH_SHORT).show();
			//发送消息绑定失败 send message to notify bind fail
			sendEmptyMessage(MessageType.BaiscMessage.SEVICE_BIND_FAIL);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e("client", "onServiceConnected");
			mIzkcService = IZKCService.Stub.asInterface(service);
			if(mIzkcService!=null){
				try {
					Toast.makeText(BaseActivity.this, getString(R.string.service_bind_success), Toast.LENGTH_SHORT).show();
					//获取产品型号 get product model
					DEVICE_MODEL = mIzkcService.getDeviceModel();
					//设置当前模块 set current function module
					mIzkcService.setModuleFlag(module_flag);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				//发送消息绑定成功 send message to notify bind success
				sendEmptyMessage(MessageType.BaiscMessage.SEVICE_BIND_SUCCESS);
			}
		}
	};
}
