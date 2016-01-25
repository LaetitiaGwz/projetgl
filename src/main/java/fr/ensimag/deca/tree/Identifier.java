package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.TableMethode;
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
    protected void codePreGenInit(DecacCompiler compiler){
        //pas besoin de registre
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        NonTypeDefinition t = localEnv.get(getName());

        if(t == null) {
            throw new ContextualError("Undefinded variable " + getName(), getLocation());
        }

        setDefinition(t);
        setType(t.getType());
        return t.getType();
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition t = compiler.getEnvTypes().get(compiler.getSymbols().create(getName().getName()));

        if(t == null) {
            throw new ContextualError("Type " + getName().getName() + " undefinded.", getLocation());
        }

        setDefinition(t);
        setType(t.getType());
        return t.getType();
    }

    @Override
    public Type verifyClass(DecacCompiler compiler) throws ContextualError {
        TypeDefinition c = compiler.getEnvTypes().get(compiler.getSymbols().create(getName().getName()));

        if(c == null || (!(c instanceof ClassDefinition))) {
            throw new ContextualError("Class " + getName().getName() + " undefinded.", this.getLocation());
        }

        setDefinition(c);
        setType(c.getType());
        return c.getType();
    }

    @Override
    public Type verifyMethod(Signature s, DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        MethodDefinition c = localEnv.getMethodDef(compiler.getSymbols().create(getName().getName()));

        if(c == null) {
            throw new ContextualError("Method " + getName().getName() + " undefinded.", this.getLocation());
        }
        else if(!c.getSignature().accepts(s)) {
            throw new ContextualError("Invalid signature for method " + getName().getName(), this.getLocation());
        }

        setDefinition(c);
        setType(c.getType());
        return c.getType();
    }
    
    
    private Definition definition;

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        if(getDefinition().isField()){
            compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.R1)); // on charge l'objet
            compiler.addInstruction(new LOAD(new RegisterOffset(getFieldDefinition().getIndex(),Register.R1),Register.R1)); //on charge l'élément
        }
        else if(getDefinition().isClass()){
            compiler.addInstruction(new LOAD(getClassDefinition().getOperand(),Register.R1));
        }
        else if(getDefinition().isMethod()){

        }
        else if(getDefinition().isParam()){

        }
        else if(getDefinition().isExpression()){
            compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(), Register.R1));
        }
        else{

        }
        if(definition.getType().isInt())
            compiler.addInstruction(new WINT());
        else
            compiler.addInstruction(new WFLOAT());


    }

    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        if(getDefinition().isField()){
            compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),register)); // on charge l'objet
            compiler.addInstruction(new LOAD(new RegisterOffset(getFieldDefinition().getIndex(),register),register)); //on charge l'élément
        }
        else if(getDefinition().isClass()){
            compiler.addInstruction(new LOAD(getClassDefinition().getOperand(),register));
        }
        else if(getDefinition().isMethod()){

        }
        else if(getDefinition().isParam()){

        }
        else if(getDefinition().isExpression()){
            compiler.addInstruction(new LOAD(getNonTypeDefinition().getOperand(), register));
        }
        else{

        }

    }

    @Override
    protected void codeGenInit(DecacCompiler compiler){
        RegisterOffset stock = new RegisterOffset(compiler.getRegManager().getGB(), Register.GB);
        this.getNonTypeDefinition().setOperand(stock);
        compiler.getRegManager().incrementGB();
    }

    @Override
    protected void codeGenInitMethod(DecacCompiler compiler){
        RegisterOffset stock = new RegisterOffset(compiler.getRegManager().getLB(), Register.LB);
        this.getNonTypeDefinition().setOperand(stock);
        compiler.getRegManager().incrementLB();
    }


    @Override
    public DVal getDval() {
        return this.getNonTypeDefinition().getOperand();
    }


    @Override
    protected void codeGenInitClass(DecacCompiler compiler){
        RegisterOffset stock = new RegisterOffset(compiler.getRegManager().getGB(), Register.GB);
        this.getClassDefinition().setOperand(stock);
        compiler.getRegManager().incrementGB();

    }

    @Override
    protected void codeGenNot(DecacCompiler compiler){
        this.codegenExpr(compiler,Register.R0);
            compiler.addInstruction(new LOAD(this.getNonTypeDefinition().getOperand(),Register.R0));
            compiler.addInstruction(new ADD(new ImmediateInteger(1),Register.R0));
            compiler.addInstruction(new REM(new ImmediateInteger(2),Register.R0));
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler){
        this.codegenExpr(compiler,Register.R0);
        compiler.addInstruction(new CMP(0,Register.R0));
        compiler.addInstruction(new BEQ(compiler.getLblManager().getLabelFalse()));
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
