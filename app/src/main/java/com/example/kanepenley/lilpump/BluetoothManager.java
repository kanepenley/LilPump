package com.example.kanepenley.lilpump;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
/**
 * Created by brandon on 11/30/17.
 */

public class BluetoothManager {
    BluetoothAdapter btAdapter = null;
    BluetoothDevice Pump = null;
    static String PumpAddress = "B8:27:EB:50:64:BA";
    BluetoothSocket PumpSocket = null;
    InputStream PumpInput = null;
    OutputStream PumpOutput = null;
    final UUID PumpUUID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    BluetoothDevice CGM = null;
    BluetoothSocket CGMSocket = null;
    InputStream CGMInput = null;
    OutputStream CGMOutput = null;
    final UUID CGMUUID = UUID.fromString("66666666-6666-6666-6666-666666666666");

    public BluetoothManager(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Pump = btAdapter.getRemoteDevice(PumpAddress);
    }

    public void ConnectToPump(){
        try {
            PumpSocket = Pump.createRfcommSocketToServiceRecord(PumpUUID);
        } catch (IOException e) {
        }

        btAdapter.cancelDiscovery();

        try {
            PumpSocket.connect();
        } catch (IOException e) {

        }

        try {
            PumpInput = PumpSocket.getInputStream();
            PumpOutput = PumpSocket.getOutputStream();
        } catch (IOException e) {

        }
    }

    public void PumpSend(byte data){

        try{
            PumpOutput.write(data);
        }catch(IOException e) {

        }
    }
}
