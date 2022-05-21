package com.example.binderpooldemo;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by didiwei on 2022/5/21
 * desc: ISpeak.aidl的实现类，这个类之前是放在Service中的，但是因为引入了Binder连接池，所以要把它和Service分开
 * 但其实这个实现类还是运行在服务端的进程中
 */
public class SpeakImpl extends ISpeak.Stub{
    @Override
    public void speak() throws RemoteException {
        int pid = android.os.Process.myPid();

        Log.v("ljh","这里是SpeakImpl里的speak方法，当前进程ID为：" + pid);
    }
}
