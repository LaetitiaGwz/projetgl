package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Pair (condition, instructions) representing one element of an
 * if/else if/else if/ sequence.
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public abstract class AbstractIfThen extends Tree {
    protected abstract void verifyIfThen(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
            throws ContextualError;

}
