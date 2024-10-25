package fr.ensimag.deca.tree;

import java.util.List;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    public static final Logger LOG = Logger.getLogger(ListDeclParam.class);

    @Override
    public void decompile(IndentPrintStream s) {

        List<AbstractDeclParam> l = getList();
        Iterator<AbstractDeclParam> it = l.iterator();
        while (it.hasNext()) {
            AbstractDeclParam i = it.next();
            i.decompile(s);
            if (it.hasNext()){
                s.println(",");
            }
            
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Signature sig) throws ContextualError {
        LOG.debug("Vérification des paramètres...");
        int i = -3;
        for (AbstractDeclParam p : getList()) {
            LOG.trace("Vérification du paramètre " + p.paramName.getName().getName());
            Type t = p.verifyDeclParam(compiler,localEnv,currentClass,i);
            sig.add(t);
            i--;
        }
        LOG.debug("Fin de la vérifications des paramètres !");
    }

    void codeGenListDeclParam(DecacCompiler compiler) {
        LOG.debug("Génération des paramètres..");
        int i = -3; // Les params sont positionnés à partir de -3(LB) dans la pile
        for (AbstractDeclParam p : getList()){
            LOG.trace("Génération du paramètre " + p.paramName.getName().getName());
            p.codeGenDeclParam(compiler,i);
            i--;
        }
    }
}
