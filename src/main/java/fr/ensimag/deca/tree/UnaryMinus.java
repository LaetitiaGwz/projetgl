package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

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
        compiler.addInstruction(new LOAD(new ImmediateInteger(0),unRight));
        compiler.getTableRegistre().setEtatRegistreTrue(compiler.getTableRegistre().getLastregistre());
        compiler.addInstruction(new SUB(compiler.getDval(),unRight));
        this.setRegistreUtil(unRight);
        this.setUtilisation();
        compiler.setDVal(unRight);
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
        this.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(compiler.getDval(),Register.R1));
        if(this.getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if(this.getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
