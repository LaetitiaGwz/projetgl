package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!type.isFloat() && !type.isInt()) {
            throw new ContextualError("Must apply unary minus to an int or float. We have : " + type.getName(), getLocation());
        }

        setType(type);
        return type;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
