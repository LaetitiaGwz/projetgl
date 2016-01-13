package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        this.getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(3,Register.GB)));
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        if(rightType.sameType(leftType)) {
            return rightType;
        }
        else if(rightType.isFloat() && leftType.isInt()) {
            // Conversion du leftoperand
            setLeftOperand(new ConvFloat(getLeftOperand()));
            return getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(rightType.isInt() && leftType.isFloat()) {
            // Conversion du rightoperand
            setRightOperand(new ConvFloat(getRightOperand()));
            return getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else {
            throw new ContextualError("Assignement of expressions which types are differents.", getLocation());
        }
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
