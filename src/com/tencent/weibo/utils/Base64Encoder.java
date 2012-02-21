package com.tencent.weibo.utils;

public class Base64Encoder {
    private static final char[] encodeTable;
    private static final char last2byte = (char) Integer
            .parseInt("00000011", 2);
    private static final char last4byte = (char) Integer
            .parseInt("00001111", 2);
    private static final char last6byte = (char) Integer
            .parseInt("00111111", 2);
    private static final char lead2byte;
    private static final char lead4byte;
    private static final char lead6byte = (char) Integer
            .parseInt("11111100", 2);

    static {
        lead4byte = (char) Integer.parseInt("11110000", 2);
        lead2byte = (char) Integer.parseInt("11000000", 2);
        char[] arrayOfChar = new char[64];
        arrayOfChar[0] = 65;
        arrayOfChar[1] = 66;
        arrayOfChar[2] = 67;
        arrayOfChar[3] = 68;
        arrayOfChar[4] = 69;
        arrayOfChar[5] = 70;
        arrayOfChar[6] = 71;
        arrayOfChar[7] = 72;
        arrayOfChar[8] = 73;
        arrayOfChar[9] = 74;
        arrayOfChar[10] = 75;
        arrayOfChar[11] = 76;
        arrayOfChar[12] = 77;
        arrayOfChar[13] = 78;
        arrayOfChar[14] = 79;
        arrayOfChar[15] = 80;
        arrayOfChar[16] = 81;
        arrayOfChar[17] = 82;
        arrayOfChar[18] = 83;
        arrayOfChar[19] = 84;
        arrayOfChar[20] = 85;
        arrayOfChar[21] = 86;
        arrayOfChar[22] = 87;
        arrayOfChar[23] = 88;
        arrayOfChar[24] = 89;
        arrayOfChar[25] = 90;
        arrayOfChar[26] = 97;
        arrayOfChar[27] = 98;
        arrayOfChar[28] = 99;
        arrayOfChar[29] = 100;
        arrayOfChar[30] = 101;
        arrayOfChar[31] = 102;
        arrayOfChar[32] = 103;
        arrayOfChar[33] = 104;
        arrayOfChar[34] = 105;
        arrayOfChar[35] = 106;
        arrayOfChar[36] = 107;
        arrayOfChar[37] = 108;
        arrayOfChar[38] = 109;
        arrayOfChar[39] = 110;
        arrayOfChar[40] = 111;
        arrayOfChar[41] = 112;
        arrayOfChar[42] = 113;
        arrayOfChar[43] = 114;
        arrayOfChar[44] = 115;
        arrayOfChar[45] = 116;
        arrayOfChar[46] = 117;
        arrayOfChar[47] = 118;
        arrayOfChar[48] = 119;
        arrayOfChar[49] = 120;
        arrayOfChar[50] = 121;
        arrayOfChar[51] = 122;
        arrayOfChar[52] = 48;
        arrayOfChar[53] = 49;
        arrayOfChar[54] = 50;
        arrayOfChar[55] = 51;
        arrayOfChar[56] = 52;
        arrayOfChar[57] = 53;
        arrayOfChar[58] = 54;
        arrayOfChar[59] = 55;
        arrayOfChar[60] = 56;
        arrayOfChar[61] = 57;
        arrayOfChar[62] = 43;
        arrayOfChar[63] = 47;
        encodeTable = arrayOfChar;
    }

    public static String encode(byte[] paramArrayOfByte) {
        StringBuffer localStringBuffer = new StringBuffer(
                3 + (int) (1.34D * paramArrayOfByte.length));
        int i = 0;
        int j = 0;
        for (int k = 0; k < paramArrayOfByte.length; k++) {
            i %= 8;
            while (i < 8) {
                switch (i) {
                case 0:
                    j = (paramArrayOfByte[k] & lead6byte) >>> 2;
                    break;
                case 2:
                    j = paramArrayOfByte[k] & last6byte;
                    break;
                case 4:
                    j = (paramArrayOfByte[k] & last4byte) << 2;
                    if (k + 1 >= paramArrayOfByte.length)
                        break;
                    j = (char) (j | (paramArrayOfByte[(k + 1)] & lead2byte) >>> 6);
                    break;
                case 6:
                    j = (paramArrayOfByte[k] & last2byte) << 4;
                    if (k + 1 >= paramArrayOfByte.length)
                        break;
                    j = (char) (j | (paramArrayOfByte[(k + 1)] & lead4byte) >>> 4);
                case 1:
                case 3:
                case 5:
                }
                localStringBuffer.append(encodeTable[j]);
                i += 6;
            }
        }
        if (localStringBuffer.length() % 4 != 0)
            for (i = 4 - localStringBuffer.length() % 4; i > 0; i--)
                localStringBuffer.append("=");
        return localStringBuffer.toString();
    }
}
