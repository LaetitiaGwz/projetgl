package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

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


        if(leftType.isInt() && rightType.isInt()) {
        }
        else if (leftType.isInt() && rightType.isFloat()) {
            // on convertit le leftoperand int -> float
            setLeftOperand(new ConvFloat(getLeftOperand()));
            getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if (leftType.isFloat() && rightType.isInt()) {
            // on convertit le rightoperand int -> float
            setRightOperand(new ConvFloat(getRightOperand()));
            getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if (leftType.isFloat() && rightType.isFloat()) {
        }
        else {
            throw new ContextualError("Comparison on non-numbers. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
        }

        Type t = new BooleanType(compiler.getSymbols().create("boolean"));

        setType(t);
        return t;

    }


}
