package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {

        Type floatType = new FloatType(compiler.getSymbols().create("float"));
        getOperand().setType(new IntType(compiler.getSymbols().create("int")));

        setType(floatType);
        return floatType;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        getOperand().codeGenOPLeft(compiler);
        GPRegister target = Register.getR(compiler.getRegManager().getLastregistre());
        compiler.getRegManager().setEtatRegistreTrue(compiler.getRegManager().getLastregistre());
        compiler.addInstruction(new FLOAT(getOperand().getdValue(), target));
        this.setdValue(target);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        compiler.addInstruction(new FLOAT(getOperand().getdValue(), Register.R1));
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler){
        compiler.addInstruction(new FLOAT(getOperand().getdValue(), Register.R1));
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler) {
        this.codeGenInst(compiler);
    }
}
