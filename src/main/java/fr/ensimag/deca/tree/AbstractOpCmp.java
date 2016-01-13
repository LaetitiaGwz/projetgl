package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        Type t;

        if(leftType.isInt() && rightType.isInt()) {
            t = leftType;
        }
        else if (leftType.isInt() && rightType.isFloat()) {
            // on convertit le leftoperand int -> float
            setLeftOperand(new ConvFloat(getLeftOperand()));
            t = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if (leftType.isFloat() && rightType.isInt()) {
            // on convertit le rightoperand int -> float
            setRightOperand(new ConvFloat(getRightOperand()));
            t = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if (leftType.isFloat() && rightType.isFloat()) {
            t = leftType;
        }
        else {
            throw new ContextualError("Comparison on non-numbers. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
        }

        setType(t);
        return t;

    }


}
