package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i=compiler.getTableRegistre().getLastregistre();
        compiler.getTableRegistre().setEtatRegistreTrue(i);
        GPRegister target= Register.getR(i);
        this.setRegistreUtil(target);
        int convBool = (getValue()) ? 1 : 0; // Conversion booléen -> integer
        compiler.addInstruction(new LOAD(new ImmediateInteger(convBool), target));
    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        this.value=!this.getValue();
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        int i=compiler.getTableRegistre().getLastregistre();
        compiler.getTableRegistre().setEtatRegistreTrue(i);
        GPRegister target= Register.getR(i);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0),target));
        this.codeGenInst(compiler);
        compiler.addInstruction(new CMP(this.getRegistreUtil(),target));
        compiler.addInstruction(new BEQ(compiler.getLabel()));
        compiler.getTableRegistre().setEtatRegistreFalse(compiler.getTableRegistre().getLastregistre()-1);
        compiler.getTableRegistre().setEtatRegistreFalse(i); // on libère les deux
    }
}
