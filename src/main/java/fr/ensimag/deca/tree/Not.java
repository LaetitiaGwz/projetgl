package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

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
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new TSTO(1));
            compiler.addInstruction(new BOV(new Label("stack_overflow")));
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        getOperand().codegenExpr(compiler,register);
        compiler.addInstruction(new CMP(1,register));
        compiler.addInstruction(new BEQ(compiler.getLblManager().getLabelTrue()));
        compiler.addInstruction(new BRA(compiler.getLblManager().getLabelFalse()));
        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
        }
        compiler.getRegManager().setTableRegistre(table);
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
