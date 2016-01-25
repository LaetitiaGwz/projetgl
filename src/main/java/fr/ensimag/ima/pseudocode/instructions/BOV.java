package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author Ensimag
 * @date 01/01/2016
 */
public class BOV extends BranchInstruction {

    public BOV(Label op) {
        super(op);
    }

    public BOV(Label op, DecacCompiler compiler) {
        super(op);
        if(compiler.getCompilerOptions().getNoCheck())
            super.doDisplay = false;
    }

}
