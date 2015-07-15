package com.gionji.switchuno.serialport;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gionji.switchuno.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Luigi on 14/07/2015.
 */
public class IndoleSerialProtocol {

    final static String TAG = "IndoleSerialProtocol";

    Context mContext;

    SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;

    /*
        Uart1 (/dev/ttyO4), 9600, N, 8, 1
     */

    private byte STX                    = 0x02;

    private byte CMD_FIRMWARE           = 0x00;
    private byte CMD_FRESET             = 0x01;
    private byte CMD_SWITCHLED          = 0x02;
    private byte CMD_GETTEMP            = 0x03;
    private byte CMD_GETPROXANDLIGHT    = 0x04;
    private byte CMD_ENABLELED          = 0x05;
    private byte CMD_ENABLEPRESNOT      = 0x06;

    private byte LEN;
    private byte DATI0, DATI1, DATI2;
    private byte CRC;

    public IndoleSerialProtocol (Context context, SerialPort mSerialPort) {
        this.mContext = context;
        this.mSerialPort = mSerialPort;

        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
    }

    public String getFirmwareVersion() {
        String version = "";

        byte[] data = {STX,
                CMD_FIRMWARE,
                (byte)0x00};

        byte crc = calculateCRC(new byte[]{STX,CMD_FIRMWARE,(byte)0x00});

        new RequestTask().execute(data[0], data[1], data[2], crc);

        return version;
    }

    public String resetMicrocontroller(){
        return "";
    }

    public String switchEmergencyLED(int status) {
        return "";
    }

    public String getTemperature() {
        return "";
    }

    public String getProximityAndLight() {
        return "";
    }

    public String getProximity() {
        return "";
    }

    public String getLight() {
        return "";
    }

    public String enableEmergencyLED(int status, short thresold) {
        return "";
    }

    public String enablePresenceNotification(int status, short thresold) {
        return "";
    }

    private byte calculateCRC (byte[] byteArray) {
        byte crc = 1;
        for (int i=0; i < byteArray.length; i++){
            crc += byteArray[i];
        }
        return crc;
    }

    private class RequestTask extends AsyncTask<Byte, Void, String> {

        @Override
        protected String doInBackground(Byte... params) {
            int size;
            String received = "";
            try {
                for (int i=0; i<params.length; i++) {
                    mOutputStream.write(params[i]);
                }

                byte[] buffer = new byte[16];
                if (mInputStream == null) return "Error";
                size = mInputStream.read(buffer);
                if (size > 0) {
                    received = new String(buffer, 0, size);

                    Log.i(TAG, "String received: " + received );
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error";
            }

            return received; //TODO: return message parsed
        }

        @Override
        protected void onPostExecute(String result) {
            //TODO: result messaggio ritornato
            Toast.makeText(mContext, result ,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
