

package org.acra;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Tools to retrieve key/value pairs from static fields and getters of any
 * class. Reflection API usage allows to retrieve data without having to
 * implement a class for each android version of each intersting class.
 * It can also help find hidden properties.
 * 
 * @author cream jiang
 * 
 */
public class ReflectionCollector {
    /**
     * Retrieves key/value pairs from static fields of a class.
     * @param someClass the class to be inspected.
     * @return A human readable string with a key=value pair on each line.
     */
    public static String collectConstants(Class<? extends Object> someClass) {
        StringBuilder result = new StringBuilder();

        Field[] fields = someClass.getFields();
        for (Field field : fields) {
            result.append(field.getName()).append("=");
            try {
                result.append(field.get(null).toString());
            } catch (IllegalArgumentException e) {
                result.append("N/A");
            } catch (IllegalAccessException e) {
                result.append("N/A");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Retrieves key/value pairs from static getters of a class (get*() or is*()).
     * @param someClass the class to be inspected.
     * @return A human readable string with a key=value pair on each line.
     */
    public static String collectStaticGettersResults(Class<? extends Object> someClass) {
        StringBuilder result = new StringBuilder();
        Method[] methods = someClass.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0
                    && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && !method.getName().equals("getClass")) {
                try {
                    result.append(method.getName()).append('=').append(method.invoke(null, (Object[]) null))
                            .append("\n");
                } catch (IllegalArgumentException e) {
                    // NOOP
                } catch (IllegalAccessException e) {
                    // NOOP
                } catch (InvocationTargetException e) {
                    // NOOP
                }
            }
        }

        return result.toString();
    }

}
