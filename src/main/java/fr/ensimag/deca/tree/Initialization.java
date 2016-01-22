package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
        GPRegister reg = compiler.getRegManager().getGBRegister();
        getExpression().codegenExpr(compiler, reg);
    }

    @Override
    protected void codeGenInitFieldFloat(DecacCompiler compiler){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        for(int i=2;i<compiler.getCompilerOptions().getRegistre();i++){
            compiler.addInstruction(new PUSH(Register.getR(i)));
        }
        compiler.getRegManager().resetTableRegistre();
        GPRegister reg =compiler.getRegManager().getGBRegister();
        getExpression().codegenExpr(compiler,reg);
        compiler.addInstruction(new LOAD(reg,Register.R0));
        for(int i=compiler.getCompilerOptions().getRegistre()-1;i>1;i--){
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.getRegManager().setTableRegistre(table); //on les remets à la fin

    }
    @Override
    protected void codeGenInitFieldInt(DecacCompiler compiler){//l'expression fait tout
        this.codeGenInitFieldFloat(compiler);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        getExpression().decompileInst(s);

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
