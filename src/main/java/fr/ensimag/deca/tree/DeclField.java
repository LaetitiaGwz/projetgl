package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclField extends AbstractDeclField {

    public AbstractIdentifier getVarName() {
        return varName;
    }

    public AbstractInitialization getInitialization() {
        return initialization;
    }

    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclField(AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyMembers(Type t, Visibility visibility, DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), visibility, currentClass, currentClass.incNumberOfFields());

        try {
            currentClass.getMembers().declare(compiler.getSymbols().create(varName.getName().getName()), fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Double definition of variable " + getVarName().getName().getName(), getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void verifyBody(Type t, Visibility visibility, DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        getVarName().codeGenInit(compiler);
        DAddr adress = this.getVarName().getNonTypeDefinition().getOperand();
        if(getInitialization().getExpression()!=null){
            getInitialization().codeGenInit(compiler);
            Register regLeft = (Register) this.getInitialization().getExpression().getdValue();
            compiler.addInstruction(new LOAD(regLeft,Register.R0));
            compiler.addInstruction(new STORE(Register.R0, adress));
        }
        else{

            if(getVarName().getType().isInt()){
                compiler.addInstruction(new LOAD(new ImmediateInteger(0),Register.R0));
                compiler.addInstruction(new STORE(Register.R0, adress));
            }
            else if(getVarName().getType().isFloat()){
                compiler.addInstruction(new LOAD(new ImmediateFloat(0),Register.R0));
                compiler.addInstruction(new STORE(Register.R0, adress));
            }
            else if(getVarName().getType().isBoolean()){
                compiler.addInstruction(new LOAD(new ImmediateInteger(0),Register.R0));
                compiler.addInstruction(new STORE(Register.R0, adress));
            }
        }
        compiler.resetTableRegistre();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
