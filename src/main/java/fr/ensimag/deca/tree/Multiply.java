package fr.ensimag.deca.tree;


import com.sun.tools.javac.resources.compiler;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void mnemoOp(DecacCompiler compiler, DVal left, GPRegister right) {
        compiler.addInstruction(new MUL(left,right));
        //if(this.getType().isFloat())
          //  compiler.addInstruction(new BOV(new Label("arith_overflow"), compiler));
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }


}
