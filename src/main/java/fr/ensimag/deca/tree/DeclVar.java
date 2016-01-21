package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
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
        
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);

        // On tente de déclarer la variable dans l'environnement. Sinon erreur contextuelle.
        try {
            localEnv.declare(compiler.getSymbols().create(getVarName().getName().getName()), new VariableDefinition(t, getLocation()));
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Multiple declaration of variable " + getVarName().getName().getName(), getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        getVarName().codeGenInit(compiler);
        if(getInitialization().getExpression()!=null){
            getInitialization().codeGenInit(compiler);
            Register regLeft = (Register) this.getInitialization().getExpression().getdValue();
            DAddr adress = this.getVarName().getNonTypeDefinition().getOperand();
            compiler.addInstruction(new STORE(regLeft, adress));
            compiler.resetTableRegistre();
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getVarName().decompile(s);
        getInitialization().decompile(s);
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
