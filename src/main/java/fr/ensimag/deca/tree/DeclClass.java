package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(Class.class);

    protected AbstractIdentifier name;
    protected AbstractIdentifier superClass;

    protected ListDeclFieldSet declFields;
    protected ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclFieldSet declFields, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(superClass);
        Validate.notNull(declFields);
        Validate.notNull(methods);

        this.name = name;
        this.superClass = superClass;
        this.declFields = declFields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        ClassDefinition superClassDef = compiler.getRootEnv().getClassDef(compiler.getSymbols().create(superClass.getName().getName()));

        // Erreur si la superClass n'existe pas
        if(superClassDef == null) {
            throw new ContextualError("Unexistant superClass " + superClass.getName().getName(), getLocation());
        }

        //name.verifyClass();
        //superclass.verifyClass();

        ClassType classType = new ClassType(compiler.getSymbols().create(name.getName().getName()), getLocation(), superClassDef);
        this.name.setDefinition(new ClassDefinition(classType, getLocation(), null));
        this.name.setType(classType);

        try {
            compiler.getRootEnv().declareClass(name.getName(), name.getClassDefinition());
        } catch (EnvironmentExp.DoubleDefException $e) {
            throw new ContextualError("Class " + name.getName().getName() + " twice declared.", getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {

    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, false);
        declFields.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        superClass.iter(f);
        methods.iter(f);
        declFields.iter(f);
    }

}
