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
    protected void codeGenInst(DecacCompiler compiler){
        getOperand().codeGenOPRight(compiler);
        GPRegister unRight= Register.getR(compiler.getTableRegistre().getLastregistre());
        if(getType().isInt())
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), unRight));
        else
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), unRight));
        compiler.getTableRegistre().setEtatRegistreTrue(compiler.getTableRegistre().getLastregistre());
        compiler.addInstruction(new SUB(getOperand().getdValue(), unRight));


        this.setdValue(unRight);
        this.setUtilisation();
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        getOperand().codeGenInst(compiler);
        if(this.getType().isInt()){
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R1));
            compiler.addInstruction(new SUB(getOperand().getdValue(), Register.R1));
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()){
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R1));
            compiler.addInstruction(new SUB(getOperand().getdValue(), Register.R1));
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler) {
        Validate.isTrue(getType().isFloat());
        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R1));
        compiler.addInstruction(new SUB(getOperand().getdValue(), Register.R1));
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
