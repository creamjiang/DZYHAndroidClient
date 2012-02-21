package com.alipay.android.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRemoteServiceCallback extends IInterface {
    public abstract void startActivity(String paramString1,
            String paramString2, int paramInt, Bundle paramBundle)
            throws RemoteException;

    public static abstract class Stub extends Binder implements
            IRemoteServiceCallback {
        private static final String DESCRIPTOR = "com.alipay.android.app.IRemoteServiceCallback";
        static final int TRANSACTION_startActivity = 1;

        public Stub() {
            attachInterface(this,
                    "com.alipay.android.app.IRemoteServiceCallback");
        }

        public static IRemoteServiceCallback asInterface(IBinder paramIBinder) {
            Object localObject;
            if (paramIBinder != null) {
                localObject = paramIBinder
                        .queryLocalInterface("com.alipay.android.app.IRemoteServiceCallback");
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
                        .enforceInterface("com.alipay.android.app.IRemoteServiceCallback");
                String str2 = paramParcel1.readString();
                String str1 = paramParcel1.readString();
                int i = paramParcel1.readInt();
                Bundle localBundle;
                if (paramParcel1.readInt() == 0)
                    localBundle = null;
                else
                    localBundle = (Bundle) Bundle.CREATOR
                            .createFromParcel(paramParcel1);
                startActivity(str2, str1, i, localBundle);
                paramParcel2.writeNoException();
                break;
            case 1598968902:
                paramParcel2
                        .writeString("com.alipay.android.app.IRemoteServiceCallback");
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
                return "com.alipay.android.app.IRemoteServiceCallback";
            }

            public void startActivity(String paramString1, String paramString2,
                    int paramInt, Bundle paramBundle) throws RemoteException {
                Parcel localParcel1 = Parcel.obtain();
                Parcel localParcel2 = Parcel.obtain();
                try {
                    localParcel1
                            .writeInterfaceToken("com.alipay.android.app.IRemoteServiceCallback");
                    localParcel1.writeString(paramString1);
                    localParcel1.writeString(paramString2);
                    localParcel1.writeInt(paramInt);
                    if (paramBundle != null) {
                        localParcel1.writeInt(1);
                        paramBundle.writeToParcel(localParcel1, 0);
                    }
                    // while (true)
                    {
                        this.mRemote.transact(1, localParcel1, localParcel2, 0);
                        localParcel2.readException();
                        // return;
                        localParcel1.writeInt(0);
                    }
                } finally {
                    localParcel2.recycle();
                    localParcel1.recycle();
                }
                // throw localObject;
            }
        }
    }
}
