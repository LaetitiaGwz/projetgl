package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a Deca type (builtin or class).
 *
 * @author gl41
 * @date 01/01/2016
 */
public class TypeDefinition extends Definition {

    public TypeDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getKind() {
        return "type";
    }

    @Override
    public boolean isExpression() {
        return false;
    }

}
