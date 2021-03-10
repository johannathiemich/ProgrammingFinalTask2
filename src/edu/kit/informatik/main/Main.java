package edu.kit.informatik.main;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.gameLogic.Board;
import edu.kit.informatik.gameLogic.Game;
import edu.kit.informatik.gameLogic.Standard;
import edu.kit.informatik.gameLogic.Torus;
import edu.kit.informatik.userInteraction.IllegalInputException;
import edu.kit.informatik.userInteraction.UserInterface;

/**
 * This class creates the whole game and, starts and runs it. Therefore, this
 * class contains the main method.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 *
 */
public final class Main {

    private Main() {
        // intentionally left blank to avoid instantiation
    }

    /**
     * This method creates a new user interface and starts the interactive
     * dialog with the user
     * 
     * @param args
     *            containing information about which type of board is used
     *            (either standard or torus)
     */
    public static void main(String[] args) {
        Board board;
        // checking command line parameter
        if (args.length == 1) {
            try {
                // determining game mode
                if (args[0].equals("standard")) {
                    board = new Standard(16, 4, 6, 6);
                    UserInterface userInterface = new UserInterface(new Game(2, board));
                    // running game
                    userInterface.interactiveSequence();
                } else if (args[0].equals("torus")) {
                    board = new Torus(16, 4, 6, 6);
                    UserInterface userInterface = new UserInterface(new Game(2, board));
                    // running game
                    userInterface.interactiveSequence();
                } else {
                    // not the right parameter
                    Terminal.printLine("Error, illegal board type has been entered. Please choose standard or torus.");
                    System.exit(1);
                }
            } catch (IllegalInputException e) {
                // this should not happen, since parameters are not variable in
                // this case and are chosen in the right way
                Terminal.printLine(e.getMessage());
            }
        } else {
            Terminal.printLine("Error, illegal number of arguments in commandline. "
                    + "Exactly one parameter is needed (standard or torus).");
            System.exit(1);
        }
    }

}
