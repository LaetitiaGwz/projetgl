package fr.ensimag.deca.tree;

import com.sun.org.apache.xpath.internal.operations.Bool;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
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
    protected void codeGenIfThen(DecacCompiler compiler){
        // Calcul de la condition
        getCondition().codeGenInst(compiler);
        // Instructions
        getInstructions().codeGenListInst(compiler);

    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
