package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr {

    AbstractIdentifier className;
    AbstractExpr var;

    public InstanceOf(AbstractIdentifier className, AbstractExpr var) {
        Validate.notNull(className);
        Validate.notNull(var);

        this.className = className;
        this.var = var;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        className.verifyClass(compiler);
        var.verifyExpr(compiler, localEnv, currentClass);

        Type t = compiler.getRootEnv().getTypeDef(compiler.getSymbols().create("boolean")).getType();
        setType(t);
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrint(s, prefix, false);
        className.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        var.iter(f);
        className.iter(f);
    }
}
