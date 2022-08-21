package backupcc.tui;

import static backupcc.strings.Util.repeatChar;

/**
 * Implementa uma barra de status de progresso no terminal. 
 * 
 * @since 16 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public class ProgressBar {
    
    /*
    Total de itens que serao processados
    */
    private final int total;
    
    /*
    Comprimento da barra em caracteres
    */
    private final int barLength;
    
    /*
    Quantos caracteres ja foram impressos na barra de progresso
    */
    private int filled;
    
    private final int foregroundColor;
    
    private final int backgroundColor;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe.
     * 
     * @param total Total de itens a serem processados.
     * 
     * @param barLength Largura da barra em caracateres.
     * 
     * @param fg A cor de texto para a barra de progresso.
     * 
     * @param bg A cor de fundo para a barra de progresso.
     */
    public ProgressBar(
        final int total,
        final int barLength,
        final int fg,
        final int bg
    ) {
        
        this.total = total;
        this.barLength = barLength;
        foregroundColor = fg;
        backgroundColor = bg;
        filled = 0;
         
    }//construtor
            
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe.
     * 
     * @param total Total de itens a serem processados.
     * 
     * @param barLength Largura da barra em caracateres.
     * 
     * @param fg A cor de texto para a barra de progresso.
     * 
     */
    public ProgressBar(
        final int total,
        final int barLength,
        final int fg
        
    ) {
        
        this(total, barLength, fg, Tui.NONE);
         
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Exibe ou reexibe a barra de progresso no terminal.
     */
    public void show() {
        
        if (Tui.isWindowsOS()) {
              
            System.out.println("0%|" + repeatChar(' ', barLength - 2) + "|100%");
            System.out.println("  v" + repeatChar(' ', barLength - 2) + "v");
            System.out.print("  " + repeatChar('#', filled));
        }
        else {
            
            Tui.setColor(foregroundColor);
            if (backgroundColor != Tui.NONE) Tui.setBgColor(backgroundColor);

            Tui.hideCursor();
            System.out.print(
                "0%[" + 
                repeatChar('#', filled) +
                repeatChar(' ', barLength - filled) + 
                "]100%"
            );
            Tui.resetCursorPosition();
        }
        
        Tui.cursorMoved = false;
        
    }//show()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Atualiza a barra de progresso.
     * 
     * @param done Quantos itens ja foram processados.
     */
    public void update(final int done) {
        
        if (Tui.cursorMoved) show();
        
        int f = done * barLength / total;
        
        if (f <= filled) return;
        
        filled = f;
        
       if (Tui.isWindowsOS()) {
              
            System.out.print("#");
     
        }
        else { 
          
            System.out.print(
                "0%[" + 
                repeatChar('#', filled) +
                repeatChar(' ', barLength - filled) + 
                "]100%"
            );

            Tui.resetCursorPosition();
        }
        
        if (done == total) Tui.restoreTerminalDefaults();
             
    }//update()
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Metodo para testes da classe.
     * 
     * @param args Nao usado.
     */
    public static void main(String[] args) {
        
        int tot = 900000;
        
        //Tui.ansiCodesDisable();
        
        ProgressBar pb = new ProgressBar(tot, 50, Tui.RED, Tui.WHITE);
        
        pb.show();
        
                
        for (int i = 0; i <= tot; i++) {
          
            if (i==100000) {
               Tui.restoreTerminalDefaults();
               Tui.println("");
               Tui.println("");Tui.println("");Tui.println("");
               Tui.println("");Tui.println("");Tui.println("");
               Tui.println("");Tui.println("");
            }
            if (i==450000) {
               Tui.restoreTerminalDefaults();
               Tui.println("");
               Tui.println("");Tui.println("");Tui.println("");
               Tui.println("");Tui.println("");Tui.println("");
               Tui.println("");Tui.println("");
            }
            pb.update(i);
        }
        
      
    }//main()
    
}//classe ProgressBar
