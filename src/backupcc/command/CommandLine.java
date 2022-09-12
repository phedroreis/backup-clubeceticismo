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
                    "Links para posts n\u00E3o ser\u00E3o convertidos " +
                    "para vers\u00E3o est\u00E1tica"
                );
            }
            case 1 ->  {
                backupcc.tui.Tui.println(
                    "Links para posts da p\u00E1gina inicial ser\u00E3o " +
                    "convertidos para vers\u00E3o est\u00E1tica"
                );
            }
            case 2 ->  {
                backupcc.tui.Tui.println(
                    "Links para posts da p\u00E1gina inicial e das " +
                    "p\u00E1ginas de se\u00E7\u00E3o ser\u00E3o convertidos" +
                    " para vers\u00E3o est\u00E1tica"
                );
            }
            case 3 ->  {
                backupcc.tui.Tui.println(
                    "Todos os links para posts ser\u00E3 convertidos para" +
                    " vers\u00E3o est\u00E1tica"
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
