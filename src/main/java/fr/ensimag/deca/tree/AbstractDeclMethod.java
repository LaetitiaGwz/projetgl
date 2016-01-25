package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Instruction
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractDeclMethod extends Tree {

    protected AbstractIdentifier name;
    protected AbstractIdentifier ret;
    protected ListDeclParam params;

    protected void verifyMembers(fr.ensimag.deca.DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type returnType = ret.verifyType(compiler);
        Signature signature = params.verifyMembers(compiler, localEnv, currentClass);

        // On vérifie d'abord que la méthode n'est pas
        //  déjà déclarée dans l'environnement parent
        NonTypeDefinition parentDef = currentClass.getSuperClass().getMembers().get(name.getName());

        int index;
        if(parentDef == null) {
            index = currentClass.incNumberOfMethods();
        }
        else {
            MethodDefinition parentMethodDef = parentDef.asMethodDefinition("method " + name.getName().getName() + " already declared in class.", getLocation());
            if (!signature.equals(parentMethodDef.getSignature())) {
                // Erreur si on tente de surcharger la méthode
                throw new ContextualError("Cannot override method " + name.getName().getName() + " with a different signature.", getLocation());
            } if (!AbstractExpr.subtype(parentMethodDef.getType(), returnType)) {
                // Erreur si le nouveau type de retour n'est pas un sous-type de l'ancien
                throw new ContextualError("Type returned by method " + name.getName().getName() + " is not a subtype of overrided method.", getLocation());
            }
            index = parentMethodDef.getIndex();
        }

        MethodDefinition methodDef = new MethodDefinition(returnType, getLocation(), signature, index);

        try {
            localEnv.declare(name.getName(), methodDef);
        } catch (AbstractEnvironnement.DoubleDefException e) {
            throw new ContextualError("Double declaration of method " + name.getName().getName(), getLocation());
        }

        name.verifyMethod(signature, compiler, localEnv);
    }

    protected abstract void verifyBody(fr.ensimag.deca.DecacCompiler compiler,
                                       EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;
    

    protected abstract void codePreGenMethod(fr.ensimag.deca.DecacCompiler compiler);
    protected abstract void codeGenMethod(fr.ensimag.deca.DecacCompiler compiler);

    protected abstract String getName();
    public abstract AbstractIdentifier getIdentifier();

    protected void decompileMethod(IndentPrintStream s) {
        decompile(s);
    }

}
