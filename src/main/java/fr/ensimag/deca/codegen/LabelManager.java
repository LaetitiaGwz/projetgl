package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Created by matthieu on 19/01/16.
 * Classe permettant de gérer l'état des labels dans la génération de code
 */
public class LabelManager {

    public LabelManager() {
        this.nbIf = 0;
        this.nbWhile = 0;
        this.nbOr = 0;
    }

    private Label labelTrue;
    public void setLabelTrue(Label target){
        this.labelTrue=target;
    }
    public Label getLabelTrue(){
        return this.labelTrue;
    }

    private Label labelFalse;
    public void setLabelFalse(Label target){
        this.labelFalse=target;
    }
    public Label getLabelFalse(){
        return this.labelFalse;
    }

    private int nbIf;// pour gerer les labels
    public void incrementIf(){
        this.nbIf++;
    }
    public int getIf(){
        return this.nbIf;
    }

    private int nbWhile;
    public void incrementWhile(){
        this.nbWhile++;
    }
    public int getWhile(){
        return this.nbWhile;
    }

    private int nbOr;
    public void incrementOr(){
        this.nbOr++;
    }
    public int getOr(){
        return this.nbOr;
    }
}
