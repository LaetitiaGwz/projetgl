package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if(getPrintHex())
                a.codeGenPrintX(compiler);
            else
                a.codeGenPrint(compiler);
        }
        compiler.addInstruction(new WNL());
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
