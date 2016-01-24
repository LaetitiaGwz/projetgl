package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.AbstractMain;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.Program;

/**
 * Created by gonthierflorentin on 24/01/16.
 */
public class GestionSP {

    private int SP=0;
    public int returnSP(Program programm){
        ListDeclClass classe= programm.getClasses();
        AbstractMain main = programm.getMain();
        this.SP+=returnSp(classe);
        this.SP+=main.returnSP();
        return SP;
    }

    public int returnSp(ListDeclClass classes){
        int stock=1;
        for(AbstractDeclClass a : classes.getList()){
            stock+=a.returnSP();
        }
        return stock;


    }









}
