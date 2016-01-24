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
            expression.setLocation(getLocation());
            expression.verifyExpr(compiler, localEnv, currentClass);
        }
        else if(!AbstractExpr.subtype(t, initType)) {
            throw new ContextualError("Incompatible type initialization. Expected " + t.getName() + ", had " + initType.getName() + ".", this.getLocation());
        }
        else if(!initType.sameType(t)) {
            // On cast
            // On crée le type et on met sa location
            Identifier typeIdentifier = new Identifier(t.getName());
            typeIdentifier.setLocation(getLocation());
            // On crée le cast et on met sa location
            Cast cast = new Cast(typeIdentifier, expression);
            cast.setLocation(getLocation());

            expression.verifyExpr(compiler, localEnv, currentClass);
        }
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler) {
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        getExpression().codegenExpr(compiler, register);
        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
        }
        compiler.getRegManager().setTableRegistre(table);
    }

    @Override
    protected void codeGenInitFieldFloat(DecacCompiler compiler){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre

        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        getExpression().codegenExpr(compiler,register);
        compiler.addInstruction(new LOAD(register,Register.R0));
        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
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
        s.println(";");

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
