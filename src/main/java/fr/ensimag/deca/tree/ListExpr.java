package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl07
 * @date 21/04/2023
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        int curr =0;
        int last = getList().size()-1;
        for (AbstractExpr i : getList()) {
            i.decompile(s);
            if(curr < last){
                s.print(",");
                curr++;
            }
        }
    }
}
