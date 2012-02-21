
package org.acra.sender;

/**
 * This exception is thrown when an error ocurred while sending crash data in a
 * {@link ReportSender} implementation.
 * 
 * @author cream jiang
 * 
 */
@SuppressWarnings("serial")
public class ReportSenderException extends Exception {

    /**
     * Creates a new {@link ReportSenderException} instance. You can provide a
     * detailed message to explain what went wrong.
     * 
     * @param detailMessage A message to explain the cause of this exception.
     * @param throwable An optional throwable which caused this Exception.
     */
    public ReportSenderException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
