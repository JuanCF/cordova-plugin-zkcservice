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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class ZKCService extends CordovaPlugin {
	public static final String TAG = "ZKCService";

	public static String MODULE_FLAG = "module_flag";
	public static int module_flag = 0;
	public static int DEVICE_MODEL = 0;
	public static String printer_firmversion;
	public static String printer_status;
	public static String printer_available;
	public static String SERVICE_VERSION = "version unknown";
	public static IZKCService mIzkcService;
    public static Boolean running_flag = true;
	
	public static String JSON_DATA;
	
	private Handler mhanlder;
    private Bitmap mBitmap = null;
	
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
		}else if("turnOnPrinter".equals(action)){
            turnOnPrinter(callbackContext);
            return true;
        }else if("turnOffPrinter".equals(action)){
            turnOffPrinter(callbackContext);
            return true;
        }else if("turnOnScanner".equals(action)){
            turnOnScanner(callbackContext);
            return true;
        }else if("turnOffScanner".equals(action)){
            turnOffScanner(callbackContext);
            return true;
        }else if("getPrinterStatus".equals(action)){
            getPrinterStatus(callbackContext);
            return true;
        }else if("testPrinter".equals(action)){
            testPrinter(args.getString(0),callbackContext);
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

    //Printer Methods

    private void turnOnPrinter(CallbackContext callbackContext) {
      int last_module_flag = module_flag;
      module_flag = 8;
      if(mIzkcService != null){
        try{
            mIzkcService.setModuleFlag(module_flag);
            callbackContext.success("Printer Turned On");
        }catch (RemoteException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            callbackContext.success(sw.toString());
        }
      }else{
        callbackContext.error("AIDL Service not connected");
      }
      module_flag = last_module_flag;
	}

    private void turnOffPrinter(CallbackContext callbackContext) {
      int last_module_flag = module_flag;
      module_flag = 9;
      if(mIzkcService != null){
         try{
            mIzkcService.setModuleFlag(module_flag);
            callbackContext.success("Printer Turned Off");
        }catch (RemoteException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            callbackContext.success(sw.toString());
        }
      }else{
        callbackContext.error("AIDL Service not connected");
      }
      module_flag = last_module_flag;
	}

    //Scanner Methods

    private void turnOnScanner(CallbackContext callbackContext) {
      if(mIzkcService != null){
        try{
            mIzkcService.turnOn();
            callbackContext.success("Scanner Turned On");
        }catch (RemoteException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            callbackContext.success(sw.toString());
        }
      }else{
        callbackContext.error("AIDL Service not connected");
      }
	}

    private void turnOffScanner(CallbackContext callbackContext) {
      if(mIzkcService != null){
        try{
            mIzkcService.turnOff();
            callbackContext.success("Scanner Turned Off");
        }catch (RemoteException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            callbackContext.success(sw.toString());
        }
      }else{
        callbackContext.error("AIDL Service not connected");
      }
	}

    private void getPrinterStatus(CallbackContext callbackContext) {
      if(mIzkcService != null){
        try{
          printer_status = mIzkcService.getPrinterStatus();
          callbackContext.success(printer_status);
        }catch(Exception  e){
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          e.printStackTrace(pw);
          callbackContext.success(sw.toString());
        }
      }else{
        callbackContext.error("AIDL Service not connected");
      }
	}

    private void testPrinter(String msg,CallbackContext callbackContext) {
      if(mIzkcService!=null){
          cordova.getThreadPool().execute(new Runnable() {
              public void run() {
                try {
                  if(mIzkcService.checkPrinterAvailable() == true){
                      printer_available = "Voucher sent to printer.";

                      mIzkcService.setAlignment(1);
                      mIzkcService.printGBKText("REPUBLICA DE HONDURAS"+ "\n");
                      mIzkcService.printGBKText("SECRETARIA DE SEGURIDAD"+ "\n");
                      mIzkcService.printGBKText("DIRECCION NACIONAL DE VIALIDAD Y"+ "\n");
                      mIzkcService.printGBKText("TRANSPORTE"+ "\n\n\n\n");
                      mIzkcService.printGBKText("Codigo de Infraccion: "+ msg+"\n\n\n\n");
                      mIzkcService.printGBKText("INFRACTOR"+ "\n\n");
                      mIzkcService.setAlignment(0);
                      mIzkcService.printGBKText("Nombre: Juan Carlos Flores Martínez"+ "\n");
                      mIzkcService.printGBKText("Licencia No.: XXXXX"+ "\n");
                      mIzkcService.printGBKText("Licencia Valida Hasta: 17-10-2022 (vigente)"+ "\n\n");
                      mIzkcService.setAlignment(1);
                      mIzkcService.printGBKText("VEHICULO"+ "\n\n");
                      mIzkcService.setAlignment(0);
                      mIzkcService.printGBKText("Marca: Toyota     Color: Negro"+ "\n");
                      mIzkcService.printGBKText("Tipo: Turismo     Año: 1998   "+ "\n");
                      mIzkcService.printGBKText("Placa: XXXXXXX"+ "\n\n");
                      mIzkcService.setAlignment(1);
                      mIzkcService.printGBKText("INFRACCION"+ "\n\n");
                      mIzkcService.setAlignment(0);
                      mIzkcService.printGBKText("Infraccion Cometida: Articulo XX#XX"+ "\n");
                      mIzkcService.printGBKText("Tipo de Infraccion: XXXX"+ "\n");
                      mIzkcService.printGBKText("Descripcion: XXXXXXX XXXXXXX XXX XXXXXXXX"+ "\n");
                      mIzkcService.printGBKText("Lugar de Infraccion: Tegucigalpa, F.M."+ "\n");
                      mIzkcService.printGBKText("Fecha y Hora Infraccion: XX/XX/XXXX - XX:XX"+ "\n\n");
                      mIzkcService.setAlignment(1);
                      mIzkcService.printGBKText("CARGOS"+ "\n\n");
                      mIzkcService.setAlignment(0);
                      mIzkcService.printGBKText("Infraccion Actual:     L XXX.00"+ "\n");
                      mIzkcService.printGBKText("Infracciones Anteriores: L 0.00"+ "\n");
                      mIzkcService.printGBKText("                        --------"+ "\n");
                      mIzkcService.printGBKText("                       L XXX.00"+ "\n\n\n");
                      mIzkcService.setAlignment(1);
                      mIzkcService.printGBKText("El infractor debera efectuar el pago de ");
                      mIzkcService.printGBKText("infracciones utilizando el app InfraccionesHN " );
                      mIzkcService.printGBKText("o desde el sitio web infraccioneshn.com o ");
                      mIzkcService.printGBKText("en las ventanillas de Banco Atlantida en un ");
                      mIzkcService.printGBKText("plazo no mayor a un mes despues de emitido ");
                      mIzkcService.printGBKText("este documento."+ "\n\n\n");
                      mIzkcService.printGBKText("Carlos Alejandro Oseguera Barrientos"+ "\n");
                      mIzkcService.printGBKText("Policia de Viabilidad y Transporte"+ "\n");
                      mIzkcService.printGBKText("DNVT - 09624"+ "\n");
                      mIzkcService.printGBKText("Tegucigalpa, Franciso Morazan"+ "\n\n\n\n");
                      Thread.sleep(100);
                      mBitmap = mIzkcService.createQRCode(msg, 384, 384);
                      mIzkcService.printBitmap(mBitmap);
                      mIzkcService.printGBKText("\n\n\n\n");
                  }else{
                      printer_available = "Printer not initialized or unavailable.";
                  }
                  callbackContext.success(printer_available);
              } catch (Exception e) {
                  StringWriter sw = new StringWriter();
                  PrintWriter pw = new PrintWriter(sw);
                  e.printStackTrace(pw);
                  callbackContext.success(sw.toString());
              }
              }
          });

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
            Context context = cordova.getActivity().getApplicationContext();
			context.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
		}catch (JSONException e){
			e.printStackTrace();
		}
	}
	
	public void bindZKCService(CallbackContext callbackContext) {
		//statements that may cause an exception
		ServiceConnection mServiceConn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				//Log.e("client", "onServiceDisconnected");
				mIzkcService = null;
				//Toast.makeText(webView.getContext(), "Failed to connect to service.", Toast.LENGTH_LONG).show();
				callbackContext.error("Failed to connect to service.");
				//发送消息绑定失败 send message to notify bind fail
			}
			
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mIzkcService = IZKCService.Stub.asInterface(service);
                Log.i("client", "onServiceConnected");
				if(mIzkcService!=null){
					try {
						
						//Get product model
						DEVICE_MODEL = mIzkcService.getDeviceModel();
						//Set current function module
						mIzkcService.setModuleFlag(module_flag);
						
						SERVICE_VERSION = mIzkcService.getServiceVersion();
						

						
						JSONObject responseJson = new JSONObject();
                        responseJson.put("DEVICE_MODEL", DEVICE_MODEL);
                        responseJson.put("SERVICE_VERSION", SERVICE_VERSION);
                        responseJson.put("STATUS", "CONNECTED");

						callbackContext.success(responseJson);
					} catch (Exception  e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						callbackContext.success(sw.toString());
					}
				}
			}
		};
		
		//com.zkc.aidl.all为远程服务的名称，不可更改
		//com.smartdevice.aidl为远程服务声明所在的包名，不可更改，
		// 对应的项目所导入的AIDL文件也应该在该包名下
		Intent intent = new Intent("com.zkc.aidl.all");
		intent.setPackage("com.smartdevice.aidl");
        Context context = cordova.getActivity().getApplicationContext();
		context.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
	}

}

