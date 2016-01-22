package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void fetchCond(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new SLE(register));
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    protected void codeGenCMPOP(DecacCompiler compiler){
        compiler.addInstruction(new BGE(compiler.getLblManager().getLabelFalse()));
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        compiler.addInstruction(new BLT(compiler.getLblManager().getLabelFalse()));
    }


}
