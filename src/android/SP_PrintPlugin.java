package com.SoftPlus.plugin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.os.Handler;
import android.os.AsyncTask;
import android.content.res.AssetManager;
import android.content.Intent;
import android.util.Xml.Encoding;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.*;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.honeywell.mobility.print.*;

public class SP_PrintPlugin extends CordovaPlugin {
    String debugTrace = "";
    LinePrinter lp = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        // final Context context = ;
        try {

            if (action.equals("write")) {
                lp.write(data.getString(0));
                callbackContext.success();
            } else if (action.equals("newLine")) {
                lp.newLine(data.getInt(0));
                callbackContext.success();
            } else if (action.equals("printGraphicBase64")) {
                String imageData = data.getString(0);
                int rotation = LinePrinter.GraphicRotationDegrees.DEGREE_0;
                if (imageData == null) {
                    callbackContext.error("Base64 encoded string is required!");
                }

                if (data.getInt(1) == -1) {
                    callbackContext.error("Offset number is required!");
                }

                if (data.getInt(2) == -1) {
                    callbackContext.error("Width number is required!");

                }

                if (data.getInt(3) == -1) {
                    callbackContext.error("Height number is required!");
                }

                if (data.getInt(4) != -1) {
                    rotation = data.getInt(4);

                }

                lp.writeGraphicBase64(imageData, rotation, data.getInt(1), // Offset in printhead dots from the left of
                                                                           // the page
                        data.getInt(2), // Desired graphic width on paper in printhead dots
                        data.getInt(3)); // Desired graphic height on paper in printhead dots

                callbackContext.success(debugTrace);
            } else if (action.equals("formFeed")) {
                lp.formFeed();
                callbackContext.success();
            } else if (action.equals("getStatus")) {
                if (lp == null) {
                    callbackContext.error("No printer object exist!");
                }
                try {
                    int[] status = lp.getStatus();
                    callbackContext.success("To do response");
                } catch (Exception e) {
                    callbackContext.error("Not connected!");
                }
            } else if (action.equals("close")) {
                if (clearIntermecPrintersEnvironment()) {
                    callbackContext.success();
                } else {
                    callbackContext.error(debugTrace);
                }
            } else if (action.equals("connect")) {

                debugTrace += "Start connect!";

                if (data.getString(0) == null) {
                    callbackContext.error("Printer name is required as a string parameter!");
                }

                if (data.getString(1) == null) {
                    callbackContext.error("Mac id is required as a string parameter!");
                }

                if (lp == null) {
                    setIntermecPrintersEnvironment(data.getString(0), data.getString(1));
                }

                if (TryToConnect()) {
                    callbackContext.success();
                } else {
                    callbackContext.error(debugTrace);
                }

                // ConnectTask task = new ConnectTask();
                // task.execute();
            } else if (action.equals("clearDebugTrace")) {
                debugTrace = "";
            } else if (action.equals("setBold")) {
                lp.setBold(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("setDoubleWide")) {
                lp.setDoubleWide(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("setDoubleHigh")) {
                lp.setDoubleHigh(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("setItalic")) {
                lp.setItalic(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("setStrikeout")) {
                lp.setStrikeout(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("setUnderline")) {
                lp.setUnderline(data.getBoolean(0));
                callbackContext.success(debugTrace);
            } else if (action.equals("writeBarcode")) {

                if (data.getInt(0) == -1) {
                    callbackContext.error("Symbology number is required!");
                }

                if (data.getString(1) == null) {
                    callbackContext.error("Barcode data string is required!");
                }

                if (data.getInt(2) == -1) {
                    callbackContext.error("Size number is required!");
                }

                if (data.getInt(3) != -1) {
                    callbackContext.error("offset number is required!");
                }

                lp.writeBarcode(data.getInt(0), data.getString(1), data.getInt(2), data.getInt(3));

                callbackContext.success(debugTrace);
            } else {
                callbackContext.success(debugTrace);
            }
        } catch (Exception e) {
            callbackContext.error(debugTrace + " - " + exToString(e));
        }

        return true;
    }

    private String loadPrintSettings(AssetManager assetManager) {

        InputStream input = null;
        ByteArrayOutputStream output = null;
        String resp = "";

        try {
            input = assetManager.open("www/files/printer_profiles.JSON");
            output = new ByteArrayOutputStream(8000);
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
            input.close();
            input = null;

            output.flush();
            output.close();
            resp = output.toString();
            output = null;

        } catch (Exception ex) {
            debugTrace += " Error reading asset file!";
        } finally {
            try {
                if (input != null) {
                    input.close();
                    input = null;
                }

                if (output != null) {
                    output.close();
                    output = null;
                }
            } catch (IOException e) {

            }
        }
        return resp;
    }

    public class BadPrinterStateException extends Exception {

        static final long serialVersionUID = 1;

        public BadPrinterStateException(String message) {
            super(message);
        }

    }

    private String processMacAdress(String paramMac) {
        if (paramMac.contains(":") == false && paramMac.length() == 12) {
            // If the MAC address only contains hex digits without the
            // ":" delimiter, then add ":" to the MAC address string.
            char[] cAddr = new char[17];

            for (int i = 0, j = 0; i < 12; i += 2) {
                paramMac.getChars(i, i + 2, cAddr, j);
                j += 2;
                if (j < 17) {
                    cAddr[j++] = ':';
                }
            }

            return new String(cAddr);
        }

        return paramMac;
    }

    private void setIntermecPrintersEnvironment(String id, String mac) {

        mac = processMacAdress(mac);

        // Setup context

        debugTrace += "Setting up extra setting!;";
        LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
        exSettings.setContext(this.cordova.getActivity().getApplicationContext());
        debugTrace += "Done Setting up extra setting!;";

        AssetManager assetManager = this.cordova.getActivity().getAssets();

        debugTrace += "Reading Settings!;";
        String jsonCmdAttribStr = loadPrintSettings(assetManager);
        debugTrace += "Finished reading!;";
        String sPrinterURI = "bt://" + mac;

        try {
            lp = new LinePrinter(jsonCmdAttribStr, id, sPrinterURI, exSettings);
        } catch (Exception e) {
            debugTrace += " - " + exToString(e);
        }

        debugTrace += "Created LinePrinter!;";
    }

    private boolean clearIntermecPrintersEnvironment() {
        try {
            lp.disconnect(); // Disconnects from the printer
            lp.close(); // Releases resources
            lp = null;

            debugTrace += "LP object disposed ";
            return true;
        } catch (Exception e) {
            debugTrace += "LinePrinterException: " + exToString(e);
            return false;
        }
    }

    private String exToString(Exception e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        String errMsg = e.getMessage();
        String stackTrace = writer.toString();
        printWriter.flush();
        return errMsg + " - " + stackTrace;
    }

    private boolean TryToConnect() {
        try {
            // A retry sequence in case the Bluetooth socket is temporarily not ready
            int numtries = 0;
            int maxretry = 2;
            while (numtries < maxretry) {
                debugTrace += "Connect LinePrinter!;";
                try {
                    lp.connect(); // Connects to the printer
                    break;
                } catch (PrinterException ex) {
                    numtries++;
                    Thread.sleep(1000);
                }
            }
            if (numtries == maxretry)
            lp.connect();// Final retry
            

            // Check the state of the printer and abort printing if there are
            // any critical errors detected.
            int[] results = lp.getStatus();
            if (results != null) {
                for (int err = 0; err < results.length; err++) {
                    if (results[err] == 223) {
                        // Paper out.
                        throw new BadPrinterStateException("Paper out");
                    } else if (results[err] == 227) {
                        // Lid open.
                        throw new BadPrinterStateException("Printer lid open");
                    }
                }
            }

            debugTrace = "Connected to a printer!;";
            return true;

        } catch (Exception e) {
            debugTrace += "LinePrinterException: " + exToString(e);
            if (lp != null) {
                clearIntermecPrintersEnvironment();
            }
            return false;
        }
    }
}
