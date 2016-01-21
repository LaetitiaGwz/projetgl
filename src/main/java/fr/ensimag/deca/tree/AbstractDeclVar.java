package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * Individual variable declaration (e.g. "x = 42" in "int x = 42, z;")
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractDeclVar extends Tree {
    
    protected abstract void verifyDeclVar(Type typeDeclaration,
            DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError;

    /**
     * Generates the code for the declaration
     * @param compiler
     */
    protected abstract void codeGenDecl(DecacCompiler compiler);

}
