package TicTacToe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Coords {
    private int row,col;
    private static final Random random = new Random();
    public Coords(int row, int col) throws Exception {
        if(row < 0 || col < 0 || row >2 || col > 2) throw new Exception("Invalid Coordinates");
        this.row = row;
        this.col = col;
    }

    public boolean equals(Coords coords) {
        return coords.row == row && coords.col == col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

public class TICTACTOE extends MouseAdapter{
    //GUI Content
    private static final TICTACTOE game= new TICTACTOE();
    private static final JFrame application = new JFrame();
    private static final GridLayout grid = new GridLayout(3,3);
    private static final JLabel labels[] = new JLabel[9];

    private static ArrayList<Coords> filledCoords = new ArrayList<>(9);
    private static byte board[][] = new byte[3][3];
    private static byte turn = 1;

    public static void main(String[] args) {
        application.setSize(300,300);
        application.setLayout(grid);
        initGrid();
        application.setVisible(true);
        application.setTitle("TIC-TAC-TOE");
        application.setLocationRelativeTo(null);
        application.setResizable(false);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static void initGrid() {
        int row = -1, col = -1;
        for(int i = 0 ; i < labels.length ; i++){
            if(i%3==0) row++;
            col = i % 3;
            labels[i] = new JLabel();
            labels[i].setName(row + " " + col);
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            labels[i].setBackground(Color.BLACK);
            labels[i].setOpaque(true);
            labels[i].addMouseListener(game);
            application.add(labels[i]);
        }
    }

    private static void addMove(Coords coords,JLabel j) throws IOException {
        if(isSafeCoord(coords)){
            filledCoords.add(coords);
            addTOBoard(coords);
            j.setIcon(getImage());

            if(isWinner()){
                JOptionPane.showMessageDialog(null,"Voila Player " + turn + " is winner.","Winner!!!",JOptionPane.PLAIN_MESSAGE);
                application.dispatchEvent(new WindowEvent(application,WindowEvent.WINDOW_CLOSING));
            }
            else if(filledCoords.size()==9){
                JOptionPane.showMessageDialog(null,"!!! DRAW !!!");
                application.dispatchEvent(new WindowEvent(application,WindowEvent.WINDOW_CLOSING));
            }

            changeTurn();
        }
        else{
            JOptionPane.showMessageDialog(null,"That place is already filled ","Invalid Move",JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       JLabel j = (JLabel)e.getSource();
        try {
            String []rowCols = j.getName().split(" ");
            int row = Integer.parseInt(rowCols[0]);
            int col = Integer.parseInt(rowCols[1]);
            addMove(new Coords(row,col),j);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static ImageIcon getImage() throws IOException {
        BufferedImage rawImg;
        if(turn==1)
            rawImg = ImageIO.read(new File("GUI_BASED\\src\\TicTacToe\\circle.png"));
        else
            rawImg = ImageIO.read(new File("GUI_BASED\\src\\TicTacToe\\cross.png"));
        // will contain resized image
        Image queen = rawImg.getScaledInstance(labels[0].getWidth(), labels[0].getHeight(),
                Image.SCALE_SMOOTH);
        return new ImageIcon(queen);
    }

    private static void addTOBoard(Coords coords) {
        board[coords.getRow()][coords.getCol()] = turn;
    }

    private static void changeTurn() {
        turn = (byte) (turn == 1 ? 2 : 1);
    }

    private static boolean isWinner() {
        return  (board[0][0]!=0 && board[0][0]==board[0][1] && board[0][1]==board[0][2]) || //horizontal check
                (board[1][0]!=0 && board[1][0]==board[1][1] && board[1][1]==board[1][2]) || //horizontal check
                (board[2][0]!=0 && board[2][0]==board[2][1] && board[2][1]==board[2][2]) || //horizontal check
                (board[0][0]!=0 && board[0][0]==board[1][0] && board[1][0]==board[2][0]) || //vertical check
                (board[0][1]!=0 && board[0][1]==board[1][1] && board[1][1]==board[2][1]) || //vertical check
                (board[0][2]!=0 && board[0][2]==board[1][2] && board[1][2]==board[2][2]) || //vertical check
                (board[0][0]!=0 && board[0][0]==board[1][1] && board[1][1]==board[2][2]) || //cross left to right check
                (board[2][0]!=0 && board[2][0]==board[1][1] && board[1][1]==board[0][2]);   //cross right to left check
    }

    private static boolean isSafeCoord(Coords newCoords) {
        for(Coords used : filledCoords){
            if(used.equals(newCoords))
                return false;
        }
        return true;
    }
}
