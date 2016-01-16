package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        this.codeGenCMPBase(compiler);
        compiler.addInstruction(new BNE(compiler.getLabel()));


    }
    
}
