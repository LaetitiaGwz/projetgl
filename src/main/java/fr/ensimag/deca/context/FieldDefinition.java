package fr.ensimag.deca.context;

import fr.ensimag.deca.codegen.TableField;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Visibility;

/**
 * Definition of a field (data member of a class).
 *
 * @author gl41
 * @date 01/01/2016
 */
public class FieldDefinition extends NonTypeDefinition {
    public int getIndex() {
        return index;
    }

    private int index;

    private TableField tableField;

    public void initialiseTable(){
        tableField=new TableField();
    }

    public void ajoutField(AbstractDeclField ajout){
        tableField.addField(ajout);
    }

    public AbstractDeclField getField(Integer j){
        return tableField.getIndex(j);
    }

    public boolean containKey(Integer i){
        return tableField.containKey(i);
    }
    
    @Override
    public boolean isField() {
        return true;
    }

    private final Visibility visibility;
    private final ClassDefinition containingClass;
    
    public FieldDefinition(Type type, Location location, Visibility visibility,
            ClassDefinition memberOf, int index) {
        super(type, location);
        this.visibility = visibility;
        this.containingClass = memberOf;
        this.index = index;
    }
    
    @Override
    public FieldDefinition asFieldDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public ClassDefinition getContainingClass() {
        return containingClass;
    }

    @Override
    public String getKind() {
        return "field";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

}
