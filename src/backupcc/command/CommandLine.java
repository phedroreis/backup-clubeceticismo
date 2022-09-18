package backupcc.command;

/**
 * Esta classe é responsável por fazer o parse da linha de comando que pode
 * selecionar o tipo de terminal e em que tipos de páginas os links para posts
 * serão redirecionados para arquivos da cópia estática.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (10 de setembro de 2022)
 * @version 1.0
 */
public final class CommandLine {
    /**
     * Por default, a aplicação só edita os links para posts na página 
     * principal e nas de seções.
     */
    private static String postLinksRedirectLevel = "-n2";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Faz o parse dos argumentos passados na linha de comando para a 
     * aplicação.</p>
     * 
     * <p> Sem argumentos na linha de comando, o sistema irá usar o padrão de 
     * terminal de acordo com o sistema operacional onde estiver rodando e irá,
     *  na edição das páginas, redirecionar todos os links para posts</p>
     * 
     * <ul>
     * <li>
     * -w : força que as saídas para o terminal sejam no padrão de um terminal
     * do sistema Windows
     * </li>
     * <li>
     * -u : força que as saídas para o terminal sejam no padrão de sistemas
     * Unix, reconhecendo comandos ANSI
     * </li>
     * <li>
     * -n0 : Com este flag, nenhum link para post será editado para redireciona-
     * lo para o arquivo de tópico na cópia estática onde pode ser visualizado
     * </li>
     * <li>
     * -n1 : Links para posts são editados apenas na página inicial
     * </li>
     * <li>
     * -n2 : Links para posts são editados nas páginas inicial e de seções
     * </li>
     * <li>
     * -n3 : Todos os links para posts, em todas as páginas, são editados
     * </li>
     * </ul>
     * 
     * @param commands Linha de comando digitada ao executar o programa.
     */
    public CommandLine(final String[] commands) {
        
        for (String command: commands) {
            
            switch (command) {
                case "-w" -> backupcc.tui.Tui.ansiCodesDisable();
                case "-u" -> backupcc.tui.Tui.ansiCodesEnable();
                case "-n0", "-n1", "-n2", "-n3" -> 
                    postLinksRedirectLevel = command;
                default -> {
                    String[] msgs = {
                        command + " <- flag n\u00E3o reconhecido!\n",
                        "Uso : [terminal] [n\u00EDvel de edi\u00E7\u00E3o]\n",
                        "terminal: -w , -u",
                        "n\u00EDvel de edi\u00E7\u00E3o: -n0, -n1, -n2, -n3"
                    };
                    backupcc.tui.OptionBox.abortBox(msgs);
                }
            }//switch

        }//for        
               
    }//CommandLine()
    
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
     * <dd>Edita todos os links para post em todas as páginas.</dd>
     * </dl>
     * 
     * <p>O método {@link backupcc.edit.EditableLink#editFiles() editFiles }
     * faz uma chamada a este método para decidir em quais páginas editar os 
     * links para posts</p>
     * 
     * @return O nível usado para editar links para posts.
     */
    public static int getPostLinksRedirectLevel() {
        
        return (postLinksRedirectLevel.charAt(2) - 48);
        
    }//getPostLinkParseLevel()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Informação textual sobre a classe.
     * 
     * @return Informação textual sobre a classe.
     */
    @Override
    public String toString() {
        
        String toString = "Interface de terminal = modo ";
        
        toString += backupcc.tui.Tui.isWindowsOS() ? "Windows\n" : "Unix\n";
        
        switch (getPostLinksRedirectLevel()) {
            case 0 ->  {
                toString +=
                    "Links para posts n\u00E3o ser\u00E3o redirecionados " +
                    "para p\u00E1ginas est\u00E1ticas de t\u00F3picos";
            }
            case 1 ->  {
                toString +=
                    "Links para posts da p\u00E1gina inicial ser\u00E3o " +
                    "redirecionados para p\u00E1ginas de t\u00F3picos" +
                    " est\u00E1ticas";
            }
            case 2 ->  {
                toString +=
                    "Links para posts das p\u00E1ginas inicial e de " +
                    "se\u00E7\u00E3o ser\u00E3o redirecionados" +
                    " para p\u00E1ginas de t\u00F3picos est\u00E1ticas";
            }
            case 3 ->  {
                toString +=
                    "Todos os links para posts ser\u00E3 redirecionados para" +
                    " p\u00E1ginas de t\u00F3pico est\u00E1ticas";
            }
        }//switch
        
        return toString;
        
    }//toString()
   
}//classe CommandLine
