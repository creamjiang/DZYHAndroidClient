package com.tenpay.android.service;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRemoteServiceCallback extends IInterface {
    public abstract void startActivity(String paramString1,
            String paramString2, Bundle paramBundle) throws RemoteException;

    public static abstract class Stub extends Binder implements
            IRemoteServiceCallback {
        private static final String DESCRIPTOR = "com.tenpay.android.service.IRemoteServiceCallback";
        static final int TRANSACTION_startActivity = 1;

        public Stub() {
            attachInterface(this,
                    "com.tenpay.android.service.IRemoteServiceCallback");
        }

        public static IRemoteServiceCallback asInterface(IBinder paramIBinder) {
            Object localObject;
            if (paramIBinder != null) {
                localObject = paramIBinder
                        .queryLocalInterface("com.tenpay.android.service.IRemoteServiceCallback");
                if ((localObject == null)
                        || (!(localObject instanceof IRemoteServiceCallback)))
                    localObject = new Proxy(paramIBinder);
                else
                    localObject = (IRemoteServiceCallback) localObject;
            } else {
                localObject = null;
            }
            return (IRemoteServiceCallback) localObject;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int paramInt1, Parcel paramParcel1,
                Parcel paramParcel2, int paramInt2) throws RemoteException {
            boolean bool = true;
            switch (paramInt1) {
            default:
                bool = super.onTransact(paramInt1, paramParcel1, paramParcel2,
                        paramInt2);
                break;
            case 1:
                paramParcel1
                        .enforceInterface("com.tenpay.android.service.IRemoteServiceCallback");
                String str2 = paramParcel1.readString();
                String str1 = paramParcel1.readString();
                Bundle localBundle;
                if (paramParcel1.readInt() == 0)
                    localBundle = null;
                else
                    localBundle = (Bundle) Bundle.CREATOR
                            .createFromParcel(paramParcel1);
                startActivity(str2, str1, localBundle);
                paramParcel2.writeNoException();
                break;
            case 1598968902:
                paramParcel2
                        .writeString("com.tenpay.android.service.IRemoteServiceCallback");
            }
            return bool;
        }

        private static class Proxy implements IRemoteServiceCallback {
            private IBinder mRemote;

            Proxy(IBinder paramIBinder) {
                this.mRemote = paramIBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.tenpay.android.service.IRemoteServiceCallback";
            }

            public void startActivity(String paramString1, String paramString2,
                    Bundle paramBundle) throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    localParcel2
                            .writeInterfaceToken("com.tenpay.android.service.IRemoteServiceCallback");
                    localParcel2.writeString(paramString1);
                    localParcel2.writeString(paramString2);
                    if (paramBundle != null) {
                        localParcel2.writeInt(1);
                        paramBundle.writeToParcel(localParcel2, 0);
                    }
                    while (true) {
                        this.mRemote.transact(1, localParcel2, localParcel1, 0);
                        localParcel1.readException();
                        return;
                        // localParcel2.writeInt(0);
                    }
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
                // throw localObject;
            }
        }
    }
}
