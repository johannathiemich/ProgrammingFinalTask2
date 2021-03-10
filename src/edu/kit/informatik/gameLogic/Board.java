package edu.kit.informatik.gameLogic;

import java.util.ArrayList;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.userInteraction.IllegalInputException;

/**
 * This class represents a rectangular (maybe square) game board consisting of
 * square fields. This class cannot be instantiated (since it is abstract),
 * only its subclasses (standard and torus) can be instantiated.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
public abstract class Board {

    /**
     * the number of rows this board has
     */
    private final int rowNumber;

    /**
     * the number of columns this board has
     */
    private final int columnNumber;

    /**
     * the available tokens for this board
     */
    private ArrayList<Token> tokens;
    
    /**
     * the token that is currently set as "selected" 
     */
    private Token selectedToken;
    
    /**
     * the number of properties each token has
     */
    private final int numberOfTokenProperties;
    /**
     * the content of all of the fields (placed token or null)
     */
    private Token[][] contentOfFields;

    /**This method creates a new board.
     * @param pNumberOfTokens the number of tokens used on this board, has to be between 1 and 16
     * @param pNumberOfTokenProperties the number of properties each token has
     * @param pNumberOfRows the number of rows this board has
     * @param pNumberOfColumns the number of columns this board has
     * @throws IllegalInputException if pNumerOfRows, pNumberOfColumns or pNumberOfTokens are below 1
     */
    public Board(int pNumberOfTokens, int pNumberOfTokenProperties, int pNumberOfRows, int pNumberOfColumns) 
            throws IllegalInputException {
        tokens = new ArrayList<Token>();
        if (pNumberOfRows > 0 && pNumberOfColumns > 0) {
            contentOfFields = new Token[pNumberOfRows][pNumberOfColumns];
        } else {
            throw new IllegalInputException(
                    "Error, both the number of rows and the number of columns have to be greater than zero.");
        }
        selectedToken = null;
        if (pNumberOfTokens > 0) {
            for (int i = 0; i < pNumberOfTokens; i++) {
                tokens.add(new Token(i, pNumberOfTokenProperties));
            }
            numberOfTokenProperties = pNumberOfTokenProperties;
        } else {
            throw new IllegalInputException("Error, at least one token has to be used.");
        }
        rowNumber = pNumberOfRows;
        columnNumber = pNumberOfColumns;
    }

    /**This method selects a token, which means this token cannot be selected
     * anymore and has to be placed by a player now.
     * @param pToken the token to be selected
     * @throws ObjectNotFoundException if the board does not contain the chosen token
     * @throws IllegalInputException if a token has already been selected
     */
    public void select(Token pToken) throws ObjectNotFoundException, IllegalInputException {
        if (pToken != null && tokens.contains(pToken)) {
            if (selectedToken == null) {
                selectedToken = pToken;
                // selected token will not be available anymore
                tokens.remove(pToken);
            } else {
                throw new IllegalInputException(
                        "Error, a token has already been selected. Please place the token now.");
            }
        } else {
            throw new ObjectNotFoundException("Error, this token has already been used.");
        }
    }

     /** This method places a token on the board at a specific field.
     * @param pRow the x - Coordinate where the token is going to be placed (number of the row)
     * @param pColumn the y - Coordinate where the token is going to be placed (number of the column)
     * @throws IllegalInputException if a token has already been placed at this field, or if a
     *                               token has not been selected before, or if the chosen field
     *                               does not exist
     */
    public void place(int pRow, int pColumn) throws IllegalInputException {
        // validating coordinates
        if (getCoordinateRow(pRow) >= 0 && getCoordinateColumn(pColumn) >= 0 && getCoordinateRow(pRow) < rowNumber
                && getCoordinateColumn(pColumn) < columnNumber) {
            // check: there is no token on this field yet
            if (contentOfFields[getCoordinateRow(pRow)][getCoordinateColumn(pColumn)] == null) {
                // check: token has been selected before
                if (selectedToken != null) {
                    contentOfFields[getCoordinateRow(pRow)][getCoordinateColumn(pColumn)] = selectedToken;
                } else {
                    throw new IllegalInputException("Error, a token has to be selected first.");
                }
            } else {
                throw new IllegalInputException("Error, a token has already been placed at this field.");
            }
        } else {
            throw new IllegalInputException("Error, illegal field has been selected, it does not exist.");
        }
        // token has been placed --> for placing again, a token has to be
        // selected before
        selectedToken = null;
    }

    /**This methods checks whether one player already won the game.
     * @param pStartRow the x-Coordinate of the field at which the board is going to
     *                  start searching for a winning row of tokens
     * @param pStartColumn the y-Coordinate of the field at which the board is going to
     *                     start searching for a winning row of tokens
     * @return true if a win has been achieved, false if not.
     */
    public boolean checkWin(int pStartRow, int pStartColumn) {
        if (getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(pStartColumn)] != null) {
            // checking for every property of the starting token: is there a row
            // of tokens with the same property?
            for (Property property : getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(
                    pStartColumn)].getProperties()) {
                // looking at all of the neighbors of the starting token -->
                // find a winning row
                if (checkColumn(property, getCoordinateRow(pStartRow), getCoordinateColumn(pStartColumn))
                        || checkRow(property, getCoordinateRow(pStartRow), getCoordinateColumn(pStartColumn))
                        || checkDiagonalBottomRight(property, getCoordinateRow(pStartRow),
                                getCoordinateColumn(pStartColumn))
                        || checkDiagonalTopRight(property, getCoordinateRow(pStartRow),
                                getCoordinateColumn(pStartColumn))) {
                    // a winning row has been found
                    return true;
                }
            }
        }
        // no winning row found
        return false;
    }

    /**This method checks the column of the starting field for a winning row.
     * @param pProperty the property according to which the tokens in this column are
     *                 being compared.
     * @param pStartRow the x-Coordinate of the field to start searching at
     * @param pStartColumn the y-Coordinate of the field to start searching at
     * @return true if a the game has been won, false if not
     */
    private boolean checkColumn(Property pProperty, int pStartRow, int pStartColumn) {
        int counter = 1;
        int countCoordinates = 1;
        // idea: going to next field above this one until the possible winning
        // row is being interrupted or a winning row is found
        while (getCoordinateRow(pStartRow + countCoordinates) < getColumnNumber()
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn)].getProperties().contains(pProperty)) {
            // found another token for the possible winning row
            counter++;
            countCoordinates++;
            if (counter == 4) {
                // 4 tokens with one common property in a line found --> game
                // won
                return true;
            }
        }
        countCoordinates = 1;
        // idea: going to next field below this one until the possible winning
        // row is being interrupted or a winning row is found
        while ((getCoordinateRow(pStartRow - countCoordinates) >= 0
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn)].getProperties().contains(pProperty))) {
            // found token for possible winning row
            counter++;
            countCoordinates++;
            if (counter == 4) {
                // win found
                return true;
            }
        }
        return false;
    }

    /**This method checks the row of the starting field for a winning row.
     * @param pProperty the property according to which the tokens in this row are
     *            being compared.
     * @param pStartRow the x-Coordinate of the field to start searching at
     * @param pStartColumn the y-Coordinate of the field to start searching at
     * @return true if a the game has been won, false if not
     */
    private boolean checkRow(Property pProperty, int pStartRow, int pStartColumn) {
        int counter = 1;
        int countCoordinates = 1;
        // idea: going to next field on the right side of this one until the possible winning
        // row is being interrupted or a winning row is found
        while (getCoordinateColumn(pStartColumn + countCoordinates) < columnNumber
                && getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(
                        pStartColumn + countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(
                        pStartColumn + countCoordinates)].getProperties().contains(pProperty)) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        countCoordinates = 1;
        // going to next field on the left side of this one until the
        // possible winning row is being interrupted or a winning row is found
        while ((getCoordinateColumn(pStartColumn - countCoordinates) >= 0
                && getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(
                        pStartColumn - countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow)][getCoordinateColumn(
                        pStartColumn - countCoordinates)].getProperties().contains(pProperty))) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks the diagonal line (from the starting field to the
     * bottom right field) of the starting field for a winning row.
     * @param pProperty the property according to which the tokens in this diagonal
     *                are being compared.
     * @param pStartRow the x-Coordinate of the field to start searching at
     * @param pStartColumn the y-Coordinate of the field to start searching at
     * @return true if a the game has been won, false if not
     */
    private boolean checkDiagonalBottomRight(Property pProperty, int pStartRow, int pStartColumn) {
        int counter = 1;
        int countCoordinates = 1;
        // going to next field on the bottom right side of this one until the
        // possible winning row is being interrupted or a winning row is found
        while (getCoordinateRow(pStartRow + countCoordinates) < rowNumber
                && getCoordinateColumn(pStartColumn + countCoordinates) < columnNumber
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn + countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn + countCoordinates)].getProperties().contains(pProperty)) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        countCoordinates = 1;
        // going to next field on the left top side of this one until the
        // possible winning row is being interrupted or a winning row is found
        while ((getCoordinateRow(pStartRow - countCoordinates) >= 0
                && getCoordinateColumn(pStartColumn - countCoordinates) >= 0
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn - countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn - countCoordinates)].getProperties().contains(pProperty))) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks the diagonal line (from the starting field to the top
     * right field) of the starting field for a winning row.
     * @param pProperty the property according to which the tokens in this diagonal
     *               are being compared.
     * @param pStartRow the x-Coordinate of the field to start searching at
     * @param pStartColumn the y-Coordinate of the field to start searching at
     * @return true if a the game has been won, false if not
     */
    private boolean checkDiagonalTopRight(Property pProperty, int pStartRow, int pStartColumn) {
        int counter = 1;
        int countCoordinates = 1;
        // going to next field on the top right side of this one until the
        // possible winning row is being interrupted or a winning row is found
        while (getCoordinateRow(pStartRow - countCoordinates) >= 0
                && getCoordinateColumn(pStartColumn + countCoordinates) < columnNumber
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn + countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow - countCoordinates)][getCoordinateColumn(
                        pStartColumn + countCoordinates)].getProperties().contains(pProperty)) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        countCoordinates = 1;
        // going to next field on the bottom left side of this one until the
        // possible winning row is being interrupted or a winning row is found
        while ((getCoordinateRow(pStartRow + countCoordinates) < rowNumber
                && getCoordinateColumn(pStartColumn - countCoordinates) >= 0
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn - countCoordinates)] != null
                && getContentOfFields()[getCoordinateRow(pStartRow + countCoordinates)][getCoordinateColumn(
                        pStartColumn - countCoordinates)].getProperties().contains(pProperty))) {
            counter++;
            countCoordinates++;
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks whether there are empty fields left to place tokens at.
     * @return true if all the fields are occupied, false if there are empty
     *         fields left
     */
    public boolean isBoardFull() {
        for (int i = 0; i < contentOfFields.length; i++) {
            for (int j = 0; j < contentOfFields[0].length; j++) {
                if (contentOfFields[i][j] == null) {
                    // empty field found
                    return false;
                }
            }
        }
        // no empty field found
        return true;
    }

    /**
     * This method checks whether there are tokens left to be placed on the
     * field.
     * @return true if there are tokens left, false if not
     */
    public boolean areTokensLeft() {
        if (tokens.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method prints a specific row of the board. It prints the row in one
     * line, it prints the number of the token for an occupied field and a '#'
     * if the field is empty.
     * @param pRow the number of the row to be printed
     * @throws IllegalInputException if the given row number (pRow) is invalid
     */
    public void rowPrint(int pRow) throws IllegalInputException {
        if (pRow >= 0 && pRow < rowNumber) {
            String output = "";
            // summing up the output
            for (int i = 0; i < columnNumber; i++) {
                if (contentOfFields[pRow][i] == null) {
                    // empty field --> #
                    output = output + "#";
                } else {
                    // vacant field --> number of token
                    output = output + contentOfFields[pRow][i].getDecimalNumber();
                }
                if (i < columnNumber - 1) {
                output = output + " ";
                }
            }
            Terminal.printLine(output);
        } else
            throw new IllegalInputException("Error, invalid row number.");
    }

    /** This method prints a specific column of the board. It prints the column
     * in one line, it prints the number of the token for an occupied field and
     * a '#' if the field is empty.
     * @param pColumn the number of the column to be printed
     * @throws IllegalInputException if the given column number (pColumn) is invalid
     */
    public void columnPrint(int pColumn) throws IllegalInputException {
        if (pColumn >= 0 && pColumn < columnNumber) {
            String output = "";
            for (int i = 0; i < rowNumber; i++) {
                if (contentOfFields[i][pColumn] == null) {
                    output = output + "#";
                } else {
                    output = output + contentOfFields[i][pColumn].getDecimalNumber();
                }
                if (i < rowNumber - 1)  {
                output = output + " ";
                }
            }
            Terminal.printLine(output);
        } else {
            throw new IllegalInputException("Error, invalid column number.");
        }
    }

    /**This method prints the numbers of all the available tokens in one line,
     * separated by a whitespace.
     */
    public void tokenPrint() {
        String output = "";
        for (Token token : tokens) {
            output = output + token.getDecimalNumber() + " ";
        }
        Terminal.printLine(output);
    }

    /**
     * This method returns modifies a given x-coordinate in order to return it.
     * This is necessary since the subclasses of this class (torus and standard)
     * use different calculations.
     * @param pCoordinate the x-coordinate to calculate the "right" coordinate of
     * @return a modified coordinate
     */
    public abstract int getCoordinateRow(int pCoordinate);

    /**
     * This method returns modifies a given x-coordinate in order to return it.
     * This is necessary since the subclasses of this class (torus and standard)
     * use different approaches.
     * @param pCoordinate the x-coordinate to calculate the "right" coordinate of
     * @return a modified coordinate
     */
    public abstract int getCoordinateColumn(int pCoordinate);

    /**
     * This method returns the list of tokens
     * @return the tokens
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * This method returns the content of the fields.
     * @return the contentOfFields
     */
    public Token[][] getContentOfFields() {
        return contentOfFields;
    }

    /**
     * This method returns the number of the rows of this board.
     * @return the rowNumber
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * This method returns the number of the columns of this board.
     * @return the columnNumber
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * This method returns the token that has been selected.
     * @return the selectedToken
     */
    public Token getSelectedToken() {
        return selectedToken;
    }
    
    /**
     * This method returns the number of properties each token has.
     * @return the number of properties each token has
     */
    public int getNumberOfTokenProperties() {
        return numberOfTokenProperties;
    }

    /**
     * This method changes the selected token.
     * @param selectedToken the selectedToken to set
     */
    public void setSelectedToken(Token selectedToken) {
        this.selectedToken = selectedToken;
    }
}