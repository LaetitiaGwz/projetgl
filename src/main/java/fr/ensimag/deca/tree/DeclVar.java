package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author @AUTHOR@
 * @date @DATE@
 */
public class DeclVar extends AbstractDeclVar {

    public AbstractIdentifier getVarName() {
        return varName;
    }
    
    public AbstractInitialization getInitialization() {
        return initialization;
    }
    
    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(Type t, DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
