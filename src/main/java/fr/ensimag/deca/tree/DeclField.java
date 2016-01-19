package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
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
