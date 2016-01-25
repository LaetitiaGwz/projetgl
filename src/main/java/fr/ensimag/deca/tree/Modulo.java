package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if(leftType.isInt() && rightType.isInt()) {
            this.setType(leftType);
            return leftType;
        }
        else {
            throw new ContextualError("Modulo must be used with two int.", this.getLocation());
        }
    }

    @Override
    protected void mnemoOp(DecacCompiler compiler, DVal left, GPRegister right) {
        compiler.addInstruction(new REM(left, right));
        compiler.addInstruction(new BOV(new Label("overflow_error"), compiler));
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
