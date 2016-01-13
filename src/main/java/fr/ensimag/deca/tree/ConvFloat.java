package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {

        Type floatType = new FloatType(compiler.getSymbols().create("float"));

        setType(floatType);
        return floatType;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
