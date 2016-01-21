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
public class ListDeclVarSet extends TreeList<AbstractDeclVarSet> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclVarSet a : this.getList()){
            a.decompile(s);
        }
    }

    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclVarSet varSet : this.getList()) {
            varSet.verifyDeclVarSet(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDecl(DecacCompiler compiler) {
        // run codegen on each declaration set
        for(AbstractDeclVarSet declVarSet : getList()){
            declVarSet.codegenDeclVarSet(compiler);
        }
    }
    public void codeGenListDeclMethod(DecacCompiler compiler) {
        // run codegen on each declaration set
        for(AbstractDeclVarSet declVarSet : getList()){
            declVarSet.codegenDeclVarSetMethod(compiler);
        }
    }

}
