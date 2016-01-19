package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
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
        getRightOperand().codeGenOPLeft(compiler);
        getLeftOperand().codeGenInst(compiler);
        Register regLeft = (Register) getRightOperand().getdValue();
        DAddr adress = this.getLeftOperand().getNonTypeDefinition().getOperand();
        compiler.addInstruction(new STORE(regLeft, adress));
        compiler.getRegManager().resetTableRegistre();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        if(rightType.isInt() && leftType.isFloat()) {
            // Conversion du rightoperand
            setRightOperand(new ConvFloat(getRightOperand()));
            getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(!rightType.sameType(leftType)) {
            throw new ContextualError("Assignement incompatible : cannot cast " + rightType + " into " + leftType + ".", getLocation());
        }
        
        Type t = compiler.getRootEnv().getTypeDef(compiler.getSymbols().create("boolean")).getType();
        setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
