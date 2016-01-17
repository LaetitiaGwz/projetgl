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
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if(!type.isBoolean()) {
            throw new ContextualError("Must apply 'not' operator to a boolean. We have : " + type.getName(), getLocation());
        }

        setType(type);
        return type;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
            getOperand().codeGenNot(compiler);
        this.setRegistreUtil(getOperand().getRegistreUtil());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
