package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Deca Identifier
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Identifier extends AbstractIdentifier {

    private int nbMethod=0;
    public int getNbMethod(){
        return this.nbMethod;
    }
    public void setNbMethod(int enplus){
        this.nbMethod=enplus;
    }
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a NonTypeDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public NonTypeDefinition getNonTypeDefinition() {
        try {
            return (NonTypeDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a NonType identifier, you can't call getNonTypeDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        NonTypeDefinition t = localEnv.get(compiler.getSymbols().create(getName().getName()));
        
        if(t == null) {
            throw new ContextualError("Undefinded variable " + getName(), getLocation());
        }
        setDefinition(t);
        return t.getType();
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition t = compiler.getRootEnv().getTypeDef(compiler.getSymbols().create(getName().getName()));

        if(t == null) {
            throw new DecacInternalError("Type " + getName().getName() + " undefinded.");
        }

        setDefinition(t);
        return t.getType();
    }

    @Override
    public Type verifyClass(DecacCompiler compiler) throws ContextualError {
        ClassDefinition c = compiler.getRootEnv().getClassDef(compiler.getSymbols().create(getName().getName()));

        if(c == null) {
            throw new ContextualError("Class " + getName().getName() + " undefinded.", this.getLocation());
        }

        setDefinition(c);
        return c.getType();
    }
    
    
    private Definition definition;

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(),Register.R1));
        if(definition.getType().isInt())
            compiler.addInstruction(new WINT());
        else
            compiler.addInstruction(new WFLOAT());


    }
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        int i = compiler.getRegManager().getLastregistre();
        GPRegister reg = Register.getR(i);
        compiler.getRegManager().setEtatRegistreTrue(i);
        this.setdValue(reg);
        compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(), reg));
    }
    @Override
    protected void codeGenInit(DecacCompiler compiler){
        RegisterOffset stock = new RegisterOffset(compiler.getRegManager().getGB(), Register.GB);
        this.getNonTypeDefinition().setOperand(stock);
        compiler.getRegManager().incrementGB();
    }

    @Override
    protected void codeGenInitClass(DecacCompiler compiler, int nbMethode){
        this.codeGenInit(compiler);
        this.setNbMethod(nbMethode);

    }

    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        DAddr stock = this.getNonTypeDefinition().getOperand();
        int i=compiler.getRegManager().getLastregistre();
        compiler.getRegManager().setEtatRegistreTrue(i);
        compiler.addInstruction(new LOAD(stock,Register.getR(i)));
        this.setdValue(Register.getR(i));
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.setdValue(this.getNonTypeDefinition().getOperand());
    }
    @Override
    protected void codeGenNot(DecacCompiler compiler){
            int i = compiler.getRegManager().getLastregistre();
            GPRegister target= Register.getR(i);
            compiler.getRegManager().setEtatRegistreTrue(i);
            compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(),target));
            compiler.addInstruction(new ADD(new ImmediateInteger(1),target));
            compiler.addInstruction(new REM(new ImmediateInteger(2),target));
            this.setdValue(target);
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        int i = compiler.getRegManager().getLastregistre();
        GPRegister target= Register.getR(i);
        compiler.getRegManager().setEtatRegistreTrue(i);
        compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(),target));
        compiler.addInstruction(new CMP(new ImmediateInteger(0),target));
        compiler.addInstruction(new BEQ(compiler.getLblManager().getLabelFalse()));
        compiler.getRegManager().setEtatRegistreFalse(i);
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler){
        int i = compiler.getRegManager().getLastregistre();
        GPRegister target= Register.getR(i);
        compiler.getRegManager().setEtatRegistreTrue(i);
        compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(),target));
        compiler.addInstruction(new CMP(new ImmediateInteger(0),target));
        compiler.addInstruction(new BNE(compiler.getLblManager().getLabelFalse()));
        compiler.getRegManager().setEtatRegistreFalse(i);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
        //TODO enlever le debug
        else {
            s.println("/!\\No definition");
        }
    }

}
