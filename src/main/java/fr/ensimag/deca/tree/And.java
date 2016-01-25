package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        Label endAnd = new Label("endAnd"+compiler.getLblManager().getOr());
        compiler.getLblManager().incrementOr();
        Label stock = compiler.getLblManager().getLabelFalse();
        Label stockTrue= compiler.getLblManager().getLabelTrue();
        compiler.getLblManager().setLabelFalse(endAnd);
        getLeftOperand().codeGenCMP(compiler);
        getRightOperand().codeGenCMP(compiler);
        compiler.addInstruction(new BRA(stockTrue));
        compiler.addLabel(endAnd);
        compiler.addInstruction(new BRA(stock));
        compiler.getLblManager().setLabelFalse(stock);
    }

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre

        GPRegister stock;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new TSTO(1));
            compiler.addInstruction(new BOV(new Label("stack_overflow")));
            compiler.addInstruction(new PUSH(Register.getR(i)));
            stock = Register.getR(i);
            setPush();
        }
        else{
            stock = compiler.getRegManager().getGBRegister();

        }
        getLeftOperand().codegenExpr(compiler,register);
        getRightOperand().codegenExpr(compiler,stock);
        compiler.addInstruction(new MUL(stock,register));
        if(getPop()){
            compiler.addInstruction(new POP(stock));
            popDone();
        }
        compiler.getRegManager().setTableRegistre(table);
    }
}
