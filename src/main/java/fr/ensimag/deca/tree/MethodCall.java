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

        ClassDefinition classDef = compiler.getEnvTypes().getClassDef(t.getName());
        Signature s = params.verifySignature(compiler, localEnv, currentClass);

        Type retType = method.verifyMethod(s, compiler, classDef.getMembers(), currentClass);
        setType(retType);
        return retType;
    }

    @Override
    public void codegenExpr(DecacCompiler compiler,GPRegister register){
        compiler.addInstruction(new ADDSP(1+params.size()));

        if(!params.isEmpty()){
            for(int j=0;j<params.size();j++){
                params.getList().get(j).codegenExpr(compiler,register);
                compiler.addInstruction(new STORE(register,new RegisterOffset(-j,Register.SP)));
            }
        }
        obj.codegenExpr(compiler,register);
        compiler.addInstruction(new STORE(register,new RegisterOffset(0,Register.SP)));
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));
        compiler.addInstruction(new SUBSP(1+params.size()));
        compiler.addInstruction(new STORE(register,(DAddr)obj.getDval()));
        }
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        compiler.addInstruction(new ADDSP(1+params.size()));
        GPRegister register =Register.getR(2);
        if(!params.isEmpty()){
            for(int j=0;j<params.size();j++){
                params.getList().get(j).codegenExpr(compiler,register);
                compiler.addInstruction(new STORE(register,new RegisterOffset(-j-1,Register.SP)));
            }
        }
        obj.codegenExpr(compiler,register);
        compiler.addInstruction(new STORE(register,new RegisterOffset(0,Register.SP)));
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));
        compiler.addInstruction(new SUBSP(1+params.size()));
        compiler.addInstruction(new STORE(Register.getR(2),(DAddr)obj.getDval()));

    }
    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        this.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.R0,Register.R1));
        if(method.getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else{
            compiler.addInstruction(new WINT());
        }


    }
    @Override
    protected void codeGenPrintX(DecacCompiler compiler){
        this.codegenExpr(compiler,Register.R1);
        compiler.addInstruction(new WFLOATX());

    }


    @Override
    public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print(" ");
        s.print(method.getName().toString()+"(");
        params.decompile(s);
        s.println(");");

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
