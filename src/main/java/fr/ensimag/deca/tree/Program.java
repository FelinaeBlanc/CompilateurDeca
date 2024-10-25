package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.CheckPoint;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.deca.Erreur;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void optimizeProgram(DecacCompiler compiler) throws ContextualError {
        main.optimizeMain(compiler);
        classes.optimizeClasses(compiler);
    }

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        // Vérification des classes
        LOG.debug("Verification des classes...");
        classes.verifyListClass(compiler); // Passe 1 (appel passe 2 incluse dedans)
        classes.verifyListClassBody(compiler); // Passe 3 (pour les classes)

        // Vérification du main
        LOG.debug("Verification du programme principal...");
        main.verifyMain(compiler);
        LOG.debug("Fin de la vérification du programme.");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // Reset Gestionnaire Mémoire
        compiler.getGestionnaireMemoire().init(compiler, true);
        

        compiler.addComment("Debut vTable");
        classes.codeGenCreateVtable(compiler); 
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        
        compiler.getGestionnaireMemoire().buildStack(compiler); // Construit TSTO et ADDSP

        classes.codeGenInits(compiler);
        classes.codeGenMethods(compiler);
    }


    @Override
    public void codeGenErrors(DecacCompiler compiler){
        // Génère les Erreurs qui sont utilisées !
        for (Erreur uneErreur : compiler.getGestionnaireErreur().getErreurs().values()) {
            if (uneErreur.isUsed()){
                uneErreur.codeGenInst(compiler);
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
