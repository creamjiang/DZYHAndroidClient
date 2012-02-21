package com.dld.android.view;

import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.style.StrikethroughSpan;
import org.xml.sax.XMLReader;

public class MyHtmlTagHandler implements TagHandler {
    private Object getLast(Editable paramEditable, Class paramClass) {
        Object localObject = null;
        Object[] arrayOfObject = paramEditable.getSpans(0,
                paramEditable.length(), paramClass);
        if (arrayOfObject.length != 0) {
            int i = arrayOfObject.length;
            while (i > 0) {
                if (paramEditable.getSpanFlags(arrayOfObject[(i - 1)]) != 17) {
                    i--;
                    continue;
                }
                localObject = arrayOfObject[(i - 1)];
            }
        }
        return localObject;
    }

    private void processStrike(boolean paramBoolean, Editable paramEditable) {
        int j = paramEditable.length();
        if (!paramBoolean) {
            Object localObject = getLast(paramEditable, StrikethroughSpan.class);
            int i = paramEditable.getSpanStart(localObject);
            paramEditable.removeSpan(localObject);
            if (i != j)
                paramEditable.setSpan(new StrikethroughSpan(), i, j, 33);
        } else {
            paramEditable.setSpan(new StrikethroughSpan(), j, j, 17);
        }
    }

    public void handleTag(boolean paramBoolean, String paramString,
            Editable paramEditable, XMLReader paramXMLReader) {
        if ((paramString.equalsIgnoreCase("strike"))
                || (paramString.equals("s")))
            processStrike(paramBoolean, paramEditable);
    }
}
