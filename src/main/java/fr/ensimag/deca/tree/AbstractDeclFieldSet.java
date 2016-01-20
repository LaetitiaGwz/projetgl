package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Declaration of a set of variables (e.g. "int x = 42, z;")
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractDeclFieldSet extends Tree {

    protected abstract void verifyMembers(DecacCompiler compiler,
                      EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void verifyBody(DecacCompiler compiler,
                                          EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void codeGenFieldSet(DecacCompiler compiler);
}
