package com.example.binderpooldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by didiwei on 2022/5/21
 * desc: 服务端，这个Service只需要返回BinderPool即可
 */
public class BinderPoolService extends Service {
    private int pid = android.os.Process.myPid();
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("ljh","这里是Service里的onBind方法，当前进程ID为：" + pid);
        return mBinderPool;
    }
}