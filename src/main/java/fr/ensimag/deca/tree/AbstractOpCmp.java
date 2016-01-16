package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;

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

        if (leftType.sameType(rightType)) {
            if (!leftType.isInt() && !rightType.isFloat() && (this instanceof AbstractOpIneq)) {
                throw new ContextualError("Inequality on non-numbers. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
            }
        } else {
            if (leftType.isInt() && rightType.isFloat()) {
                // on convertit le leftoperand int -> float
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (leftType.isFloat() && rightType.isInt()) {
                // on convertit le rightoperand int -> float
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else {
                throw new ContextualError("Comparison on variables which types are differents and non-castable. Left : " + leftType.getName() + " Right : " + rightType.getName(), getLocation());
            }
        }

        Type t = new BooleanType(compiler.getSymbols().create("boolean"));

        setType(t);
        return t;
    }
        protected void codeGenCMPBase(DecacCompiler compiler){// pour l'héritage
        this.getLeftOperand().codeGenOPLeft(compiler);
        GPRegister cmpRight= getLeftOperand().getRegistreUtil();
        this.getRightOperand().codeGenOPRight(compiler);
        DVal cmpLeft = compiler.getDval();
        compiler.addInstruction(new CMP(cmpLeft,cmpRight));
        }

    protected void codeGenCMP(DecacCompiler compiler){// pour que chacun fasse sa suite

    }




}
