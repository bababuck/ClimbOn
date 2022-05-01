package com.example.climbon;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

public class BluetoothService {
    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;
    private final static String logTag = "BluetoothService";

    public BluetoothService(BluetoothSocket socket) {
        mmSocket = socket;
        OutputStream tmpOut = null;
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(logTag, "Error occurred when creating output stream", e);
        }

        mmOutStream = tmpOut;
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(logTag, "Error occurred when sending data", e);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(logTag, "Could not close the connect socket", e);
        }
    }
}
