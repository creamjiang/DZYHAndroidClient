package com.dld.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializeUtil implements SerializeConst {
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private void clipAndAppendByteByTime(long paramLong, int paramInt) {
        for (int i = paramInt - 1; i >= 0; i--)
            appendByte((byte) (int) (0xFF & paramLong >> i * 8));
    }

    public void appendBoolean(boolean paramBoolean) {
        int i;
        if (!paramBoolean)
            i = 0;
        else
            i = 1;
        appendByte((byte) i);
    }

    public void appendByte(byte paramByte) {
        this.byteArrayOutputStream.write(paramByte);
    }

    public void appendChar(char paramChar) {
        for (int i = 0; i < 2; i++)
            appendByte((byte) (0xFF & paramChar >> i * 8));
    }

    public void appendInt(int paramInt) {
        clipAndAppendByteByTime(paramInt, 4);
    }

    public void appendLong(long paramLong) {
        clipAndAppendByteByTime(paramLong, 8);
    }

    public void appendShort(int paramInt) {
        clipAndAppendByteByTime(paramInt, 2);
    }

    public void appendString(String paramString) {
        int j = 0;
        int i = 0;
        if (paramString != null) {
            j = (short) paramString.length();
            appendShort(j);
            i = 0;
        }
        while (true) {
            if (i >= j)
                return;
            appendChar(paramString.charAt(i));
            i += 1;
        }
    }

    public byte[] toByteArray() {
        byte[] arrayOfByte = (byte[]) null;
        try {
            this.byteArrayOutputStream.close();
            arrayOfByte = this.byteArrayOutputStream.toByteArray();
            this.byteArrayOutputStream = null;
            return arrayOfByte;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }
}
