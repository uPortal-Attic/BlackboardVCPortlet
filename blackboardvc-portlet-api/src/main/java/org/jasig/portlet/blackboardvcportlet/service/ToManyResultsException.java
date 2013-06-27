package org.jasig.portlet.blackboardvcportlet.service;

/**
 * Thrown if more than the allowed number of results were returned.
 * 
 * @author Eric Dalquist
 */
public class ToManyResultsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ToManyResultsException() {
        super();
    }

    public ToManyResultsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToManyResultsException(String message) {
        super(message);
    }

    public ToManyResultsException(Throwable cause) {
        super(cause);
    }

}
