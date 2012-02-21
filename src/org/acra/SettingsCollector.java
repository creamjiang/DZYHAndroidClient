

package org.acra;

import java.lang.reflect.Field;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;

/**
 * Helper to collect data from {@link System} and {@link Secure} Settings
 * classes.
 * 
 * @author cream jiang
 * 
 */
public class SettingsCollector {
    /**
     * Collect data from {@link android.provider.Settings.System}. This
     * collector uses reflection to be sure to always get the most accurate data
     * whatever Android API level it runs on.
     * 
     * @param ctx
     *            the application context.
     * @return A human readable String containing one key=value pair per line.
     */
    public static String collectSystemSettings(Context ctx) {
        StringBuilder result = new StringBuilder();
        Field[] keys = Settings.System.class.getFields();
        for (Field key : keys) {
            // Avoid retrieving deprecated fields... it is useless, has an
            // impact on perfs, and the system writes many warnings in the
            // logcat.
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class) {
                try {
                    Object value = Settings.System.getString(ctx.getContentResolver(), (String) key.get(null));
                    if (value != null) {
                        result.append(key.getName()).append("=").append(value).append("\n");
                    }
                } catch (IllegalArgumentException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                } catch (IllegalAccessException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                }
            }
        }

        return result.toString();
    }

    /**
     * Collect data from {@link android.provider.Settings.Secure}. This
     * collector uses reflection to be sure to always get the most accurate data
     * whatever Android API level it runs on.
     * 
     * @param ctx
     *            the application context.
     * @return A human readable String containing one key=value pair per line.
     */
    public static String collectSecureSettings(Context ctx) {
        StringBuilder result = new StringBuilder();
        Field[] keys = Settings.Secure.class.getFields();
        for (Field key : keys) {
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class && isAuthorized(key)) {
                try {
                    Object value = Settings.Secure.getString(ctx.getContentResolver(), (String) key.get(null));
                    if (value != null) {
                        result.append(key.getName()).append("=").append(value).append("\n");
                    }
                } catch (IllegalArgumentException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                } catch (IllegalAccessException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                }
            }
        }

        return result.toString();
    }

	private static boolean isAuthorized(Field key) {
		if(key==null
				|| key.getName().startsWith("WIFI_AP")) {
			return false;
		}
		return true;
	}

}
