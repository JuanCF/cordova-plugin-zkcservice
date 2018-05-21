package com.smartdevice.aidl;

import com.smartdevice.aidl.ICallBack;
import android.graphics.Bitmap;
import android.content.Context;

interface IZKCService{

	/**
	* Set the current function module
	* @param cb:	Implement callbacks for the ICallback interface
	*/
	boolean setModuleFlag(int module);
	/************************************************************
	 *                                                        ***
	 *                printers                                ***
	 *                                                        ***
	 ************************************************************/

	 /**
	* register
	* @param cb:	Implement callbacks for the ICallback interface
	*/
	void registerCallBack(String flag,ICallBack callback);

	/**
	* Cancellation callback
	* @param cb:	Implement callbacks for the ICallback interface
	*/
	void unregisterCallBack(String flag,ICallBack callback);

	/**
	* Stop callback tasks
	*/
	void stopRunningTask();

	/**
	* Determine if the callback task is running
	*/
	boolean isTaskRunning();

	/**
	* Get Printer Firmware Version
	*/
	void getFirmwareVersion();

	/**
	* Get ZKCService service version
	*/
	String getServiceVersion();

	/**
	* Get ZKCService service version
	*/
	int getDeviceModel();

	/**
	 * Initialize the printer
	 */
	void printerInit();

	/**
	* Get printer status
	*/
	String getPrinterStatus();

	/**
	* Printer self-test, printer prints self-test page
	*/
	void printerSelfChecking();

	/**
	* Check if the printer is idle
	*/
	boolean checkPrinterAvailable();

	/**
	* Print using original instructions.
	* @param data 指令
	* @param callback  结果回调
	*/
	void sendRAWData(String flag,in byte[] data);

	/**
    * Check if the printer is idle
    */
    boolean setPrinterLanguage(String lang, int langCode);

	/**
	* Set the alignment mode to affect printing later, unless initialized
	* @param alignment:	Alignment 0--left, 1--center, 2--right
	* @param callback  Result callback
	*/
	void setAlignment(int alignment);

	/**
	* Set the print font to affect printing later unless initialized
	* (Currently only two fonts are supportedCurrently only two fonts are supported)
	* @param typeface:	Font Type 0--ASCII(12*24) Chinese Character(24*24),1--ASCII(8*16) Chinese Character(16*16)
	*/
	void setTypeface(int type);

	/**
	*Setting the font size has an effect on printing later unless initialized
	* Adjusting the font size will affect the character width, and the number of characters per line will also change.
	* @param fontsize: Font size type 0-- Characters are normal: Not zoomed in, 1--characters 2 times tall: Zoom in portrait,
	* 2-character 2x width: horizontal zoom, 3-character 2x zoom
	*/
	void setFontSize(int type);

	/**
	* Print text, word width full line wrap typesetting, not full line print unless forced line break
	* @param text:	Translation error
	*/
	void printGBKText(String text);

	/**
	* Print text, word width full line wrap typesetting, not full line print unless forced line break
	* @param text:	The text string to be printed
	*/
	void printUnicodeText(String text);

	/**
	* Print the text of the specified font. The font setting is valid only for this time.
	* @param text:			To print text
	* @param typeface:		Font type
	* @param fontsize:		Font size type
	*/
	void printTextWithFont(String text, int type, int size);

	/**
	* Print the text of the specified font, font size, and alignment. The font setting is valid only for this time.
	* @param text:			To print text
	* @param typeface:		Font type
	* @param fontsize:		Font size type
	* @param alginStyle:	Alignment (0 left, 1 center, 2 right)
	*/
	void printTextAlgin(String text, int type, int size, int alginStyle);

	/**
	* Print a row of the table, you can specify the column width, alignment
	* @param colsTextArr   Column array of text strings
	* @param colsWidthArr  Column width array (calculated in English characters, each Chinese character occupies two English characters, each width is greater than 0)
	* @param colsAlign	        Column alignment (0 left, 1 center, 2 right)
	* Remarks: The length of the array of three parameters should be the same. If the width of colsText[i] is greater than colsWidth[i], the text is wrapped.
	*/
	void printColumnsText(in String[] colsTextArr, in int[] colsWidthArr, in int[] colsAlign);

	/**
	* Print picture
	* @param bitmap: 	Picture bitmap object
	*/
	void printBitmap(in Bitmap bitmap);

	/**
	* Print image (with alignment)
	* @param bitmap: 	Picture bitmap object
	* @parm position:   Image Location 0--left, 1--center, 2--right
	*/
	void printBitmapAlgin(in Bitmap bitmap, int width, int height,int position);

