package backupcc.command;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (10 de setembro de 2022)
 * @version 1.0
 */
public final class CommandLine {
    /**
     * <p>Por default, a aplicação só edita os links para posts na página 
     * principal.</p>
     */
    private static String postLinkParseLevel = "-n2";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Faz o parse dos argumentos passados na linha de comando para a 
     * aplicação.</p>
     * 
     * @param commands Linha de comando digitada ao executar o programa.
     */
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
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p> Há 4 modos de editar links para posts: </p>
     * 
     * <dl>
     * <dt>Nível 0</dt>
     * <dd>Não edita nenhum link para post</dd>
     * <dt>Nível 1</dt>
     * <dd>Edita apenas na página principal do fórum.</dd>
     * <dt>Nível 2</dt>
     * <dd>Edita na pág. principal e nas de headers e seções</dd>
     * <dt>Nível 3</dt>
     * <dd>Edita todos os links para post em todas as páginas</dd>
     * </dl>
     * 
     * @return O nível usado para editar links para posts.
     */
    public static int getPostLinkParseLevel() {
        
        return (postLinkParseLevel.charAt(2) - 48);
        
    }//getPostLinkParseLevel()
    
    
}//classe CommandLine
