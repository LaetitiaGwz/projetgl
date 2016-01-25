package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclMethod extends AbstractDeclMethod {

    protected ListInst body;
    protected ListDeclVarSet declVars;

    protected EnvironmentExp methodEnv;

    private Line tstoInst;

    @Override
    public AbstractIdentifier getIdentifier(){
        return this.name;
    }

    @Override
    protected void setTSTO(DecacCompiler compiler, int maxStackSize) {
        tstoInst.setInstruction(new TSTO(maxStackSize));
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
    protected void verifyBody(fr.ensimag.deca.DecacCompiler compiler,
                              EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // On instancie l'environnement de la méthode dont l'env parent est celui de la classe
        methodEnv = new EnvironmentExp(currentClass.getMembers());

        params.verifyBody(compiler, methodEnv, currentClass);
        declVars.verifyListDeclVariable(compiler, methodEnv, currentClass);
        body.verifyListInst(compiler, methodEnv, currentClass, ret.getType());
    }

    @Override
    protected void codePreGenMethod(fr.ensimag.deca.DecacCompiler compiler) {
        name.getMethodDefinition().setLabel(compiler.getLblManager().getLabelFalse());

    }
    @Override
    protected void codeGenMethod(fr.ensimag.deca.DecacCompiler compiler) {
        declVars.codePreGenListDeclMethod(compiler);
        compiler.add(new Line(name.getMethodDefinition().getLabel()));
        tstoInst = new Line(new TSTO(1));
        compiler.add(tstoInst);
        compiler.add(new Line(new BOV(new Label("stack_overflow"))));

        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        for(int i=2;i<compiler.getCompilerOptions().getRegistre();i++){
            compiler.addInstruction(new PUSH(Register.getR(i)));
        }
        Label fin = new Label("fin."+getIdentifier().getMethodDefinition().getLabel().toString());
        compiler.getLblManager().setLabelFalse(fin);
        params.codeGenListDecl(compiler);
        declVars.codeGenListDeclMethod(compiler);
        body.codeGenListInst(compiler);
        if(!ret.getType().isVoid()){
            compiler.addInstruction(new WSTR("Erreur : sortie de la methode "+name.getName().toString() +" sans return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        compiler.addLabel(fin);


        for(int i=compiler.getCompilerOptions().getRegistre()-1;i>1;i--){
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.getRegManager().setTableRegistre(table); //on les remets à la fin
        compiler.addInstruction(new RTS());

    }



    @Override
    public void decompile(IndentPrintStream s) {
        s.print(ret.getName().toString()+" "+ name.getName().toString()+"(");
        params.decompile(s);
        s.println("){");
        declVars.decompile(s);
        body.decompile(s);
        s.println("}");


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
