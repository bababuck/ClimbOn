package com.example.climbon;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class ClimbOnApplication extends Application {
    /* Holds global data for storage.

    TODO: Add null checks when gathering data since can be deleted
    - Try/catch statements
    */
    public UniversalData data = new UniversalData();
    public SQLiteDatabase db;

    public BluetoothService bluetoothService;

    @Override
    public void onCreate() {
        super.onCreate();
        setupSQL();
        setupBluetooth();
    }

    private void setupSQL() {
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        db = dbHelper.getReadableDatabase();
    }
    private void setupBluetooth() {
        bluetoothService = new BluetoothService();
    }

//    private void setupBluetooth() {
//        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) this.getSystemService(this.BLUETOOTH_SERVICE)).getAdapter();
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        BluetoothDevice device = pairedDevices.iterator().next();
//        String deviceHardwareAddress = device.getAddress();
//
//        if(device.getBondState()==device.BOND_BONDED) {
//            Log.d("ClimbOnApplication", device.getName());
//            BluetoothSocket mSocket = null;
//            try {
//                mSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
//                bluetoothService = new BluetoothService(mSocket);
//            } catch (IOException e1) {
//                Log.d("ClimbOnApplication", "socket not created");
//            }
//        }
//    }
}