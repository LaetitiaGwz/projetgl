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
        // a / b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister divRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal divLeft =compiler.getDval();
        compiler.addInstruction(new DIV(divLeft,divRight));
        // a <- a/ b
    }

}
