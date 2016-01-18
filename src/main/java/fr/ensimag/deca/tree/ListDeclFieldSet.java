package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ListDeclFieldSet extends TreeList<AbstractDeclFieldSet> {

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    void verifyMembers(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclFieldSet field : getList()) {
            field.verifyDeclFieldSet(compiler, localEnv, currentClass);
        }
    }

    void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv,
                    ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListDecl(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
