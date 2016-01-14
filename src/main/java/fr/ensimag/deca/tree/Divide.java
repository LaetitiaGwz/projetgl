package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        this.getLeftOperand().codeGenInst(compiler);
        DVal left=compiler.getDval();
        this.getRightOperand().codeGenInst(compiler);
        GPRegister right=this.getRightOperand().getRegistreUtil();
        compiler.addInstruction(new DIV(left,right));

    }

}
