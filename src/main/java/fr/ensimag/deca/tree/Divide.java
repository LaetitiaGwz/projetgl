package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void mnemoOp(DecacCompiler compiler, DVal left, GPRegister right) {
         if(this.getType().isFloat()) {
            // Instruction DIV pour les flottants
            compiler.addInstruction(new DIV(left, right));
        }else{
            // Instruction QUO pour les entiers
            compiler.addInstruction(new QUO(left, right));
        }
        //compiler.addInstruction(new BOV(new Label("arith_overflow"), compiler));
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
