package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Selection extends AbstractLValue {

    AbstractExpr obj;
    AbstractIdentifier field;

    public Selection(AbstractExpr obj, AbstractIdentifier field) {
        Validate.notNull(obj);
        Validate.notNull(field);

        this.obj = obj;
        this.field = field;
    }


    @Override
    public  SymbolTable.Symbol getName(){
        return field.getName();
    }

    @Override
    public Definition getDefinition() {
        return field.getDefinition();
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
        return field.getClassDefinition();
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
        return field.getMethodDefinition();
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
        return field.getFieldDefinition();
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
        return  field.getVariableDefinition();
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
        return field.getNonTypeDefinition();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = obj.verifyExpr(compiler, localEnv, currentClass);

        ClassDefinition classDef = compiler.getEnvTypes().getClassDef(t.getName());

        Type retType = field.verifyExpr(compiler, classDef.getMembers(), currentClass);

        // Erreur si le field est protected et qu'on ne se trouve pas dans sa classe
        if(!classDef.equals(currentClass) &&
                field.getFieldDefinition().getVisibility() == Visibility.PROTECTED) {
            throw new ContextualError("Cannot call a protected field outside its class.", getLocation());
        }

        setType(retType);
        return retType;
    }

    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        GPRegister stock =compiler.getRegManager().getGBRegister();
        //on ne peut appliquer this qu'à un field
        obj.codegenExpr(compiler,stock);
        //on a l'adresse de l'objet
        compiler.addInstruction(new LOAD(new RegisterOffset(field.getFieldDefinition().getIndex(),stock),register));

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        this.codegenExpr(compiler,Register.R1);
        if(this.getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else{
            compiler.addInstruction(new WINT());
        }

    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler){
        this.codegenExpr(compiler,Register.R1);
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print("."+field.getName().toString());

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        field.iter(f);
    }
}
