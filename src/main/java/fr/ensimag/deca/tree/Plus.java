package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
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
        // a + b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister addRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal addLeft =compiler.getDval();
        compiler.addInstruction(new ADD(addLeft,addRight));
        // a <- a + b
        // on libère le registre de b
        compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
    }

    @Override
    protected void codeGenOP(DecacCompiler compiler){
        // a + b
        this.getLeftOperand().codeGenOP(compiler);
        GPRegister addRight= this.getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOP(compiler);
        DVal addLeft =compiler.getDval();
        compiler.addInstruction(new ADD(addLeft,addRight));
        // a <- a + b
        compiler.setDVal(addRight);
        this.setRegistreUtil(addRight);
        compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
    }
}
