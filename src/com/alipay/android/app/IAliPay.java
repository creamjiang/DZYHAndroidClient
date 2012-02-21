package com.alipay.android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAliPay extends IInterface {
    public abstract String Pay(String paramString1, String paramString2,
            String paramString3) throws RemoteException;

    public abstract void registerCallback(
            IRemoteServiceCallback paramIRemoteServiceCallback)
            throws RemoteException;

    public abstract String test() throws RemoteException;

    public abstract void unregisterCallback(
            IRemoteServiceCallback paramIRemoteServiceCallback)
            throws RemoteException;

    public static abstract class Stub extends Binder implements IAliPay {
        private static final String DESCRIPTOR = "com.alipay.android.app.IAliPay";
        static final int TRANSACTION_Pay = 1;
        static final int TRANSACTION_registerCallback = 3;
        static final int TRANSACTION_test = 2;
        static final int TRANSACTION_unregisterCallback = 4;

        public Stub() {
            attachInterface(this, "com.alipay.android.app.IAliPay");
        }

        public static IAliPay asInterface(IBinder paramIBinder) {
            Object localObject;
            if (paramIBinder != null) {
                localObject = paramIBinder
                        .queryLocalInterface("com.alipay.android.app.IAliPay");
                if ((localObject == null)
                        || (!(localObject instanceof IAliPay)))
                    localObject = new Proxy(paramIBinder);
                else
                    localObject = (IAliPay) localObject;
            } else {
                localObject = null;
            }
            return (IAliPay) localObject;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int paramInt1, Parcel paramParcel1,
                Parcel paramParcel2, int paramInt2) throws RemoteException {
            boolean bool = true;
            String str;
            switch (paramInt1) {
            default:
                bool = super.onTransact(paramInt1, paramParcel1, paramParcel2,
                        paramInt2);
                break;
            case 1:
                paramParcel1.enforceInterface("com.alipay.android.app.IAliPay");
                str = Pay(paramParcel1.readString(), paramParcel1.readString(),
                        paramParcel1.readString());
                paramParcel2.writeNoException();
                paramParcel2.writeString(str);
                break;
            case 2:
                paramParcel1.enforceInterface("com.alipay.android.app.IAliPay");
                str = test();
                paramParcel2.writeNoException();
                paramParcel2.writeString(str);
                break;
            case 3:
                paramParcel1.enforceInterface("com.alipay.android.app.IAliPay");
                registerCallback(IRemoteServiceCallback.Stub
                        .asInterface(paramParcel1.readStrongBinder()));
                paramParcel2.writeNoException();
                break;
            case 4:
                paramParcel1.enforceInterface("com.alipay.android.app.IAliPay");
                unregisterCallback(IRemoteServiceCallback.Stub
                        .asInterface(paramParcel1.readStrongBinder()));
                paramParcel2.writeNoException();
                break;
            case 1598968902:
                paramParcel2.writeString("com.alipay.android.app.IAliPay");
            }
            return bool;
        }

        private static class Proxy implements IAliPay {
            private IBinder mRemote;

            Proxy(IBinder paramIBinder) {
                this.mRemote = paramIBinder;
            }

            public String Pay(String paramString1, String paramString2,
                    String paramString3) throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    localParcel2
                            .writeInterfaceToken("com.alipay.android.app.IAliPay");
                    localParcel2.writeString(paramString1);
                    localParcel2.writeString(paramString2);
                    localParcel2.writeString(paramString3);
                    this.mRemote.transact(1, localParcel2, localParcel1, 0);
                    localParcel1.readException();
                    String str = localParcel1.readString();
                    return str;
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
                // throw localObject;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.alipay.android.app.IAliPay";
            }

            public void registerCallback(
                    IRemoteServiceCallback paramIRemoteServiceCallback)
                    throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    localParcel2
                            .writeInterfaceToken("com.alipay.android.app.IAliPay");
                    if (paramIRemoteServiceCallback != null) {
                        // localIBinder =
                        // paramIRemoteServiceCallback.asBinder();
                        // localParcel2.writeStrongBinder(localIBinder);
                        this.mRemote.transact(3, localParcel2, localParcel1, 0);
                        localParcel1.readException();
                        return;
                    }
                    IBinder localIBinder = null;
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
            }

            public String test() throws RemoteException {
                Parcel localParcel1 = Parcel.obtain();
                Parcel localParcel2 = Parcel.obtain();
                try {
                    localParcel1
                            .writeInterfaceToken("com.alipay.android.app.IAliPay");
                    this.mRemote.transact(2, localParcel1, localParcel2, 0);
                    localParcel2.readException();
                    String str = localParcel2.readString();
                    return str;
                } finally {
                    localParcel2.recycle();
                    localParcel1.recycle();
                }
                // throw localObject;
            }

            public void unregisterCallback(
                    IRemoteServiceCallback paramIRemoteServiceCallback)
                    throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    localParcel2
                            .writeInterfaceToken("com.alipay.android.app.IAliPay");
                    if (paramIRemoteServiceCallback != null) {
                        // localIBinder =
                        // paramIRemoteServiceCallback.asBinder();
                        // localParcel2.writeStrongBinder(localIBinder);
                        this.mRemote.transact(4, localParcel2, localParcel1, 0);
                        localParcel1.readException();
                        return;
                    }
                    IBinder localIBinder = null;
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
            }
        }
    }
}
