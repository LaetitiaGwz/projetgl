package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Cast extends AbstractCast {

    AbstractIdentifier type;
    AbstractExpr expr;

    public Cast (AbstractIdentifier type, AbstractExpr expr) {
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type castType = type.verifyType(compiler);
        Type opType = expr.verifyExpr(compiler, localEnv, currentClass);

        if(!castCompatible(localEnv, opType, castType)) {
            throw new ContextualError("Incompatible cast from " + opType + " to " + castType, getLocation());
        }

        setType(castType);
        return castType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.println(")");


    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        expr.codeGenInst(compiler); // Calcul de l'expression
        // Récupération de l'expression calculée
        GPRegister target = Register.getR(compiler.getTableRegistre().getLastregistre());
        compiler.getTableRegistre().setEtatRegistreTrue(compiler.getTableRegistre().getLastregistre());
        // Cast de l'expression
        if(this.getType().isFloat())
            compiler.addInstruction(new FLOAT(expr.getdValue(),target));
        else
            compiler.addInstruction(new INT(expr.getdValue(),target));
        this.setdValue(target);

    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler) {
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler) {
        this.codeGenInst(compiler);
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        expr.codeGenInst(compiler);
        if(this.getType().isFloat()) {
            compiler.addInstruction(new FLOAT(expr.getdValue(), Register.R1));
            compiler.addInstruction(new WFLOAT());
        }else {
            compiler.addInstruction(new INT(expr.getdValue(), Register.R1));
            compiler.addInstruction(new WINT());
        }
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler){
        Validate.isTrue(this.getType().isFloat());
        expr.codeGenInst(compiler);
        compiler.addInstruction(new FLOAT(expr.getdValue(), Register.R1));
        compiler.addInstruction(new WFLOATX());
    }
}
