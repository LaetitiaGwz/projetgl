package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

public abstract class AbstractThis extends AbstractExpr{

    protected abstract void codeGenThis(DecacCompiler compiler);
}
