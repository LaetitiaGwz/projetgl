package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's NonTypeDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment (corresponding to superclass or superblock).
 * Searching a definition is done in the EnvironmentExp first and in the
 * parentEnvironment if it fails. Insertion is always done in the first
 * EnvironmentExp of the list.
 * 
 * @author @AUTHOR@
 * @date @DATE@
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public NonTypeDefinition get(Symbol key) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment throws
     * DoubleDefException if the symbol is already declared in the first 
     * Environment, and shadows the old declaration if the declaration was
     * done in an ancestor Environment.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the last level of the
     *             environment.
     */
    public void declare(Symbol name, NonTypeDefinition def) throws DoubleDefException {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
