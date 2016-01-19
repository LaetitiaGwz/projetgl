package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl41
 * @date 01/01/2016
 */
public class DeclFieldSet extends AbstractDeclFieldSet {

    public Visibility getVisibility() {
        return visibility;
    }

    public AbstractIdentifier getFieldType() {
        return type;
    }

    public ListDeclField getDeclFields() {
        return declFields;
    }

    private Visibility visibility;
    private AbstractIdentifier type;
    private ListDeclField declFields;

    public DeclFieldSet(Visibility visibility, AbstractIdentifier type, ListDeclField declFields) {
        super();
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(declFields);
        Validate.isTrue(!declFields.isEmpty(),
                "A list of field declarations cannot be empty");
        this.visibility = visibility;
        this.type = type;
        this.declFields = declFields;
    }

    @Override
    protected Type verifyMembers(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type t = type.verifyType(compiler);

        for(AbstractDeclField field : declFields.getList()) {
            field.verifyMembers(t, visibility, compiler, localEnv, currentClass);
        }
        return t;
    }

    @Override
    protected Type verifyBody(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codegenDeclFieldSet(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        declFields.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        declFields.prettyPrint(s, prefix, true);
    }

}
