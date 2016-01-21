package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclMethodAss extends AbstractDeclMethod {

    AbstractIdentifier name;
    AbstractIdentifier ret;
    ListDeclParam params;
    String code;

    @Override
    public AbstractIdentifier getIdentifier(){
        return this.name;
    }

    public DeclMethodAss(AbstractIdentifier name, AbstractIdentifier ret, ListDeclParam params, String code) {
        Validate.notNull(name);
        Validate.notNull(ret);
        Validate.notNull(params);
        Validate.notNull(code);

        this.name = name;
        this.ret = ret;
        this.params = params;
        this.code = code;
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler,
                                EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codePreGenMethod(DecacCompiler compiler) {

    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected String getName(){
        return name.getName().toString();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        name.iter(f);
        ret.iter(f);
        params.iter(f);
        //code.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        ret.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        //code.prettyPrint(s, prefix, true);
    }
}
