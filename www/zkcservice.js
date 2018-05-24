var exec = cordova.require('cordova/exec');

var service = "ZKCService";

exports.Servicebind = function(args, success, error) {
	exec(win, fail, service, "Servicebind", [args]);
};

exports.onDataReceived = function(handle, success, error) {
	exec(win, fail, service, "onDataReceived", [handle]);
};

exports.onError = function(handle, success, error) {
	exec(win, fail, service, "onError", [handle]);
};

exports.sendData = function(handle, data, success, error) {
	exec(win, fail, service, "sendData", [handle, data]);
};
