package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        // a - b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister subRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal subLeft =compiler.getDval();
        compiler.addInstruction(new SUB(subLeft,subRight));
        // a <- a - b

    }
    @Override
    protected void codeGenOP(DecacCompiler compiler){
        // a - b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister subRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal subLeft =compiler.getDval();
        compiler.addInstruction(new SUB(subLeft,subRight));
        // a <- a - b
        this.setRegistreUtil(subRight);
        compiler.setDVal(subRight);

    }
    
}
