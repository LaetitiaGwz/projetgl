package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.SymbolTable;
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
        GPRegister mulRight= (GPRegister) this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal mulLeft = this.getRightOperand().getRegistreUtil();
        compiler.addInstruction(new MUL(mulLeft,mulRight));
        if(this.getType().isFloat())
            compiler.addInstruction(new BOV(new Label("overflow_error")));
        // a <- a * b
        //on libère le registre de b
        this.setRegistreUtil(mulRight);
   }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.codeGenInst(compiler);
        if(getRightOperand().getUtilisation()){
            compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
        }
    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }




}
