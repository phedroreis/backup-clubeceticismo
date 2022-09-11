package backupcc.edit;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Um filtro para selecionar quais arquivos HTML terão links para posts 
 * convertidos para a versão estática.
 * 
 * @author "Pedro Reis"
 * @since 10 de setembro de 2022
 * @version 1.0
 */
final class PageFilter implements FilenameFilter {
    
    private final int postLinkParseLevel;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public PageFilter() {
        
        postLinkParseLevel = 
            backupcc.command.CommandLine.getPostLinkParseLevel();
       
    }//construtor
     
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Verifica se eh um arquivo cujo nome corresponde a uma pagina do forum
     * gravada no disco.
     * 
     * @param dir nao usado.
     * 
     * @param filename O nome do arquivo.
     * 
     * @return true se eh um arquivo cujo nome corresponde a uma pagina do forum
     * gravada no disco.
     */
    @Override
    public boolean accept(File dir, String filename) {
        
        switch (postLinkParseLevel) {            
            case 3 : if (filename.startsWith("t=")) return true;
            case 2 : if (filename.startsWith("f=")) return true;
            case 1 : return filename.equals(backupcc.file.Util.INDEX_HTML);
            default: return false;
        }//switch
           
    }//accept()
           
}//classe PageFilter

