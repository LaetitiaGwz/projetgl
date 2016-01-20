package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{

    AbstractExpr obj;
    AbstractIdentifier method;
    ListExpr params;

    public MethodCall(AbstractExpr obj, AbstractIdentifier method, ListExpr params) {
        Validate.notNull(obj);
        Validate.notNull(method);
        Validate.notNull(params);

        this.obj = obj;
        this.method = method;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type t = obj.verifyExpr(compiler, localEnv, currentClass);

        ClassDefinition classDef = compiler.getRootEnv().getClassDef(compiler.getSymbols().create(t.getName().getName()));

        // On vérifie que la méthode appartient a la classe
        MethodDefinition methodDef = classDef.getMembers().getMethodDef(compiler.getSymbols().create(method.getName().getName()));

        // On vérifie les paramètre entrés
        params.verifyParams(compiler, localEnv, currentClass, methodDef.getSignature(), getLocation());

        Type retType = method.verifyMethod(compiler, classDef.getMembers(), currentClass);
        setType(retType);
        return retType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        method.iter(f);
        params.iter(f);
    }
}
