package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
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
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister divRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal divLeft =compiler.getDval();
        compiler.addInstruction(new QUO(divLeft,divRight));
        // a <- a/ b
        // on libère le registre de b
        compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
    }

    @Override
    protected void codeGenOP(DecacCompiler compiler){
        // a / b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister divRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal divLeft =compiler.getDval();
        compiler.addInstruction(new QUO(divLeft,divRight));
        // a <- a/ b
        compiler.setDVal(divRight);
        this.setRegistreUtil(divRight);
        compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
    }


}
