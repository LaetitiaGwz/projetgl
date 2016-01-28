package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

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
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre

        GPRegister stock;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new TSTO(1));
            compiler.addInstruction(new BOV(new Label("stack_overflow")));
            compiler.addInstruction(new PUSH(Register.getR(i)));
            stock = Register.getR(i);
            setPush();
        }
        else{
            stock = compiler.getRegManager().getGBRegister();

        }
        for (AbstractIfThen i: getList()) {
            i.codeGenIfThen(compiler,stock);
        }
        if(getPop()){
            compiler.addInstruction(new POP(stock));
            popDone();
        }
        compiler.getRegManager().setTableRegistre(table);

    }
    protected void codePreGenListIfThen(DecacCompiler compiler){
        boolean[] table = compiler.getFakeRegManager().getTableRegistre(); //on verifie les registre
        compiler.getFakeRegManager().getGBRegister();
        compiler.addMaxFakeRegister(compiler.getFakeRegManager().getLastregistre());

        for (AbstractIfThen i: getList()) {
            i.codePreGenIfThen(compiler);

        }
        compiler.getFakeRegManager().setTableRegistre(table);
    }
}
