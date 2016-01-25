package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * String literal
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Null extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {

        Type nullType = new NullType(compiler.getSymbols().create("null"));

        setType(nullType);
        return nullType;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        //nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
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
        return "Null";
    }

}
