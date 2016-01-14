package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Cast extends AbstractCast {

    AbstractIdentifier type;
    AbstractExpr expr;

    public Cast (AbstractIdentifier type, AbstractExpr expr) {
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type castType = type.verifyType(compiler);
        Type opType = expr.verifyExpr(compiler, localEnv, currentClass);

        if(!castCompatible(localEnv, opType, castType)) {
            throw new ContextualError("Incompatible cast from " + opType + " to " + castType, getLocation());
        }

        setType(castType);
        return castType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
