package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractInitialization extends Tree {

    /**
     * Check the type of an initialization.
     * @param compiler The compiler main class containing the type environment.
     * @param t The type which the initialization has to match.
     * @param localEnv The environment in which the initialization is done.
     * @param currentClass The class in which the initialization is done.
     * @throws ContextualError if types don't match.
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void codeGenInit(DecacCompiler compiler);
    protected abstract void codePreGenInit(DecacCompiler compiler);
    protected abstract void codeGenInitFieldFloat(DecacCompiler compiler);
    protected abstract void codeGenInitFieldInt(DecacCompiler compiler);
    protected abstract AbstractExpr getExpression();
}
