package com.tencent.weibo.beans;

import java.io.Serializable;

public class QParameter implements Serializable, Comparable<QParameter> {
    private static final long serialVersionUID = -9041723304674844461L;
    private String name;
    private String value;

    public QParameter(String paramString1, String paramString2) {
        this.name = paramString1;
        this.value = paramString2;
    }

    public int compareTo(QParameter paramQParameter) {
        int i = this.name.compareTo(paramQParameter.getName());
        if (i == 0)
            i = this.value.compareTo(paramQParameter.getValue());
        return i;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setValue(String paramString) {
        this.value = paramString;
    }
}
