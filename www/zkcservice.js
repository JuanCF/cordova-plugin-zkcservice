var exec = cordova.require('cordova/exec');

var service = "ZKCService";

exports.Servicebind = function(args, success, error) {
	exec(success, error, service, "Servicebind", [args]);
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
