import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a
 * GUI window. 
 *
 * @author Mahad Ahmed 101220427
 * @version April 08, 2023
 */
public class TicTacToeGUI extends MouseAdapter implements ActionListener
{
    private JButton board[][] = new JButton[3][3];
    private JPanel display = new JPanel();
    private JTextField progress = new JTextField();
    private JTextField scoreBoard = new JTextField();
    private int xScore = 0;
    private int oScore = 0;
    private int tieScore = 0;
    
    private static ImageIcon xIconSource = new ImageIcon("x_symbol.png");
    private static Image image1 = xIconSource.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    private static ImageIcon xIcon = new ImageIcon(image1);

    private static ImageIcon oIconSource = new ImageIcon("o_symbol.png");
    private static Image image2 = oIconSource.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    private static ImageIcon oIcon = new ImageIcon(image2);
    
    private static ImageIcon blankIcon = new ImageIcon();    
    private ImageIcon playerIcon;
    
    AudioClip click;
    
    public static final String PLAYER_X = "X";
    public static final String PLAYER_O = "O"; 
    public static final String EMPTY = " ";  
    public static final String TIE = "T"; 
    
    private String player;   
    
    private String winner;  
    
    private int numFreeSquares; 
    
    private JMenuItem newItem, quitItem, resetStats, changePlayer;
           
    /**
     * Constructor for objects of class TicTacToeGUI
     */
    public TicTacToeGUI()
    {
        JFrame frame = new JFrame("Tic Tac Toe");
        Container contentPane = frame.getContentPane(); 
        contentPane.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension (500, 500));
        
        board = new JButton[3][3];
        display = new JPanel();
        display.setLayout(new GridLayout(3,3));
        contentPane.add(display);
        
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        
        JMenu fileMenu = new JMenu("Options"); 
        menubar.add(fileMenu);
        fileMenu.addMouseListener(this);
        
        newItem = new JMenuItem("New Game");
        fileMenu.add(newItem);
        
        changePlayer = new JMenuItem("Change Starting Player");
        fileMenu.add(changePlayer);
        
        resetStats = new JMenuItem("Reset Statistics");
        fileMenu.add(resetStats);
        
        quitItem = new JMenuItem("Quit Game"); 
        fileMenu.add(quitItem); 
        
