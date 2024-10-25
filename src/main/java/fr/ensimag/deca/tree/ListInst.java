package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import java.util.List;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentVarValue;
/**
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ListInst extends TreeList<AbstractInst> {

    public void optimizeListInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        for (AbstractInst i : getList()){
            i.optimizeInst(compiler, envVar);
        }
    }

    public void rmDeadCodeListInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        List<AbstractInst> laListe = getList();
        for (int i = 0; i < laListe.size(); i++) {
            
            List<AbstractInst> lesInsts = laListe.get(i).optimizeInsts();
            if (lesInsts != null){ // Si n'est pas nul, ça signifie qu'on peut remplacer l'inst actuel contre une liste d'autre 
                laListe.remove(i); // Vire l'inst
                for (int l = 0; l < lesInsts.size(); l++) {
                    laListe.add(i++, lesInsts.get(l));
                }
            }
        }
    }

    
    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Type returnType)throws ContextualError {
        for (AbstractInst i : getList()){
            i.verifyInst(compiler,localEnv,currentClass,returnType);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
