package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

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
        this.codeGenConv(compiler);
        compiler.addInstruction(new FLOAT(compiler.getDval(),this.getRegistreUtil()));
    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler) {
        codeGenInst(compiler);
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler) {
        codeGenInst(compiler);
    }
}
