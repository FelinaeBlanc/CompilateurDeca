package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentVarValue;
import java.util.List;
/**
 * Instruction
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractInst extends Tree {
    
    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError { throw new UnsupportedOperationException("optimizeInst not yet implemented "+ this);}
    protected List<AbstractInst> optimizeInsts() throws ContextualError {return null;}
    
    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to the "return" attribute (void in the main bloc).
     */    
    protected abstract void verifyInst(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     */
    protected abstract void codeGenInst(DecacCompiler compiler);

    /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon if needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }

    protected void codeGenPrintHex(DecacCompiler compiler) {
    }
}
