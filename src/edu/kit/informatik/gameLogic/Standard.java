package edu.kit.informatik.gameLogic;

import edu.kit.informatik.userInteraction.IllegalInputException;

/**
 * This class represents the game mode "standard", which means: if a coordinate
 * outside of the range of the board is being entered (for some command), the
 * command is invalid. So the outer fields of the board are NOT neighbors to the
 * opposite outer fields.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
public class Standard extends Board {

    /**
     * This method creates a new standard mode board.
     * 
     * @param pNumberOfTokens
     *            the number of tokens used on this board
     * @param pNumberOfTokenProperties 
     *          the number of properties each token has
     * @param pNumberOfRows
     *            the number of rows this board is going to have
     * @param pNumberOfColumns
     *            the number of columns this board is going to have
     * @throws IllegalInputException
     *             if the entered row number, column number or number of tokens
     *             is smaller than 1
     */
    public Standard(int pNumberOfTokens, int pNumberOfTokenProperties, int pNumberOfRows, int pNumberOfColumns)
            throws IllegalInputException {
        super(pNumberOfTokens, pNumberOfTokenProperties, pNumberOfRows, pNumberOfColumns);
    }

    /**
     * This method returns the given row number; since this is a standard board,
     * the row number is not changed (an invalid row number would mean: row
     * number outside the given board and is not allowed at a standard board).
     */
    @Override
    public int getCoordinateRow(int pCoordinate) {
        return pCoordinate;
    }

    /**
     * This method returns the given column number; since this is a standard
     * board, the column number is not changed (an invalid column number would
     * mean: column number outside the given board and is not allowed at a
     * standard board).
     */
    @Override
    public int getCoordinateColumn(int pCoordinate) {
        return pCoordinate;
    }
}
