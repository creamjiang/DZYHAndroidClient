package com.dld.coupon.util;

import com.dld.android.util.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

public class SimpleMultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();
    private String boundary = null;
    boolean isSetFirst = false;
    boolean isSetLast = false;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    public SimpleMultipartEntity() {
        StringBuffer localStringBuffer = new StringBuffer();
        Random localRandom = new Random();
        for (int i = 0; i < 30; i++)
            localStringBuffer.append(MULTIPART_CHARS[localRandom
                    .nextInt(MULTIPART_CHARS.length)]);
        this.boundary = localStringBuffer.toString();
    }

    public void addPart(String paramString1, File paramFile, String paramString2) {
        try {
            addPart(paramString1, paramFile.getName(), new FileInputStream(
                    paramFile), paramString2);
            return;
        } catch (FileNotFoundException localFileNotFoundException) {
            while (true)
                LogUtils.e("test", localFileNotFoundException.getMessage(),
                        localFileNotFoundException);
        }
    }

    public void addPart(String paramString1, String paramString2) {
        addPart(paramString1, paramString2, false);
    }

    // ERROR //
    public void addPart(String paramString1, String paramString2,
            InputStream paramInputStream, String paramString3) {
        // Byte code:
        // 0: aload_0
        // 1: invokevirtual 96
        // com/doujiao/coupon/util/SimpleMultipartEntity:writeFirstBoundaryIfNeeds
        // ()V
        // 4: new 98 java/lang/StringBuilder
        // 7: dup
        // 8: ldc 100
        // 10: invokespecial 103 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 13: aload 4
        // 15: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 18: ldc 108
        // 20: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 23: invokevirtual 109 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 26: astore 5
        // 28: aload_0
        // 29: getfield 37 com/doujiao/coupon/util/SimpleMultipartEntity:out
        // Ljava/io/ByteArrayOutputStream;
        // 32: new 98 java/lang/StringBuilder
        // 35: dup
        // 36: ldc 111
        // 38: invokespecial 103 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 41: aload_1
        // 42: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 45: ldc 113
        // 47: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 50: aload_2
        // 51: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 54: ldc 115
        // 56: invokevirtual 106 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 59: invokevirtual 109 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 62: invokevirtual 119 java/lang/String:getBytes ()[B
        // 65: invokevirtual 123 java/io/ByteArrayOutputStream:write ([B)V
        // 68: aload_0
        // 69: getfield 37 com/doujiao/coupon/util/SimpleMultipartEntity:out
        // Ljava/io/ByteArrayOutputStream;
        // 72: aload 5
        // 74: invokevirtual 119 java/lang/String:getBytes ()[B
        // 77: invokevirtual 123 java/io/ByteArrayOutputStream:write ([B)V
        // 80: aload_0
        // 81: getfield 37 com/doujiao/coupon/util/SimpleMultipartEntity:out
        // Ljava/io/ByteArrayOutputStream;
        // 84: ldc 125
        // 86: invokevirtual 119 java/lang/String:getBytes ()[B
        // 89: invokevirtual 123 java/io/ByteArrayOutputStream:write ([B)V
        // 92: sipush 4096
        // 95: newarray byte
        // 97: astore 6
        // 99: aload_3
        // 100: aload 6
        // 102: invokevirtual 131 java/io/InputStream:read ([B)I
        // 105: istore 5
        // 107: iload 5
        // 109: bipush 255
        // 111: if_icmpne +15 -> 126
        // 114: aload_0
        // 115: getfield 37 com/doujiao/coupon/util/SimpleMultipartEntity:out
        // Ljava/io/ByteArrayOutputStream;
        // 118: invokevirtual 134 java/io/ByteArrayOutputStream:flush ()V
        // 121: aload_3
        // 122: invokevirtual 137 java/io/InputStream:close ()V
        // 125: return
        // 126: aload_0
        // 127: getfield 37 com/doujiao/coupon/util/SimpleMultipartEntity:out
        // Ljava/io/ByteArrayOutputStream;
        // 130: aload 6
        // 132: iconst_0
        // 133: iload 5
        // 135: invokevirtual 140 java/io/ByteArrayOutputStream:write ([BII)V
        // 138: goto -39 -> 99
        // 141: astore 5
        // 143: ldc 78
        // 145: aload 5
        // 147: invokevirtual 141 java/io/IOException:getMessage
        // ()Ljava/lang/String;
        // 150: aload 5
        // 152: invokestatic 87 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 155: aload_3
        // 156: invokevirtual 137 java/io/InputStream:close ()V
        // 159: goto -34 -> 125
        // 162: astore 5
        // 164: ldc 78
        // 166: aload 5
        // 168: invokevirtual 141 java/io/IOException:getMessage
        // ()Ljava/lang/String;
        // 171: aload 5
        // 173: invokestatic 87 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 176: goto -51 -> 125
        // 179: astore 6
        // 181: aload_3
        // 182: invokevirtual 137 java/io/InputStream:close ()V
        // 185: aload 6
        // 187: athrow
        // 188: astore 5
        // 190: ldc 78
        // 192: aload 5
        // 194: invokevirtual 141 java/io/IOException:getMessage
        // ()Ljava/lang/String;
        // 197: aload 5
        // 199: invokestatic 87 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 202: goto -17 -> 185
        // 205: astore 5
        // 207: ldc 78
        // 209: aload 5
        // 211: invokevirtual 141 java/io/IOException:getMessage
        // ()Ljava/lang/String;
        // 214: aload 5
        // 216: invokestatic 87 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 219: goto -94 -> 125
        //
        // Exception table:
        // from to target type
        // 4 121 141 java/io/IOException
        // 126 138 141 java/io/IOException
        // 155 159 162 java/io/IOException
        // 4 121 179 finally
        // 126 138 179 finally
        // 143 155 179 finally
        // 181 185 188 java/io/IOException
        // 121 125 205 java/io/IOException
    }

    public void addPart(String paramString1, String paramString2,
            boolean paramBoolean) {
        writeFirstBoundaryIfNeeds();
        try {
            this.out.write(("Content-Disposition: form-data; name=\""
                    + paramString1 + "\"\r\n").getBytes());
            this.out.write("Content-Type: text/plain; charset=UTF-8\r\n"
                    .getBytes());
            this.out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
            this.out.write(paramString2.getBytes());
            ByteArrayOutputStream localByteArrayOutputStream = this.out;
            StringBuilder localStringBuilder = new StringBuilder("\r\n--")
                    .append(this.boundary);
            if (paramBoolean)
                ;
            for (String str = "--";; str = "\r\n") {
                localByteArrayOutputStream.write(str.getBytes());
                break;
            }
        } catch (IOException localIOException) {
            LogUtils.e("test", localIOException.getMessage(), localIOException);
        }
    }

    public void addPart(String paramString1, String paramString2,
            byte[] paramArrayOfByte, String paramString3) {
        writeFirstBoundaryIfNeeds();
        try {
            String str = "Content-Type: " + paramString3 + "\r\n";
            this.out.write(("Content-Disposition: form-data; name=\""
                    + paramString1 + "\"; filename=\"" + paramString2 + "\"\r\n")
                    .getBytes());
            this.out.write(str.getBytes());
            this.out.write("Content-Transfer-Encoding: binary\r\n\r\n"
                    .getBytes());
            this.out.write(paramArrayOfByte, 0, paramArrayOfByte.length);
            this.out.flush();
            return;
        } catch (IOException localIOException) {
            while (true)
                LogUtils.e("test", localIOException.getMessage(),
                        localIOException);
        }
    }

    public void consumeContent() throws IOException,
            UnsupportedOperationException {
        if (!isStreaming())
            return;
        throw new UnsupportedOperationException(
                "Streaming entity does not implement #consumeContent()");
    }

    public InputStream getContent() throws IOException,
            UnsupportedOperationException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }

    public Header getContentEncoding() {
        return null;
    }

    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return this.out.toByteArray().length;
    }

    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary="
                + this.boundary);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!this.isSetFirst)
            ;
        try {
            this.out.write(("--" + this.boundary + "\r\n").getBytes());
            this.isSetFirst = true;
            return;
        } catch (IOException localIOException) {
            while (true)
                LogUtils.e("test", localIOException);
        }
    }

    public void writeLastBoundaryIfNeeds() {
        if (this.isSetLast)
            ;
        // while (true)
        {
            // return;
            try {
                this.out.write(("\r\n--" + this.boundary + "--\r\n").getBytes());
                this.isSetLast = true;
            } catch (IOException localIOException) {
                // while (true)
                // LogUtils.e("test", localIOException.getMessage(),
                // localIOException);
            }
        }
    }

    public void writeTo(OutputStream paramOutputStream) throws IOException {
        paramOutputStream.write(this.out.toByteArray());
    }
}
