package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!type.isBoolean()) {
            throw new ContextualError("Must apply 'not' operator to a boolean. We have : " + type.getName(), getLocation());
        }

        setType(type);
        return type;
    }

    protected void codePreGenCMPOP(DecacCompiler compiler){
        getOperand().codePreGenExpr(compiler);
    }

    protected void codePreGenExpr(DecacCompiler compiler){
        getOperand().codePreGenExpr(compiler);
    }
    @Override
    protected void codeGenCMPOP(DecacCompiler compiler){
        getOperand().codegenExpr(compiler, Register.R0);
        compiler.addInstruction(new CMP(0,Register.R0));
        compiler.addInstruction(new BNE(compiler.getLblManager().getLabelFalse()));
    }

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {
        getOperand().codegenExpr(compiler, register);
        compiler.addInstruction(new CMP(0, register));
        compiler.addInstruction(new SEQ(register));
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        Label stock =compiler.getLblManager().getLabelFalse();
        compiler.getLblManager().setLabelFalse(compiler.getLblManager().getLabelTrue());
        compiler.getLblManager().setLabelTrue(stock);
        getOperand().codeGenCMP(compiler);
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
