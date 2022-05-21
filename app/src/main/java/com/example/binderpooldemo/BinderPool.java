package com.example.binderpooldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by didiwei on 2022/5/21
 * desc: 里面有IBinderPool的实现类
 */
public class BinderPool {
    public static final int BINDER_SPEAK = 0;
    public static final int BINDER_CALCULATE = 1;

    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPool mInstance;//单例
    private CountDownLatch mCountDownLatch;//实现线程同步的

    //连接Service的回调
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);

            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //实现Service的重连
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient,0);
            mBinderPool = null;

            connectBinderPoolService();
        }
    };

    private BinderPool(Context context){
        this.mContext = context;

        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if(mInstance == null){
            synchronized (BinderPool.class){
                if(mInstance == null){
                    mInstance = new BinderPool(context);
                }
            }
        }

        return mInstance;
    }

    //连接服务端
    private synchronized void connectBinderPoolService(){
        mCountDownLatch = new CountDownLatch(1);

        //连接Service
        Intent service = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(service,mServiceConnection,Context.BIND_AUTO_CREATE);

        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //对外暴露的 queryBinder 方法
    public IBinder queryBinder(int binderCode){
        IBinder iBinder = null;

        if(mBinderPool != null){
            try {
                iBinder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return iBinder;
    }

    //IBinderPool的实现类
    public static class BinderPoolImpl extends IBinderPool.Stub{

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder iBinder = null;
            switch (binderCode){
                case BINDER_SPEAK:
                    iBinder = new SpeakImpl().asBinder();
                    break;
                case BINDER_CALCULATE:
                    iBinder = new CalculateImpl().asBinder();
                    break;
                default:
                    break;
            }
            return iBinder;
        }
    }





}
