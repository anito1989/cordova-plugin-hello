/*global cordova, module*/

module.exports = {

    connect: function (printerName, macAdressString, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "connect", [printerName, macAdressString]);
    },

    close: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "close", [null]);
    },

    getStatus: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "getStatus", [null]);
    },

    //General commands
    printGraphicBase64: function (imageString, offsetNumber, widthNumber, heightNumber, rotationNumberOptional, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "printGraphicBase64", [imageString,offsetNumber, widthNumber, heightNumber, rotationNumberOptional]);
    },
    //General commands
    writeBarcode: function (symbologyNumber, dataString, sizeNumber, offsetNumber, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "writeBarcode", [symbologyNumber, dataString, sizeNumber, offsetNumber]);
    },
    setBold: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setBold", [boolean]);
    },
    setDoubleWide: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setDoubleWide", [boolean]);
    },
    setDoubleHigh: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setDoubleHigh", [boolean]);
    },
    setItalic: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setItalic", [boolean]);
    },
    setStrikeout: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setStrikeout", [boolean]);
    },
    setUnderline: function (boolean, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "setUnderline", [boolean]);
    },
    write: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "write", [string]);
    },
    newLine: function (numberOfLines, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "newLine", [numberOfLines]);
    },
    formFeed: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "formFeed", [null]);
    },




    //Debug commands
    getDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "getDebugTrace", [null]);
    },
    clearDebugTrace: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SP_PrintPlugin", "clearDebugTrace", [null]);
    },
};
