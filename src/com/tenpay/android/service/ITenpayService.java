package com.tenpay.android.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public abstract interface ITenpayService extends IInterface {
    public abstract String gotoPay(Map paramMap,
            IRemoteServiceCallback paramIRemoteServiceCallback)
            throws RemoteException;

    public abstract String shareLogin(Map paramMap,
            IRemoteServiceCallback paramIRemoteServiceCallback)
            throws RemoteException;

    public static abstract class Stub extends Binder implements ITenpayService {
        private static final String DESCRIPTOR = "com.tenpay.android.service.ITenpayService";
        static final int TRANSACTION_gotoPay = 1;
        static final int TRANSACTION_shareLogin = 2;

        public Stub() {
            attachInterface(this, "com.tenpay.android.service.ITenpayService");
        }

        public static ITenpayService asInterface(IBinder paramIBinder) {
            Object localObject;
            if (paramIBinder != null) {
                localObject = paramIBinder
                        .queryLocalInterface("com.tenpay.android.service.ITenpayService");
                if ((localObject == null)
                        || (!(localObject instanceof ITenpayService)))
                    localObject = new Proxy(paramIBinder);
                else
                    localObject = (ITenpayService) localObject;
            } else {
                localObject = null;
            }
            return (ITenpayService) localObject;
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
                paramParcel1
                        .enforceInterface("com.tenpay.android.service.ITenpayService");
                str = gotoPay(paramParcel1.readHashMap(getClass()
                        .getClassLoader()),
                        IRemoteServiceCallback.Stub.asInterface(paramParcel1
                                .readStrongBinder()));
                paramParcel2.writeNoException();
                paramParcel2.writeString(str);
                break;
            case 2:
                paramParcel1
                        .enforceInterface("com.tenpay.android.service.ITenpayService");
                str = shareLogin(paramParcel1.readHashMap(getClass()
                        .getClassLoader()),
                        IRemoteServiceCallback.Stub.asInterface(paramParcel1
                                .readStrongBinder()));
                paramParcel2.writeNoException();
                paramParcel2.writeString(str);
                break;
            case 1598968902:
                paramParcel2
                        .writeString("com.tenpay.android.service.ITenpayService");
            }
            return bool;
        }

        private static class Proxy implements ITenpayService {
            private IBinder mRemote;

            Proxy(IBinder paramIBinder) {
                this.mRemote = paramIBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.tenpay.android.service.ITenpayService";
            }

            public String gotoPay(Map paramMap,
                    IRemoteServiceCallback paramIRemoteServiceCallback)
                    throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    Object localObject1 = null;
                    localParcel2
                            .writeInterfaceToken("com.tenpay.android.service.ITenpayService");
                    localParcel2.writeMap(paramMap);
                    if (paramIRemoteServiceCallback != null) {
                        localObject1 = paramIRemoteServiceCallback.asBinder();
                        localParcel2.writeStrongBinder((IBinder) localObject1);
                        this.mRemote.transact(1, localParcel2, localParcel1, 0);
                        localParcel1.readException();
                        // localObject1 = localParcel1.readString();
                        return (String) localObject1;
                    }
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
                return null;
            }

            public String shareLogin(Map paramMap,
                    IRemoteServiceCallback paramIRemoteServiceCallback)
                    throws RemoteException {
                Parcel localParcel2 = Parcel.obtain();
                Parcel localParcel1 = Parcel.obtain();
                try {
                    localParcel2
                            .writeInterfaceToken("com.tenpay.android.service.ITenpayService");
                    localParcel2.writeMap(paramMap);
                    if (paramIRemoteServiceCallback != null) {
                        Object localObject1 = null;
                        localObject1 = paramIRemoteServiceCallback.asBinder();
                        localParcel2.writeStrongBinder((IBinder) localObject1);
                        this.mRemote.transact(2, localParcel2, localParcel1, 0);
                        localParcel1.readException();
                        localObject1 = localParcel1.readString();
                        return (String) localObject1;
                    }
                } finally {
                    localParcel1.recycle();
                    localParcel2.recycle();
                }
                return null;
            }
        }
    }
}
