package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        int i=compiler.getTableRegistre().getLastregistre();
        compiler.getTableRegistre().setEtatRegistreTrue(i);
        this.setRegistreUtilise(i);
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()),Register.getR(i)));




    }

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
