package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

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
        for (AbstractIfThen i: getList()){
            i.codeGenIfThen(compiler);
        }

    }
}
