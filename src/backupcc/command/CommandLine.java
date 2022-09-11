package backupcc.command;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (10 de setembro de 2022)
 * @version 1.0
 */
public final class CommandLine {
    
    private static String postLinkParseLevel = "-n1";
    
    public static void parseCommandLine(final String[] commands) {
        
        for (String command: commands) {
            
            switch (command) {
                case "-w" -> backupcc.tui.Tui.ansiCodesDisable();
                case "-u" -> backupcc.tui.Tui.ansiCodesEnable();
                case "-n0", "-n1", "-n2", "-n3" -> postLinkParseLevel = command;
                default -> {
                    backupcc.tui.Tui.printlnc(
                            command + " <- flag nao reconhecido!",
                            backupcc.tui.Tui.RED
                    );
                    backupcc.tui.Tui.printlnc(
                            "Uso : [ -w | -u] [ -n0 | -n1 | -n2 | -n3]",
                            backupcc.tui.Tui.RED
                    ); 
                    System.exit(0);
                }
            }

        }//for
        
        backupcc.tui.Tui.print("Interface de terminal = modo ");
        if (backupcc.tui.Tui.isWindowsOS())
            backupcc.tui.Tui.println("Windows");
        else
            backupcc.tui.Tui.println("Unix");
        
        switch (getPostLinkParseLevel()) {
            case 0 ->  {
                backupcc.tui.Tui.println(
                    "Links para posts nao serao convertidos para versao estatica"
                );
            }
            case 1 ->  {
                backupcc.tui.Tui.println(
                    "Links para posts da pagina inicial serao convertidos " +
                    "para versao estatica"
                );
            }
            case 2 ->  {
                backupcc.tui.Tui.println(
                    "Links para posts da pagina inicial e das paginas de " +
                    "secao serao convertidos para versao estatica"
                );
            }
            case 3 ->  {
                backupcc.tui.Tui.println(
                    "Todos os links para posts nao serao convertidos para" +
                    " versao estatica"
                );
                backupcc.tui.Tui.println(
                    "AVISO: isto pode tornar o backup extremamente lento!"
                );
            }
        }//switch
        
    }//parseCommandLine()
    
    public static int getPostLinkParseLevel() {
        
        return (postLinkParseLevel.charAt(2) - 48);
        
    }//getPostLinkParseLevel()
    
    
}//classe CommandLine
