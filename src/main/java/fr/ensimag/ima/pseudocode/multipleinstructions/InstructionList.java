package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.ima.pseudocode.Instruction;

import java.util.LinkedList;

/**
 * Created by matthieu on 12/01/16.
 * Liste d'instructions
 */
public abstract class InstructionList {
    private LinkedList<Instruction> list = new LinkedList<Instruction>();

    /**
     * Ajoute une instruction à la liste
     * @param i l'instruction qui va être ajoutée en fin de liste
     */
    protected void addInstruction(Instruction i){
        list.add(i);
    }

    public LinkedList<Instruction> getList() {
        return list;
    }
}
