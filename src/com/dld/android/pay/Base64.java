package com.dld.android.pay;

public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int EIGHTBIT = 8;
    private static final int FOURBYTE = 4;
    private static final int LOOKUPLENGTH = 64;
    private static final char PAD = '=';
    private static final int SIGN = -128;
    private static final int SIXTEENBIT = 16;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final byte[] base64Alphabet = new byte[''];
    private static final boolean fDebug = true;
    private static final char[] lookUpBase64Alphabet = new char[64];

    static {
        int i, j;
        for (i = 0; i < 128; i++)
            base64Alphabet[i] = -1;
        for (i = 90; i >= 65; i--)
            base64Alphabet[i] = (byte) (i - 65);
        for (i = 122; i >= 97; i--)
            base64Alphabet[i] = (byte) (26 + (i - 97));
        for (i = 57; i >= 48; i--)
            base64Alphabet[i] = (byte) (52 + (i - 48));
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (i = 0; i <= 25; i++)
            lookUpBase64Alphabet[i] = (char) (i + 65);
        i = 26;
        for (j = 0; i <= 51; j++) {
            lookUpBase64Alphabet[i] = (char) (j + 97);
            i++;
        }
        i = 52;
        for (j = 0; i <= 61; j++) {
            lookUpBase64Alphabet[i] = (char) (j + 48);
            i++;
        }
        lookUpBase64Alphabet[62] = '+';
        lookUpBase64Alphabet[63] = '/';
    }

    public static byte[] decode(String paramString) {
        // label251: Object localObject = null;
        Object localObject = null;
        if (paramString != null) {
            char[] arrayOfChar = paramString.toCharArray();
            int i = removeWhiteSpace(arrayOfChar);
            if (i % 4 == 0) {
                int i3 = i / 4;
                if (i3 != 0) {
                    // ((byte[])null);
                    int m = 0;
                    byte[] arrayOfByte = new byte[i3 * 3];
                    int i1 = 0;
                    int k = 0;
                    int i6;
                    while (true) {
                        int i5 = i3 - 1;
                        if (m >= i5)
                            break;
                        i5 = i1 + 1;
                        char c2 = arrayOfChar[i1];
                        if (isData(c2)) {
                            i1 = i5 + 1;
                            char c3 = arrayOfChar[i5];
                            if (isData(c3)) {
                                int i11 = i1 + 1;
                                i5 = arrayOfChar[i1];
                                if (!isData((char) i5))
                                    ;
                                // break label251;
                                i1 = i11 + 1;
                                i11 = arrayOfChar[i11];
                                if (isData((char) i11)) {
                                    byte i9 = base64Alphabet[c2];
                                    int i10 = base64Alphabet[c3];
                                    i6 = base64Alphabet[i5];
                                    int i12 = base64Alphabet[i11];
                                    int i13 = k + 1;
                                    arrayOfByte[k] = (byte) (i9 << 2 | i10 >> 4);
                                    k = i13 + 1;
                                    arrayOfByte[i13] = (byte) ((i10 & 0xF) << 4 | 0xF & i6 >> 2);
                                    i9 = (byte) (k + 1);
                                    arrayOfByte[k] = (byte) (i12 | i6 << 6);
                                    m++;
                                    k = i9;
                                    continue;
                                }
                            }
                        }
                        arrayOfChar = null;
                        // break label632;
                    }
                    int i9 = i1 + 1;
                    i3 = arrayOfChar[i1];
                    if (isData((char) i3)) {
                        i6 = i9 + 1;
                        i1 = arrayOfChar[i9];
                        if (isData((char) i1)) {
                            int i4 = base64Alphabet[i3];
                            int i2 = base64Alphabet[i1];
                            i9 = i6 + 1;
                            i6 = arrayOfChar[i6];
                            // (i9 + 1);
                            i9++;
                            char c1 = arrayOfChar[i9];
                            int i7 = 0;
                            if ((isData((char) i6)) && (isData(c1))) {
                                m = base64Alphabet[i6];
                                i7 = base64Alphabet[c1];
                                int n = k + 1;
                                arrayOfByte[k] = (byte) (i4 << 2 | i2 >> 4);
                                k = n + 1;
                                arrayOfByte[n] = (byte) ((i2 & 0xF) << 4 | 0xF & m >> 2);
                                // (k + 1);
                                k++;
                                arrayOfByte[k] = (byte) (i7 | m << 6);
                                localObject = arrayOfByte;
                                // break label632;
                            }
                            int j;
                            if ((!isPad((char) i7)) /* || (!isPad(localObject)) */) {
                                if ((isPad((char) i7)) /*
                                                        * ||
                                                        * (!isPad(localObject))
                                                        */) {
                                    localObject = null;
                                    // break label632;
                                }
                                int i8 = base64Alphabet[i7];
                                if ((i8 & 0x3) == 0) {
                                    localObject = new byte[2 + m * 3];
                                    m *= 3;
                                    System.arraycopy(arrayOfByte, 0,
                                            localObject, 0, m);
                                    j = k + 1;
                                    // localObject[k] = (byte)(i4 << 2 | i2 >>
                                    // 4);
                                    // localObject[j] = (byte)((i2 & 0xF) << 4 |
                                    // 0xF & i8 >> 2);
                                    // break label632;
                                }
                                localObject = null;
                                // break label632;
                            }
                            if ((i2 & 0xF) == 0) {
                                localObject = new byte[1 + m * 3];
                                m *= 3;
                                // System.arraycopy(j, 0, localObject, 0, m);
                                // localObject[k] = (byte)(i4 << 2 | i2 >> 4);
                                // break label632;
                            }
                            localObject = null;
                            // break label632;
                        }
                    }
                    localObject = null;
                } else {
                    localObject = new byte[0];
                }
            } else {
                localObject = null;
            }
        } else {
            localObject = null;
        }
        label632: return (byte[]) localObject;
    }

    public static String encode(byte[] paramArrayOfByte) {
        Object localObject;
        if (paramArrayOfByte != null) {
            int i = 8 * paramArrayOfByte.length;
            if (i != 0) {
                int k = i % 24;
                int m = i / 24;
                if (k == 0)
                    i = m;
                else
                    i = m + 1;
                // ((char[])null);
                localObject = new char[i * 4];
                int n = 0;
                int i2 = 0;
                int j = 0;
                int i1;
                while (n < m) {
                    i1 = i2 + 1;
                    int i6 = paramArrayOfByte[i2];
                    i2 = i1 + 1;
                    int i5 = paramArrayOfByte[i1];
                    i1 = i2 + 1;
                    i2 = paramArrayOfByte[i2];
                    int i3 = (byte) (i5 & 0xF);
                    int i4 = (byte) (i6 & 0x3);
                    int i7;
                    if ((i6 & 0xFFFFFF80) != 0)
                        i7 = (byte) (0xC0 ^ i6 >> 2);
                    else
                        i7 = (byte) (i6 >> 2);
                    if ((i5 & 0xFFFFFF80) != 0)
                        i6 = (byte) (0xF0 ^ i5 >> 4);
                    else
                        i6 = (byte) (i5 >> 4);
                    if ((i2 & 0xFFFFFF80) != 0)
                        i5 = (byte) (0xFC ^ i2 >> 6);
                    else
                        i5 = (byte) (i2 >> 6);
                    int i8 = j + 1;
                    ((char[]) localObject)[j] = lookUpBase64Alphabet[i7];
                    j = i8 + 1;
                    ((char[]) localObject)[i8] = lookUpBase64Alphabet[(i6 | i4 << 4)];
                    i4 = j + 1;
                    ((char[]) localObject)[j] = lookUpBase64Alphabet[(i5 | i3 << 2)];
                    j = i4 + 1;
                    ((char[]) localObject)[i4] = lookUpBase64Alphabet[(i2 & 0x3F)];
                    n++;
                    i2 = i1;
                }
                if (k != 8) {
                    if (k == 16) {
                        n = paramArrayOfByte[i2];
                        i2 = paramArrayOfByte[(i2 + 1)];
                        k = (byte) (i2 & 0xF);
                        m = (byte) (n & 0x3);
                        if ((n & 0xFFFFFF80) != 0)
                            i1 = (byte) (0xC0 ^ n >> 2);
                        else
                            i1 = (byte) (n >> 2);
                        if ((i2 & 0xFFFFFF80) != 0)
                            n = (byte) (0xF0 ^ i2 >> 4);
                        else
                            n = (byte) (i2 >> 4);
                        i2 = j + 1;
                        ((char[]) localObject)[j] = lookUpBase64Alphabet[i1];
                        j = i2 + 1;
                        ((char[]) localObject)[i2] = lookUpBase64Alphabet[(n | m << 4)];
                        m = j + 1;
                        ((char[]) localObject)[j] = lookUpBase64Alphabet[(k << 2)];
                        j = m + 1;
                        ((char[]) localObject)[m] = 61;
                    }
                } else {
                    m = paramArrayOfByte[i2];
                    k = (byte) (m & 0x3);
                    if ((m & 0xFFFFFF80) != 0)
                        n = (byte) (0xC0 ^ m >> 2);
                    else
                        n = (byte) (m >> 2);
                    m = j + 1;
                    ((char[]) localObject)[j] = lookUpBase64Alphabet[n];
                    j = m + 1;
                    ((char[]) localObject)[m] = lookUpBase64Alphabet[(k << 4)];
                    k = j + 1;
                    ((char[]) localObject)[j] = 61;
                    // (k + 1);
                    k++;
                    ((char[]) localObject)[k] = 61;
                }
                localObject = new String((byte[]) localObject);
            } else {
                localObject = "";
            }
        } else {
            localObject = null;
        }
        return (String) localObject;
    }

    private static boolean isData(char paramChar) {
        boolean i;
        if ((paramChar >= '') || (base64Alphabet[paramChar] == -1))
            i = false;
        else
            i = true;
        return i;
    }

    private static boolean isPad(char paramChar) {
        boolean i;
        if (paramChar != '=')
            i = false;
        else
            i = true;
        return i;
    }

    private static boolean isWhiteSpace(char paramChar) {
        boolean i;
        if ((paramChar == ' ') || (paramChar == '\r') || (paramChar == '\n')
                || (paramChar == '\t'))
            i = true;
        else
            i = false;
        return i;
    }

    private static int removeWhiteSpace(char[] paramArrayOfChar) {
        if (paramArrayOfChar != null) {
            int k = paramArrayOfChar.length;
            int i = 0;
            int j;
            for (int m = 0; i < k; m = j) {
                if (isWhiteSpace(paramArrayOfChar[i])) {
                    j = m;
                } else {
                    j = m + 1;
                    paramArrayOfChar[m] = paramArrayOfChar[i];
                }
                i++;
            }
        }
        int m = 0;
        return m;
    }
}
