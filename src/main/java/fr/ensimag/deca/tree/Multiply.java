package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
        GPRegister mulRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal mulLeft =compiler.getDval();
        compiler.addInstruction(new MUL(mulLeft,mulRight));
        // a <- a * b
        //on libère le registre de b
        this.setRegistreUtil(mulRight);
        compiler.setDVal(mulRight);
   }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.codeGenInst(compiler);
        if(this.getUtilisation()){
            compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
        }
    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        this.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(this.getRegistreUtil(),Register.R1));
        if(this.getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()){
            compiler.addInstruction(new LOAD(this.getRegistreUtil(),Register.R1));
        }


    }


}
