package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    private DVal dValue;

    public void setdValue(DVal dval){
        this.dValue =dval;
        this.setUtilisation();
    }

    public DVal getdValue(){
        return this.dValue;
    }

    private boolean utilisation= false;
    public void getNonTypeDefinition(){

    }

    public void setUtilisation(){
        this.utilisation= true;
    }

    public boolean getUtilisation(){
        return this.utilisation;
    }

      /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * @param compiler
     * @param localEnv
     *            Environment in which the expression should be checked
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     * @return the Type of the expression
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type condType = this.verifyExpr(compiler, localEnv, currentClass);

        if(!condType.isBoolean())   {
            throw new ContextualError("Condition must be a boolean.", getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected  void codeGenPrint(DecacCompiler compiler){

    }

    protected  void codeGenPrintX(DecacCompiler compiler){
        codeGenPrint(compiler);
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    }



    protected void codeGenOPRight(DecacCompiler compiler){

    }
    protected void codeGenOPLeft(DecacCompiler compiler){

    }
    protected void codeGenNot(DecacCompiler compiler){

    }

    protected void codeGenCMP(DecacCompiler compiler){
        // pour les booleens
    }
    protected void codeGenCMPNot(DecacCompiler compiler){
        // pour le or
    }

    protected  void codeGenCMPOP(DecacCompiler compiler){
        
    }




    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    protected void decompileCMP(IndentPrintStream s ){
        decompile(s);
    }
    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
        else {
            s.print(prefix);
            s.print("/!\\ no type");
            s.println();
        }
    }

    protected boolean subtype(EnvironmentExp env, Type parent, Type child) {
        return (parent.sameType(child));
    }

    protected boolean assignCompatible(EnvironmentExp env, Type object, Type value) {
        return (object.isFloat() && value.isInt()) || subtype(env, object, value);
    }

    protected boolean castCompatible(EnvironmentExp env, Type from, Type to) {
        if(from.isVoid()) {
            return false;
        }
        return assignCompatible(env, from, to) || assignCompatible(env, to, from);
    }
}
