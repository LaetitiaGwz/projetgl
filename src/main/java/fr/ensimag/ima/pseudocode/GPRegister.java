package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.RegisterManager;
import org.apache.commons.lang.Validate;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 * 
 * @author Ensimag
 * @date 01/01/2016
 */
public class GPRegister extends Register {
    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private int number;

    GPRegister(String name, int number) {
        super(name);
        this.number = number;
    }

    /**
     *
     * @param regManager the register manager
     * @return true if the curent register is the last register (ie  there is no
     * more free register)
     */
    public boolean isLastRegister(RegisterManager regManager){
        return this.getNumber() == regManager.getTailleTable()-1;
    }

    public GPRegister next(){
        Validate.isTrue(getNumber() < 16);
        return getR(getNumber() + 1);
    }

}
