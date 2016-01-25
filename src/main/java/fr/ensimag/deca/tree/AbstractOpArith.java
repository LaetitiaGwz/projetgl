package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        GPRegister reg = compiler.getRegManager().getGBRegister();
        this.codegenExpr(compiler, reg);
        compiler.addInstruction(new LOAD(reg, Register.R1));
        if(this.getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        compiler.getRegManager().resetTableRegistre();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        Type opType;

        if(rightType.sameType(leftType)) {
            opType = rightType;
        }
        else if(rightType.isFloat() && leftType.isInt()) {
            // Conversion du leftoperand
            setLeftOperand(new ConvFloat(getLeftOperand()));
            opType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(rightType.isInt() && leftType.isFloat()) {
            // Conversion du rightoperand
            setRightOperand(new ConvFloat(getRightOperand()));
            opType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else {
            throw new ContextualError("Arithmetic operation on expressions which types are differents. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
        }

        setType(opType);
        return opType;
    }

    protected void codePreGenExp(DecacCompiler compiler){
        boolean[] table = compiler.getFakeRegManager().getTableRegistre(); //on verifie les registre

        getLeftOperand().codePreGenExpr(compiler);
        if(getRightOperand().getDval()==null){
            compiler.getFakeRegManager().getGBRegister();
            compiler.addMaxFakeRegister(compiler.getFakeRegManager().getLastregistre());
            getRightOperand().codePreGenExpr(compiler);
        }
        compiler.getFakeRegManager().setTableRegistre(table);

    }
    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        getLeftOperand().codegenExpr(compiler, register);
        if(getRightOperand().getDval() != null){
            mnemoOp(compiler, getRightOperand().getDval(), register);
        }else {
            GPRegister stock;
            if(compiler.getRegManager().noFreeRegister()){
                int i =compiler.getRegManager().getGBRegisterInt(register.getNumber());
                compiler.addInstruction(new TSTO(1));
                compiler.addInstruction(new BOV(new Label("stack_overflow")));
                compiler.addInstruction(new PUSH(Register.getR(i)));
                stock = Register.getR(i);
                setPush();
            }
            else {
                stock = compiler.getRegManager().getGBRegister();
            }
            getRightOperand().codegenExpr(compiler, stock);
            mnemoOp(compiler, stock, register);
            if(getPop()){
                compiler.addInstruction(new POP(stock));
                popDone();
            }
        }
        compiler.getRegManager().setTableRegistre(table);
    }

    protected abstract void mnemoOp(DecacCompiler compiler, DVal left,GPRegister right);

    @Override
    public DVal getDval() {
        return null;
    }
}
