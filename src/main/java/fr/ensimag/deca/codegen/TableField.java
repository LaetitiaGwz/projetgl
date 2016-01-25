package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.AbstractDeclField;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by gonthierflorentin on 20/01/16.
 */
public class TableField {

    private Map<Integer,AbstractDeclField> tableField;

    public TableField(){
        tableField=new HashMap<Integer, AbstractDeclField>();
    }


    public void addField(AbstractDeclField ajout){
        tableField.put(ajout.getVarName().getFieldDefinition().getIndex(),ajout);
    }

    public AbstractDeclField getIndex(Integer i){
        return tableField.get(i);
    }

    public boolean containKey(Integer i){
        return tableField.containsKey(i);
    }
}
