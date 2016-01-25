package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl41
 * @date 01/01/2016
 */
public class NoInitialization extends AbstractInitialization {

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        //Nothing to do
    }

    @Override
    protected void codePreGenInit(DecacCompiler compiler) {
        // Nothing to do
    }
    @Override
    protected void codeGenInit(DecacCompiler compiler) {
        // Nothing to do
    }
    @Override
    protected  void codeGenInitFieldFloat(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));


    }
    @Override
    protected void codeGenInitFieldInt(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
    }

    @Override
    protected AbstractExpr getExpression(){
        return null;
    }
    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println(";");
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
