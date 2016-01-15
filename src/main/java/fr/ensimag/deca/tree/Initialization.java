package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type initType = expression.verifyExpr(compiler, localEnv, currentClass);
        if(initType.isInt() && t.isFloat()) {
            expression = new ConvFloat(expression);
            expression.verifyExpr(compiler, localEnv, currentClass);
        }
        else if(!initType.sameType(t)) {
            throw new ContextualError("Incompatible type initialization. Expected " + t.getName() + ", had " + initType.getName() + ".", this.getLocation());
        }
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler) {
        if(getExpression().getType().isInt()){
            getExpression().codeGenInst(compiler);
        }
        else if (getExpression().getType().isFloat()){
            getExpression().codeGenInst(compiler);
        }
        else{ // boolean a gérer
            getExpression().codeGenOP(compiler);
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
