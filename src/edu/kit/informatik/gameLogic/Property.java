package edu.kit.informatik.gameLogic;

/**
 * This enum represents the different properties a token can have.
 * Only package visibility has been chosen because of principles of capsuling and the secret principle.
 * This class is not needed in another package other than the one it is in right now.
 * @author Johanna Thiemich
 * @version 1.0
 *
 */
enum Property {
    
    /**
     * token has black color
     */
    BLACK,
    
    /**
     * token has white color
     */
    WHITE,
    
    /**
     * token is square
     */
    SQUARE,
    
    /**
     * token is cylindrically
     */
    CYLINDRICALLY,
    
    /**
     * token is small
     */
    SMALL,
    
    /**
     * token is big
     */
    BIG,
    
    /**
     * token is solid
     */
    SOLID,
    
    /**
     * token is hollow
     */
    HOLLOW;
    

}
