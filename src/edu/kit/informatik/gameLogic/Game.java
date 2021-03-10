package edu.kit.informatik.gameLogic;

import java.util.ArrayList;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.userInteraction.IllegalInputException;

/**
 * This class coordinates the course of the game, e.g. determines whose turn it
 * is to select/place a token and checking whether someone already won. The game
 * that is handled by this class has the following rules: Usually two players
 * (player 1 and player 2) select and place tokens in alternating order. So
 * player 1 starts by selecting a token that player 2 has to place on the board.
 * The player that places a token in this way that 4 tokens which have at least
 * one attribute in common are next to each other in a row, column or diagonal,
 * wins. If no tokens are left but no player won, a draw is achieved.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
public class Game {

    /**
     * the number of moves that have already been made in the course of the game
     */
    private int numberOfMoves;

    /**
     * the board that the game is being executed on
     */
    private Board board;

    /**
     * the players taking part in the game
     */
    private ArrayList<Player> players;

    /**
     * the player who won the game
     */
    private Player winningPlayer;

    /**
     * saves whether the game has already been won
     */
    private boolean gameWon;

    /**
     * saves whether a draw has been achieved in the game
     */
    private boolean gameDraw;

    /**
     * This method creates a new game object.
     * 
     * @param pNumberOfPlayers
     *            number of players who will take part in the game
     * @param pBoard
     *            the board that is being played on
     * @throws IllegalInputException
     *             if the board is null or the number of players is smaller than
     *             one.
     */
    public Game(int pNumberOfPlayers, Board pBoard) throws IllegalInputException {
        if (pBoard != null) {
            board = pBoard;
        } else {
            throw new IllegalInputException("Error, the board must not be null.");
        }
        players = new ArrayList<Player>();
        // at least one player has to participate
        if (pNumberOfPlayers < 0) {
            throw new IllegalInputException("Error, number of players has to be greater than zero.");
        } else {
            // initializing the players
            for (int i = 1; i <= pNumberOfPlayers; i++) {
                if (i == 1) {
                    // first player is the selecting player at first
                    players.add(new Player(i, Status.SELECTING));
                } else if (i == 2) {
                    // second player is the placing player at first
                    players.add(new Player(i, Status.PLACING));
                } else {
                    /*
                     * originally, the game was created for two players --> no
                     * specifications for more than two players, they are set to
                     * default
                     */
                    players.add(new Player(i, Status.DEFAULT));
                }
            }
        }
        winningPlayer = null;
        // first move is move number 0
        numberOfMoves = 0;
        gameWon = false;
        gameDraw = false;

    }

    /**
     * This method selects a token with a specific number. Selecting means:
     * Taking it out of the (virtual) bag, so that it can be placed on the board
     * by the other player in the next step.
     * 
     * @param pNumberOfToken
     *            the number of the token that is to be selected
     * @throws IllegalInputException
     *             if the game is already over, then no tokens can be selected
     *             anymore
     * @throws ObjectNotFoundException
     *             if the chosen token is not available
     */
    public void select(int pNumberOfToken) throws IllegalInputException, ObjectNotFoundException {
        // making sure the game is not over yet
        if (!gameWon && !gameDraw) {
            Token selectedToken = null;
            for (int i = 0; i < board.getTokens().size(); i++) {
                // finding the token with the specified number
                if (board.getTokens().get(i).equals(new Token(pNumberOfToken, board.getNumberOfTokenProperties()))) {
                    selectedToken = board.getTokens().get(i);
                    break;
                }
            }
            // selecting the found token
            board.select(selectedToken);
        } else {
            throw new IllegalInputException("Error, the game is already over.");
        }
    }

    /**
     * This method resets the last step (which was selecting a token). This is
     * needed in case the place command goes wrong, then a token has to be
     * selected again by the same player.
     */
    public void resetMove() {
        // adding selected token to available tokens again
        if (board.getSelectedToken() != null) {
            board.getTokens().add(board.getSelectedToken());
            board.setSelectedToken(null);
        }
    }

    /**
     * This method places a token on the board and changes the tasks of the
     * players (placing and selecting a token).
     * 
     * @param pRow
     *            the x-Coordinate where the token has to be placed (number of
     *            the row)
     * @param pColumn
     *            the y-Coordinate where the token has to be placed (number of
     *            the column)
     * @throws IllegalInputException
     *             if the game is already over (tokens can only be placed if
     *             game is not over yet) or if the given coordinates (pRow,
     *             pColumn) are invalid.
     */
    public void place(int pRow, int pColumn) throws IllegalInputException {
        //game must not be over yet
        if (!gameWon && !gameDraw) {
            board.place(pRow, pColumn);
            /*
             * every time a token has been placed: check whether a win has been
             * achieved, starting at the field where the last token has been
             * placed at
             */
            if (board.checkWin(pRow, pColumn)) {
                // game has been won by this move
                gameWon = true;
                winningPlayer = getPlacingPlayer();
                winningPlayer.setStatus(Status.WINNING);
                return;
            } else if (!board.checkWin(pRow, pColumn) && (board.isBoardFull() || !board.areTokensLeft())) {
                // draw has been achieved
                gameDraw = true;
                return;
            }
        } else {
            throw new IllegalInputException("Error, the game is already over.");
        }
        // changing the order of the players
        Player helpPlacingPlayer = getPlacingPlayer();
        getSelectingPlayer().setStatus(Status.PLACING);
        helpPlacingPlayer.setStatus(Status.SELECTING);
        // increasing number of moves
        numberOfMoves++;
    }

    /**
     * This method returns the player that is placing the token next.
     * 
     * @return the player that has to place the token next
     */
    public Player getPlacingPlayer() {
        for (Player player : players) {
            // finding the placing player
            if (player.getStatus() == Status.PLACING) {
                return player;
            }
        }
        // no placing player found
        return null;
    }

    /**
     * This method returns the player that is selecting the token next.
     * 
     * @return the player that has to select the token next.
     */
    public Player getSelectingPlayer() {
        for (Player player : players) {
            // finding the selecting player
            if (player.getStatus() == Status.SELECTING) {
                return player;
            }
        }
        // selecting player not found
        return null;
    }

    /**
     * This method prints which player won in which round, in this format: <br>
     * P + numberOfPlayer + wins <br>
     * winningRound
     * 
     * @throws IllegalInputException
     *             the game has not been won by one player yet
     */
    public void printWin() throws IllegalInputException {
        if (gameWon) {
            Terminal.printLine("P" + getWinningPlayer().getNumber() + " wins");
            Terminal.printLine((new Integer(numberOfMoves)).toString());
        } else {
            throw new IllegalInputException("Error, the game has not been won by one player yet.");
        }
    }

    /**
     * This method returns the board that is being played on.
     * 
     * @return the board that is being played on
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method returns the player that won the game.
     * 
     * @return the winning player
     */
    public Player getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * This method returns whether the game has already been won
     * 
     * @return true if the game has already been won, false if not
     */
    public boolean getGameWon() {
        return gameWon;
    }

    /**
     * This method whether the game has ended by draw.
     * 
     * @return true if the game has ended by draw, false if not
     */
    public boolean getGameDraw() {
        return gameDraw;
    }

}
