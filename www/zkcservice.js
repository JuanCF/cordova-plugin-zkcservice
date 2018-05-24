var exec = cordova.require('cordova/exec');

var service = "ZKCService";

exports.bindService = function(win, fail) {
	exec(win, fail, service, "bindService", []);
};

exports.onDataReceived = function(handle, win, fail) {
	exec(win, fail, service, "onDataReceived", [handle]);
};

exports.onError = function(handle, win, fail) {
	exec(win, fail, service, "onError", [handle]);
};

exports.sendData = function(handle, data, win, fail) {
	exec(win, fail, service, "sendData", [handle, data]);
};
