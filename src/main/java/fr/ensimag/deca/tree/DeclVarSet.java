package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclVarSet extends AbstractDeclVarSet {
    public AbstractIdentifier getType() {
        return type;
    }

    public ListDeclVar getDeclVars() {
        return declVars;
    }

    private AbstractIdentifier type;
    private ListDeclVar declVars;

    public DeclVarSet(AbstractIdentifier type, ListDeclVar declVars) {
        super();
        Validate.notNull(type);
        Validate.notNull(declVars);
        Validate.isTrue(!declVars.isEmpty(),
                "A list of variable declarations cannot be empty");
        this.type = type;
        this.declVars = declVars;
    }

    @Override
    protected Type verifyDeclVarSet(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type t;
        // On détermine le type de la variable à partir de l'Identifier
        if(type.getName().getName().compareTo("int") == 0) {
            t = new IntType(type.getName());
        }
        else if (type.getName().getName().compareTo("float") == 0) {
            t = new FloatType(type.getName());
        }
        else if (type.getName().getName().compareTo("String") == 0) {
            t = new StringType(type.getName());
        }
        else if (type.getName().getName().compareTo("boolean") == 0) {
            t = new BooleanType(type.getName());
        }
        else if (type.getName().getName().compareTo("void") == 0) {
            t = new VoidType(type.getName());
        }
        else {
            throw new UnsupportedOperationException("Not implemented for variable of type " + type.getName().getName());
        }


        // On implémente sa définition
        type.setDefinition(new VariableDefinition(t, getLocation()));

        // On tente de déclarer la variable dans l'environnement. Sinon erreur
        try {
            for(AbstractDeclVar var : declVars.getList()) {
                DeclVar castedVar = (DeclVar) var;
                localEnv.declare(castedVar.getVarName().getName(), type.getVariableDefinition());
            }
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Multiple declaration of variable " + type.getName().getName(), getLocation());
        }

        //erreur si type = void
        if(type.getDefinition().getType().isVoid()) {
            throw new ContextualError("A variable can not be declared as void.", getLocation());
        }

        for(AbstractDeclVar var : declVars.getList()) {
            var.verifyDeclVar(type.getType(), compiler, localEnv, currentClass);
        }

        return type.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        declVars.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        declVars.prettyPrint(s, prefix, true);
    }
}
