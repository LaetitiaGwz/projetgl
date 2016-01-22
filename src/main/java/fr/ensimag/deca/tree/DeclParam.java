package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclParam extends AbstractDeclParam {

    public AbstractIdentifier getVarName() {
        return varName;
    }
    private int indice;
    private AbstractIdentifier type;
    private AbstractIdentifier varName;
    public void setIndice(int i){
        indice=i;
    }
    public int getIndice(){
        return indice;
    }


    public DeclParam(AbstractIdentifier type, AbstractIdentifier varName) {
        Validate.notNull(varName);
        Validate.notNull(type);
        this.varName = varName;
        this.type = type;
    }

    @Override
    protected Type verifyMembers(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        return type.verifyType(compiler);
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        VariableDefinition paramDef = new VariableDefinition(type.getType(), getLocation());

        try {
            localEnv.declare(compiler.getSymbols().create(varName.getName().getName()), paramDef);
        } catch (AbstractEnvironnement.DoubleDefException e) {
            throw new ContextualError("Double definition of variable " + varName.getName().getName(), getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }


    @Override
    protected void codeGenDecl(DecacCompiler compiler) {
        varName.getNonTypeDefinition().setOperand(new RegisterOffset(indice, Register.LB));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName().toString()+" "+varName.getName().toString());
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        varName.iter(f);
        type.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varName.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }
}
