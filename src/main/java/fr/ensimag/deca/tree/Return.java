package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
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
        if (detectedType.isInt() && returnType.isFloat()) {
            retValue = new ConvFloat(retValue);
            retValue.verifyExpr(compiler, localEnv, currentClass);
        }
        else if(!AbstractExpr.subtype(returnType, detectedType)) {
            throw new ContextualError("Return type does not match definition. Expected : " + returnType.getName().getName()
                    + ", got : " + detectedType.getName().getName(), getLocation());
        }
        else if(!returnType.equals(detectedType)) {
            // On cast
            // On crée le type et on met sa location
            Identifier typeIdentifier = new Identifier(returnType.getName());
            typeIdentifier.setLocation(getLocation());
            // On crée le cast et on met sa location
            Cast cast = new Cast(typeIdentifier, retValue);
            cast.setLocation(getLocation());

            retValue.verifyExpr(compiler, localEnv, currentClass);
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        retValue.codegenExpr(compiler, Register.R0);
        compiler.addInstruction(new BRA(compiler.getLblManager().getLabelFalse()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        retValue.decompile(s);
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
