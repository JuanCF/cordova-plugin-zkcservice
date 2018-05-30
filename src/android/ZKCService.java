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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.smartdevice.aidl.IZKCService;

public class ZKCService extends CordovaPlugin {
	public static final String TAG = "ZKCService";
	
	//getBond bond  = new getBond();
	
	//Loginfo("Declaring static attributes...");
	
	public static String MODULE_FLAG = "module_flag";
	public static int module_flag = 0;
	public static int DEVICE_MODEL = 0;
	public static String printer_firmversion;
	public static String printer_status;
	public static String printer_available;
	public static String SERVICE_VERSION = "version unknown";
	public static IZKCService mIzkcService;
	
	public static String JSON_DATA;
	
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
		else if("printAirtime".equals(action)) {
			printAirtime(args.getString(0), callbackContext);
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

	public void printAirtime(String strData, CallbackContext callbackContext){
		//Retrieve Airtime receipt data.
		JSON_DATA = strData;
		try{
			JSONObject obj = new JSONObject(JSON_DATA);
			JSONArray airtimedata = obj.getJSONArray("airtimedata");
			int datalen = airtimedata.length();
			
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
							
							
							printer_status = mIzkcService.getPrinterStatus();						
							if(mIzkcService.checkPrinterAvailable() == true){
								printer_available = "Airtime data sent to printer.";
								
								//Begin print airtime voucher.
								mIzkcService.printTextAlgin("***** Receipt *****",0,2,1);
								mIzkcService.generateSpace();
								mIzkcService.setAlignment(0);
								mIzkcService.printTextAlgin("Smartbill Platform",0,2,1);
								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
								LocalDateTime now = LocalDateTime.now();							
								mIzkcService.printTextAlgin("Time: "+dtf.format(now),0,2,1);
								mIzkcService.generateSpace();
								mIzkcService.generateSpace();
								
								for (int i = 0; i < datalen; ++i){
									try{
									JSONObject receipt = airtimedata.getJSONObject(i);
									mIzkcService.printGBKText(receipt.getString("business"));
									mIzkcService.printGBKText(receipt.getString("operator"));
									mIzkcService.printGBKText(receipt.getString("airtime"));
									mIzkcService.printGBKText(receipt.getString("amount"));
									mIzkcService.printGBKText(receipt.getString("helpline"));
									mIzkcService.printGBKText(receipt.getString("howto"));
									}catch (JSONException e){
										e.printStackTrace();
									}
									
								}
								
								mIzkcService.generateSpace();
								mIzkcService.generateSpace();
								mIzkcService.generateSpace();
								mIzkcService.printTextAlgin("Proudly by:",0,2,0);
								mIzkcService.printTextAlgin("Venus Dawn Technologies",0,2,1);
								mIzkcService.printTextAlgin("www.venusdawn.co.ls",0,2,1);
								
								//End of airtime voucher.
							}else{
								printer_available = "Printer not initialized or unavailable.";
							}
												
							//result.put("devicemodel",DEVICE_MODEL);
							/* result.put("service_v", SERVICE_VERSION); */
							
							//Toast.makeText(webView.getContext(), "Service version: "+SERVICE_VERSION, Toast.LENGTH_LONG).show();
							callbackContext.success(printer_available);
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
		}catch (JSONException e){
			e.printStackTrace();
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
						
						
						printer_status = mIzkcService.getPrinterStatus();						
						if(mIzkcService.checkPrinterAvailable() == true){
							printer_available = "Available";
							mIzkcService.printGBKText("I have been through the fire, I ran the race and I won.");
							mIzkcService.generateSpace();
							mIzkcService.generateSpace();
							mIzkcService.generateSpace();
							mIzkcService.generateSpace();
						}else{
							printer_available = "Unavailable";
						}
						
						
					
						//result.put("devicemodel",DEVICE_MODEL);
						/* result.put("service_v", SERVICE_VERSION); */
						
						//Toast.makeText(webView.getContext(), "Service version: "+SERVICE_VERSION, Toast.LENGTH_LONG).show();
						callbackContext.success(printer_available);
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
