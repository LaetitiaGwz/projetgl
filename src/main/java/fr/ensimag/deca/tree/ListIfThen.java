package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class ListIfThen extends TreeList<AbstractIfThen> {

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void codeGenListIfThen(DecacCompiler compiler){

        int lastLabelNb = compiler.getIf() + getList().size() + 1;
        for (AbstractIfThen i: getList()) {
            i.codeGenIfThen(compiler);
            compiler.addInstruction(new BRA(new Label("fin_if" + lastLabelNb)));
        }

    }
}
