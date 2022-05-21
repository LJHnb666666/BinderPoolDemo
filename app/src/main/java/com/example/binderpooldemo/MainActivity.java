package com.example.binderpooldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private ISpeak iSpeak;
    private ICalculate iCalculate;
    private int pid = android.os.Process.myPid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                startWork();
            }
        }).start();
    }

    private void startWork(){
        Log.v("ljh","这里是客户端，当前进程为：" + pid);

        BinderPool instance = BinderPool.getInstance(this);
        IBinder iBinderSpeak = instance.queryBinder(BinderPool.BINDER_SPEAK);
        iSpeak = ISpeak.Stub.asInterface(iBinderSpeak);

        try {
            iSpeak.speak();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder iBinderCalculate = instance.queryBinder(BinderPool.BINDER_CALCULATE);
        iCalculate = ICalculate.Stub.asInterface(iBinderCalculate);
        try {
            iCalculate.add(1,2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}