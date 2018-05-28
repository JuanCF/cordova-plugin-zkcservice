package ls.co.zkcplugin;

import java.io.File;  
import java.io.PrintWriter;  
import java.io.StringWriter;
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
import org.apache.cordova.PluginResult;
import android.widget.Toast;

import com.smartdevice.aidl.IZKCService;

public class ZKCService extends CordovaPlugin {
	public static final String TAG = "ZKCService";
	
	//getBond bond  = new getBond();
	
	//Loginfo("Declaring static attributes...");
	
	public static String MODULE_FLAG = "module_flag";
	public static int module_flag = 0;
	public static int DEVICE_MODEL = 0;
	public static String printer_firmversion;
	public static String SERVICE_VERSION = "version unknown";
	public static IZKCService mIzkcService;
	
	private Handler mhanlder; 
	
	@Override
	public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException {
		if ("ToastIt".equals(action)) {
			ToastIt(args.getString(0), callbackContext);
			return true;
		}
		else if("bindZKCService".equals(action)) {
			bindZKCService(callbackContext);
			return true;
		}
		return false;
	}
	
	private void ToastIt(String msg, CallbackContext callbackContext) {
		if (msg == null || msg.length() == 0) {
			callbackContext.error("Empty message!");
		} else {
			Toast.makeText(webView.getContext(), msg, Toast.LENGTH_LONG).show();
			callbackContext.success(msg);
		}
	}

	
	public void bindZKCService(CallbackContext callbackContext) {
		//statements that may cause an exception
		ServiceConnection mServiceConn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.e("client", "onServiceDisconnected");
				mIzkcService = null;
				Toast.makeText(webView.getContext(), "Failed to connect to service.", Toast.LENGTH_LONG).show();
				callbackContext.error("Failed to connect to service.");
				//发送消息绑定失败 send message to notify bind fail
				//sendEmptyMessage(MessageType.BaiscMessage.SEVICE_BIND_FAIL);
			}
			
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.e("client", "onServiceConnected");
				mIzkcService = IZKCService.Stub.asInterface(service);
				if(mIzkcService!=null){
					try {
						
						//获取产品型号 get product model
						DEVICE_MODEL = mIzkcService.getDeviceModel();
						//设置当前模块 set current function module
						mIzkcService.setModuleFlag(module_flag);
						
						SERVICE_VERSION = mIzkcService.getServiceVersion();
						
						
						//result.put("devicemodel",DEVICE_MODEL);
						/* result.put("service_v", SERVICE_VERSION); */
						
						Toast.makeText(webView.getContext(), "Service version: "+SERVICE_VERSION, Toast.LENGTH_LONG).show();
						callbackContext.success(DEVICE_MODEL);
					} catch (RemoteException e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						callbackContext.success(sw.toString());
					}
					//发送消息绑定成功 send message to notify bind success
					//sendEmptyMessage(MessageType.BaiscMessage.SEVICE_BIND_SUCCESS);
				}
			}
		};
		
		//com.zkc.aidl.all为远程服务的名称，不可更改
		//com.smartdevice.aidl为远程服务声明所在的包名，不可更改，
		// 对应的项目所导入的AIDL文件也应该在该包名下
		Intent intent = new Intent("com.zkc.aidl.all");
		intent.setPackage("com.smartdevice.aidl");
		webView.getContext().bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	//Loginfo("Trying to create the connection and bind to the ZKC service.");
	
    /* bond.createBond();
	
	public class getBond{
		public getBond() {}
        
		public void Loginfo(String message) {
			Log.i(TAG, message);
		}
		
		public void createBond() {
			try {
				
			
			} catch (RuntimeException e)‏ {
				//error handling code 
				Log.e(TAG, System.out.println(e));
			}
		}
	
	}
	 */
	
}

/* public class ZKCService extends CordovaPlugin {
	
	/* private static final String DURATION_LONG = "long";
	
	public static final String TAG = "ZKCService";
	
	public static String MODULE_FLAG = "module_flag";
	public static int module_flag = 0;
	public static int DEVICE_MODEL = 0;
	private Handler mhanlder; 
	
	public static IZKCService mIzkcService;
	
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
		if ("bindService".equals(action)) {
			//echo(args.getString(0), callbackContext);
			
			String message = "I got your hand; can't drown!";
			String duration = "long";
			
			Toast toast = Toast.makeText(cordova.getActivity(), message,
			DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
			// Display toast
			toast.show();
			// Send a positive result to the callbackContext
			PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
			callbackContext.sendPluginResult(pluginResult);
			return true;
		}
		callbackContext.error("\"" + action + "\" is not a recognized action.");
		return false;
	} */
	
	/* public void bindService(){
		//com.zkc.aidl.all为远程服务的名称，不可更改
		//com.smartdevice.aidl为远程服务声明所在的包名，不可更改，
		// 对应的项目所导入的AIDL文件也应该在该包名下
		Intent intent = new Intent("com.zkc.aidl.all");
		intent.setPackage("com.smartdevice.aidl");
		bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
		
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
} */
