package com.alexxx.a4_intentsreceiversservicesandnotifications.taskTwo;

import java.util.Observable;

public class BroadCastHandler extends Observable {


    public static BroadCastHandler handler;


    public static BroadCastHandler getInstance()
    {
        if(handler == null)
            handler = new BroadCastHandler();

        return handler;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
      return null;
    }

    public void updateValue(Object data)
    {
        synchronized (this)
        {
            setChanged();
            notifyObservers(data);
        }
    }


}
