package fr.ensimag.deca.all;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 *
 * @author Ensimag
 * @date 21/04/2023
 */

public class ManualTestAll {
    public static void main(String args[]) throws IOException {
        Logger.getRootLogger().setLevel(Level.DEBUG);
        args = new String[1];
        args[0] = "/user/9/.base/veyratm/home/Projet_GL/src/test/java/fr/ensimag/deca/all/test.deca";
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);
        parser.setDecacCompiler(compiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        if (prog == null) {
            System.exit(1);
            return;  //Unreachable, but silents a warning.
        }
        try {
            prog.verifyProgram(compiler);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }

        System.out.println("\n---- From the following Abstract Syntax Tree ----");
        prog.prettyPrint(System.out);
        System.out.println("\n---- We generate the following assembly code ----");
        prog.codeGenProgram(compiler);
        String result =  compiler.displayIMAProgram();
        System.out.println(result);


        try {
            Process process = Runtime.getRuntime().exec("decac " + args[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
