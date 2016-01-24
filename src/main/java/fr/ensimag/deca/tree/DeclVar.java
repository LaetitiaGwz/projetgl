package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclVar extends AbstractDeclVar {

    public AbstractIdentifier getVarName() {
        return varName;
    }
    
    public AbstractInitialization getInitialization() {
        return initialization;
    }
    
    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(Type t, DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);

        // On tente de déclarer la variable dans l'environnement. Sinon erreur contextuelle.
        try {
            localEnv.declare(getVarName().getName(), new VariableDefinition(t, getLocation()));
        }
        catch (AbstractEnvironnement.DoubleDefException e) {
            throw new ContextualError("Multiple declaration of variable " + getVarName().getName().getName(), getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        getVarName().codeGenInit(compiler);
        if(getInitialization().getExpression() != null){
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
            this.getInitialization().getExpression().codegenExpr(compiler, register);
            DAddr adress = this.getVarName().getNonTypeDefinition().getOperand();
            compiler.addInstruction(new STORE(register, adress));
            if(getPop()){
                compiler.addInstruction(new POP(register));
                popDone();
            }
        }
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        getVarName().codeGenInitMethod(compiler);
        if(getInitialization().getExpression() != null){
            getInitialization().codeGenInit(compiler);
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
            this.getInitialization().getExpression().codegenExpr(compiler, register);
            DAddr adress = this.getVarName().getNonTypeDefinition().getOperand();
            compiler.addInstruction(new STORE(register, adress));
            if(getPop()){
                compiler.addInstruction(new POP(register));
                popDone();
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getVarName().decompile(s);
        getInitialization().decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
