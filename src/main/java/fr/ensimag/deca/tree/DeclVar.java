package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.multipleinstructions.GlobalVarDef;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclVar extends AbstractDeclVar {

    public AbstractIdentifier getVarName() {
        return varName;
    }
    
    public AbstractInitialization getInitialization() {
        return initialization;
    }
    
    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(Type t, DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // On tente de déclarer la variable dans l'environnement. Sinon erreur contextuelle.
        try {
            localEnv.declare(getVarName().getName(), new VariableDefinition(t, getLocation()));
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Multiple declaration of variable " + getVarName(), getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        SymbolTable.Symbol symbol = this.getVarName().getName();

        GPRegister r0 = Register.R0;
        Initialization toto = (Initialization)initialization;
        compiler.addInstructionList(new GlobalVarDef(symbol, 1, r0, compiler.getMemoryMap()));
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
