package com.dld.coupon.util;

//import C;
//import I;
import com.dld.android.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class LetterUtil {
    private static final char[] alphatable;
    private static Map<Character, Character> special;
    private static final int[] table;

    static {
        char[] localObject = new char[26];
        localObject[0] = 65;
        localObject[1] = 66;
        localObject[2] = 67;
        localObject[3] = 68;
        localObject[4] = 69;
        localObject[5] = 70;
        localObject[6] = 71;
        localObject[7] = 72;
        localObject[8] = 73;
        localObject[9] = 74;
        localObject[10] = 75;
        localObject[11] = 76;
        localObject[12] = 77;
        localObject[13] = 78;
        localObject[14] = 79;
        localObject[15] = 80;
        localObject[16] = 81;
        localObject[17] = 82;
        localObject[18] = 83;
        localObject[19] = 84;
        localObject[20] = 85;
        localObject[21] = 86;
        localObject[22] = 87;
        localObject[23] = 88;
        localObject[24] = 89;
        localObject[25] = 90;
        alphatable = localObject;
        int[] localObject1 = new int[27];
        localObject1[0] = 45217;
        localObject1[1] = 45253;
        localObject1[2] = 45761;
        localObject1[3] = 46318;
        localObject1[4] = 46826;
        localObject1[5] = 47010;
        localObject1[6] = 47297;
        localObject1[7] = 47614;
        localObject1[8] = 47614;
        localObject1[9] = 48119;
        localObject1[10] = 49062;
        localObject1[11] = 49324;
        localObject1[12] = 49896;
        localObject1[13] = 50371;
        localObject1[14] = 50614;
        localObject1[15] = 50622;
        localObject1[16] = 50906;
        localObject1[17] = 51387;
        localObject1[18] = 51446;
        localObject1[19] = 52218;
        localObject1[20] = 52218;
        localObject1[21] = 52218;
        localObject1[22] = 52698;
        localObject1[23] = 52980;
        localObject1[24] = 53689;
        localObject1[25] = 54481;
        localObject1[26] = 55289;
        table = localObject1;
        special = new HashMap();
        special.put(Character.valueOf('1'), Character.valueOf('C'));
        special.put(Character.valueOf('2'), Character.valueOf('T'));
        special.put(Character.valueOf('奚'), Character.valueOf('X'));
        special.put(Character.valueOf('昝'), Character.valueOf('Z'));
    }

    public static char char2Alpha(char paramChar) {
        if ((paramChar < 'a') || (paramChar > 'z')) {
            if ((paramChar < 'A') || (paramChar > 'Z')) {
                int i = gbValue(paramChar);
                if (i >= table[0]) {
                    int j = 0;
                    while (j < 26) {
                        if (!match(j, i)) {
                            j++;
                            continue;
                        }
                        if (j < 26) {
                            paramChar = alphatable[j];
                            // break label131;
                            return paramChar;
                        }
                        paramChar = '*';
                        // break label131;
                        return paramChar;
                    }
                    Character localCharacter = (Character) special
                            .get(Character.valueOf(paramChar));
                    char c;
                    if (localCharacter != null)
                        c = localCharacter.charValue();
                    else
                        c = '[';
                    paramChar = c;
                } else {
                    paramChar = '*';
                }
            }
        } else
            paramChar = (char) (65 + (paramChar + 'ﾟ'));
        label131: return paramChar;
    }

    private static int gbValue(char paramChar) {
        int i = 0;
        Object localObject = new String() + paramChar;
        try {
            localObject = ((String) localObject).getBytes("GB2312");
            if (((byte[]) localObject).length >= 2) {
                i = 0xFF00 & ((byte[]) localObject)[0] << 8;
                int j = ((byte[]) localObject)[1];
                i += (j & 0xFF);
            }
        } catch (Exception localException) {
            i = 42;
        }
        return i;
    }

    public static String getLetters(String paramString) {
        String str = ChangeCode.toSimple(paramString);
        StringBuilder localStringBuilder = new StringBuilder();
        int j = 0;
        try {
            while (true) {
                int i = str.length();
                if (j >= i)
                    return localStringBuilder.toString();
                localStringBuilder.append(char2Alpha(str.charAt(j)));
                j++;
            }
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    private static boolean match(int paramInt1, int paramInt2) {
        boolean i = false;
        if (paramInt2 >= table[paramInt1]) {
            for (int j = paramInt1 + 1; (j < 26)
                    && (table[j] == table[paramInt1]); j++) {
                if (j != 26) {
                    if (paramInt2 < table[j])
                        i = true;
                } else if (paramInt2 <= table[j])
                    i = true;
            }
        }
        return i;
    }
}
