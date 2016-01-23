package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class This extends AbstractThis {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if(currentClass == null) {
            throw new ContextualError("Cannot use 'this' in main.", getLocation());
        }
        this.setType(currentClass.getType());
        return currentClass.getType();
    }

    @Override
    protected void codeGenThis(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),Register.getR(2)));
    }
    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        compiler.addInstruction(new LOAD(Register.getR(2),register));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){

    }

    @Override
    public void decompile(IndentPrintStream s) {
    s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // Leaf node
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // Leaf node
    }
}
