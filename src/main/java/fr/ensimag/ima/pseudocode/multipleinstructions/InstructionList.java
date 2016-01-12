package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.ima.pseudocode.Instruction;

import java.util.LinkedList;

/**
 * Created by matthieu on 12/01/16.
 * List of instructions
 */
public abstract class InstructionList {
    private LinkedList<Instruction> list = new LinkedList<Instruction>();

    protected void addInstruction(Instruction i){
        list.add(i);
    }

    public LinkedList<Instruction> getList() {
        return list;
    }
}
