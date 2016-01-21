package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        setType(new FloatType(compiler.getSymbols().create("float")));
        return this.getType();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        compiler.addInstruction(new WSTR("entrez un float"));
        compiler.addInstruction(new RFLOAT());
        this.setdValue(Register.R1);

    }
    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }
    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        this.codeGenInst(compiler);
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("readFloat();");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
