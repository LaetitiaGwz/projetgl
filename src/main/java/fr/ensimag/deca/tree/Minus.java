package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void mnemoOp(DecacCompiler compiler, DVal left, GPRegister right) {
       compiler.addInstruction(new SUB(left, right));
        if(this.getType().isFloat())
            compiler.addInstruction(new BOV(new Label("overflow_error"), compiler));
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }


}
