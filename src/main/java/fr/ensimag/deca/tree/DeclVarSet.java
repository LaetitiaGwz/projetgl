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
public class DeclVarSet extends AbstractDeclVarSet {
    public AbstractIdentifier getType() {
        return type;
    }

    public ListDeclVar getDeclVars() {
        return declVars;
    }

    private AbstractIdentifier type;
    private ListDeclVar declVars;

    public DeclVarSet(AbstractIdentifier type, ListDeclVar declVars) {
        super();
        Validate.notNull(type);
        Validate.notNull(declVars);
        Validate.isTrue(!declVars.isEmpty(),
                "A list of variable declarations cannot be empty");
        this.type = type;
        this.declVars = declVars;
    }

    @Override
    protected Type verifyDeclVarSet(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        declVars.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        declVars.prettyPrint(s, prefix, true);
    }
}
