var exec = require('cordova/exec');

var service = "ZKCService";

exports.ToastIt = function(args, success, error) {
	exec(success, error, service, "ToastIt", [args]);
};

//ZKC AIDL Service

exports.bindZKCService = function(args, success, error) {
	exec(success, error, service, "bindZKCService", [args]);
};

//Printer Methods

exports.turnOnPrinter = function(args, success, error) {
	exec(success, error, service, "turnOnPrinter", [args]);
};

exports.turnOffPrinter = function(args, success, error) {
	exec(success, error, service, "turnOffPrinter", [args]);
};

exports.getPrinterStatus = function(args, success, error) {
	exec(success, error, service, "getPrinterStatus", [args]);
};

exports.testPrinter = function(args, success, error) {
	exec(success, error, service, "testPrinter", [args]);
};

exports.printAirtime = function(args, success, error) {
	exec(success, error, service, "printAirtime", [args]);
};

exports.printBase64Image = function(args, success, error) {
	exec(success, error, service, "printBase64Image", [args]);
};

exports.printText = function(args, success, error) {
	exec(success, error, service, "printText", [args]);
};

//Scanner Methods

exports.turnOffScanner = function(args, success, error) {
	exec(success, error, service, "turnOffScanner", [args]);
};

exports.turnOnScanner = function(args, success, error) {
	exec(success, error, service, "turnOnScanner", [args]);
};

//PSAM Methods

exports.onDataReceived = function(handle, success, error) {
	exec(success, error, service, "onDataReceived", [handle]);
};

exports.onError = function(handle, success, error) {
	exec(success, error, service, "onError", [handle]);
};

exports.sendData = function(handle, data, success, error) {
	exec(success, error, service, "sendData", [handle, data]);
};
