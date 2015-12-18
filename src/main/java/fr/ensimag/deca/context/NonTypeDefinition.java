package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition other than a type.
 *
 * @author gl41
 * @date 01/01/2016
 */
public abstract class NonTypeDefinition extends Definition {

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public DAddr getOperand() {
        return operand;
    }
    private DAddr operand;

    public NonTypeDefinition(Type type, Location location) {
        super(type, location);
    }

}
