package backupcc.main;

import java.io.IOException;

/**
 * Ponto de entrada para execucao da aplicacao.
 * 
 * @since 1.0 (14 de agosto de 2022)
 * @author "Pedro Reis"
 * @version 1.1
 */
public final class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) {
        
        /* Processa os parametros de linha de comando*/
        backupcc.command.CommandLine commandLine = 
            new backupcc.command.CommandLine(args);
        
        backupcc.tui.Tui.println(commandLine.toString() + "\n");
                  
        /* Cria os diretorios se ainda nao existirem */
        try {  
            
            backupcc.file.Util.mkDirs(backupcc.file.Util.RAW_PAGES);
            backupcc.file.Util.createWarningFile();
            
        }
        catch (IOException e) {
            
            String[] msgs = {
                e.getMessage() + "\n",
                "Imposs\u00EDvel criar diret\u00F3rio ou arquivo" 
            };
                        
            backupcc.tui.OptionBox.abortBox(msgs);
                           
        }//try-catch
        
        /* Inicializa o sistema para um backup incremental */
        backupcc.incremental.Incremental.init();
        
        backupcc.tui.Tui.println(
            backupcc.incremental.Incremental.lastBackupDatetime() + "\n"
        );
        
        backupcc.datetime.Util.setBackupStartTime();
        
        backupcc.fetch.FetchPages.downloadPages();
        
        backupcc.fetch.FetchStaticFiles.fetchStaticFiles();
           
        
        try {
            
            backupcc.edit.EditableLink.editFiles();
            
        }
        catch (backupcc.exception.IOExceptionX e) {
                     
            e.handler();
            
        }//try-catch
        
        /* Grava arquivos de finalizacao */
        try {
            
            backupcc.incremental.Incremental.finish();
            
        }
        catch (IOException e) {
            
            System.err.println(e);
            System.exit(1);
  
        }//try-catch
        
        backupcc.tui.OptionBox.theEndBox();
        
    }//main()
    
}//classe Main
