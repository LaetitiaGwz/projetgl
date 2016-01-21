package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractLValue extends AbstractExpr{

        public abstract NonTypeDefinition getNonTypeDefinition();

        public abstract ClassDefinition getClassDefinition();

        public abstract Definition getDefinition();

        public abstract FieldDefinition getFieldDefinition();

        public abstract MethodDefinition getMethodDefinition();

        public abstract SymbolTable.Symbol getName();


}
