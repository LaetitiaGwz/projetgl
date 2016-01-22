package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        setType(new BooleanType(compiler.getSymbols().create("boolean")));
        return this.getType();

    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    /*
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i=compiler.getRegManager().getLastregistre();
        compiler.getRegManager().setEtatRegistreTrue(i);
        GPRegister target= Register.getR(i);
        this.setdValue(target);
        int convBool = (getValue()) ? 1 : 0; // Conversion booléen -> integer
        compiler.addInstruction(new LOAD(new ImmediateInteger(convBool), target));
    }
    */

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {
        int convBool = (getValue()) ? 1 : 0; // Conversion booléen -> integer
        compiler.addInstruction(new LOAD(new ImmediateInteger(convBool), register));
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        this.value = !this.getValue();
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        if(!getValue())
            compiler.addInstruction(new BRA(compiler.getLblManager().getLabelFalse()));
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler){
        this.codeGenNot(compiler);
        this.codeGenCMP(compiler);
    }
}
