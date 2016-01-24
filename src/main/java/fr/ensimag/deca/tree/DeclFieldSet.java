package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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
    protected void verifyMembers(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type t = type.verifyType(compiler);
        this.type.setType(t);

        for(AbstractDeclField field : declFields.getList()) {
            field.verifyMembers(t, visibility, compiler, localEnv, currentClass);
        }
    }

    @Override
    protected void verifyBody(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        for(AbstractDeclField field : declFields.getList()) {
            field.verifyBody(type.getType(), visibility, compiler, localEnv, currentClass);
        }
    }

    @Override
    protected void codeGenFieldSet(DecacCompiler compiler) {

        for( AbstractDeclField a : declFields.getList()){
            if(getFieldType().getName().toString().equals("float")){
                a.codeGenFieldFloat(compiler);
            }
            else{
                a.codeGenFieldInt(compiler);
            }

        }

    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getFieldType().getName().toString()+" ");
        int i = declFields.size();
        for(AbstractDeclField a : declFields.getList()){
            a.decompile(s);
            if(i>1){
                s.print(",");
            }
            else{
                s.println(";");
            }
            i--;
        }

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
