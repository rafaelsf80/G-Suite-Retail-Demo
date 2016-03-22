package com.rafaelsf80.d4w.retail.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class D4WSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static D4WSyncAdapter sD4WSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("d4wSyncService", "onCreate - d4wSyncService");
        synchronized (sSyncAdapterLock) {
            if (sD4WSyncAdapter == null) {
                sD4WSyncAdapter = new D4WSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sD4WSyncAdapter.getSyncAdapterBinder();
    }
}