package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.AbstractExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl41
 * @date 01/01/2016
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public boolean equals(Object other) {
        if(other == null || (!(other instanceof Signature))) return false;
        Signature s = (Signature) other;
        if(size() != s.size()) return false;
        for (int i = 0; i < size(); i++) {
            if(!paramNumber(i).sameType(s.paramNumber(i))) return false;
        }
        return true;
    }

    public boolean accepts(Signature s) {
        if(s == null) {
            return false;
        }
        if(size() != s.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if(paramNumber(i).sameType(s.paramNumber(i))) {
                return true;
            }
            if(paramNumber(i).isClass() && s.paramNumber(i).isClass()) {
                if(!((ClassType)paramNumber(i)).isSubClassOf((ClassType)s.paramNumber(i))) {
                    return false;
                }
            }
            else if(!paramNumber(i).isFloat() || !s.paramNumber(i).isInt()) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size(); i++) {
            h *= paramNumber(i).getName().hashCode();
        }
        return h;
    }

    public String toString() {
        String s = "Affichage de la signature : ";
        for (int i = 0; i < size(); i++) {
            s += paramNumber(i) + ",";
        }
        return s;
    }
}
