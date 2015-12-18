package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Individual variable declaration (e.g. "x = 42" in "int x = 42, z;")
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public abstract class AbstractDeclVar extends Tree {
    
    protected abstract void verifyDeclVar(Type typeDeclaration,
            DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError;
}
