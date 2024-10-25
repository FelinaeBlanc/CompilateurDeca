package fr.ensimag.deca;

import java.io.File;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;


public class CompilerTask implements Callable<Boolean> {
   private final DecacCompiler dCompiler;
   private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

   public CompilerTask(CompilerOptions options, File source) {
      this.dCompiler = new DecacCompiler(options, source);
   }

   @Override
   //renvoie true si la compilation s'est bien passée sinon renvoie une erreur
   public Boolean call() throws Exception {
      try {
         boolean notCompile = dCompiler.compile();
         if (notCompile) {
            throw new DecacFatalError("There was an error during compilation");
         }
      } catch (Exception e) {
         throw new DecacFatalError("There was an error during compilation");
      }
      LOG.info("Compilation du fichier : " + dCompiler.getSource() + " terminé");
      return true;
   }
}
