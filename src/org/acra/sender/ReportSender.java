
package org.acra.sender;

import org.acra.CrashReportData;
import org.acra.ReportField;

/**
 * A simple interface for defining various crash report senders. You can reuse
 * {@link HttpPostSender} to send reports to your custom server-side report
 * collection script even if you expect (or prefer) specific names for each
 * report field as {@link HttpPostSender#HttpPostSender(String, java.util.Map)}
 * can take a Map<ReportField, String> as an input to convert each field name to
 * your preferred POST parameter name.
 * 
 */
public interface ReportSender {
    /**
     * Send crash report data. You don't have to take care of managing Threads,
     * just implement what is necessary to handle the data. ACRA will use a
     * specific Thread (not the UI Thread) to run your sender.
     * 
     * @param errorContent
     *            Stores key/value pairs for each report field. A report field
     *            is identified by a {@link ReportField} enum value.
     * @throws ReportSenderException
     *             If anything goes fatally wrong during the handling of crash
     *             data, you can (should) throw a {@link ReportSenderException}
     *             with a custom message.
     */
    public void send(CrashReportData errorContent) throws ReportSenderException;
}
