package fr.ensimag.deca.codegen;

import com.sun.org.apache.xpath.internal.operations.Variable;

import java.util.HashMap;
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
    private Map<String,Integer> globalVariables = new HashMap<String, Integer>();

    public MemoryMap() {
        GB = GB_BASE;
        SP = SP_BASE;
        LB = LB_BASE;
        PC = PC_BASE;
    }

    public int getGlobalVariable(String var){
        return globalVariables.get(var);
    }

    public int storeGlobalVariable(int value, String var){
        globalVariables.put(var,value);
        return addGB(4);
    }

    /**
     * Increment GB by val
     * @param val the value that will be added to GB
     * @return the previous value of GB
     */
    public int addGB(int val){
        int previous = GB;
        GB += val;
        return previous;
    }

    /**
     *
     * @param val
     * @return
     */
    public int addSP(int val){
        int previous = SP;
        SP += val;
        return previous;
    }

    public int addLB(int val){
        int previous = LB;
        LB += val;
        return previous;
    }
}
