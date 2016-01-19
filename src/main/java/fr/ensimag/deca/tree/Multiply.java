package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        // a * b
        this.getLeftOperand().codeGenOPLeft(compiler);
        GPRegister mulRight= (GPRegister) this.getLeftOperand().getdValue();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal mulLeft = this.getRightOperand().getdValue();
        compiler.addInstruction(new MUL(mulLeft,mulRight));
        if(this.getType().isFloat())
            compiler.addInstruction(new BOV(new Label("overflow_error")));
        // a <- a * b
        //on libère le registre de b
        this.setdValue(mulRight);
   }

}
