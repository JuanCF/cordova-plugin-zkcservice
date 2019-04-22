var exec = require('cordova/exec');

var service = "ZKCService";

exports.ToastIt = function(args, success, error) {
	exec(success, error, service, "ToastIt", [args]);
};

exports.printAirtime = function(args, success, error) {
	exec(success, error, service, "printAirtime", [args]);
};

exports.turnOnPrinter = function(args, success, error) {
	exec(success, error, service, "turnOnPrinter", [args]);
};

exports.turnOffPrinter = function(args, success, error) {
	exec(success, error, service, "turnOffPrinter", [args]);
};

exports.getPrinterStatus = function(args, success, error) {
	exec(success, error, service, "getPrinterStatus", [args]);
};

exports.bindZKCService = function(args, success, error) {
	exec(success, error, service, "bindZKCService", [args]);
};

exports.onDataReceived = function(handle, success, error) {
	exec(success, error, service, "onDataReceived", [handle]);
};

exports.onError = function(handle, success, error) {
	exec(success, error, service, "onError", [handle]);
};

exports.sendData = function(handle, data, success, error) {
	exec(success, error, service, "sendData", [handle, data]);
};
