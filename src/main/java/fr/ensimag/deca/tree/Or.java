package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        Label suiteOr = new Label("suiteOr"+compiler.getLblManager().getOr());
        Label finOr = new Label("finOr"+compiler.getLblManager().getOr());
        Label elseOr = new Label ("elseOr"+compiler.getLblManager().getOr());
        compiler.getLblManager().incrementOr();
        Label stock = compiler.getLblManager().getLabelFalse();
        Label stockTrue=compiler.getLblManager().getLabelTrue();
        compiler.getLblManager().setLabelFalse(suiteOr); // on test premier si faux, on test deuxième
        getLeftOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(finOr));
        compiler.addLabel(suiteOr);
        compiler.getLblManager().setLabelTrue(finOr);
        compiler.getLblManager().setLabelFalse(elseOr);
         //on test deuxième, si faux, on quitte le or
        getRightOperand().codeGenCMP(compiler);
        compiler.addLabel(elseOr);
        compiler.addInstruction(new BRA(stock));
        compiler.addLabel(finOr);
        compiler.addInstruction(new BRA(stockTrue));
        compiler.getLblManager().setLabelFalse(stock);
    }

}
