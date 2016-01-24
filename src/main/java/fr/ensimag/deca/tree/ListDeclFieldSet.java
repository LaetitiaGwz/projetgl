package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ListDeclFieldSet extends TreeList<AbstractDeclFieldSet> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclFieldSet a : getList()){
            a.decompile(s);
        }
        s.println();

    }

    void verifyMembers(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclFieldSet field : getList()) {
            field.verifyMembers(compiler, localEnv, currentClass);
        }
    }

    void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv,
                    ClassDefinition currentClass) throws ContextualError {
        for(AbstractDeclFieldSet field : getList()) {
            field.verifyBody(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDecl(DecacCompiler compiler) {
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        for(int i=2;i<compiler.getCompilerOptions().getRegistre();i++){
            compiler.addInstruction(new PUSH(Register.getR(i)));
        }
        compiler.getRegManager().resetTableRegistre();
        for(AbstractDeclFieldSet a : getList()){
            a.codeGenFieldSet(compiler);
        }
        for(int i=compiler.getCompilerOptions().getRegistre()-1;i>1;i--){
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.getRegManager().setTableRegistre(table); //on les remets à la fin
    }

}
