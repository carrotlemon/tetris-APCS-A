import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
// 0 - black, 1 - aqua I, 2 - yellow O, 3 - purple T, 4 - green S, 5 - red Z, 6 - blue J, 7 - orange L
public class Tetris {

    public static ImageIcon[][] board;
    public static ImageIcon[][] shadowBoard;
    public static ImageIcon[][] nextBoard;
    public static ImageIcon[][] heldBoard;
    public static ImageIcon E, I, O, T, S, Z, J, L;
    public static ImageIcon sE, sI, sO, sT, sS, sZ, sJ, sL;

    private int level, score;
    private Piece current, held, next1, next2, next3;
    private boolean holdToggle; // you can only hold once before placing a piece

    public Tetris() {
        board = new ImageIcon[24][10]; // first 4 rows not visible but used for spawning in new pieces
        shadowBoard = new ImageIcon[24][10];
        heldBoard = new ImageIcon[4][6];
        nextBoard = new ImageIcon[14][6];
        holdToggle = true;

        next1 = new Piece(); // sets next pieces beforehand
        next2 = new Piece();
        next3 = new Piece();

        E = new ImageIcon("black.png"); // sets variables for files
        I = new ImageIcon("aqua.png");
        O = new ImageIcon("yellow.png");
        T = new ImageIcon("purple.png");
        S = new ImageIcon("green.png");
        Z = new ImageIcon("red.png");
        J = new ImageIcon("blue.png");
        L = new ImageIcon("orange.png");

        sI = new ImageIcon("aquaS.png");
        sO = new ImageIcon("yellowS.png");
        sT = new ImageIcon("purpleS.png");
        sS = new ImageIcon("greenS.png");
        sZ = new ImageIcon("redS.png");
        sJ = new ImageIcon("blueS.png");
        sL = new ImageIcon("orangeS.png");

        for(int r = 0; r < 24; r++) {
            for(int c = 0; c < 10; c++) {
                board[r][c] = E;
                shadowBoard[r][c] = E;
            }
        }
        for(int r = 0; r < 14; r++) {
            for(int c = 0; c < 6; c++) {
                nextBoard[r][c] = E;
                if(r < 4)
                    heldBoard[r][c] = E;
            }
        }
        level = 0;
        newPiece();
    }
    public void draw(myJFrame frame, Graphics g) {
        //System.out.println("Width: " + frame.getWidth() + " Height: " + frame.getHeight());
        current.updateShadow();

        int width = ((frame.getWidth()-15)); // 800
        for(int r = 0; r < 22; r++) {
            for(int c = 0; c < 10; c++) {
                //if(c == 0 || c == 9)
                    //System.out.println(width/4+c*(width/20)+ " " + (width/20));
                if(r < 2 && !board[r+2][c].equals(E) || r >= 2)
                    g.drawImage(board[r+2][c].getImage(), width/4+c*(width/20), (r*(width/20)), (width/20), (width/20), frame);
                // (image, x, y, width, height, frame)

                // SHADOW PIECE
                
                if(shadowBoard[r+2][c].equals(I)) {
                    shadowBoard[r+2][c] = sI;
                } else if (shadowBoard[r+2][c].equals(O)) {
                    shadowBoard[r+2][c] = sO;
                } else if (shadowBoard[r+2][c].equals(T)) {
                    shadowBoard[r+2][c] = sT;
                } else if (shadowBoard[r+2][c].equals(S)) {
                    shadowBoard[r+2][c] = sS;
                } else if (shadowBoard[r+2][c].equals(Z)) {
                    shadowBoard[r+2][c] = sZ;
                } else if (shadowBoard[r+2][c].equals(J)) {
                    shadowBoard[r+2][c] = sJ;
                } else if (shadowBoard[r+2][c].equals(L)) {
                    shadowBoard[r+2][c] = sL;
                }

                if(r > 2 && !shadowBoard[r+2][c].equals(E))
                    g.drawImage(shadowBoard[r+2][c].getImage(), width/4+c*(width/20), (r*(width/20)), (width/20), (width/20), frame);
            }
        }
        //printShadowBoard();
        
        // nextBoard
        for(int r = 0; r < 14; r++) {
            for(int c = 0; c < 6; c++) {
                if((r+1)%5 != 0)
                    g.drawImage(nextBoard[r][c].getImage(), 25*(width/32)+c*(width/32), (width/10)+(r*(width/32)), (width/32), (width/32), frame);
                // (image, x, y, width, height, frame)
            }
        }

        // heldBoard
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 6; c++) {
                g.drawImage(heldBoard[r][c].getImage(), (width/32)+c*(width/32), (width/10)+(r*(width/32)), (width/32), (width/32), frame);
                // (image, x, y, width, height, frame)
            }
        }
    }  
    // -------------------------------------------------------------------
    public void updateNextBoard() { // nextboard is 14x6
        // next1 (1,1) | next2 (6,1) | next3 (11,1)
        for(int r = 0; r < 14; r++) {
            for(int c = 0; c < 6; c++) {
                if(!nextBoard[r][c].equals(E))
                    nextBoard[r][c] = E;
            }
        }
        next1.setLocation(1,1);
        next2.setLocation(1,6);
        next3.setLocation(1,11);
        next1.placePiece(nextBoard);
        next2.placePiece(nextBoard);
        next3.placePiece(nextBoard);
    }
    public void hold() {
        //System.out.println("Held " + held + "current " + current);
        if(holdToggle) {
            if(held == null) {
                current.removePiece(board);
                held = current;
                newPiece();
            } else {
                current.removePiece(board);
                current.setRotation(0);
                held.removePiece(heldBoard);
                Piece temp = held;
                held = current;
                current = temp;
                current.setLocation(4, 0);
            }
            held.setLocation(1, 1);
            held.placePiece(heldBoard);
            holdToggle = false;
        }
        
        //System.out.println("Held " + held + "current " + current);
    }
    public int getScore() {
        return score;
    }
    public int clearLines() {
        int scoreT = 0, temp = 0;
        for(int r = 0; r < 24; r++) { // finds full rows
            boolean full = rowFull(r);
            if(full) {
                for(int i = 0; i < 10; i++) { // removes full rows
                    board[r][i] = E;
                }
                temp++;
            }
            if(!full || r == 23){
                if(temp == 1)
                    scoreT += 40*(level+1);
                else if(temp == 2)
                    scoreT += 100*(level+1);
                else if(temp == 3)
                    scoreT += 300*(level+1);
                else if(temp == 4)
                    scoreT += 1200*(level+1);
                temp = 0;
            }
        }
        //System.out.println("scoreT: " + scoreT);
        if(scoreT == 0)
            return 0;
        // move lines to fill empty lines (sort)
        //System.out.println("ggggggggggggg");
        for(int i = 23; i >= 4; i--) {
            if(rowEmpty(i)) {
                int notEmptyRow = -1;
                for(int j = i-1; j >= 4; j--) { // finds the first non empty row
                    if(!rowEmpty(j)) {
                        notEmptyRow = j;
                        break;
                    }
                }
                if(notEmptyRow == -1) {
                    break;// if all the remaining lines are empty then break
                } else { // swaps
                    ImageIcon[] tmp = board[i];
                    board[i] = board[notEmptyRow];
                    board[notEmptyRow] = tmp;
                }
            }
        }
        score += scoreT;
        return scoreT;
    }
    private boolean rowFull(int r) { // returns true if a row is full and false otherwise
        for(ImageIcon c : board[r])
            if(c.equals(E))
                return false;
        return true;
    }
    private boolean rowEmpty(int r) { // returns true if a row is empty and false otherwise
        for(ImageIcon c : board[r])
            if(!c.equals(E))
                return false;
        return true;
    }
    public void hardDrop() { // sets piece location to the lowest it can go
        current.setLocation(current.getX(), current.lowestFit(), board);
    }
    public void move(int direction) { // moves the piece based on direction
        if(fits()) {
            if(direction == 0) { // up (only for testing purposes)
                current.setLocation(current.getX(), current.getY()-1, board);
            } else if(direction == 1) { // right
                current.setLocation(current.getX()+1, current.getY(), board);
            } else if(direction == 2) { // down
                current.setLocation(current.getX(), current.getY()+1, board);
            } else { // left
                current.setLocation(current.getX()-1, current.getY(), board);
            }
        }
    }
    public void rotateCW() {
        current.rotateCW();
    }
    public void rotateCCW() {
        current.rotateCCW();
    }
    public void rotate180() {
        current.rotate180();
    }
    public boolean fits() {
        return true;
    }
    public Piece getCurrent() {
        return current;
    }
    public void newPiece() {
        current = next1;
        next1 = next2;
        next2 = next3;
        next3 = new Piece();
        current.setLocation(4,2, board);
        current.placePiece(board);
        holdToggle = true;
        updateNextBoard();
        //System.out.println(current.getX() + " " + current.getY());
    }
    public int getColor(int x, int y) { // returns color of board[x][y]
        ImageIcon temp = board[y][x];
        
        if(temp == I) {
            return 1;
        } else if(temp == O) {
            return 2;
        } else if(temp == T) {
            return 3;
        } else if(temp == S) {
            return 4;
        } else if(temp == Z) {
            return 5;
        } else if(temp == J) {
            return 6;
        } else if(temp == L) {
            return 7;
        } else { // temp == E
            return 0;
        }
    }
    public void printBoard() { // prints board in terminal for debugging
        String s = "";
        for(ImageIcon[] r : board) {
            for(ImageIcon c : r) {
                if(c.equals(E)) {
                    s+="E";
                } else if(c.equals(I)) {
                    s+="I";
                } else if(c.equals(O)) {
                    s+="O";
                } else if(c.equals(T)) {
                    s+="T";
                } else if(c.equals(S)) {
                    s+="S";
                } else if(c.equals(Z)) {
                    s+="Z";
                } else if(c.equals(J)) {
                    s+="J";
                } else if(c.equals(L)) {
                    s+="L";
                }
                s+=" ";
            }
            s+="\n";
        }
        System.out.println(s);
    }
    public void printShadowBoard() { // prints shadowboard in terminal for debugging
        String s = "";
        for(ImageIcon[] r : shadowBoard) {
            for(ImageIcon c : r) {
                if(c.equals(E)) {
                    s+="E";
                } else if(c.equals(sI)) {
                    s+="I";
                } else if(c.equals(sO)) {
                    s+="O";
                } else if(c.equals(sT)) {
                    s+="T";
                } else if(c.equals(sS)) {
                    s+="S";
                } else if(c.equals(sZ)) {
                    s+="Z";
                } else if(c.equals(sJ)) {
                    s+="J";
                } else if(c.equals(sL)) {
                    s+="L";
                }
                s+=" ";
            }
            s+="\n";
        }
        System.out.println(s);
    }
}
