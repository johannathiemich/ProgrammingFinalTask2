package edu.kit.informatik.userInteraction;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.gameLogic.Game;
import edu.kit.informatik.gameLogic.ObjectNotFoundException;

/**
 * This class handles the interaction with the user.Important: Large parts of
 * this class have been taken from the class CommandLine of the given solution
 * of assignment no. 06.
 * 
 * @author Johanna Thiemich
 * @version 1.0
 */
public class UserInterface {

    /**
     * the game that the user's commands are being executed on
     */
    private Game game;

    /**
     * This method creates a new user interface
     * 
     * @param pGame
     *            the game that the user's commands are being executed on
     */
    public UserInterface(Game pGame) {
        game = pGame;
    }

    /**
     * This method starts and continues the interactive dialog with the user by
     * executing the input commands.
     */
    public void interactiveSequence() {
        String input = "";
        String[] commands = new String[0];

        /*
         * the method expects new input until the command "quit" is being
         * entered
         */
        while (!input.equals("quit")) {
            // get the new command
            input = Terminal.readLine();
            // separate input at the first whitespace occurrence
            commands = input.split("\\s", 2);
            try {
                switch (commands[0]) {
                /*
                 * before each command is being executed, the number of given
                 * parameters is being checked
                 */
                case "quit":
                    checkParameterNumber(commands.length, 1);
                    quit();
                    break;
                case "bag":
                    checkParameterNumber(commands.length, 1);
                    bag();
                    break;
                case "select":
                    checkParameterNumber(commands.length, 2);
                    select(commands[1]);
                    break;
                case "rowprint":
                    checkParameterNumber(commands.length, 2);
                    rowPrint(commands[1]);
                    break;
                case "colprint":
                    checkParameterNumber(commands.length, 2);
                    colPrint(commands[1]);
                    break;
                case "place":
                    checkParameterNumber(commands.length, 2);
                    place(commands[1]);
                    break;
                default:
                    // check: empty input?
                    if (input.trim().length() == 0) {
                        throw new IllegalInputException("Error, please enter a command");
                    } else {
                        // unknown command
                        throw new IllegalInputException("Error, only the following commands are allowed: "
                                + "quit, select, place, bag, rowprint, colprint.");
                    }
                }
                // in case of illegal input
            } catch (IllegalInputException e) {
                Terminal.printLine(e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
            }
        }
    }

    /**
     * This method exits the program.
     */
    private void quit() {
        System.exit(0);
    }

    /**
     * This method handles the "bag" command, printing all the available tokens.
     */
    private void bag() {
        game.getBoard().tokenPrint();
    }

    /**
     * This method handles the "select" command, selecting a token
     * 
     * @param pCommand
     *            a String containing an integer number greater or equal to zero
     *            representing the number of the token to be selected
     * @throws IllegalInputException
     *             if the input String is not a valid integer number or if a
     *             token has already been selected or if the wanted token is not
     *             available anymore
     */
    private void select(String pCommand) throws IllegalInputException {
        // default token number
        int token = -1;
        try {
            token = Integer.parseInt(pCommand);
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Error, " + pCommand + " is not a valid integer number.");
        }
        if (token >= 0) {
            try {
                // selecting the token
                game.select(token);
                // command successfully executed
                Terminal.printLine("OK");
            } catch (ObjectNotFoundException e) {
                // token not found
                throw new IllegalInputException(e.getMessage());
            }
        } else {
            throw new IllegalInputException("Error, the token number " + token + " does not exist.");
        }
    }

    /**
     * This method handles the "rowprint" command by printing the content of a
     * row of the board
     * 
     * @param pCommand
     *            String containing the number of the row to be printed (integer
     *            number)
     * @throws IllegalInputException
     *             if the String does not contain a valid integer number or if
     *             the board does not contain a row with the given number
     */
    private void rowPrint(String pCommand) throws IllegalInputException {
        try {
            int numberOfRow = Integer.parseInt(pCommand);
            // printing
            game.getBoard().rowPrint(numberOfRow);
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Error, " + pCommand + " is not a valid integer number.");
        }

    }

    /**
     * This method handles the "colprint" command by printing the content of a
     * column of the board
     * 
     * @param pCommand
     *            String containing the number of the column to be printed
     *            (integer number)
     * @throws IllegalInputException
     *             if the String does not contain a valid integer number or if
     *             the board does not contain a column with the given number
     */
    private void colPrint(String pCommand) throws IllegalInputException {
        try {
            int numberOfRow = Integer.parseInt(pCommand);
            // print
            game.getBoard().columnPrint(numberOfRow);
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Error, " + pCommand + " is not a valid integer number.");
        }

    }

    /**
     * This method places the token selected before on the board.
     * 
     * @param pCommand
     *            a String containing the coordinates where the token is going
     *            to be placed. It has the format: RowNumber;ColumnNumber (both
     *            integer numbers)
     * @throws IllegalInputException
     *             if the String does not contain valid integer numbers or if
     *             the coordinates are not valid.
     */
    private void place(String pCommand) throws IllegalInputException {
        String[] parameters = new String[0];
        try {
            parameters = extractArguments(pCommand, 2);
        } catch (IllegalInputException e) {
            game.resetMove();
            throw new IllegalInputException(e.getMessage());
        }
        int rowNumber = -1;
        int columnNumber = -1;
        try {
            rowNumber = Integer.parseInt(parameters[0]);
            columnNumber = Integer.parseInt(parameters[1]);
        } catch (NumberFormatException e) {
            // place command fails: selection of token has to be undone, token
            // has to be selected again
            game.resetMove();
            throw new IllegalInputException(
                    "Error, " + parameters[0] + " or " + parameters[1] + " are not valid integers.");
        }
        try {
            game.place(rowNumber, columnNumber);
        } catch (IllegalInputException e) {
            // place command undo selection of token
            game.resetMove();
            throw new IllegalInputException(e.getMessage());
        }
        if (game.getGameWon()) {
            // game has been won --> print who won
            game.printWin();
        } else if (game.getGameDraw()) {
            // draw has been achieved
            Terminal.printLine("draw");
        } else if (!game.getBoard().isBoardFull()) {
            // game has not been won and did not end by draw -->continue
            Terminal.printLine("OK");
        }
    }

    /**
     * This method checks whether the given number of parameters equals the
     * required number
     * 
     * @param pGiven
     *            number of found parameters in given String
     * @param pRequired
     *            number of parameters required for invoking a specific method
     * @throws IllegalInputException
     *             in case the number of found given parameters does not match
     *             the number of expected parameters
     */
    private void checkParameterNumber(int pGiven, int pRequired) throws IllegalInputException {
        if (pGiven != pRequired && pRequired == 2) {
            throw new IllegalInputException(
                    "Error, this command requires exactly one parameter to work, but you provided no one.");
        } else if (pGiven != pRequired && pRequired == 1) {
            throw new IllegalInputException(
                    "Error, this command does not accept any additional parameter, but you provided some.");
        }
    }

    /**
     * This method splits a given String by all semicolon occurrences and checks
     * whether the number of arguments meets the expected number.
     * 
     * @param pCommand
     *            String that is going to be split
     * @param pExpected
     *            number of parameters
     * @return found parameters, in an Array of String (each parameter is one
     *         element in the array)
     * @throws IllegalInputException
     *             if number of found parameters does not meet the expected
     *             number or if there are too many semicolon occurrences
     */
    private String[] extractArguments(String pCommand, int pExpected) throws IllegalInputException {
        // split by all semicolon occurrences
        String[] parameters = pCommand.trim().split(";");

        if (parameters.length != pExpected && pExpected >= 0 || !checkChar(pCommand, ';', pExpected - 1)) {
            throw new IllegalInputException("Error, this command requires exactly " + pExpected
                    + " semicolon-separated parameters" + " to work, but you provided " + parameters.length + ".");
        }
        return parameters;
    }

    /**
     * This method checks whether a character exists a certain number of times
     * in a given String.
     * 
     * @param pString
     *            String to be checked for character occurrences
     * @param pChar
     *            character to be searched for
     * @param pExpected
     *            expected number of character occurrences
     * @return true if number of character occurrences matches the expected
     *         number, <br>
     *         false if not
     */
    private boolean checkChar(String pString, char pChar, int pExpected) {
        int counter = 0;
        for (int i = 0; i < pString.length(); i++) {
            if (pString.charAt(i) == pChar) {
                // char found --> increase counter
                counter++;
            }
        }
        if (counter == pExpected) {
            return true;
        } else {
            return false;
        }
    }

}
