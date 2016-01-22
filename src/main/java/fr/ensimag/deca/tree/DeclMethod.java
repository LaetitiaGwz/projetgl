package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclMethod extends AbstractDeclMethod {

    AbstractIdentifier name;
    AbstractIdentifier ret;
    ListDeclParam params;
    ListInst body;
    ListDeclVarSet declVars;

    EnvironmentExp methodEnv;

    @Override
    public AbstractIdentifier getIdentifier(){
        return this.name;
    }
    public DeclMethod(AbstractIdentifier name, AbstractIdentifier ret, ListDeclParam params, ListInst body, ListDeclVarSet declVars) {
        Validate.notNull(name);
        Validate.notNull(ret);
        Validate.notNull(params);
        Validate.notNull(body);
        Validate.notNull(declVars);

        this.name = name;
        this.ret = ret;
        this.params = params;
        this.body = body;
        this.declVars = declVars;
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler,
                                EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type returnType = ret.verifyType(compiler);
        Signature signature = params.verifyMembers(compiler, localEnv, currentClass);

        // On vérifie d'abord que la méthode n'est pas
        //  déjà déclarée dans l'environnement parent
        MethodDefinition parentDef = localEnv.getMethodDef(name.getName());
        int index;
        if(parentDef == null) {
            index = currentClass.incNumberOfMethods();
        }
        else {
            index = parentDef.getIndex();
        }

        MethodDefinition methodDef = new MethodDefinition(returnType, getLocation(), signature, index);

        try {
            localEnv.declareMethod(name.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Double declaration of method " + name.getName().getName(), getLocation());
        }

        name.verifyMethod(signature, compiler, localEnv, currentClass);
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // On instancie l'environnement de la méthode dont l'env parent est celui de la classe
        methodEnv = new EnvironmentExp(currentClass.getMembers());

        params.verifyBody(compiler, methodEnv, currentClass);
        declVars.verifyListDeclVariable(compiler, methodEnv, currentClass);
        body.verifyListInst(compiler, methodEnv, currentClass, ret.getType());
    }

    @Override
    protected void codePreGenMethod(DecacCompiler compiler) {
        name.getMethodDefinition().setLabel(compiler.getLblManager().getLabelFalse());
    }
    @Override
    protected void codeGenMethod(DecacCompiler compiler) {
        compiler.addLabel(name.getMethodDefinition().getLabel());
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        for(int i=2;i<compiler.getCompilerOptions().getRegistre();i++){
            compiler.addInstruction(new PUSH(Register.getR(i)));
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),Register.getR(2)));// on sauve l'objet dans R2 tt le temps
        compiler.getRegManager().setEtatRegistreTrue(2);
        Label fin = new Label("fin."+getIdentifier().getMethodDefinition().getLabel().toString());
        compiler.getLblManager().setLabelFalse(fin);
        params.codeGenListDecl(compiler);
        declVars.codeGenListDeclMethod(compiler);
        body.codeGenListInst(compiler);
        compiler.addLabel(fin);







        for(int i=compiler.getCompilerOptions().getRegistre()-1;i>1;i--){
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.getRegManager().setTableRegistre(table); //on les remets à la fin
        compiler.addInstruction(new RTS());
        compiler.getRegManager().initialiseLB();

    }



    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getName(){
        return ret.getName().toString();
    }
    @Override
    protected
    void iterChildren(TreeFunction f) {
        name.iter(f);
        ret.iter(f);
        params.iter(f);
        declVars.iter(f);
        body.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ret.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        declVars.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
