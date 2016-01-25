package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class IfThen extends AbstractIfThen {

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getInstructions() {
        return instructions;
    }

    public IfThen(AbstractExpr condition, ListInst instructions) {
        Validate.notNull(condition);
        Validate.notNull(instructions);
        this.condition = condition;
        this.instructions = instructions;
    }

    private AbstractExpr condition;
    private ListInst instructions;

    @Override
    protected void verifyIfThen(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        condition.verifyCondition(compiler, localEnv, currentClass);

        instructions.verifyListInst(compiler, localEnv, currentClass, returnType);

    }
    @Override
    protected void codePreGenIfThen(DecacCompiler compiler){
        getCondition().codePreGenCMP(compiler);
        getInstructions().codePreGenListInst(compiler);
    }
    @Override
    protected void codeGenIfThen(DecacCompiler compiler,GPRegister register){
        // Calcul de la condition
        Label endIf = new Label("endIf"+compiler.getLblManager().getIf());
        compiler.getLblManager().incrementIf();
        getCondition().codegenExpr(compiler,register);
        compiler.addInstruction(new CMP(0,register));
        compiler.addInstruction(new BEQ(endIf));
        // Instructions
        getInstructions().codeGenListInst(compiler);
        compiler.addInstruction(new BRA(compiler.getLblManager().getLabelFalse()));
        compiler.addLabel(endIf);

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        getCondition().decompileCMP(s);
        s.println("){");
        getInstructions().decompile(s);
        s.println("}");

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        instructions.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, true);
    }
}
