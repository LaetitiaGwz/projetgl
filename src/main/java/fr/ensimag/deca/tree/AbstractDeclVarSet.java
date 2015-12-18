package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Declaration of a set of variables (e.g. "int x = 42, z;")
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public abstract class AbstractDeclVarSet extends Tree {
    
    protected abstract Type verifyDeclVarSet(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
}
