package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    public void verifyMethodsMembers(DecacCompiler compiler, EnvironmentExp localEnv,
                                     ClassDefinition currentClass)
            throws ContextualError {
        for(AbstractDeclMethod method : getList()) {
            method.verifyMembers(compiler, localEnv, currentClass);
        }
    }

    public void verifyMethodsBody(DecacCompiler compiler, EnvironmentExp localEnv,
                                     ClassDefinition currentClass)
            throws ContextualError {
        for(AbstractDeclMethod method : getList()) {
            method.verifyBody(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}