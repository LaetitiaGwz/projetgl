package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class New extends AbstractNew {

    AbstractIdentifier className;

    public New(AbstractIdentifier className) {
        Validate.notNull(className);

        this.className = className;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = className.verifyClass(compiler);
        setType(t);
        return t;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = compiler.getRegManager().getLastregistre();
        GPRegister reg = Register.getR(i);
        compiler.getRegManager().setEtatRegistreTrue(i);
        this.setdValue(reg);
        compiler.addInstruction(new NEW(className.getClassDefinition().getNumberOfFields()+1,reg));
        compiler.addInstruction(new BOV(new Label("tas_plein")));
        compiler.addInstruction(new LEA(className.getClassDefinition().getOperand(),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,reg)));
        compiler.addInstruction(new PUSH(reg));
        compiler.addInstruction(new BSR(new Label("init."+className.getName().toString())));
        compiler.addInstruction(new POP(reg));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
    }
}
