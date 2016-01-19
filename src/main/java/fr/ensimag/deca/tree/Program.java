package fr.ensimag.deca.tree;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
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
    private EnvironmentExp environmentExp;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");

        EnvironmentExp env = new EnvironmentExp(null);
        try {
            env.declareType(compiler.getSymbols().create("int"), new TypeDefinition(new IntType(compiler.getSymbols().create("int")), Location.BUILTIN));
            env.declareType(compiler.getSymbols().create("float"), new TypeDefinition(new FloatType(compiler.getSymbols().create("float")), Location.BUILTIN));
            env.declareType(compiler.getSymbols().create("String"), new TypeDefinition(new StringType(compiler.getSymbols().create("String")), Location.BUILTIN));
            env.declareType(compiler.getSymbols().create("boolean"), new TypeDefinition(new BooleanType(compiler.getSymbols().create("boolean")), Location.BUILTIN));
            env.declareType(compiler.getSymbols().create("void"), new TypeDefinition(new VoidType(compiler.getSymbols().create("void")), Location.BUILTIN));

            env.declareType(compiler.getSymbols().create("Object"), new ClassDefinition(new ClassType(compiler.getSymbols().create("Object"), Location.BUILTIN, null), Location.BUILTIN, null));
        }
        catch (EnvironmentExp.DoubleDefException $e) {
            throw new DecacInternalError("Double definition of builtin types.");
        }
        compiler.setRootEnv(env);


        getClasses().verifyListClass(compiler);
        getMain().verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {

        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
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
