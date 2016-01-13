package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        int i= compiler.getTableRegistre().getLastregistre();
        compiler.getTableRegistre().setEtatRegistreTrue(i);
        if(this.getLeftOperand().getType().sameType(new IntType(compiler.getSymbols().create("int")))){
            this.getLeftOperand().codeGenInst(compiler);
            int j= this.getLeftOperand().getRegistreUtilise();
            this.getRightOperand().codeGenInst(compiler);

            compiler.addInstruction(new STORE(Register.getR(j), ));
            compiler.addInstruction(new MUL(,Register.getR(j)));
        }
        else if(this.getLeftOperand().getType().sameType(new IntType(compiler.getSymbols().create("float")))){
            this.getLeftOperand().codeGenInst(compiler);
            int j=this.getLeftOperand().getRegistreUtilise();
            this.getRightOperand().codeGenInst(compiler);


            compiler.addInstruction(new MUL(,Register.getR(j)));


        }



    }

}
