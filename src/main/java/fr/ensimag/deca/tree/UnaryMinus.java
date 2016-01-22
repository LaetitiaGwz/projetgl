package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!type.isFloat() && !type.isInt()) {
            throw new ContextualError("Must apply unary minus to an int or float. We have : " + type.getName(), getLocation());
        }

        setType(type);
        return type;
    }

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {
        getOperand().codegenExpr(compiler, register);
        compiler.addInstruction(new OPP(register,register));
    }

    @Override
    public DVal getDval() {
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        GPRegister reg = compiler.getRegManager().getGBRegister();
        getOperand().codegenExpr(compiler, reg);
        compiler.addInstruction(new OPP(reg, Register.R1));
        if(this.getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        compiler.getRegManager().resetTableRegistre();
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler) {
        Validate.isTrue(getType().isFloat());
        GPRegister reg = compiler.getRegManager().getGBRegister();
        getOperand().codegenExpr(compiler, reg);
        compiler.addInstruction(new OPP(reg, Register.R1));
        compiler.addInstruction(new WFLOATX());
        compiler.getRegManager().resetTableRegistre();
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
