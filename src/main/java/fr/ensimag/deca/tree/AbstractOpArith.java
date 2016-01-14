package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        Type opType;

        if(rightType.sameType(leftType)) {
            opType = rightType;
        }
        else if(rightType.isFloat() && leftType.isInt()) {
            // Conversion du leftoperand
            setLeftOperand(new ConvFloat(getLeftOperand()));
            opType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(rightType.isInt() && leftType.isFloat()) {
            // Conversion du rightoperand
            setRightOperand(new ConvFloat(getRightOperand()));
            opType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else {
            throw new ContextualError("Arithmetic operation on expressions which types are differents. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
        }

        setType(opType);
        return opType;
    }
}
