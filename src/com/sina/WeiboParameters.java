package com.sina;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class WeiboParameters {
    private List<String> mKeys = new ArrayList();
    private Bundle mParameters = new Bundle();

    public void add(String paramString1, String paramString2) {
        if (!this.mKeys.contains(paramString1)) {
            this.mKeys.add(paramString1);
            this.mParameters.putString(paramString1, paramString2);
        } else {
            this.mParameters.putString(paramString1, paramString2);
        }
    }

    public void addAll(WeiboParameters paramWeiboParameters) {
        for (int i = 0; i < paramWeiboParameters.size(); i++)
            add(paramWeiboParameters.getKey(i),
                    paramWeiboParameters.getValue(i));
    }

    public void clear() {
        this.mKeys.clear();
        this.mParameters.clear();
    }

    public String getKey(int paramInt) {
        String str;
        if ((paramInt < 0) || (paramInt >= this.mKeys.size()))
            str = "";
        else
            str = (String) this.mKeys.get(paramInt);
        return str;
    }

    public int getLocation(String paramString) {
        int i;
        if (!this.mKeys.contains(paramString))
            i = -1;
        else
            i = this.mKeys.indexOf(paramString);
        return i;
    }

    public String getValue(int paramInt) {
        String str = (String) this.mKeys.get(paramInt);
        return this.mParameters.getString(str);
    }

    public String getValue(String paramString) {
        return this.mParameters.getString(paramString);
    }

    public void remove(int paramInt) {
        String str = (String) this.mKeys.get(paramInt);
        this.mParameters.remove(str);
        this.mKeys.remove(str);
    }

    public void remove(String paramString) {
        this.mKeys.remove(paramString);
        this.mParameters.remove(paramString);
    }

    public int size() {
        return this.mKeys.size();
    }
}
