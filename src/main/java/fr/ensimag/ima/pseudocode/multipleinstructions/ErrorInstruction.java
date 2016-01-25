package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 *  Liste de deux instructions pour générer une erreur à l'éxécution :
 *  - Affiche le message d'erreur
 *  - Arrete le programme
 */
public class ErrorInstruction extends InstructionList {
    public ErrorInstruction(String errorMsg) {
        addInstruction(new WSTR(errorMsg));
        addInstruction(new ERROR());
    }
}
