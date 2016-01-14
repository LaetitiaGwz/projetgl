package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.deca.codegen.MemoryMap;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Created by matthieu on 12/01/16.
 * Definition of a global variable
 */
public class GlobalVarDef extends InstructionList {
    public GlobalVarDef(SymbolTable.Symbol symbol, DVal value, GPRegister reg) {
        // LOAD #val, RI
        addInstruction(new LOAD(value,reg));
        // STORE RI, offset(GB)
        //RegisterOffset gbOffset = memoryMap.storeGlobalVariable(symbol);
        //addInstruction(new STORE(reg, gbOffset));
    }
}
