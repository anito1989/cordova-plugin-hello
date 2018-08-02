/*global cordova, module*/

module.exports = {

    connect: function (printerName, macAdressString, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "connect", [printerName, macAdressString]);
    },

    close: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "close", [null]);
    },

    getStatus: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "getStatus", [null]);
    },

    //General commands
    printGraphicBase64: function (imageString, offsetNumber, widthNumber, heightNumber, rotationNumberOptional, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "printGraphicBase64", [imageString,offsetNumber, widthNumber, heightNumber, rotationNumberOptional]);
    },
    //General commands
    writeBarcode: function (symbologyNumber, dataString, sizeNumber, offsetNumber, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "writeBarcode", [symbologyNumber, dataString, sizeNumber, offsetNumber]);
    },
    setBold: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setBold", [boolean]);
    },
    setDoubleWide: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setDoubleWide", [boolean]);
    },
    setDoubleHigh: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setDoubleHigh", [boolean]);
    },
    setItalic: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setItalic", [boolean]);
    },
    setStrikeout: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setStrikeout", [boolean]);
    },
    setUnderline: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "setUnderline", [boolean]);
    },
    write: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "write", [string]);
    },
    newLine: function (numberOfLines, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "newLine", [numberOfLines]);
    },
    formFeed: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "formFeed", [null]);
    },




    //Debug commands
    getDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "getDebugTrace", [null]);
    },
    clearDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "clearDebugTrace", [null]);
    },
};
