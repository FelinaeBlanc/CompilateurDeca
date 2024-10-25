package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl07
 * @date 21/04/2023
 */

 

 public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getOnlyVerification() {
        return onlyVerification;
    }

    public boolean getDoDecompile(){
        return doDecompile;
    }

    public int getRMax(){
        return rMax;
    }

    public boolean getDoOptimize() {
        return doOptimize;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getNoCheck() {
        return noCheck;
    }


    private int debug = 0;
    private boolean noCheck = false;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean onlyVerification = false;
    private boolean doDecompile = false;
    private boolean doOptimize = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private int rMax = 16;


    public void parseArgs(String[] args) throws CLIException {
        if (args.length != 0){
            for (int i = 0; i < args.length ; i++) {
                switch (args[i]){
                    case "-b":
                        printBanner = true;
                        break;
                    case "-n":
                        this.noCheck = true; 
                        break;
                    case "-r":
                        rMax = Integer.parseInt(args[i+1]);
                        if (!(4 <= rMax && rMax <= 16)) {
                            throw new CLIException("Parameter after -r must be between 4 and 16");
                        }
                        i++;
                        break;
                    case "-d":
                        debug += 1;
                        break;
                    case "-P":
                        parallel = true;
                        break;
                    case "-p":
                        doDecompile = true;
                        break;
                    case "-v":
                        onlyVerification = true;
                        break;
                    case "-o":
                        doOptimize = true;
                        break;
                    default:
                        File f = new File(args[i]);
                        sourceFiles.add(f);
                        break;
                }
            }
        }
   
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.throw new UnsupportedOperationException("decac -b not yet implemented");
        switch (getDebug()) {
            case QUIET: break; // keep default
            case INFO:
                logger.setLevel(Level.INFO); break;
            case DEBUG:
                logger.setLevel(Level.DEBUG); break;
            case TRACE:
                logger.setLevel(Level.TRACE); break;
            default:
                logger.setLevel(Level.ALL); break;
        }
        logger.info("Le niveau de trace de l'application est défini sur " + logger.getLevel());

        /*boolean assertsEnabled = true;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        throw new UnsupportedOperationException("not yet implemented");*/
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
