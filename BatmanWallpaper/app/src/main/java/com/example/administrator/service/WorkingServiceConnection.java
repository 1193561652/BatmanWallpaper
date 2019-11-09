package com.example.administrator.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.testlua.IReqWorkingAidlInterface;
import com.example.testlua.IRspWorkingAidlInterface;

public class WorkingServiceConnection implements ServiceConnection {
    class RspWorkingCallback extends IRspWorkingAidlInterface.Stub {
        protected WorkingServiceInterface mWorkingInterface = null;

        public RspWorkingCallback(WorkingServiceInterface workingInterface) {
            mWorkingInterface = workingInterface;
        }
        public void onRspWorking(String needWorking) {
            Log.v("RspWorkingCallback", needWorking);
            if(mWorkingInterface != null) {
                mWorkingInterface.setNeedWorking(needWorking);
            }
        }
    }

    IRspWorkingAidlInterface rspCallback = null;
    public IReqWorkingAidlInterface reqInterface = null;
    protected WorkingServiceInterface mWorkingInterface = null;
    public WorkingServiceConnection(WorkingServiceInterface workingInterface) {
        rspCallback = new RspWorkingCallback(workingInterface);
        mWorkingInterface = workingInterface;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // 这里和本地服务不同，本地服务是强制类型转换，远程服务直接使用代理完成
        //iMusician = IMusician.Stub.asInterface(service);

        reqInterface = IReqWorkingAidlInterface.Stub.asInterface(service);
        try {
            reqInterface.regsetRspCallback(rspCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(reqInterface != null) {
            try {
                reqInterface.unregsetRspCallback(rspCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(mWorkingInterface != null) {
            mWorkingInterface.onDisconnect();
        }
    }

    public String needWorking() {
        if(reqInterface != null) {
            try {
                return reqInterface.request();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "error_call";
    }

    public String mixInfo() {
        if(reqInterface != null) {
            try {
                return reqInterface.mixInfo();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "error";
    }

    public void tiggerReq() {
        if(reqInterface != null) {
            try {
                reqInterface.tiggerReq();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public String getError() {
        if(reqInterface != null) {
            try {
                return reqInterface.getError();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "error";
    }

    public void setWorking(String needWorking) {
        if(reqInterface != null) {
            try {
                reqInterface.setWorking(needWorking);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAlive() {
        if(reqInterface != null) {
            try {
                return reqInterface.isAlive();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}