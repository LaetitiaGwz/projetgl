package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        Type opType;

        if(!(leftType.isBoolean() && rightType.isBoolean())) {
            throw new ContextualError("Boolean operation on non-boolean operands. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
        }

        setType(leftType);
        return leftType;
    }

}
