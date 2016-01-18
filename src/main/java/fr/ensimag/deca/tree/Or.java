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
        Label endOr = new Label("endOr"+ compiler.getOr());
        compiler.incrementeOr();
        getLeftOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(endOr));
        getRightOperand().codeGenCMP(compiler);
        compiler.addLabel(endOr);

        //AbstractExpr byPass= new Not(new And(new Not(getLeftOperand()),new Not(getRightOperand())));
    }

}
