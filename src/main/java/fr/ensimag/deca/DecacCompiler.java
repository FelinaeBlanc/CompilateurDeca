package fr.ensimag.deca;

import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.LocationException;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.CheckPoint;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label; 
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl07
 * @date 21/04/2023
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    private final GestionnaireErreur gestionnaireErreur;
    private final GestionnaireMemoire gestionnaireMemoire;
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);
    public final List<AbstractDeclMethod> methodGlobalList = new ArrayList<>();
    
    /**
     * Portable newline character.
     */

    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.gestionnaireErreur = new GestionnaireErreur();
        this.gestionnaireMemoire = new GestionnaireMemoire();
    }


    /**
     * Les getteurs 
     */
    public File getSource() { return source; }
    public CompilerOptions getCompilerOptions() { return compilerOptions; }
    public GestionnaireErreur getGestionnaireErreur(){ return gestionnaireErreur; }
    public GestionnaireMemoire getGestionnaireMemoire(){ return gestionnaireMemoire; }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    
    // Checkpoints, edéclarer les méthodes de IMAProgram dans DecacCompiler car il est private, et pour centraliser le passage pour le gestionnaire de memoire  
    public void addInstruction(Instruction instruction, CheckPoint checkPoint, boolean updateContext){
        if (updateContext){ getGestionnaireMemoire().updateContext(this, instruction); }
        program.addInstruction(instruction,checkPoint);
    }

    public void addInstruction(Instruction instruction, CheckPoint checkPoint){ addInstruction(instruction,checkPoint,true); }
    public CheckPoint makeCheckPoint(){ return program.makeCheckPoint(); }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        getGestionnaireMemoire().updateContext(this, instruction);
        program.addInstruction(instruction);
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        getGestionnaireMemoire().updateContext(this, instruction);
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    

    public Symbol createSymbol(String name) {
        return symbolTable.create(name);
    }
    
    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {

        int index = source.getAbsolutePath().lastIndexOf(".");
        String destNoExt = source.getAbsolutePath().substring(0, index);

        String sourceFile = source.getAbsolutePath();
        String destFile = destNoExt+".ass";

        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,PrintStream out, PrintStream err) throws DecacFatalError, LocationException, EnvironmentExp.DoubleDefException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        if (getCompilerOptions().getDoDecompile()){ // Si on veut juste décompiler, on arrête le programme ensuite
            LOG.info("Decompilation en cours..."); 
            prog.decompile(out);
            return false;
        }
        
        LOG.info("Vérification du programme en cours...");
        prog.verifyProgram(this);
        LOG.info("Vérification des décorations en cours...");
        assert(prog.checkAllDecorations());
        
        if (getCompilerOptions().getOnlyVerification()){ return false; } // Arrête si on voulait juste vérifier le programme
        
        if (getCompilerOptions().getDoOptimize()) { // Optimisation du programme après l'enrichissement
            prog.optimizeProgram(this);
        }

        addComment("start main program");
        LOG.info("Génération du code en cours..."); 
        prog.codeGenProgram(this);
        addComment("end main program");

        // Génère les erreurs
        prog.codeGenErrors(this);
        
        LOG.debug("Le code assembleur est :" + nl + program.display());
        
        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Ecriture du fichier assembleur en cours ...");
        LOG.info("Le fichier de sortie est : " + destName);

        program.display(new PrintStream(fstream));
        LOG.info("Compilation du fichier " + sourceName + " réussi !");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

}
