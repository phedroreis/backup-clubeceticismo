package backupcc.main;

import static backupcc.fetch.FetchPages.downloadPages;
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
public class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) {
        
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
        
        try {  
            
            mkDirs(backupcc.file.Util.RAW_PAGES);
            
        }
        catch (IOException e) {
            
            String[] msgs = {
                "Imposs\u00EDvel criar diret\u00F3rio:\n",
                e.getMessage()
            };
                        
            abortBox(msgs);
                           
        }//try-catch
               
        Incremental.init();
        
        try {
            
            downloadPages();
           
        }
        catch (IOException | UnexpectedHtmlFormatException e) {
            
            System.err.println(e);
            System.exit(1);
  
        }
        
        try {
            
            Incremental.finish();
            
        }
        catch (IOException e) {
            
            System.err.println(e);
            System.exit(1);
  
        }
        
    }//main()
    
}//classe Main
