
package org.acra;

/**
 * Defines the different user interaction modes for ACRA.
 * <ul>
 * <li>SILENT: No interaction, reports are sent silently and a "Force close"
 * dialog terminates the app.</li>
 * <li>TOAST: A simple Toast is triggered when the application crashes, the
 * Force close dialog is not displayed.</li>
 * <li>NOTIFICATION: A status bar notification is triggered when the application
 * crashes, the Force close dialog is not displayed. When the user selects the
 * notification, a dialog is displayed asking him if he is ok to send a report</li>
 * </ul>
 */
public enum ReportingInteractionMode {
    /**
     * No interaction, reports are sent silently and a "Force close" dialog
     * terminates the app.
     */
    SILENT,
    /**
     * A status bar notification is triggered when the application crashes, the
     * Force close dialog is not displayed. When the user selects the
     * notification, a dialog is displayed asking him if he is ok to send a
     * report.
     */
    NOTIFICATION,
    /**
     * A simple Toast is triggered when the application crashes, the Force close
     * dialog is not displayed.
     */
    TOAST;
}