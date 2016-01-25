package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionSP;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {

        EnvironmentTypes envTypes = new EnvironmentTypes(null);
        try {
            declareTypes(compiler, envTypes);
            declareObject(compiler, envTypes);
        }
        catch (AbstractEnvironnement.DoubleDefException $e) {
            throw new DecacInternalError("Double definition of builtin types.");
        }
        compiler.setEnvTypes(envTypes);


        getClasses().verifyListClass(compiler);
        getClasses().verifyListClassMembers(compiler);
        getClasses().verifyListClassBody(compiler);
        getMain().verifyMain(compiler);
    }

    private void declareTypes(DecacCompiler compiler, EnvironmentTypes env) throws AbstractEnvironnement.DoubleDefException {
        env.declare(compiler.getSymbols().create("int"), new TypeDefinition(new IntType(compiler.getSymbols().create("int")), Location.BUILTIN));
        env.declare(compiler.getSymbols().create("float"), new TypeDefinition(new FloatType(compiler.getSymbols().create("float")), Location.BUILTIN));
        env.declare(compiler.getSymbols().create("String"), new TypeDefinition(new StringType(compiler.getSymbols().create("String")), Location.BUILTIN));
        env.declare(compiler.getSymbols().create("boolean"), new TypeDefinition(new BooleanType(compiler.getSymbols().create("boolean")), Location.BUILTIN));
        env.declare(compiler.getSymbols().create("void"), new TypeDefinition(new VoidType(compiler.getSymbols().create("void")), Location.BUILTIN));
    }

    private void declareObject(DecacCompiler compiler, EnvironmentTypes envTypes) throws AbstractEnvironnement.DoubleDefException {

        // Definition de la classe Object
        ClassDefinition Object = new ClassDefinition(new ClassType(compiler.getSymbols().create("Object"), Location.BUILTIN, null), Location.BUILTIN, null);
        Object.setOperand(new RegisterOffset(1,Register.GB));

        // Définition de la méthode Equals
        int equalIndex = Object.incNumberOfMethods();
        Type returnType = envTypes.get(compiler.getSymbols().create("boolean")).getType();
        Signature signature = new Signature();
        signature.add(Object.getType());
        MethodDefinition equalDef = new MethodDefinition(returnType, Location.BUILTIN, signature, equalIndex);
        equalDef.setLabel(new Label("code.Object.equals"));


        /* On ajoute le tout aux environnements */

        // la méthode equals() à l'environnement de la classe Object
        Object.getMembers().declare(compiler.getSymbols().create("equals"), equalDef);
        // Object à l'environnement root
        envTypes.declare(compiler.getSymbols().create("Object"), Object);
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        GestionSP gestionSP = new GestionSP();
        int SP=gestionSP.returnSP(this);
        compiler.addInstruction(new TSTO(SP));
        compiler.addInstruction(new BOV(new Label("stack_overflow")));
        compiler.addInstruction(new ADDSP(SP));
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
        compiler.getRegManager().incrementGB();
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
        compiler.getRegManager().incrementGB();

        //pile initial
        for(AbstractDeclClass a: classes.getList()){
            a.codePreGenMethodClass(compiler);
            compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
            compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
            compiler.getRegManager().incrementGB();
            Integer i=2;
            while(a.returnIdentifier().getClassDefinition().containKey(i)){
                compiler.addInstruction(new LOAD(new LabelOperand(
                        a.returnIdentifier().getClassDefinition().getMethod(i).getIdentifier().getMethodDefinition().getLabel()),
                        Register.R0));
                compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegManager().getGB(),Register.GB)));
                compiler.getRegManager().incrementGB();
                i++;
            }


        }
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());



        //on ecrit maintenant les instructions des methodes
        compiler.addLabel(new Label("init.Object"));
        compiler.addInstruction(new RTS());
        compiler.addLabel(new Label("code.Object.equals"));
        //Identifier eq= new Identifier()
        //DeclMethod equals = new DeclMethod()
        //public boolean equals (Object other) {
        //    return this == other;
        //}
        compiler.addInstruction(new TSTO(2));
        compiler.addInstruction(new BOV(new Label("stack_overflow")));
        compiler.addInstruction(new PUSH(Register.getR(2)));
        compiler.addInstruction(new PUSH(Register.getR(3)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.getR(2))); // on charge l'object
        compiler.addInstruction(new LOAD(new RegisterOffset(-3,Register.LB),Register.getR(3))); // on charge l'object à test
        compiler.addInstruction(new CMP(Register.getR(2),Register.getR(3)));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new POP(Register.getR(3)));
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new RTS());

        for(AbstractDeclClass a: classes.getList()){
            a.codeGenFieldClass(compiler);
            a.codeGenMethodClass(compiler);
        }


    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
