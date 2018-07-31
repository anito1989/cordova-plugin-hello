package com.example.plugin;

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

import com.honeywell.mobility.print.LinePrinter;
import com.honeywell.mobility.print.LinePrinterException;
import com.honeywell.mobility.print.PrintProgressEvent;
import com.honeywell.mobility.print.PrintProgressListener;

public class Hello extends CordovaPlugin {
    AssetManager assetManager;
    String debugTrace = "";

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        // final Context context = ;
        assetManager = this.cordova.getActivity().getAssets();
        String sPrinterID = "PR3";
        String sPrinterURI = "bt://00:1D:DF:55:71:6A";
        LinePrinter lp = null;

        try {
            debugTrace += " Reading Settings!;";
            String jsonCmdAttribStr = loadPrintSettings(assetManager);
            debugTrace += " Finished reading!;";

            // Setup context
            debugTrace += " Setting up extra setting!;";
            LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
            exSettings.setContext(this.cordova.getActivity().getApplicationContext());
            debugTrace += " Done Setting up extra setting!;";

            PrintTask task = new PrintTask(jsonCmdAttribStr, sPrinterID, sPrinterURI, exSettings);
            task.execute("go go go");

            callbackContext.success(debugTrace);
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String errMsg = e.getMessage();
            String stackTrace = writer.toString();
            printWriter.flush();
            callbackContext.error(debugTrace + " - " + errMsg + " - " + stackTrace);
            // PluginResult resultB = new PluginResult(PluginResult.Status.Error, debugTrace
            // + " - " + errMsg + " - " + stackTrace);
            // callbacks.sendPluginResult(resultB);
        }

        // cordova.getThreadPool().execute(new Runnable() {
        // public void run() {

        // }
        // });

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

    public class PrintTask extends AsyncTask<String, Integer, String> {

        String _jsonCmdAttribStr;
        String _sPrinterID;
        String _sPrinterURI;
        LinePrinter.ExtraSettings _exSettings;

        public PrintTask(String jsonCmdAttribStr, String sPrinterID, String sPrinterURI,
                LinePrinter.ExtraSettings exSettings) {
            super();
            _jsonCmdAttribStr = jsonCmdAttribStr;
            _sPrinterID = sPrinterID;
            _sPrinterURI = sPrinterURI;
            _exSettings = exSettings;
        }

        /**
         * Runs on the UI thread before doInBackground(Params...).
         */
        @Override
        protected String doInBackground(String... args) {

            lp = new LinePrinter(_jsonCmdAttribStr, _sPrinterID, _sPrinterURI, _exSettings);
            debugTrace += " Created LinePrinter!;";
            return "good";
        }

        /**
         * Runs on the UI thread after doInBackground method. The specified result
         * parameter is the value returned by doInBackground.
         */
        @Override
        protected void onPostExecute(String result) {

            // Displays the result (number of bytes sent to the printer or
            // exception message) in the Progress and Status text box.
            if (result != null) {
            }
        }

    }
}
