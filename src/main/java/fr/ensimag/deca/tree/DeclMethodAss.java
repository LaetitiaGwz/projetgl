package fr.ensimag.deca.tree;

import com.sun.tools.classfile.Instruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclMethodAss extends AbstractDeclMethod {

    protected String code;
    private Line tstoInst;

    protected EnvironmentExp methodEnv;

    @Override
    public AbstractIdentifier getIdentifier(){
        return this.name;
    }

    @Override
    protected void setTSTO(DecacCompiler compiler, int maxStackSize) {
       //on fait rien
    }

    public DeclMethodAss(AbstractIdentifier name, AbstractIdentifier ret, ListDeclParam params, String code) {
        Validate.notNull(name);
        Validate.notNull(ret);
        Validate.notNull(params);
        Validate.notNull(code);

        this.name = name;
        this.ret = ret;
        this.params = params;
        this.code = code;
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // On instancie l'environnement de la méthode dont l'env parent est celui de la classe
        methodEnv = new EnvironmentExp(currentClass.getMembers());

        params.verifyBody(compiler, methodEnv, currentClass);
    }


    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        compiler.addLabel(getIdentifier().getMethodDefinition().getLabel());
        compiler.add(new InlinePortion(code.substring(1,code.length()-1).replaceAll("\\\\\"", "\"")));
    }

    @Override
    protected void codePreGenMethod(DecacCompiler compiler){
        getIdentifier().getMethodDefinition().setLabel(new Label(getIdentifier().getName().toString()));
    }
    @Override
    protected String getName(){
        return name.getName().toString();
    }

    @Override
    public void decompile(IndentPrintStream s) {
    s.println(code);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        name.iter(f);
        ret.iter(f);
        params.iter(f);
        //code.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        ret.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        //code.prettyPrint(s, prefix, true);
    }
}
