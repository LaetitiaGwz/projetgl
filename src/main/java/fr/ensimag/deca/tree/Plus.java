package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        this.getLeftOperand().codeGenInst(compiler);
        getLeftOperand().setRegistreUtilise(compiler);
        this.getRightOperand().codeGenInst(compiler);
        getRightOperand().setRegistreUtilise(compiler);
        compiler.addInstruction(new ADD(this.getRightOperand().getRegistreUtilise(),this.getLeftOperand().getRegistreUtilise()));

    }
}
