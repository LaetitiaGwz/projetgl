package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dictionary associating identifier's NonTypeDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment (corresponding to superclass or superblock).
 * Searching a definition is done in the EnvironmentExp first and in the
 * parentEnvironment if it fails. Insertion is always done in the first
 * EnvironmentExp of the list.
 * 
 * @author gl41
 * @date 01/01/2016
 */
public class EnvironmentExp extends AbstractEnvironnement {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    
    protected EnvironmentExp parentEnvironment;
    /**
     * Contains definition methods, parameters, variables and fields
     */
    protected HashMap<Symbol, NonTypeDefinition> vars ;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment ;
        this.vars = new HashMap<Symbol, NonTypeDefinition>();
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     * @param key
     *          Name of the symbol
     * @return
     *          the definition of the symbol in the environment or null if 
     *          the symbol is undefined
     */
    public NonTypeDefinition get(Symbol key) {
        NonTypeDefinition result = vars.get(key);
        if (result != null) {
            return result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.get(key);
        }
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
        NonTypeDefinition res = vars.get(name);
        if (res != null) {
            throw new DoubleDefException();
        }
        else {
            this.vars.put(name, def);
        }
    }

    @Override
    public String toString() {
        String s = "Affichage des variables et méthodes : ";
        for (Map.Entry<Symbol, NonTypeDefinition> entry : vars.entrySet()) {
            s += "\nSymbol : " + entry.getKey().getName() + " Definition : " + entry.getValue();
        }
        s += "\n#############################";
        return s;
    }
}
