package fr.ensimag.deca.context;

import fr.ensimag.deca.codegen.TableMethode;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl41
 * @date 01/01/2016
 */
public class ClassDefinition extends TypeDefinition {


    private TableMethode tableMethode;

    public void initialiseTable(){
        tableMethode=new TableMethode();
    }

    public void ajoutMethod(AbstractDeclMethod ajout){
        tableMethode.addMethode(ajout);
    }

    public AbstractDeclMethod getMethod(Integer j){
        return tableMethode.getIndex(j);
    }

    public boolean containKey(Integer i){
        return tableMethode.containKey(i);
    }
    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public DAddr getOperand() {
        return operand;
    }
    private DAddr operand;

    public int incNumberOfFields() {
        this.numberOfFields++;
        return this.numberOfFields;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    }

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass;

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }

    @Override
    public String getKind() {
        return "class";
    }
    
}
