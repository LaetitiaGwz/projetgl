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
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new NEW(className.getClassDefinition().getNumberOfFields()+1,Register.R1));
        compiler.addInstruction(new BOV(new Label("tas_plein")));
        compiler.addInstruction(new LEA(className.getClassDefinition().getOperand(),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,Register.R1)));
        compiler.addInstruction(new ADDSP(1));
        compiler.addInstruction(new STORE(Register.R1,new RegisterOffset(0,Register.SP)));
        compiler.addInstruction(new BSR(new Label("init."+className.getName().toString())));
        compiler.addInstruction(new SUBSP(1));
        compiler.addInstruction(new LOAD(Register.R1,register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new "+className.getName().toString());

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
