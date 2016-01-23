package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractIdentifier extends AbstractLValue {

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    public abstract ClassDefinition getClassDefinition();

    public abstract Definition getDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract FieldDefinition getFieldDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    public abstract MethodDefinition getMethodDefinition();

    public abstract SymbolTable.Symbol getName();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a NonTypeDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract NonTypeDefinition getNonTypeDefinition();


    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    public abstract VariableDefinition getVariableDefinition();

    public abstract void setDefinition(Definition definition);

    protected abstract void codeGenInitClass(DecacCompiler compiler);
    protected abstract void codeGenInit(DecacCompiler compiler);
    protected abstract void codeGenInitMethod(DecacCompiler compiler);

    /**
     * If this object identifies a type, this function gets its definition from the
     * env_types of the program. It also sets its definition and its type so that both
     * can be displayed in the abstract tree.
     * @param compiler The compiler main class containing the environment.
     * @return The corresponding type in the environment.
     * @throws ContextualError If no such type exists in the environment.
     */
    public abstract Type verifyType(DecacCompiler compiler) throws ContextualError;

    /**
     * If this object identifies a class, this function gets its definition from the
     * env_types of the program. It also sets its definition and its type so that both
     * can be displayed in the abstract tree.
     * @param compiler The compiler main class containing the environment.
     * @return The corresponding ClassType in the environment.
     * @throws ContextualError If no such class exists in the environment.
     */
    public abstract Type verifyClass(DecacCompiler compiler) throws ContextualError;

    /**
     * If this object identifies a method, this function gets its definition from the
     * localEnv. It also sets its definition and its type so that both can be displayed
     * in the abstract tree.
     * Moreover signatures from fetched definition and parameters
     * are checked to be identical or a ContextualError is thrown.
     * @param s The signature which has to be checked.
     * @param compiler The compiler main class containing the environment.
     * @param localEnv The environment in which the method is fetch.
     * @return The return type of the method checked from its definition.
     * @throws ContextualError If the signatures don't correspond or the method doesn't exist
     * in the specified environment.
     */
    public abstract Type verifyMethod(Signature s, DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;

}
