package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
        this.getLeftOperand().codeGenOPLeft(compiler);
        GPRegister addRight= (GPRegister) this.getLeftOperand().getdValue();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal addLeft = this.getRightOperand().getdValue();
        compiler.addInstruction(new ADD(addLeft, addRight));
        // Vérification des overflow pour les flottants
        if(this.getType().isFloat())
            compiler.addInstruction(new BOV(new Label("overflow_error")));
        // a <- a + b
        this.setdValue(addRight);
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
