package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.Intent;

import android.util.Xml.Encoding;
import android.util.Base64;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;



import com.honeywell.mobility.print.LinePrinter;
import com.honeywell.mobility.print.LinePrinterException;
import com.honeywell.mobility.print.PrintProgressEvent;
import com.honeywell.mobility.print.PrintProgressListener;

public class Hello extends CordovaPlugin {  
    AssetManager assetManager;
    String debugTrace = "";

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
       Context context = this.cordova.getActivity().getApplicationContext();
        assetManager = this.cordova.getActivity().getAssets();       


        try {
            debugTrace += " Reading Settings!;";
            String jsonCmdAttribStr = loadPrintSettings(assetManager);
            debugTrace += " Finished reading!;";


            // Setup context
            debugTrace += " Setting up extra setting!;";
            LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
            exSettings.setContext(context);
            debugTrace += " Done Setting up extra setting!;";
            }
            catch (Exception e) {
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String errMsg = e.getMessage();
                String stackTrace = writer.toString();
                printWriter.flush();

                // PluginResult resultB = new PluginResult(PluginResult.Status.Error, debugTrace + " - " + errMsg + " - " + stackTrace);
                // callbacks.sendPluginResult(resultB);            
            }

        // cordova.getThreadPool().execute(new Runnable() {
        //     public void run() {

                
        //     }
        // });

        PluginResult pluginResult = new  PluginResult(PluginResult.Status.NO_RESULT); 
        pluginResult.setKeepCallback(true); // Keep callback

        return true;
    }

    private String loadPrintSettings(AssetManager assetManager) {

        InputStream input = null;
        ByteArrayOutputStream output = null;
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
        output = null;
        return output.toString();
    }

}
