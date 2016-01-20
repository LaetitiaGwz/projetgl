package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gonthierflorentin on 20/01/16.
 */
public class TableMethode {

    private Map<Integer,AbstractDeclMethod> tableMethode = new HashMap<Integer, AbstractDeclMethod>();


    public void addMethode(AbstractDeclMethod ajout){
        tableMethode.put(ajout.getIdentifier().getMethodDefinition().getIndex(),ajout);
    }

    public AbstractDeclMethod getIndex(Integer i){
       return tableMethode.get(i);
    }

    public boolean containKey(Integer i){
        return tableMethode.containsKey(i);
    }
}
