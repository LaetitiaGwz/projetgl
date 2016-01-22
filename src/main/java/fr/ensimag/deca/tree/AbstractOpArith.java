package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
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

    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        getLeftOperand().codegenExpr(compiler, register);
        if(getRightOperand().getDval() != null){
            mnemoOp(compiler, getRightOperand().getDval(), register);
        }else if(register.isLastRegister(compiler.getRegManager())){
            compiler.addInstruction(new PUSH(register));
            getRightOperand().codegenExpr(compiler, register);
            compiler.addInstruction(new LOAD(register, Register.R0));
            compiler.addInstruction(new POP(register));
            mnemoOp(compiler, Register.R0, register);
        }else {
            GPRegister nextFreeReg = register.next();
            getRightOperand().codegenExpr(compiler, nextFreeReg);
            mnemoOp(compiler, nextFreeReg, register);
        }
    }

    protected abstract void mnemoOp(DecacCompiler compiler, DVal left,GPRegister right);

    @Override
    public DVal getDval() {
        return null;
    }
}
