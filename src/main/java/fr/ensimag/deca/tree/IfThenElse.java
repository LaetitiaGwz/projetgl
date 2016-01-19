package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl41
 * @date 01/01/2016
 */
public class IfThenElse extends AbstractInst {
    private ListIfThen ifThen;
    private ListInst elseBranch;

    public IfThenElse(ListIfThen ifThen, ListInst elseBranch) {
        Validate.notNull(ifThen);
        Validate.notNull(elseBranch);
        this.ifThen = ifThen;
        this.elseBranch = elseBranch;
    }

    public ListIfThen getIfThen() {
        return ifThen;
    }

    public ListInst getElseBranch() {
        return elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        for(AbstractIfThen ifBranch : ifThen.getList()) {
            ifBranch.verifyIfThen(compiler, localEnv, currentClass, returnType);
        }
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label finIf = new Label("fin_if" + compiler.getIf()); // à la suite du else
        compiler.incrementeIf(); // on s'assure qu'on en ai pas d'autre
        compiler.setLabel(finIf); // on le sécurise pour la suite
        getIfThen().codeGenListIfThen(compiler);
        getElseBranch().codeGenListInst(compiler);
        compiler.addLabel(finIf);
        }

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractIfThen a : getIfThen().getList()){
            a.decompile(s);
        }
        s.print("esle{");
        for(AbstractInst b : getElseBranch().getList()){
            b.decompile(s);

        }
        s.println("}");

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        ifThen.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ifThen.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
