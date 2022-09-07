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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ponto de entrada para execucao da aplicacao.
 * 
 * @since 14 de agosto de 2022
 * @author "Pedro Reis"
 * @version 1.0
 */
public class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) {
        
        /* Verifica os parametros de linha de comando*/
        if (args.length > 0) {
            switch (args[0]) {
                case "--forcew":
                case "-w":
                    backupcc.tui.Tui.ansiCodesDisable();
                    break;
                case "--forceu":
                case "-u":
                    backupcc.tui.Tui.ansiCodesEnable();
                    break;
                default:
                    backupcc.tui.Tui.printlnc(
                        args[0] + " <- flag nao reconhecido!",
                        backupcc.tui.Tui.RED
                    );
                    backupcc.tui.Tui.printlnc(
                        "Uso : [--forcew | -w | --forceu | -u]",
                        backupcc.tui.Tui.RED
                    ); 
                    System.exit(0);
            }
        }
        
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
                "Falha ao gravpar arquivos\n",
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
