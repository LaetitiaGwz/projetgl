package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        Label endAnd = new Label("endAnd"+compiler.getOr());
        compiler.incrementeOr();
        Label stock= compiler.getLabel();
        compiler.setLabel(endAnd);
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(stock));
        compiler.addLabel(endAnd);
        compiler.setLabel(stock);
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler){
        this.codeGenNot(compiler);
    }
}
