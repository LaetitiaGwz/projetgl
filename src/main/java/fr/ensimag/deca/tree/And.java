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
        Label endAnd = new Label("endAnd"+compiler.getOr());
        compiler.incrementeOr();
        Label stock= compiler.getLabelFalse();
        Label stockTrue= compiler.getLabelTrue();
        compiler.setLabelFalse(endAnd);
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(stockTrue));
        compiler.addLabel(endAnd);
        compiler.addInstruction(new BRA(stock));
        compiler.setLabelFalse(stock);
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        Label endAnd = new Label("endAnd"+compiler.getOr());
        Label suiteAnd = new Label ("suiteAnd"+compiler.getOr());
        compiler.incrementeOr();
        Label stock= compiler.getLabelFalse();
        Label stockTrue= compiler.getLabelTrue();
        compiler.setLabelFalse(endAnd);
        compiler.setLabelTrue(suiteAnd);
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
        compiler.addLabel(suiteAnd);
        compiler.addInstruction(new BRA(stockTrue));
        compiler.addLabel(endAnd);
        compiler.addInstruction(new BRA(stock));
        compiler.setLabelFalse(stock);
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler){
        this.codeGenNot(compiler);
    }
}
