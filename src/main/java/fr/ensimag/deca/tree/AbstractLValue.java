package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl41
 * @date 01/01/2016
 */
public abstract class AbstractLValue extends AbstractExpr{

        public abstract NonTypeDefinition getNonTypeDefinition();

}
