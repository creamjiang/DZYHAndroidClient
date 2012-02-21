
package org.acra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

/**
 * Collects results of the <code>dumpsys</code> command.
 * 
 * @author cream jiang
 * 
 */
class DumpSysCollector {

    /**
     * Collect results of the <code>dumpsys meminfo</code> command restricted to
     * this application process.
     * 
     * @return The execution result.
     */
    protected static String collectMemInfo() {
        StringBuilder meminfo = new StringBuilder();
        try {
            ArrayList<String> commandLine = new ArrayList<String>();
            commandLine.add("dumpsys");
            commandLine.add("meminfo");
            commandLine.add(Integer.toString(android.os.Process.myPid()));

            Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                meminfo.append(line);
                meminfo.append("\n");
            }

        } catch (IOException e) {
            Log.e(ACRA.LOG_TAG, "DumpSysCollector.meminfo could not retrieve data", e);
        }

        return meminfo.toString();
    }

}