package edu.kit.informatik.gameLogic;

import java.util.ArrayList;

import edu.kit.informatik.userInteraction.IllegalInputException;

/**
 * This class represents a token of a board game. It can be placed on the board.
 * 4 tokens in a row, having at least one common property, lead to winning the
 * game.
 * 
 * Only package visibility has been chosen because of principles of capsuling and the secret principle.
 * This class is not needed in another package other than the one it is in right now.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
class Token {

    /**
     * the number that this token has (every token has its own number, there are
     * no duplicate numbers)
     */
    private int decimalNumber;

    /**
     * the decimal number in binary number system, it determines the properties
     * of this token
     */
    private String binaryNumberString;

    /**
     * the properties of this token
     */
    private ArrayList<Property> properties;

    /**
     * This creates a new token with a specific number and a certain number of
     * properties
     * 
     * @param pNumber
     *            the decimal number this token has
     * @param pNumberOfProperties
     *            the number of properties this token is going to have
     * @throws IllegalInputException
     *             if the the number of the token is smaller than zero or if the
     *             number of properties is smaller than one
     */
    public Token(int pNumber, int pNumberOfProperties) throws IllegalInputException {
        // checking decimal number and number of properties
        if (pNumber >= 0 && pNumberOfProperties > 0) {
            decimalNumber = pNumber;
            properties = new ArrayList<Property>();
            // turning decimal number into binary number
            String pPropertiesString = Integer.toBinaryString(pNumber);
            // adding leading zeros --> reach the length that equals the number
            // of properties
            // each digit determines a property of this token (e.g. 4 digits ->
            // 4 properties)
            while (pPropertiesString.length() < pNumberOfProperties) {
                String help = "0" + pPropertiesString;
                pPropertiesString = help;
            }
            binaryNumberString = pPropertiesString;
            // checking every single digit to determine the properties
            for (int i = 0; i < binaryNumberString.length(); i++) {
                switch (i) {
                // first digit (starting at the left side of the number)
                case 0:
                    if (binaryNumberString.charAt(i) == '0') {
                        properties.add(Property.BLACK);
                    } else {
                        properties.add(Property.WHITE);
                    }
                    break;
                // second digit
                case 1:
                    if (binaryNumberString.charAt(i) == '0') {
                        properties.add(Property.SQUARE);
                    } else {
                        properties.add(Property.CYLINDRICALLY);
                    }
                    break;
                // third digit
                case 2:
                    if (binaryNumberString.charAt(i) == '0') {
                        properties.add(Property.SMALL);
                    } else {
                        properties.add(Property.BIG);
                    }
                    break;
                // fourth digit
                case 3:
                    if (binaryNumberString.charAt(i) == '0') {
                        properties.add(Property.HOLLOW);
                    } else {
                        properties.add(Property.SOLID);
                    }
                    break;
                // all other digits
                /*
                 * for this project, only for the first 4 digits was specified
                 * which property the tokens are supposed to have, no definition
                 * of tokens with more than 4 properties, so I decided to just
                 * randomly add properties (out of the given properties) if a
                 * token has more than 4 attributes. This way, the number of
                 * properties of a token is dynamic and can be as the user needs
                 * it.
                 */
                default:
                    addRandomProperty();
                }

            }
        } else {
            throw new IllegalInputException("Error, the number of the token must be greater than or equal to "
                    + "zero and the number of properties has to be at least one.");
        }
    }

    /**
     * This method adds a random property to the list of properties of this
     * token.
     */
    private void addRandomProperty() {
        // creating random number for random property
        int randomNumber = (int) (Math.random() * 8 + 1);
        switch (randomNumber) {
        case 1:
            this.properties.add(Property.BLACK);
            break;
        case 2:
            this.properties.add(Property.WHITE);
            break;
        case 3:
            this.properties.add(Property.SQUARE);
            break;
        case 4:
            this.properties.add(Property.CYLINDRICALLY);
            break;
        case 5:
            this.properties.add(Property.SMALL);
            break;
        case 6:
            this.properties.add(Property.BIG);
            break;
        case 7:
            this.properties.add(Property.SOLID);
            break;
        case 8:
            this.properties.add(Property.HOLLOW);
        default: // this should not happen (random number between 1 and 8)
            this.addRandomProperty();
        }
    }

    /**
     * This method checks whether this token equals another token. Two tokens
     * are equal if their decimal numbers and their number of properties are
     * equal. The original equals method had to be overwritten because of using
     * the contains or remove method of the arrayList containing tokens
     */
    // no need for overriding hashCode method because of primitive type integer
    @Override
    public boolean equals(Object pObject) {
        if (pObject == null || !(pObject instanceof Token)) {
            return false;
        }
        Token token = (Token) pObject;
        /*
         * tokens are equal if their decimal numbers and their number of
         * properties are equal
         */
        if (token.getDecimalNumber() == this.getDecimalNumber()
                && token.getProperties().size() == this.properties.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns the decimal number of this token.
     * 
     * @return the decimal number of this token
     */
    public int getDecimalNumber() {
        return decimalNumber;
    }

    /**
     * This method returns the list of properties of this token.
     * 
     * @return the properties of this token
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }
}
