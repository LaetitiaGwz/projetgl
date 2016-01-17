package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        compiler.addInstruction(new BLE(compiler.getLabel()));
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        compiler.addInstruction(new BGT(compiler.getLabel()));
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, Label label) {
        compiler.addInstruction(new BGT(label));
    }
}