	/**
	* Create a one-dimensional barcode picture
	* @param data: 		Barcode data
	* @param symbology: 	Barcode type
	*    0 -- UPC-A，
	*    1 -- UPC-E，
	*    2 -- JAN13(EAN13)，
	*    3 -- JAN8(EAN8)，
	*    4 -- CODE39，
	*    5 -- ITF，
	*    6 -- CODABAR，
	*    7 -- CODE93，
	*    8 -- CODE128
	* @param height: 		Barcode height, value 1 to 255, default 162
	* @param width: 		Barcode width, values 0 to 5, default 3
	* @param displayText:	Whether to display text
	*/
	Bitmap createBarCode(String data, int codeFormat, int width, int height, boolean displayText);

	/**
	* Create a 2D barcode picture
	* @param data:			QR code data
	* @param modulesize:	Two-dimensional block size (unit: point, value 1 to 16)
	*/
	Bitmap createQRCode(String data, int width, int height);

	/********************************
	********Date Added: 2017/03/14******
	*********************************/

	/*
	*Print stickers to get clearance
	*/
	void generateSpace();

	/********************************
	********Date added 2017/04/25******
	*********************************/

	/*
	*Print picture (grayscale)
	*/
	boolean printImageGray(in Bitmap bitmap);

	/*
	*Print picture (raster)
	*/
	boolean printRasterImage(in Bitmap bitmap);

	/*
	*Print UNINCODE (raster)
	*/
	boolean printUnicode_1F30(String textStr);

	/*
	*Set the print language
	*/
	void setPrintLanguage(String language);

	/*
	*Get the firmware version 1
	*/
	String getFirmwareVersion1();

	/*
	*Get the firmware version 2
	*/
	String getFirmwareVersion2();

	/*
	*Specify print bar code
	*/
//	boolean printBarCode(String content);

	/*
	*Specify to print two-dimensional code
	*/
//	boolean printQrCode(String content);



	/************************************************************
	 *                                                        ***
	 *                Customer Display                        ***
	 *                                                        ***
	 ************************************************************/

	 /**
	 * Control panel backlight power
	 * @param btFlg Control backlight brightness 0===“Light; 1===” is off
	 */

	 void openBackLight(int btFlg);

	 /**
	 *  Display color pictures, unlimited format
	 * @param bitmapSrc Display picture bitmap
	 */

	 boolean showRGB565Image(in Bitmap bitmapSrc);

	 /**
	 *  Display color pictures, unlimited format
	 * @param path Picture path
	 */

	 boolean showRGB565ImageByPath(String path);

	 /**
	 * Specify a location to display color pictures, unlimited format
	 * @param bitmapSrc
	 *            image
	 * @param x
	 *            image
	 * @param y
	 *            Starting point Y coordinate
	 * @param width
	 *            Display width
	 * @param height
	 *            Display height
	 * @return
	 */

	 boolean showRGB565ImageLocation(in Bitmap bitmapSrc,int x, int y, int width, int height);

	 /**
	 * Update screen LOGO
	 * @param bitmapSrc
	 * @return
	 */

	 boolean updateLogo(in Bitmap bitmapSrc);

	 /**
	 * Update screen LOGO
	 * @param path
	 * @return
	 */

	 boolean updateLogoByPath(String path);

	 /**
	 * Display two-color pictures
	 *
	 * @param BackColor
	 * @param ForeColor
	 * @param bitmapSrc
	 * @return
	 */

	 boolean showDotImage(int BackColor, int ForeColor,in Bitmap bitmapSrc);

	  /**
	 * The screen shows a color picture in the center
	 * @param bitmapSrc
	 * @return
	 */

	 boolean showRGB565ImageCenter(in Bitmap bitmapSrc);

	 /************************************************************
	 *                                                        ***
	 *                	PSAM card                                ***
	 *                                                        ***
	 ************************************************************/

	/**
	* Turn on PSAM power
	*/

	int Open();

	/**
	* Turn off the PSAM power
	*/

	int Close(long fd);

	/**
	* Open/close GPIO port
	* @return  true--success; false--fail
	*/

	boolean setGPIO(int io,int status);

	/**
	* Open the specified PSAM card
	* @param carPositin: PSAM card location
	* @return Returns 0 success
	*/

	int openCard(int carPositin);

	/**
	 * Turn on device 2
	 *
	 * @param Card slot number
	 */

