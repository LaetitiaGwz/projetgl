package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    Signature verifyMembers(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Signature s = new Signature();
        for(AbstractDeclParam p : getList()) {
            s.add(p.verifyMembers(compiler, localEnv, currentClass));
        }

        return s;
    }

    void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv,
                            ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclParam p : getList()) {
            p.verifyBody(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDecl(DecacCompiler compiler) {
        int i= -3;
        for(AbstractDeclParam p : getList()){
            p.setIndice(i);
            p.codeGenDecl(compiler);
            i--;
        }
    }

}
