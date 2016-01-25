package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * @date 01/01/2016
 */
public abstract class Instruction {

    // On met ce booléen a true si on veut qu'une instruction
    // n'aparraisse pas dans le code (BOV par exemple)
    protected boolean doDisplay = true;

    String getName() {
        return this.getClass().getSimpleName();
    }

    abstract void displayOperands(PrintStream s);

    void display(PrintStream s) {
        if(doDisplay) {
            s.print(getName());
            displayOperands(s);
        }
    }

}
