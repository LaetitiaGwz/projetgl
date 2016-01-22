package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Instruction
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractDeclMethod extends Tree {
    /**
     * Verify the method as a member for contextual error.
     *
     * @param compiler
     * @param localEnv
     *            Environment in which the instruction should be checked
     * @param currentClass
     *            Definition of the class containing the instruction, or null in
     *            the main program.
     */
    protected abstract void verifyMembers(DecacCompiler compiler,
           EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;
    /**
     * Verify the method body for contextual error.
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
    protected abstract void verifyBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;
    

    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     */
    protected abstract void codePreGenMethod(DecacCompiler compiler);
    protected abstract void codeGenMethod(DecacCompiler compiler);

    protected abstract String getName();
    public abstract AbstractIdentifier getIdentifier();

    /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon 
     * if needed
     */
    protected void decompileMethod(IndentPrintStream s) {
        decompile(s);
    }
}
