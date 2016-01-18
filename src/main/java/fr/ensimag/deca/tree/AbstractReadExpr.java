package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * read...() statement.
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    protected void decompileInst(IndentPrintStream s){
        decompile(s);

    }


}
