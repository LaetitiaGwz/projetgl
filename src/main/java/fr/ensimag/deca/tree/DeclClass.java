package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ListeMethodeClasse;
import fr.ensimag.deca.codegen.TableField;
import fr.ensimag.deca.codegen.TableMethode;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
        s.print("class "+name.getName().toString());
        if(!superClass.getName().toString().equals("Object")){
            s.print(" extends "+superClass.getName().toString());
        }
        s.println("{");
        declFields.decompile(s);
        methods.decompile(s);
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // On vérifie que la superclasse existe bien
        superClass.verifyClass(compiler);
        // On récupère la définition de la superClass dans ce contexte également (nécessaire pour la déclaration du type)
        ClassDefinition superClassDef = compiler.getEnvTypes().getClassDef(superClass.getName());

        ClassType classType = new ClassType(name.getName(), getLocation(), superClassDef);
        this.name.setType(classType);

        ClassDefinition classDef = classType.getDefinition();
        name.setDefinition(classDef);

        // On déclare la class dans l'envRoot
        // Erreur si déjà existante
        try {
            compiler.getEnvTypes().declare(name.getName(), name.getClassDefinition());
        } catch (EnvironmentExp.DoubleDefException $e) {
            throw new ContextualError("Class " + name.getName().getName() + " twice declared.", getLocation());
        }

        // On met en place les définitions
        name.verifyClass(compiler);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

        // On hérite des index de la superClass
        // Pas besoin de vérifier qu'elle est non null
        (name.getClassDefinition()).setNumberOfMethods(superClass.getClassDefinition().getNumberOfMethods());
        (name.getClassDefinition()).setNumberOfFields(superClass.getClassDefinition().getNumberOfFields());

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
    protected void codePreGenMethodClass(DecacCompiler compiler){
        // on recupère l'adresse de la superclasse
        compiler.addInstruction(new LEA(superClass.getClassDefinition().getOperand(),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
        name.codeGenInitClass(compiler);
        name.getClassDefinition().initialiseTable();

        //ne va commencer qu'à 2 , code.Object.equals a rajouter à la main par la suite
        for(AbstractDeclMethod a : methods.getList()){
            compiler.getLblManager().setLabelFalse(new Label("code."+name.getName().toString()+"."+a.getIdentifier().getName()));
            a.codePreGenMethod(compiler); // on en profite pour regler la methode
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
    @Override
    protected void codeGenFieldClass(DecacCompiler compiler){
        compiler.addLabel(new Label("init."+name.getName().toString())); //pour s'en rappeler
        declFields.codeGenListDecl(compiler);
        if(!superClass.getName().toString().equals("Object")){
            compiler.addInstruction(new PUSH(Register.R1)); // on sauvegarde R1 pour la superclass
            compiler.addInstruction(new BSR(new Label("init."+superClass.getName().toString())));
            compiler.addInstruction(new SUBSP(1));
        }
        compiler.addInstruction(new RTS());
    }

    @Override
    protected void codeGenMethodClass(DecacCompiler compiler){

        for(AbstractDeclMethod a : methods.getList()){
            a.codeGenMethod(compiler);
        }

    }

}
