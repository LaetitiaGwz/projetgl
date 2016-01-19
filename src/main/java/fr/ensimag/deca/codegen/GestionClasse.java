package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.LabelOperand;

import java.util.LinkedList;

/**
 * Created by gonthierflorentin on 18/01/16.
 */
public class GestionClasse {


    private LinkedList<LabelOperand> listeDeclClasse;

    public GestionClasse(){
        this.listeDeclClasse= new LinkedList<LabelOperand>();
    }

    public void ajoutLabel(LabelOperand ajout){
        this.listeDeclClasse.add(ajout);
    }

    public int sizeLabel(){
        return this.listeDeclClasse.size();
    }

}
