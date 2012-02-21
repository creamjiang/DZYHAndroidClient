package com.dld.protocol;

public class DataConvert {
    public static int byte2int(byte[] paramArrayOfByte, int paramInt) {
        int k;
        if (paramArrayOfByte[paramInt] < 0)
            k = 256 + paramArrayOfByte[paramInt];
        else
            k = paramArrayOfByte[paramInt];
        int m;
        if (paramArrayOfByte[(paramInt + 1)] < 0)
            m = 256 + paramArrayOfByte[(paramInt + 1)];
        else
            m = paramArrayOfByte[(paramInt + 1)];
        int j;
        if (paramArrayOfByte[(paramInt + 2)] < 0)
            j = 256 + paramArrayOfByte[(paramInt + 2)];
        else
            j = paramArrayOfByte[(paramInt + 2)];
        int i;
        if (paramArrayOfByte[(paramInt + 3)] < 0)
            i = 256 + paramArrayOfByte[(paramInt + 3)];
        else
            i = paramArrayOfByte[(paramInt + 3)];
        return (k << 24) + (m << 16) + (j << 8) + (i << 0);
    }

    public static int byte2short(byte[] paramArrayOfByte, int paramInt) {
        int i;
        if (paramArrayOfByte[paramInt] < 0)
            i = 256 + paramArrayOfByte[paramInt];
        else
            i = paramArrayOfByte[paramInt];
        int j;
        if (paramArrayOfByte[(paramInt + 1)] < 0)
            j = 256 + paramArrayOfByte[(paramInt + 1)];
        else
            j = paramArrayOfByte[(paramInt + 1)];
        return (i << 8) + (j << 0);
    }

    public static void writeInt(byte[] paramArrayOfByte, int paramInt1,
            int paramInt2) {
        paramArrayOfByte[(paramInt1 + 3)] = (byte) (paramInt2 & 0xFF);
        paramArrayOfByte[(paramInt1 + 2)] = (byte) (0xFF & paramInt2 >> 8);
        paramArrayOfByte[(paramInt1 + 1)] = (byte) (0xFF & paramInt2 >> 16);
        paramArrayOfByte[(paramInt1 + 0)] = (byte) (0xFF & paramInt2 >> 24);
    }

    public static void writeShort(byte[] paramArrayOfByte, int paramInt,
            short paramShort) {
        paramArrayOfByte[(paramInt + 1)] = (byte) (paramShort & 0xFF);
        paramArrayOfByte[(paramInt + 0)] = (byte) (0xFF & paramShort >> 8);
    }
}
