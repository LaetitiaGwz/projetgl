package fr.ensimag.deca.tree;

//import com.sun.tools.doclint.Env;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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

    EnvironmentExp classEnv;

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

        // On récupère la définition de la superClass dans ce contexte également (nécessaire pour la déclaration du type)
        ClassDefinition superClassDef = compiler.getRootEnv().getClassDef(compiler.getSymbols().create(superClass.getName().getName()));

        ClassType classType = new ClassType(compiler.getSymbols().create(name.getName().getName()), getLocation(), superClassDef);
        this.name.setType(classType);

        name.setDefinition(new ClassDefinition(classType, getLocation(), superClassDef));

        // On déclare la class dans l'envRoot
        // Erreur si déjà existante
        try {
            compiler.getRootEnv().declareClass(compiler.getSymbols().create(name.getName().getName()), name.getClassDefinition());
        } catch (EnvironmentExp.DoubleDefException $e) {
            throw new ContextualError("Class " + name.getName().getName() + " twice declared.", getLocation());
        }

        // On met en place les définitions
        name.verifyClass(compiler);
        superClass.verifyClass(compiler);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        this.classEnv = new EnvironmentExp(compiler.getRootEnv());
        methods.verifyMethodsMembers(compiler, classEnv, name.getClassDefinition());
        declFields.verifyMembers(compiler, classEnv, name.getClassDefinition());
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

    protected void codeGenTest(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("test")), Register.R1));
    }



}
