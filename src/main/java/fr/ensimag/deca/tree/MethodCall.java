package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{

    AbstractExpr obj;
    AbstractIdentifier method;
    ListExpr params;

    public MethodCall(AbstractExpr obj, AbstractIdentifier method, ListExpr params) {
        Validate.notNull(obj);
        Validate.notNull(method);
        Validate.notNull(params);

        this.obj = obj;
        this.method = method;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type t = obj.verifyExpr(compiler, localEnv, currentClass);

        ClassDefinition classDef = compiler.getRootEnv().getClassDef(t.getName());
        Signature s = params.verifySignature(compiler, localEnv, currentClass);

        Type retType = method.verifyMethod(s, compiler, classDef.getMembers(), currentClass);
        setType(retType);
        return retType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        int i = compiler.getRegManager().getLastregistre();
        GPRegister reg = Register.getR(i);
        compiler.getRegManager().setEtatRegistreTrue(i);
        compiler.addInstruction(new ADDSP(1+params.size()));
        if(!params.isEmpty()){
            for(int j=1;j<params.size()+1;j++){
                params.getList().get(j).codeGenInst(compiler);
                compiler.addInstruction(new LOAD(params.getList().get(j).getdValue(),reg));
                compiler.addInstruction(new STORE(reg,new RegisterOffset(-j,Register.SP)));
            }
        }
        obj.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(obj.getdValue(),reg));
        compiler.addInstruction(new STORE(reg,new RegisterOffset(0,Register.SP)));
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));
        compiler.addInstruction(new SUBSP(1+params.size()));
        this.setdValue(Register.R0); // on sauvergarde toujours au cas où
    }

    @Override
    protected void codeGenOPRight(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }
    @Override
    protected void codeGenOPLeft(DecacCompiler compiler){
        this.codeGenInst(compiler);
    }
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        method.iter(f);
        params.iter(f);
    }
}
