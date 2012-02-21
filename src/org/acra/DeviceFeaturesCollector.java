
package org.acra;

import static org.acra.ACRA.LOG_TAG;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Features declared as available on the device. Available only with API level >
 * 5.
 * 
 * @author cream jiang
 * 
 */
public class DeviceFeaturesCollector {
    public static String getFeatures(Context ctx) {
        if (Compatibility.getAPILevel() >= 5) {
            StringBuffer result = new StringBuffer();
            PackageManager pm = ctx.getPackageManager();
            try {
                Method getSystemAvailableFeatures = PackageManager.class.getMethod("getSystemAvailableFeatures", (Class[])null);
                Object[] features = (Object[])getSystemAvailableFeatures.invoke(pm);
                for(Object feature : features) {
                    String featureName = (String)feature.getClass().getField("name").get(feature);
                    if(featureName != null) {
                        result.append(featureName);
                    } else {
                        Method getGlEsVersion = feature.getClass().getMethod("getGlEsVersion", (Class[]) null); 
                        String glEsVersion = (String)getGlEsVersion.invoke(feature);
                        result.append("glEsVersion = ");
                        result.append(glEsVersion);
                    }
                    result.append("\n");
                }
            } catch (Throwable e) {
                Log.w(LOG_TAG, "Error : ", e);
                result.append("Could not retrieve data: ");
                result.append(e.getMessage());
            }
            
            return result.toString();
        } else {
            return "Data available only with API Level > 5";
        }
    }
}
