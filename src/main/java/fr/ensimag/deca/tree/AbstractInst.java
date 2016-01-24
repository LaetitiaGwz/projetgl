package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Instruction
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractInst extends Tree {


    /**
     * Verify the instruction for contextual error.
     * 
     * @param compiler
     * @param localEnv
     *            Environment in which the instruction should be checked
     * @param currentClass
     *            Definition of the class containing the instruction, or null in
     *            the main program.
     * @param returnType
     *            Return type of the method being analyzed (may be void). void
     *            in the main program.
     */
    protected abstract void verifyInst(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError;
    

    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     * @param endLabel
     *            label following the last generated line 
     *            (null if absent or unknown)
     */
    protected abstract void codeGenInst(DecacCompiler compiler);

     /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon 
     * if needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }
}
