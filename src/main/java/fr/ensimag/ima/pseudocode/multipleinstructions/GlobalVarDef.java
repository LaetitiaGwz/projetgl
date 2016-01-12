package fr.ensimag.ima.pseudocode.multipleinstructions;

import fr.ensimag.deca.codegen.MemoryMap;
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
    public GlobalVarDef(String var, int value, GPRegister reg, MemoryMap memoryMap) {
        // LOAD #val, RI
        addInstruction(new LOAD(value,reg));
        // STORE RI, offset(GB)
        RegisterOffset gbOffset = new RegisterOffset(memoryMap.storeGlobalVariable(value,var), Register.GB);
        addInstruction(new STORE(reg, gbOffset));
    }
}
