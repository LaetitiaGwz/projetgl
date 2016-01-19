package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.HashMap;
import java.util.Map;

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
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    
    protected EnvironmentExp parentEnvironment;
    protected HashMap<Symbol, Definition> environment ;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment ;
        this.environment = new HashMap<Symbol, Definition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
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
        Definition result = environment.get(key);
        if (result != null && (result instanceof NonTypeDefinition)) {
            return (NonTypeDefinition) result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.get(key);
        }
    }

    public TypeDefinition getTypeDef(Symbol key) {
        Definition result = environment.get(key);
        if (result != null && (result instanceof TypeDefinition)) {
            return (TypeDefinition) result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.getTypeDef(key);
        }
    }

    public ClassDefinition getClassDef(Symbol key) {
        Definition result = environment.get(key);
        if (result != null && (result instanceof ClassDefinition)) {
            return (ClassDefinition) result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.getClassDef(key);
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
        Definition res = environment.get(name);
        if (res != null && (res instanceof NonTypeDefinition)) {
            throw new DoubleDefException();
        }
        else {
            this.environment.put(name, def);
        }
    }
    public void declareClass(Symbol name, ClassDefinition def) throws DoubleDefException {
        Definition res = environment.get(name);
        if (res != null && (res instanceof ClassDefinition)) {
            throw new DoubleDefException();
        }
        else {
            this.environment.put(name, def);
        }
    }
    public void declareType(Symbol name, TypeDefinition def) throws DoubleDefException {
        Definition res = environment.get(name);
        if (res != null && (res instanceof TypeDefinition)) {
            throw new DoubleDefException();
        }
        else {
            this.environment.put(name, def);
        }
    }

    @Override
    public String toString() {
        String s = "Affichage de l'environnement : \n";

        for (Map.Entry<Symbol, Definition> entry : environment.entrySet()) {
            s += "\nSymbol : " + entry.getKey().getName() + " Definition : " + entry.getValue();
        }
        return s;
    }
}
