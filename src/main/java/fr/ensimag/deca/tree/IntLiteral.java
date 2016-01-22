package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl41
 * @date 01/01/2016
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        setType(new IntType(compiler.getSymbols().create("int")));
        return this.getType();
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){

        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()),Register.R1)); // pour sortir une valeur: on est obligé de passer par R1
        compiler.addInstruction(new WINT());
    }

    /*
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        int i=compiler.getRegManager().getLastregistre();
        compiler.getRegManager().setEtatRegistreTrue(i);
        GPRegister target= Register.getR(i);
        this.setdValue(target);
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()),target));
    }
    */

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()), register));
    }

    @Override
    public DVal getDval() {
        return new ImmediateInteger(this.getValue());
    }

    /*
    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.setdValue(new ImmediateInteger(this.getValue()));
    }
    */

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