        /* 
        CTRL + N = Create a new fresh game
        CTRL + Q = Quit game
        CTRL + R = Reset the statistics (refresh the score)
        CTRL + C = Change the starting player (restarts the game) 
        */
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        resetStats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
        changePlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));
        
        newItem.addActionListener(this);
        resetStats.addActionListener(this);
        changePlayer.addActionListener(this);
        quitItem.addActionListener(new ActionListener() // create an anonymous inner class
        { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0); // quit
            }
        } // end of anonymous subclass
        );
        
        playGame();

        progress = new JTextField(200);
        progress.setEditable(false);
        scoreBoard = new JTextField(200);
        scoreBoard.setEditable(false);
        contentPane.add(progress, BorderLayout.PAGE_END);
        contentPane.add(scoreBoard, BorderLayout.PAGE_START);
        
        progress.setText(this.toString());
        progress.setFont(new Font("Times New Roman", Font.BOLD, 18));
        scoreBoard.setText("Current turn: " + player + "\n " + "X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
        scoreBoard.setFont(new Font("Times New Roman", Font.BOLD, 18));
        
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        frame.pack();
        frame.setResizable(false); 
        frame.setVisible(true); 
        
    }

    /**
    * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
    * and indicates no winner yet, 9 free squares and the current player is player X.
    */
    private void clearBoard()
    {
      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            board[i][j].setText(EMPTY);
            board[i][j].setIcon(blankIcon);
            board[i][j].setDisabledIcon(null);
            board[i][j].setEnabled(true);
         }
      }
      winner = EMPTY;
      numFreeSquares = 9;
      player = PLAYER_X;    
      playerIcon = xIcon;
    }
    
    /**
    * Plays one game of Tic Tac Toe.
    */

    public void playGame()
    {
        numFreeSquares = 9;
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            JButton button = new JButton(EMPTY);
            if (board[i][j] == null) {
                board[i][j] = button;
                button.addActionListener(this);
                display.add(button);
            }
          }
        }
        
        clearBoard();
    }
    
    /**
    * Returns a string representing the current state of the game. This should
    * look like a regular tic tac toe board, and be followed by a message if 
    * the game is over that says who won (or indicates a tie).
    *
    * @return String representing the tic tac toe game state 
    */
    public String toString() 
    {
        String theBoard = " ";
        if (! winner.equals(EMPTY)){
            if (winner.equals(TIE)) {
                URL urlclick = TicTacToeGUI.class.getResource("tie_sound.wav");
                click = Applet.newAudioClip(urlclick);
                click.play();
                theBoard += "\n" + "Tie game.";
            } else {
                URL urlclick = TicTacToeGUI.class.getResource("won_sound.wav");
                click = Applet.newAudioClip(urlclick);
                click.play();
                theBoard += "\n Game over! " + winner + " has won!";
            }
        } else {
            URL urlclick = TicTacToeGUI.class.getResource("lofi_lobby.wav");
            click = Applet.newAudioClip(urlclick);
            click.loop();
            theBoard += "\n Game in progress.";
        }
        return theBoard; 
    }
    
    /**
    * Prints the board to standard out using toString().
    */
    public void print() 
    {
        System.out.println(this.toString()); 
    }
    
    /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    * @param int row of square just set
    * @param int col of square just set
    * 
    * @return true if we have a winner, false otherwise
    */
    private boolean haveWinner(int row, int col) 
    {
        if (numFreeSquares>4) {
            return false;
        }
        
        if ( board[row][0].getIcon().equals(board[row][1].getIcon()) &&
            board[row][1].getIcon().equals(board[row][2].getIcon()) ) {
            return true;
        }
        if ( board[0][col].getIcon().equals(board[1][col].getIcon()) &&
            board[0][col].getIcon().equals(board[2][col].getIcon()) ) {
            return true;
        }
        if (row==col) {
            if ( board[0][0].getIcon().equals(board[1][1].getIcon()) &&
            board[0][0].getIcon().equals(board[2][2].getIcon()) ) {
                return true;
            }
        }
        if (row==2-col) {
            if ( board[0][2].getIcon().equals(board[1][1].getIcon()) &&
            board[0][2].getIcon().equals(board[2][0].getIcon()) ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the game is done by checking if there is a winner. 
     * If winner is present, update the progress bar at the bottom of
     * the GUI and make sure it is uneditable. Update the scoreboard 
     * with the result (whether X or O won or if there was a tie).
     * 
     * @param i the integer value of the row
     * @param j the integer value of the column
     */
    private void gameDone(int i , int j){
        if (haveWinner(i,j)) {
            winner = player;
            progress.setText(this.toString());
            // progress.setText("Game over: " + winner + " has won!");
            progress.setEditable(false);
            if (winner == PLAYER_X) {
                xScore += 1;
                scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
            } else if(winner == PLAYER_O) {
                oScore += 1;
                scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
            }
        } else if (numFreeSquares==0) {
            winner = TIE;
            tieScore += 1;
            progress.setText(this.toString());
            scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
        }
    }
    
    /**
     * Update the player depending on who's turn it is.
     */
    private void updatePlayer() {
        if (player==PLAYER_X) {
            player=PLAYER_O;
            playerIcon = oIcon;
            scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
        } else { 
            player=PLAYER_X;
            playerIcon = xIcon;
            scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
        }
    }
    
    /** 
    * This action listener is called when the user clicks on 
    * any of the GUI's buttons. 
    */
    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();  
        if (o instanceof JButton) {
        
            JButton button = (JButton)o;
        
            for (int i = 0; i<3; i++) {
                for (int j = 0; j<3; j++) {
                    if (winner.equals(EMPTY)) { 
                        if (board[i][j] == button) {
                            scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
                            URL urlclick = TicTacToeGUI.class.getResource("select_sound.wav");
                            click = Applet.newAudioClip(urlclick);
                            click.play();
                            button.setIcon(playerIcon);
                            button.setDisabledIcon(playerIcon);
                            button.setEnabled(false);
                            
                            numFreeSquares--;
                            gameDone(i,j);
                            
                            updatePlayer();
                        }
                    }
                }
            }
        } else { 
            JMenuItem item = (JMenuItem)o;
            
            if (item == newItem) {
                clearBoard();
                progress.setText(this.toString());
                click.stop();
                scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
            } else if (item == resetStats) {
                xScore = 0;
                oScore = 0;
                tieScore = 0;
                scoreBoard.setText("Current turn: " + player + "\n X's Score: " + xScore + "\n O's Score: " + oScore + "\n Ties: " + tieScore);
            } else if (item == changePlayer) {
                clearBoard();
                updatePlayer();
            }
        }
    }
    
    /**
    * Detects when the mouse enters the component.  We are only "listening" to the
    * JMenu.  We highlight the menu name when the mouse goes into that component.
    * 
    * @param e The mouse event triggered when the mouse was moved into the component
    */
    public void mouseEntered(MouseEvent e) {        
        JMenu item =(JMenu) e.getSource();
        item.setSelected(true);
    }

    /**
    * Detects when the mouse exits the component.  We are only "listening" to the
    * JMenu.  We stop highlighting the menu name when the mouse exits  that component.
    * 
    * @param e The mouse event triggered when the mouse was moved out of the component
    */
    public void mouseExited(MouseEvent e) {
        JMenu item =(JMenu) e.getSource();
        item.setSelected(false);
    }
}
