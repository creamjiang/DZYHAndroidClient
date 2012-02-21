package com.dld.android.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

public class MyCellIDRequestEntity implements HttpEntity {
    protected int myCellID;
    protected int myLAC;

    public MyCellIDRequestEntity(int paramInt1, int paramInt2) {
        this.myCellID = paramInt1;
        this.myLAC = paramInt2;
    }

    public void consumeContent() throws IOException {
    }

    public InputStream getContent() throws IOException, IllegalStateException {
        return null;
    }

    public Header getContentEncoding() {
        return null;
    }

    public long getContentLength() {
        return -1L;
    }

    public Header getContentType() {
        return new BasicHeader("Content-Type", "application/binary");
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return true;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream paramOutputStream) throws IOException {
        DataOutputStream localDataOutputStream = new DataOutputStream(
                paramOutputStream);
        localDataOutputStream.writeShort(21);
        localDataOutputStream.writeLong(0L);
        localDataOutputStream.writeUTF("fr");
        localDataOutputStream.writeUTF("Sony_Ericsson-K750");
        localDataOutputStream.writeUTF("1.3.1");
        localDataOutputStream.writeUTF("Web");
        localDataOutputStream.writeByte(27);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.writeInt(3);
        localDataOutputStream.writeUTF("");
        localDataOutputStream.writeInt(this.myCellID);
        localDataOutputStream.writeInt(this.myLAC);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.writeInt(0);
        localDataOutputStream.flush();
    }
}

/*
 * Location: G:\DEV\MobileDev\反编译\classes_dex2jar\ Qualified Name:
 * com.dld.android.util.MyCellIDRequestEntity JD-Core Version: 0.6.0
 */