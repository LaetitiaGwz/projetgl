package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl41
 * @date 01/01/2016
 */
public class ListExpr extends TreeList<AbstractExpr> {

    public Signature verifySignature(DecacCompiler compiler, EnvironmentExp localEnv,
                                  ClassDefinition currentClass)
            throws ContextualError {
        Signature s = new Signature();
        // Erreur si types différents pour un certain paramètre
        for (AbstractExpr expr : getList()) {
            s.add(expr.verifyExpr(compiler, localEnv, currentClass));
        }
        return s;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractExpr a : getList()){
            a.decompile(s);
        }
    }
}
