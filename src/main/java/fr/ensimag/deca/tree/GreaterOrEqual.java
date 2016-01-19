package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 * Operator "x >= y"
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenCMPOP(DecacCompiler compiler){
        compiler.addInstruction(new BLT(compiler.getLblManager().getLabelFalse()));
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        compiler.addInstruction(new BGE(compiler.getLblManager().getLabelFalse()));
    }

}
