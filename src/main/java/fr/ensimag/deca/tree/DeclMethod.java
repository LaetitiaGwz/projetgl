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

    Identifier ret;
    ListDeclParam params;
    ListInst body;

    public DeclMethod(Identifier ret, ListDeclParam params, ListInst body) {
        Validate.notNull(ret);
        Validate.notNull(params);
        Validate.notNull(body);

        this.ret = ret;
        this.params = params;
        this.body = body;
    }

    @Override
    protected void verifyMember(DecacCompiler compiler,
                                EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass,
                              Type returnType)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
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
        ret.iter(f);
        params.iter(f);
        body.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ret.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
