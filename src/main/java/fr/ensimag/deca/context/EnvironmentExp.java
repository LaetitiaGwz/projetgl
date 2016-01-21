package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    
    protected EnvironmentExp parentEnvironment;
    /**
     * Contains definition parameters, variables and fields
     */
    protected HashMap<Symbol, NonTypeDefinition> vars ;
    /**
     * Contains definition of methods
     */
    class MethodKey {
        public Symbol symbol;
        public Signature signature;
        public MethodKey(Symbol symbol, Signature signature) {
            this.symbol = symbol;
            this.signature = signature;
        }
        public int hashCode() {
            return symbol.hashCode()*signature.hashCode();
        }
        public boolean equals(Object k) {
            if(k == null) return false;
            if(!(k instanceof MethodKey)) return false;
            return symbol.equals(((MethodKey)k).symbol) && signature.equals(((MethodKey)k).signature);
        }
    }
    protected HashMap<MethodKey, MethodDefinition> methods ;
    /**
     * Contains definition of types and classes
     */
    protected HashMap<Symbol, TypeDefinition> types ;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment ;
        this.vars = new HashMap<Symbol, NonTypeDefinition>();
        this.methods = new HashMap<MethodKey, MethodDefinition>();
        this.types = new HashMap<Symbol, TypeDefinition>();
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
    public MethodDefinition getMethodDef(Symbol key, Signature s) {
        MethodDefinition result = methods.get(new MethodKey(key, s));
        if (result != null) {
            return result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.getMethodDef(key, s);
        }
    }

    public TypeDefinition getTypeDef(Symbol key) {
        TypeDefinition result = types.get(key);
        if (result != null) {
            return result ;
        }
        else if(parentEnvironment == null) {
            return null;
        }
        else{
            return parentEnvironment.getTypeDef(key);
        }
    }

    public ClassDefinition getClassDef(Symbol key) {
        Definition result = types.get(key);
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
        NonTypeDefinition res = vars.get(name);
        if (res != null) {
            throw new DoubleDefException();
        }
        else {
            this.vars.put(name, def);
        }
    }
    public void declareMethod(Symbol name, MethodDefinition def) throws DoubleDefException {
        MethodKey key = new MethodKey(name, def.getSignature());
        MethodDefinition res = methods.get(key);
        if (res != null) {
            throw new DoubleDefException();
        }
        else {
            this.methods.put(key, def);
        }
    }
    public void declareType(Symbol name, TypeDefinition def) throws DoubleDefException {
        TypeDefinition res = types.get(name);
        if (res != null) {
            throw new DoubleDefException();
        }
        else {
            this.types.put(name, def);
        }
    }

    @Override
    public String toString() {
        String s = "Affichage des variables : ";
        for (Map.Entry<Symbol, NonTypeDefinition> entry : vars.entrySet()) {
            s += "\nSymbol : " + entry.getKey().getName() + " Definition : " + entry.getValue();
        }
        s += "\nAffichage des méthodes : ";
        for (Map.Entry<MethodKey, MethodDefinition> entry : methods.entrySet()) {
            s += "\nSymbol : " + entry.getKey().symbol.getName() + " Definition : " + entry.getValue();
        }
        s += "\nAffichage des types : ";
        for (Map.Entry<Symbol, TypeDefinition> entry : types.entrySet()) {
            s += "\nSymbol : " + entry.getKey().getName() + " Definition : " + entry.getValue();
        }
        s += "\n#############################";
        return s;
    }
}
