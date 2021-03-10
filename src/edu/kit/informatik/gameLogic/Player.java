package edu.kit.informatik.gameLogic;

/**
 * This class represents a player participating in the game.
 * Only package visibility has been chosen because of principles of capsuling and the secret principle.
 * This class is not needed in another package other than the one it is in right now.
 * @author Johanna Thiemich
 * @version 1.0
 *
 */
class Player {

    /**
     * the number characterising this player, this number is explicit and does
     * not change
     */
    private final int number;

    /**
     * the status of this player in the course of the game (either selecting,
     * placing, winning or default
     */
    private Status status;

    /**
     * This method creates a new player with a specific number and status
     * 
     * @param pNumber
     *            the number of this player (integer number between 1 and
     *            Integer.MAX_VALUE)
     * @param pStatus
     *            current status in the course of the game of this player
     *            (placing, selecting, winning or default)
     */
    public Player(int pNumber, Status pStatus) {
        number = pNumber;
        status = pStatus;
    }

    /**
     * This method returns the current status of this player
     * 
     * @return the status of this player
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * This method returns the number of this player.
     * @return the number of this player
     */
    public int getNumber() {
        return number;
    }

    /**
     * This method changes the status of this player. The status of all the
     * players changes in every round of the game (between selecting and
     * placing)
     * 
     * @param pStatus the new status of this player
     */
    public void setStatus(Status pStatus) {
        status = pStatus;
    }

}
