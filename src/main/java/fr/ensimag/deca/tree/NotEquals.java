package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.BEQ;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        this.codeGenCMP(compiler);
        compiler.addInstruction(new BEQ(compiler.getLabel()));

    }

}
