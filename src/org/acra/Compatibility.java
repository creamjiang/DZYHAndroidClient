
package org.acra;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Build;

/**
 * Utility class containing methods enabling backward compatibility.
 * 
 * 
 */
public class Compatibility {

    /**
     * Retrieves Android SDK API level using the best possible method.
     * 
     * @return The Android SDK API int level.
     */
    static int getAPILevel() {
        int apiLevel;
        try {
            // This field has been added in Android 1.6
            Field SDK_INT = Build.VERSION.class.getField("SDK_INT");
            apiLevel = SDK_INT.getInt(null);
        } catch (SecurityException e) {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        } catch (NoSuchFieldException e) {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        } catch (IllegalArgumentException e) {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        } catch (IllegalAccessException e) {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        }

        return apiLevel;
    }

    /**
     * Retrieve the DropBoxManager service name using reflection API.
     * 
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * 
     */
    static String getDropBoxServiceName() throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field serviceName = Context.class.getField("DROPBOX_SERVICE");
        if (serviceName != null) {
            return (String) serviceName.get(null);
        }
        return null;
    }
}
