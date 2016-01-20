package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ListeMethodeClasse;
import fr.ensimag.deca.codegen.TableField;
import fr.ensimag.deca.codegen.TableMethode;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.lang.reflect.Method;

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

    @Override
    public AbstractIdentifier returnIdentifier(){
        return name;
    }

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

        ClassDefinition classDef = new ClassDefinition(classType, getLocation(), superClassDef);
        name.setDefinition(classDef);

        // On déclare la class dans l'envRoot
        // Erreur si déjà existante
        try {
            compiler.getRootEnv().declareType(compiler.getSymbols().create(name.getName().getName()), name.getClassDefinition());
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
        methods.verifyMethodsMembers(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
        declFields.verifyMembers(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        methods.verifyMethodsBody(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
        declFields.verifyBody(compiler, name.getClassDefinition().getMembers(), name.getClassDefinition());
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
    @Override
    protected void codePreGen1(DecacCompiler compiler){
        // on recupère l'adresse de la superclasse
        compiler.addInstruction(new LEA(superClass.getClassDefinition().getOperand(),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
        name.codeGenInitClass(compiler);
        name.getClassDefinition().initialiseTable();

        //ne va commencer qu'à 2 , code.Object.equals a rajouter à la main par la suite
        for(AbstractDeclMethod a : methods.getList()){
            compiler.getLblManager().setLabelFalse(new Label("code."+name.getName().toString()+"."+a.getIdentifier().getName()));
            a.codeGenMethod(compiler); // on en profite pour regler la methode
            name.getClassDefinition().ajoutMethod(a);
        }
        // on a ajouté les éléments de la classe verifions la superclasse si présente
        if(!superClass.getName().toString().equals("Object")){// besoin de récupérer éléments de la super classe que si elle existe
            Integer i=2;
            while(superClass.getClassDefinition().containKey(i) || name.getClassDefinition().containKey(i) ){
                if(!name.getClassDefinition().containKey(i)){
                    name.getClassDefinition().ajoutMethod(superClass.getClassDefinition().getMethod(i));
                }
                i++;
            }
        }
        // le tableau est rempli.


    }

    protected void codeGenDeclMethod(DecacCompiler compiler){
        for(AbstractDeclFieldSet a : declFields.getList()){

        }
    }

}
