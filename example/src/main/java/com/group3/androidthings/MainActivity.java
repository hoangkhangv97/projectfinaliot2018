package com.group3.androidthings;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.group3.androidthings.MVVM.VM.NPNHomeViewModel;
import com.group3.androidthings.MVVM.View.NPNHomeView;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity implements NPNHomeView{
    private Rc522 mRc522;
    RfidTask mRfidTask;
    private TextView mTagDetectedView;
    private TextView mTagUidView;
    private TextView mTagResultsView;
    private Button button;

    private Gpio mLedGpioR;
    private Gpio mLedGpioG;
    private Gpio mLedGpioB;
    private boolean mLedState = true;
    private boolean insert = false;

    private SpiDevice spiDevice;
    private Gpio gpioReset;
    private Handler mHandler = new Handler();
    private int count = 0;

    private static final String SPI_PORT = "SPI0.0";
    private static final String PIN_RESET = "BCM25";

    String resultsText = "";

    private NPNHomeViewModel mHomeViewModel;

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            count = count +1 ;
            redColor();
            if (count <20){
                mHandler.postDelayed(mBlinkRunnable,150);
            }else{
                count = 0;
                greenColor();
                mHandler.removeCallbacks(this);
            }

        }
    };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mHomeViewModel = new NPNHomeViewModel();
        mHomeViewModel.attach(this, this);

        mTagDetectedView = (TextView)findViewById(R.id.tag_read);
        mTagUidView = (TextView)findViewById(R.id.tag_uid);
        mTagResultsView = (TextView) findViewById(R.id.tag_results);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRfidTask = new RfidTask(mRc522);
                mRfidTask.execute();
                ((Button)v).setText(R.string.reading);
            }
        });
        PeripheralManager pioService = PeripheralManager.getInstance();
        try {
            String R = "BCM4";
            String G = "BCM5";
            String B = "BCM6";
            mLedGpioR = PeripheralManager.getInstance().openGpio(R);
            mLedGpioR.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpioG = PeripheralManager.getInstance().openGpio(G);
            mLedGpioG.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpioB = PeripheralManager.getInstance().openGpio(B);
            mLedGpioB.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            spiDevice = pioService.openSpiDevice(SPI_PORT);
            gpioReset = pioService.openGpio(PIN_RESET);
            mRc522 = new Rc522(spiDevice, gpioReset);
            mRc522.setDebugging(true);
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        greenColor();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(spiDevice != null){
                spiDevice.close();
            }
            if(gpioReset != null){
                gpioReset.close();
            }
            mLedGpioR.close();
            mLedGpioG.close();
            mLedGpioB.close();
            mHandler.removeCallbacks(mBlinkRunnable);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mLedGpioR = null;
            mLedGpioG = null;
            mLedGpioB = null;
        }
    }

    @Override
    protected void onResume() {
            super.onResume();
            mHandler.removeCallbacks(mBlinkRunnable);
            mRfidTask = new RfidTask(mRc522);
            mRfidTask.execute();
    }

    private void cyanColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Blue error");
            return;
        }
        try {
            mLedGpioB.setValue(false);
            mLedGpioG.setValue(false);
            mLedGpioR.setValue(true);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void purpleColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Blue error");
            return;
        }
        try {
            mLedGpioB.setValue(false);
            mLedGpioG.setValue(true);
            mLedGpioR.setValue(false);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void yellowColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Blue error");
            return;
        }
        try {
            mLedGpioB.setValue(true);
            mLedGpioG.setValue(false);
            mLedGpioR.setValue(false);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void redColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Red error");
            return;
        }
        try {
            mLedState = !mLedState;
            mLedGpioR.setValue(mLedState);
            mLedGpioG.setValue(true);
            mLedGpioB.setValue(true);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void blueColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Blue error");
            return;
        }
        try {
            mLedGpioB.setValue(false);
            mLedGpioG.setValue(true);
            mLedGpioR.setValue(true);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void greenColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Green error");
            return;
        }
        try {
            mLedGpioG.setValue(false);
            mLedGpioR.setValue(true);
            mLedGpioB.setValue(true);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    private void whiteColor(){
        if (mLedGpioR == null) {
            Log.d("LED","Green error");
            return;
        }
        try {
            mLedGpioG.setValue(false);
            mLedGpioR.setValue(false);
            mLedGpioB.setValue(false);
        } catch (IOException e) {
            Log.e("Error", "Error on PeripheralIO API", e);
        }
    }

    @Override
    public void onSuccessUpdateServer(JSONObject response) {
        try {
            if (!insert){
                String access = response.getString("access");
                String accessIn = response.getString("accessIn");
                String admin = response.getString("admin");

                if (admin.equals("true")) {
                    insert = true;
                }
                else {
                    insert = false;
                }

                if (access.equals("false")){
                    mHandler.post(mBlinkRunnable);
                }else if (accessIn.equals("true")){
                    try {
                        if (insert){
                            purpleColor();
                        }else{
                            blueColor();
                            sleep(2000);
                            greenColor();
                        }
                    }catch (Exception e){
                        System.out.print(e.getMessage());
                    }
                }else {
                    try {
                        if (insert){
                            purpleColor();
                        }else{
                            whiteColor();
                            sleep(2000);
                            greenColor();
                        }
                    }catch (Exception e){
                        System.out.print(e.getMessage());
                    }
                }

            }else{
                String insertRes = response.getString("insert");
                System.out.println(insertRes);
                insert =false;
                try {
                    if(insertRes.equals("true")){
                        cyanColor();
                    }else {
                        mHandler.post(mBlinkRunnable);
                    }
                    sleep(2000);
                    greenColor();
                }catch (Exception e){
                    System.out.print(e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorUpdateServer(String message) {
        System.out.println(message);
    }

    private class RfidTask extends AsyncTask<Object, Object, Boolean> {
        private static final String TAG = "RfidTask";
        private Rc522 rc522;

        RfidTask(Rc522 rc522){
            this.rc522 = rc522;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Object... params) {
            rc522.stopCrypto();
            while(true){
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
                //Check if a RFID tag has been found
                if(!rc522.request()){
                    continue;
                }
                //Check for collision errors
                if(!rc522.antiCollisionDetect()){
                    continue;
                }
                byte[] uuid = rc522.getUid();
                yellowColor();
                return rc522.selectTag(uuid);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(!success){
                mTagResultsView.setText(R.string.unknown_error);
                return;
            }
            // Try to avoid doing any non RC522 operations until you're done communicating with it.
            byte address = Rc522.getBlockAddress(2,1);
            // Mifare's card default key A and key B, the key may have been changed previously
            byte[] key = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
            // Each sector holds 16 bytes
            // In this case, Rc522.AUTH_A or Rc522.AUTH_B can be used
            try {
                //We need to authenticate the card, each sector can have a different key
                boolean result = rc522.authenticateCard(Rc522.AUTH_A, address, key);
                if (!result) {
                    mTagResultsView.setText(R.string.authetication_error);
                    return;
                }

                Log.d("ReadData",rc522.getUidString());
                Date date= new Date();

                long time = date.getTime();
                System.out.println("Time in Milliseconds: " + time);

                if (insert){
                    mHomeViewModel.updateToServer("https://bk-id.herokuapp.com/users/create/"+rc522.getUidString());
                }
                else {
                    mHomeViewModel.updateToServer("https://bk-id.herokuapp.com/users/auth/uid/"+rc522.getUidString()+"/time/"+String.valueOf(time)+"/room/202C5");
                }
                rc522.stopCrypto();
            }catch (Exception e){
                System.out.print(e.getMessage());
            }
            try{
                sleep(2000);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            onResume();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
