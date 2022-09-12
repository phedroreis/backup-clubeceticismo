package backupcc.main;

import static backupcc.edit.EditableLink.editFiles;
import static backupcc.fetch.FetchFiles.fetchFiles;
import static backupcc.fetch.FetchPages.downloadPages;
import static backupcc.file.Util.createWarningFile;
import static backupcc.file.Util.mkDirs;
import backupcc.incremental.Incremental;
import backupcc.pages.UnexpectedHtmlFormatException;
import static backupcc.tui.OptionBox.abortBox;
import java.io.IOException;

/**
 * Ponto de entrada para execucao da aplicacao.
 * 
 * @since 14 de agosto de 2022
 * @author "Pedro Reis"
 * @version 1.0
 */
public final class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) {
        
        /* Processa os parametros de linha de comando*/
        backupcc.command.CommandLine.parseCommandLine(args);
                  
        /* Cria os diretorios se ainda nao existirem */
        try {  
            
            mkDirs(backupcc.file.Util.RAW_PAGES);
            createWarningFile();
            
        }
        catch (IOException e) {
            
            String[] msgs = {
                "Imposs\u00EDvel criar diret\u00F3rio ou arquivo:\n",
                e.getMessage()
            };
                        
            abortBox(msgs);
                           
        }//try-catch
        
        /* Inicializa o sistema para um backup incremental */
        Incremental.init();
        
        backupcc.tui.Tui.println(
            backupcc.incremental.Incremental.lastBackupDatetime()
        );
        backupcc.tui.Tui.println(" ");
        
        /* Baixa paginas e arquivos do servidor */
        try {
            
            downloadPages();
            fetchFiles();
           
        }
        catch (IOException | UnexpectedHtmlFormatException e) {
            
            String[] msgs = {
                "Falha ao baixar arquivos\n",
                e.getMessage()
            };
                        
            abortBox(msgs);
  
        }
        
        try {
            
            editFiles();
            
        } catch (IOException e) {
            
            
            String[] msgs = {
                "Falha ao gravar arquivos\n",
                e.getMessage()
            };
                        
            abortBox(msgs);
            
        }
        
        /* Grava arquivos de finalizacao */
        try {
            
            Incremental.finish();
            
        }
        catch (IOException e) {
            
            System.err.println(e);
            System.exit(1);
  
        }
        
        backupcc.tui.OptionBox.theEndBox();
        
    }//main()
    
}//classe Main
