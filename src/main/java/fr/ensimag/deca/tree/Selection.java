package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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
    public NonTypeDefinition getNonTypeDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = obj.verifyExpr(compiler, localEnv, currentClass);

        ClassDefinition classDef = compiler.getRootEnv().getClassDef(t.getName());

        Type retType = field.verifyExpr(compiler, classDef.getMembers(), currentClass);

        // Erreur si le field est protected et qu'on ne se trouve pas das sa classe
        if(!classDef.equals(currentClass) && field.getFieldDefinition().getVisibility() == Visibility.PROTECTED) {
            throw new ContextualError("Cannot call a protected field outside its class.", getLocation());
        }

        setType(retType);
        return retType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
