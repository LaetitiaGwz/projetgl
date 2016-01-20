package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractDeclClass extends Tree {

    /**
     * Attribute grammar's pass 1. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Attribute grammar's pass 2. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Attribute grammar's pass 3. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void codePreGenMethod(DecacCompiler compiler);

    protected abstract void codeGenFieldClass(DecacCompiler compiler);

    protected abstract void codeGenMethodClass(DecacCompiler compiler);


    public abstract AbstractIdentifier returnIdentifier();
}
