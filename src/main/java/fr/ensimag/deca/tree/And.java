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
        Label endAnd = new Label("endAnd"+compiler.getLblManager().getOr());
        compiler.getLblManager().incrementOr();
        Label stock = compiler.getLblManager().getLabelFalse();
        Label stockTrue= compiler.getLblManager().getLabelTrue();
        compiler.getLblManager().setLabelFalse(endAnd);
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(stockTrue));
        compiler.addLabel(endAnd);
        compiler.addInstruction(new BRA(stock));
        compiler.getLblManager().setLabelFalse(stock);
    }
}
