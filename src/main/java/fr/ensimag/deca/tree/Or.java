package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;

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
    protected void codeGenBool(DecacCompiler compiler){
        AbstractExpr byPass= new Not(new And(new Not(getLeftOperand()),new Not(getRightOperand())));
        byPass.codeGenInst(compiler);
    }


}
