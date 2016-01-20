package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Return extends AbstractInst {

    AbstractExpr retValue;

    public Return(AbstractExpr retValue) {
        this.retValue = retValue;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType) throws ContextualError {

        Type detectedType = retValue.verifyExpr(compiler, localEnv, currentClass);
        if(!detectedType.sameType(returnType)) {
            throw new ContextualError("Return type does not match definition. Expected : " + returnType.getName().getName()
                    + " Got : " + detectedType.getName().getName(), getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        retValue.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        retValue.prettyPrint(s, prefix, true);
    }
}
