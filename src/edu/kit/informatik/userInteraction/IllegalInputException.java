package edu.kit.informatik.userInteraction;

/**
 * This exception is thrown if a method is invoked with an illegal argument,
 * e.g. invalid row or column number or invalid number of token.
 * @author Johanna Thiemich
 * @version 1.0
 *
 */
public class IllegalInputException extends Exception {

    /**
     * automatically generated serivalVersionUID
     */
    private static final long serialVersionUID = 81820459651580935L;

    /**
     * This method creates a new IllegalInputException
     * @param pOutput String containing information about why the exception has been thrown
     */
    public IllegalInputException(String pOutput) {
        super(pOutput);
    }

}

