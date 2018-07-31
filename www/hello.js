/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "greet", [name]);
    },
    getDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "getDebugTrace", [null]);
    },
    clearDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "clearDebugTrace", [null]);
    },
};
