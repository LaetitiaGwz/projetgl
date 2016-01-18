package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.QUO;

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
        this.getLeftOperand().codeGenOPLeft(compiler);
        GPRegister divRight= (GPRegister) this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal divLeft = this.getRightOperand().getRegistreUtil();
        if(this.getType().isFloat()) {
            // Instruction DIV pour les flottants
            compiler.addInstruction(new DIV(divLeft,divRight));
        }else{
            // Instruction QUO pour les entiers
            compiler.addInstruction(new QUO(divLeft, divRight));
        }
        compiler.addInstruction(new BOV(new Label("overflow_error")));
        // a <- a/b
        this.setRegistreUtil(divRight);
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
