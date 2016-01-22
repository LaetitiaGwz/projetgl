package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of variables within a declaration (e.g. x, y=42).
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclVar a : getList()){
            a.decompile(s);
        }

    }
}
