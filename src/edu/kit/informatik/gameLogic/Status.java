package edu.kit.informatik.gameLogic;

/**
 * This enum represents the different states a player can be in (with regard to
 * the game he is participating in).
 * 
 * Only package visibility has been chosen because of principles of capsuling and the secret principle.
 * This class is not needed in another package other than the one it is in right now.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 *
 */
enum Status {
    
    /**
     * it is this player's turn to select the token now
     */
    SELECTING,

    /**
     * it is this player's turn to place the token now
     */
    PLACING,

    /**
     * this player won the game
     */
    WINNING,

    /**
     * default status; the player is in no particular status right now (not
     * participating in the game)
     */
    DEFAULT;

}
