package fr.ensimag.deca.codegen;

import com.sun.org.apache.xpath.internal.operations.Variable;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.multipleinstructions.InstructionList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by matthieu on 12/01/16.
 * Stores the variables defining the differents part of the memory
 */
public class MemoryMap {
    private static final int GB_BASE = 0; // TODO : prendre en compte la taille de la table des méthodes
    private static final int SP_BASE = 0;
    private static final int LB_BASE = 0;
    private static final int PC_BASE = 0;

    private int GB; // Global Pointer
    private int SP; // Stack Pointer
    private int LB; // Local Pointer
    private int PC; // Program Counter

    // Map to store the location of the stored variables in memory
    private Map<SymbolTable.Symbol,DAddr> globalVariables = new HashMap<SymbolTable.Symbol, DAddr>();

    public MemoryMap() {
        GB = GB_BASE;
        SP = SP_BASE;
        LB = LB_BASE;
        PC = PC_BASE;
    }

    public RegisterOffset getGlobalVariable(SymbolTable.Symbol symbol){
        GB--;
        return (RegisterOffset) globalVariables.get(symbol);
    }

    public RegisterOffset storeGlobalVariable(SymbolTable.Symbol symbol){

        RegisterOffset gbOffset = new RegisterOffset(GB,Register.GB);
        globalVariables.put(symbol,gbOffset);
        GB++;
        return gbOffset;
    }

}
