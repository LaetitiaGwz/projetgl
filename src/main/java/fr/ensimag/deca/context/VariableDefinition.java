package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a variable.
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class VariableDefinition extends NonTypeDefinition {
    public VariableDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getKind() {
        return "variable";
    }

    @Override
    public boolean isExpression() {
        return true;
    }
}
