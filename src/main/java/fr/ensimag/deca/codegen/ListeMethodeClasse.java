package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.LabelOperand;

import java.util.LinkedList;

/**
 * Created by gonthierflorentin on 19/01/16.
 */
public class ListeMethodeClasse {

    private LinkedList<LabelOperand> tableMethode;

    public ListeMethodeClasse(){
        this.tableMethode=new LinkedList<LabelOperand>();
    }

    public void addMethode(LabelOperand ajout){
        this.tableMethode.add(ajout);
    }

    public int size(){
        return this.tableMethode.size();
    }


}
