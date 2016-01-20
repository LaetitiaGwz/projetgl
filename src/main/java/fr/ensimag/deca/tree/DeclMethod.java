package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclMethod extends AbstractDeclMethod {

    AbstractIdentifier name;
    AbstractIdentifier ret;
    ListDeclParam params;
    ListInst body;
    ListDeclVarSet declVars;

    EnvironmentExp methodEnv;

    @Override
    public AbstractIdentifier getIdentifier(){
        return this.name;
    }
    public DeclMethod(AbstractIdentifier name, AbstractIdentifier ret, ListDeclParam params, ListInst body, ListDeclVarSet declVars) {
        Validate.notNull(name);
        Validate.notNull(ret);
        Validate.notNull(params);
        Validate.notNull(body);
        Validate.notNull(declVars);

        this.name = name;
        this.ret = ret;
        this.params = params;
        this.body = body;
        this.declVars = declVars;
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler,
                                EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type returnType = ret.verifyType(compiler);
        Signature signature = params.verifyMembers(compiler, localEnv, currentClass);

        MethodDefinition methodDef = new MethodDefinition(returnType, getLocation(), signature, currentClass.incNumberOfMethods());

        try {
            localEnv.declare(compiler.getSymbols().create(name.getName().getName()), methodDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Double declaration of method " + name.getName().getName(), getLocation());
        }

        name.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // On instancie l'environnement de la méthode dont l'env parent est celui de la classe
        methodEnv = new EnvironmentExp(currentClass.getMembers());

        params.verifyBody(compiler, methodEnv, currentClass);
        body.verifyListInst(compiler, methodEnv, currentClass, ret.getType());
        declVars.verifyListDeclVariable(compiler, methodEnv, currentClass);
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        name.getMethodDefinition().setLabel(compiler.getLblManager().getLabelFalse());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getName(){
        return ret.getName().toString();
    }
    @Override
    protected
    void iterChildren(TreeFunction f) {
        name.iter(f);
        ret.iter(f);
        params.iter(f);
        declVars.iter(f);
        body.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ret.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        declVars.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
