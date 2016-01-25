package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Pair (condition, instructions) representing one element of an
 * if/else if/else if/ sequence.
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractIfThen extends Tree {
    protected abstract void verifyIfThen(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
            throws ContextualError;

    protected abstract void codeGenIfThen(DecacCompiler compiler,GPRegister register);
    protected abstract void codePreGenIfThen(DecacCompiler compiler);
}
