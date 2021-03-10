package edu.kit.informatik.gameLogic;

import edu.kit.informatik.userInteraction.IllegalInputException;

/**
 * This class represents the game mode "torus", which means: the outer fields of
 * the board are neighbors to the opposite outer fields. So if a coordinate
 * outside the usual board is being entered, the command can still be executed.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
public class Torus extends Board {

    /**
     * This method creates a new torus board
     * 
     * @param pNumberOfTokens
     *            the number of tokens used on this board
     * @param pNumberOfTokenProperties the number of properties each token is going to have
     * @param pNumberOfRows
     *            the number of rows the board is going to have
     * @param pNumberOfColumns
     *            the number of columns the board is going to have
     * @throws IllegalInputException
     *             if the entered row number, column number or number of tokens
     *             is smaller than 1
     */
    public Torus(int pNumberOfTokens, int pNumberOfTokenProperties, 
            int pNumberOfRows, int pNumberOfColumns) throws IllegalInputException {
        super(pNumberOfTokens, pNumberOfTokenProperties, pNumberOfRows, pNumberOfColumns);
    }

    /**
     * This method turns the given row number into a row number that the board
     * actually contains (if the given row number is out of range of the board)
     * 
     * @param pOriginalCoordinate row number that is supposed to be turned into a valid coordinate
     */
    @Override
    public int getCoordinateRow(int pOriginalCoordinate) {
        if (pOriginalCoordinate < 0) {
            return (super.getRowNumber() - 1) - (Math.abs((pOriginalCoordinate + 1)) % super.getRowNumber());
        } else {
            return pOriginalCoordinate % super.getRowNumber();
        }
    }

    /**
     * This method turns the given column number into a column number that the board
     * actually contains (if the given column number is out of range of the board)
     * 
     * @param pOriginalCoordinate column number that is supposed to be turned into a valid coordinate
     */
    @Override
    public int getCoordinateColumn(int pOriginalCoordinate) {
        if (pOriginalCoordinate < 0) {
            return (super.getColumnNumber() - 1) - (Math.abs((pOriginalCoordinate + 1)) % super.getColumnNumber());
        } else {
            return pOriginalCoordinate % super.getColumnNumber();
        }
    }
}
