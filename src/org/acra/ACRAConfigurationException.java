
package org.acra;

/**
 * A simple Exception used when required configuration items are missing.
 * 
 * @author cream jiang
 */
public class ACRAConfigurationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7355339673505996110L;

    public ACRAConfigurationException(String msg) {
        super(msg);
    }
}
