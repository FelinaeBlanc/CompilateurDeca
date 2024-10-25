package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;
import java.util.List;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.UndefinedVar;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

	@Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar i : getList()) {
            i.decompile(s);
            s.println();
        }
    }


    void optimizeListDeclVariable(DecacCompiler compiler,EnvironmentVarValue envVar) throws ContextualError {
        for (AbstractDeclVar d : getList()){
            d.optimizeDeclVar(compiler,envVar);
        }
    }

    void rmDeadCodeDeclVariable(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError  {
        List<AbstractDeclVar> laListe = getList();
        for (int i = 0; i < laListe.size(); i++) {

            Symbol varSymbol = laListe.get(i).getName();
            AbstractExpr valVar = envVar.getCurrentValue(varSymbol);
            if (valVar instanceof UndefinedVar){
                System.out.println("RETIRE ... "+varSymbol.getName());
                laListe.remove(i);
            } /*else if (!envVar.getIsUsed(varSymbol)){ //NON VALIDE, IL MANQUE DES CHOSES !
                // On vire seulement les variables Literaux, 
                Type type = valVar.getType();
                if (type.isBoolean() || type.isInt() || type.isFloat()){
                    laListe.remove(i);
                    System.out.println("RETIRE ... "varSymbol.getName());
                }
            }*/
 
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
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError {
        LOG.debug("Vérification des déclarations de variables dans le main...");
        for (AbstractDeclVar d : getList()){
            d.verifyDeclVar(compiler,localEnv,currentClass);
        }
        LOG.trace("Vérification des variables terminée !");
    }

    void codeGenListDeclVariable(DecacCompiler compiler) {
        LOG.debug("Génération des variables...");
        for (AbstractDeclVar d : getList()){
            d.codeGenDeclVar(compiler);
        }
        LOG.trace("Fin de la génération des variables !");
    }

    public void verifyMethodListDeclVariable(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition classDefinition) throws ContextualError {
        LOG.debug("Vérification des déclarations de variables dans les méthodes...");
        for (AbstractDeclVar d : getList()){
            d.verifyMethodListDeclVariable( compiler,  envExp,  envExpParams,  classDefinition);
        }
        LOG.trace("Vérification des variables des methodes terminée !");
    }
}
