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
        for(AbstractIfThen a : getList()){
            a.decompile(s);
        }

    }

    protected void codeGenListIfThen(DecacCompiler compiler){

        for (AbstractIfThen i: getList()) {
            i.codeGenIfThen(compiler);

        }

    }
    protected void codePreGenListIfThen(DecacCompiler compiler){
        for (AbstractIfThen i: getList()) {
            i.codePreGenIfThen(compiler);

        }
    }
}
