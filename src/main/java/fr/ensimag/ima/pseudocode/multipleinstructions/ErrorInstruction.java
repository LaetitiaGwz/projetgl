package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * Created by matthieu on 12/01/16.
 * List of two instructions for generating an error during execution :
 *  - display an error message
 *  - stops the program
 */
public class ErrorInstruction extends InstructionList {
    public ErrorInstruction(String errorMsg) {
        addInstruction(new WSTR(errorMsg));
        addInstruction(new ERROR());
    }
}
