package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl07
 * @date 21/04/2023
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.print("=====================================================================================\n");
            System.out.print(banner);
            System.out.print("=====================================================================================\n");
            System.out.println();
            System.out.println();
            System.out.println(banner_image1);
        }
        if (options.getSourceFiles().isEmpty() && !options.getPrintBanner())   {
            //TODO : à implémenter (donner les options possibles)
            System.out.println("DECAC Options:");
            System.out.println("Afficher la bannière: -b");
            System.out.println("Limiter le nombre de registres utilisées: -r [4-16]");
            System.out.println("Compiler les fichiers en parallèle: -P");
            System.out.println("Décompiler le code: -p");
            System.out.println("Uniquement vérifier le code: -v");
            System.out.println("Optimiser le code: -o");
            System.out.println("Ne pas vérifier : -n");
            System.out.println("Debug : -d");
        }
        if (options.getParallel()) {

            int nbProcesseursDispo = java.lang.Runtime.getRuntime().availableProcessors();
            ExecutorService executorService = Executors.newFixedThreadPool(nbProcesseursDispo);

            List<Future<Boolean>> futures = new ArrayList<>();
            for (File file : options.getSourceFiles()) {
                futures.add(executorService.submit(new CompilerTask(options, file)));
            }

            for (Future<Boolean> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();

            error = false;

            LOG.info("Compilation parallèle terminée");

        } else { 
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);

        LOG.info("Fin de la compilation !"); 
    }

    private static String banner = 
    " ||     '||''''| .|'''|     '||'''|. '||''''| .|'''',      /.\\      '||'''|, .|'''|  '||   ||` '||     '||''''| '||   ||` '||'''|, .|'''|\n" +
    " ||      ||   .  ||          ||   ||  ||   .  ||          // \\\\      ||   || ||       ||   ||   ||      ||   .   ||   ||   ||   || ||      \n" +
    " ||      ||'''|  `|'''|,     ||   ||  ||'''|  ||         //...\\\\     ||...|' `|'''|,  ||   ||   ||      ||'''|   ||   ||   ||...|' `|'''|, \n" +
    " ||      ||       .   ||     ||   ||  ||      ||        //     \\\\    ||       .   ||  ||   ||   ||      ||       ||   ||   || \\\\    .   || \n" +
    ".||...| .||....|  |...|'    .||...|' .||....| `|....' .//       \\\\. .||       |...|'  `|...|'  .||...| .||....|  `|...|'  .||  \\\\.  |...|'\n";
    
    
    private static String banner_image1 = 
               "        .::::::--:\n" +
               "      .-:..:::::.:-:.\n" +
               "     ...::--====-:::.:.\n" +
               "    :.::--+=:::-===-::::.\n" +
               "   .::::-=-      .-=+--:::.\n" +
               "   -.::--=         .:=+--:::.\n" +
               "  :::::---            .==-::::..      .::::------===============================================--------:::::.......\n" +
               "  -::::---             :::----:::..-=-+++==============================================================================---\n" +
               "  -:-:--=:            :.:--------::-..===--==============================================================================+-\n" +
               " :-:-:--=:            :::----------:  =======+===++++++++==++++=+========================+++++++++++++=+=================+=\n" +
               " :---:--=:            .::--===-==---..++++++++++++++++++++++*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**+\n" +
               " :---:--=:            :::-=+===-=--=--+++++**************+*+*************+++*+++++++++***********+++++++++++++++++*******#+\n" +
               " .=-----=:            ------==-----:--+++++++******************************************************************#**#*######+\n" +
               "  +-----=-            .-----------=---*********+++++***************###########*################***********#*******##**#*##-\n" +
               "  =-------             :::-----==-::::+###***************+***************##################*######*********************++-\n" +
               "  :=-----=          ..:.:---==-:    ..:===++++***************++++++++++++++++++++*++*+++++++++======-------:::::....\n" +
               "   ==-----.       ...:---===:                        .......::::::::::::::::::::::......\n" +
               "   .==---:..:......:---==-.\n" +
               "    .==---:.....::--===:.\n" +
               "     .-+=--------===-:\n" +
               "       .:=======-:.\n" +
               "          .....";    

               



}
