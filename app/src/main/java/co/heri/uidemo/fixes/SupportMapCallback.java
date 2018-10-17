package co.heri.uidemo.fixes;

import android.os.RemoteException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzaq;

import co.heri.uidemo.fixes.SupportMapFragment;

final class SupportMapCallback extends zzaq {
    private OnMapReadyCallback mMapCallback;

    SupportMapCallback(SupportMapFragment.zza var1, OnMapReadyCallback var2) {
        mMapCallback = var2;
    }

    public final void zza(IGoogleMapDelegate var1) throws RemoteException {
        mMapCallback.onMapReady(new GoogleMap(var1));
    }
}