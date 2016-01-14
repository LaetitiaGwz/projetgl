package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister mulRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal mulLeft =compiler.getDval();
        compiler.addInstruction(new MUL(mulLeft,mulRight));
        // a <- a * b
    }

    @Override
    protected void codeGenOP(DecacCompiler compiler){
        // a * b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister mulRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal mulLeft =compiler.getDval();
        compiler.addInstruction(new MUL(mulLeft,mulRight));
        // a <- a * b
        this.setRegistreUtil(mulRight);
        compiler.setDVal(mulRight);
    }



}
