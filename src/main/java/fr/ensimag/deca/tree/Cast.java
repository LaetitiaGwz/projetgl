package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Cast extends AbstractCast {

    AbstractIdentifier type;
    AbstractExpr expr;

    public Cast (AbstractIdentifier type, AbstractExpr expr) {
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type castType = type.verifyType(compiler);
        Type opType = expr.verifyExpr(compiler, localEnv, currentClass);

        if(!castCompatible(localEnv, opType, castType)) {
            throw new ContextualError("Incompatible cast from " + opType + " to " + castType, getLocation());
        }

        setType(castType);
        return castType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.println(")");


    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        // Récupération de l'expression calculée
        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        expr.codegenExpr(compiler,register); // Calcul de l'expression
        // Cast de l'expression
        if(type.getType().isFloat()){
            compiler.addInstruction(new FLOAT(register,register));
        }
        else if(type.getType().isClass()){//c'est une classe
            //if(a instanceof Bor a== null){ (B) (a) = a} else {error}
            Label caCast = new Label("cast_ok" + compiler.getLblManager().getIf()); // à la suite du else
            Label noCast= new Label("cast_not"+compiler.getLblManager().getIf());
            Label endCast= new Label("cast_end"+compiler.getLblManager().getIf());
            compiler.getLblManager().incrementIf(); // on s'assure qu'on en ai pas d'autre
            // Calcul de la condition
            if(type.getDval()==null){
                throw new DecacInternalError("element vide");
            }
            else{
                boolean[] backup =compiler.getRegManager().getTableRegistre();
                GPRegister stock;
                if(compiler.getRegManager().noFreeRegister()){
                    int i =compiler.getRegManager().getGBRegisterInt();
                    compiler.addInstruction(new PUSH(Register.getR(i)));
                    stock = Register.getR(i);
                    setPush();
                }
                else{
                    stock = compiler.getRegManager().getGBRegister();

                }

                int i=compiler.getLblManager().getIf();
                compiler.getLblManager().incrementIf();
                expr.codegenExpr(compiler,stock);
                compiler.addInstruction(new LOAD(new RegisterOffset(0,stock),stock));
                compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(),register));
                compiler.addLabel(new Label("debut.instanceof"+i));
                compiler.addInstruction(new CMP(stock,register));
                compiler.addInstruction(new BEQ(caCast)); //test si egal
                compiler.addInstruction(new LOAD(new RegisterOffset(0,stock),stock)); // on descend
                compiler.addInstruction(new CMP(new NullOperand(),stock)); //si object instance
                compiler.addInstruction(new BNE(new Label("debut.instanceof"+i))); //non, on remonte
                //on test null
                expr.codegenExpr(compiler,stock);
                compiler.addInstruction(new CMP(new NullOperand(),stock));
                compiler.addInstruction(new BNE(noCast));

                // Instructions
                compiler.addLabel(caCast);
                expr.codegenExpr(compiler,register);
                compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(),stock)); //classe, a forcément une adresse GB ou LB
                compiler.addInstruction(new STORE(stock,new RegisterOffset(0,register)));
                compiler.addInstruction(new BRA(endCast));
                compiler.addLabel(noCast);
                compiler.addInstruction(new WSTR("Erreur: bad cast from "+expr.getType().toString() +" to "+type.getName().toString()));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                compiler.addLabel(endCast);


                compiler.getRegManager().setTableRegistre(backup);
                // Instructions
                compiler.addLabel(caCast);
                compiler.addInstruction(new STORE(register,type.getVariableDefinition().getOperand())); //classe, a forcément une adresse GB ou LB

                compiler.addLabel(noCast);
                compiler.addInstruction(new WSTR("Erreur: bad cast from "+expr.getType().toString() +" to "+type.getName().toString()));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());


                if(getPop()){
                    compiler.addInstruction(new POP(stock));
                    popDone();
                }


            }


        }
        else if(type.getType().isInt()){
            compiler.addInstruction(new INT(register,register));
        }
        else {//pas de raison de cast, erreur à lever
            compiler.addInstruction(new WSTR("Erreur: bad cast from string or boolean "));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

        }

        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
        }
        compiler.getRegManager().setTableRegistre(table);

    }

    @Override
    public void codegenExpr(DecacCompiler compiler, GPRegister register) {

        if(type.getType().isFloat()){
            expr.codegenExpr(compiler, register);
            compiler.addInstruction(new FLOAT(register,register));
        }
        else if(type.getType().isInt()){
            expr.codegenExpr(compiler, register);
            compiler.addInstruction(new INT(register,register));
        }
        else if(type.getType().isClass()){//c'est une classe
            //expr.codegenExpr(compiler, register);
            //if(a instanceof Bor a== null){ (B) (a) = a} else {error}
            Label caCast = new Label("cast_ok" + compiler.getLblManager().getIf()); // à la suite du else
            Label noCast= new Label("cast_not"+compiler.getLblManager().getIf());
            Label endCast= new Label("cast_end"+compiler.getLblManager().getIf());
            compiler.getLblManager().incrementIf(); // on s'assure qu'on en ai pas d'autre
            // Calcul de la condition
            if(type==null){
                throw new DecacInternalError("element vide");
            }
            else{
                boolean[] backup =compiler.getRegManager().getTableRegistre();
                GPRegister stock;
                if(compiler.getRegManager().noFreeRegister()){
                    int i =compiler.getRegManager().getGBRegisterInt();
                    compiler.addInstruction(new PUSH(Register.getR(i)));
                    stock = Register.getR(i);
                    setPush();
                }
                else{
                    stock = compiler.getRegManager().getGBRegister();

                }

                int i=compiler.getLblManager().getIf();
                compiler.getLblManager().incrementIf();
                expr.codegenExpr(compiler,stock);
                compiler.addInstruction(new LOAD(new RegisterOffset(0,stock),stock));
                compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(),register));
                compiler.addLabel(new Label("debut.instanceof"+i));
                compiler.addInstruction(new CMP(stock,register));
                compiler.addInstruction(new BEQ(caCast)); //test si egal
                compiler.addInstruction(new LOAD(new RegisterOffset(0,stock),stock)); // on descend
                compiler.addInstruction(new CMP(new NullOperand(),stock)); //si object instance
                compiler.addInstruction(new BNE(new Label("debut.instanceof"+i))); //non, on remonte
                //on test null
                expr.codegenExpr(compiler,stock);
                compiler.addInstruction(new CMP(new NullOperand(),stock));
                compiler.addInstruction(new BNE(noCast));

                // Instructions
                compiler.addLabel(caCast);
                expr.codegenExpr(compiler,register);
                compiler.addInstruction(new BRA(endCast));
                compiler.addLabel(noCast);
                compiler.addInstruction(new WSTR("Erreur: bad cast from "+expr.getType().toString() +" to "+type.getName().toString()));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                compiler.addLabel(endCast);

                if(getPop()){
                    compiler.addInstruction(new POP(stock));
                    popDone();
                }
                compiler.getRegManager().setTableRegistre(backup);


            }


        }

        else {//pas de raison de cast, erreur à lever
            compiler.addInstruction(new WSTR("Erreur: bad cast from "+expr.getType().toString() +" to "+type.getName().toString()));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

        }


    }

    @Override
    public DVal getDval() {
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        expr.codegenExpr(compiler,register);
        if(this.getType().isFloat()) {
            compiler.addInstruction(new FLOAT(register, Register.R1));
            compiler.addInstruction(new WFLOAT());
        }else {
            compiler.addInstruction(new INT(register, Register.R1));
            compiler.addInstruction(new WINT());
        }
        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
        }
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler){
        boolean[] table=compiler.getRegManager().getTableRegistre(); //on verifie les registre
        Validate.isTrue(this.getType().isFloat());
        GPRegister register;
        if(compiler.getRegManager().noFreeRegister()){
            int i =compiler.getRegManager().getGBRegisterInt();
            compiler.addInstruction(new PUSH(Register.getR(i)));
            register = Register.getR(i);
            setPush();
        }
        else{
            register = compiler.getRegManager().getGBRegister();

        }
        expr.codegenExpr(compiler,register);
        compiler.addInstruction(new FLOAT(register, Register.R1));
        compiler.addInstruction(new WFLOATX());
        if(getPop()){
            compiler.addInstruction(new POP(register));
            popDone();
        }
    }
}