	int openCard2(inout int[] fd,int slotno);

	/**
	 * Turn on device 3
	 *
	 * @param Card slot number
	 */

	int openCard3(long fd,int slotno);

	/**
	* Close the open PSAM card
	* @return Returns 0 success
	*/
	int CloseCard();

	/**
	 *Function: Turn off device 2
	 *c parameter v2 true: device 3/device 2
	 *Parameters: [in]unsigned long fd passed in device handle to close
	 *Return value: correct 0, error non-zero
	*/

	int CloseCard2(long fd, boolean v2);

	/**
	* Reset the currently open PSAM card
	* @param power: The specified voltage size
	* @return Returns the PSAM card number as a byte array
	*/
	byte[] ResetCard(int power);

	/**
	 *Function: Device Reset 2
	 *Parameters: [in]unsigned long fd passed in device handle to close
	 *       [out]unsigned char *atr outgoing device reset information
	 *       [in/out]int *atrLen Outgoing device reset message length
	 *Return value: correct 0, error non-zero
	*/

	int ResetCard2(long fd, inout byte[] atr,inout int[] atrLen);

	/**
	 *Function: Device Reset 3
	 *Parameters: [in]unsigned long fd passed in device handle to close
	 *       [out]unsigned char *atr outgoing device reset information
	 *       [in/out]int *atrLen Outgoing device reset message length
	 *Return value: correct 0, error non-zero
	*/

	byte[] ResetCard3(long fd,int slotno,int pw);

	/**
	* Send APDU Command to Current PSAM Card
	* @param apdu: APDU command
	* @return Return the command result as a byte array
	*/
	byte[] CardApdu(in byte[] apdu);

	/**
	 *Function: Send APDU Instruction 2
	 *parameter： [in]unsigned long fd incoming device handle
	 *     [in]unsigned char *apdu Apdu command to send
	 *     [in]int apduLength The length of the apdu instruction to be sent
	 *     [out]unsigned char*response Return data content
	 *     [in/out]int* respLength Return data length
	 *Return value: correct 0, error non-zero
	 *Note: This interface does not perform automatic response data acquisition (ie, this interface does not automatically send "00c0" as a response command)
	*/

	int CardApdu2(long fd, in byte[] apdu,int apduLength, inout byte[] response,inout int[] respLength);

	/**
	 *Function: Send APDU instruction 3
	 *parameter： [in]unsigned long fdIncoming device handle
	 *     [in]unsigned char *apdu Apdu command to send
	 *     [in]int apduLength The length of the apdu instruction to be sent
	 *     [out]unsigned char*response Return data content
	 *     [in/out]int* respLength Return data length
	 *Return value: correct 0, error non-zero
	 *Note: This interface does not perform automatic response data acquisition (ie, this interface does not automatically send "00c0" as a response command)
	*/

	byte[] CardApdu3(long fd, in byte[] apdu,int apduLength);

	/************************************************************
	 *                                                        ***
	 *                	scanning                                  ***
	 *                                                        ***
	 ************************************************************/

	/**
	* Open scan
	* @param status: true:Open; false: close
	*/
	void openScan(boolean status);

	/**
	* scanning
	*/
	void scan();

	/**
	* Add a return at the end of the data
	* @param status: True: append; false: not append
	*/
	void dataAppendEnter(boolean  status);

	/**
	* Scan success tone
	* @param status:  true: need; false: no
	*/
	void appendRingTone(boolean status);

	/**
	* Continuous scan
	* @param status: true: need; false: no
	*/
	void continueScan(boolean status);

	/**
	* Scan code repeat prompt
	* @param status: true: need; false: no
	*/
	void scanRepeatHint(boolean status);

	/**
	* reset
	* @param status: true:Settings take effect; false: settings do not take effect
	*/
	void recoveryFactorySet(boolean status);

	/**
	* Send scan instructions
	* @param byte[]Command data sent
	* @return Received results
	*/
	byte[] sendCommand(in byte[] buffer);

	/**
	* Can you scan normally?
	* @return true:Scan is turned on; false: Scan is turned off；
	*/
	boolean isScanning();

	/**
	* Get ID information
	* @return ID information
	*/
	String getIdentifyInfo();

	/**
	* Turn on the power
	* @return ID information
	*/
	boolean turnOn();

	/**
	* Disconnect the power
	* @return ID information
	*/
	boolean turnOff();

	/**
	* Disconnect the power
	* @return ID information
	*/
	Bitmap getHeader();

}