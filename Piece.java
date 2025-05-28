import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class Piece {
    private int type, x, y, rotation; // x 1-10, y 1-20
    private static boolean[] bag = new boolean[8];
    // x and y refer to top left corner of piece
    /* types:
     * 1 - aqua I
     * 2 - yellow O
     * 3 - purple T
     * 4 - green S
     * 5 - red Z
     * 6 - blue J
     * 7 - orange L
    */
    public Piece()  {
        type = (int)(Math.random()*7)+1; //1-7
        boolean bagComplete = true;
        for(int i = 1; i < 8; i++) {
            if(!bag[i])
                bagComplete = false;
        }
        if(bagComplete) {
            bag = new boolean[8];
        }
        while(bag[type]) {
            type = (int)(Math.random()*7)+1; // 1-7
        }
        bag[type] = true;
        x = 4; // spawns piece at these coords
        y = 2;
        rotation = 0;
    }
    public Piece(int t, int xC, int yC, int r) {
        type = t;
        x = xC;
        y = yC;
        rotation = r;
    }
    public void updateShadow() { // updates placement 
        for(int r = 0; r < 24; r++) {
            for(int c = 0; c < 10; c++) {
                if(!(Tetris.shadowBoard[r][c].equals(Tetris.E)))
                    Tetris.shadowBoard[r][c] = Tetris.E;
            }
        }
        int tempY = y;
        y = lowestFit();
        placePiece(Tetris.shadowBoard);
        y = tempY;
    }
    public void setLocation(int xC, int yC) { // changes x and y
        x = xC;
        y = yC;
    }
    public void setLocation(int xC, int yC, ImageIcon[][] b) { // sets location to xC, yC if it fits
            int xTemp = x, yTemp = y;
            removePiece(b);
            x = xC;
            y = yC;
            if(!fits(x,y)) {
                x = xTemp;
                y = yTemp;
            }
            placePiece(b);
    }
    public int lowestFit() { // returns y coord of lowest position
        int lowY = y;
        removePiece(Tetris.board);
        while(fits(x, lowY+1)) {
            lowY++;
        }
        placePiece(Tetris.board);
        return lowY;
    }
    public void rotateCW() {
        removePiece(Tetris.board);
        rotation = (rotation+1)%4;
        if(!fits(x,y)) {
            int xC = x, yC = y;
            for(int r = x-1; r <= x+1; r++) { // test 1 radius
                for(int c = y-1; c <= y+1; c++) {
                    if((r!=x || c!=y) && fits(r,c)) {
                        xC = r; yC = c;
                        break;
                    }
                }
                if(xC != x || yC != y) {
                    break;
                }
            }
            if(xC == x && yC == y) {
                // test 2 radius
                for(int r = x-2; r <= x+2; r++) { // test 1 radius
                    for(int c = y-2; c <= y+2; c++) {
                        if((r>x+1 || r < x-1) && (c>y+1 || c<y-1) && fits(r,c)) {
                            xC = r; yC = c;
                            break;
                        }
                    }
                    if(xC != x || yC != y) {
                        break;
                    }
                }
            }
            if(xC != x || yC != y) {
                setLocation(xC, yC);
            } else {
                rotation = (rotation-1)%4;
                if(rotation < 0)
                    rotation += 4;
            } 
        }
        placePiece(Tetris.board);
    }
    public void rotateCCW() {
        removePiece(Tetris.board);
        rotation = (rotation-1)%4;
        if(rotation < 0)
            rotation += 4;
        if(!fits(x,y)) {
            int xC = x, yC = y;
            for(int r = x-1; r <= x+1; r++) { // test 1 radius
                for(int c = y-1; c <= y+1; c++) {
                    if((r!=x || c!=y) && fits(r,c)) {
                        xC = r; yC = c;
                        break;
                    }
                }
                if(xC != x || yC != y) {
                    break;
                }
            }
            if(xC == x && yC == y) {
                // test 2 radius
                for(int r = x-2; r <= x+2; r++) { // test 1 radius
                    for(int c = y-2; c <= y+2; c++) {
                        if((r>x+1 || r < x-1) && (c>y+1 || c<y-1) && fits(r,c)) {
                            xC = r; yC = c;
                            break;
                        }
                    }
                    if(xC != x || yC != y) {
                        break;
                    }
                }
            }
            if(xC != x || yC != y) {
                setLocation(xC, yC);
            } else {
                rotation = (rotation+1)%4;
            } 
        }
        placePiece(Tetris.board);
    }
    public void rotate180() {
        removePiece(Tetris.board);
        rotation = (rotation+2)%4;
        if(!fits(x,y)) {
            rotation = (rotation+2)%4;
        }
        placePiece(Tetris.board);
    }
    public int getType() { // returns type of piece
        return type;
    }
    public int getX() { // returns piece x value
        return x;
    }
    public int getY() { // returns piece y value
        return y;
    }
    public int getRotation() { // returns piece rotation value
        return rotation;
    }
    public int setRotation(int r) { // direction mutator
        int result = rotation;
        rotation = r;
        return result;
    }
    public void placePiece(ImageIcon[][] b) { // places piece based on x and y coords and rotation
        if(type == 1) { // I
            if(rotation == 0) {
                b[y+1][x] = Tetris.I;
                b[y+1][x+1] = Tetris.I;
                b[y+1][x+2] = Tetris.I;
                b[y+1][x+3] = Tetris.I;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.I;
                b[y+1][x+2] = Tetris.I;
                b[y+2][x+2] = Tetris.I;
                b[y+3][x+2] = Tetris.I;
            } else if(rotation == 2) {
                b[y+2][x] = Tetris.I;
                b[y+2][x+1] = Tetris.I;
                b[y+2][x+2] = Tetris.I;
                b[y+2][x+3] = Tetris.I;
            } else { // rotation == 3
                b[y][x+1] = Tetris.I;
                b[y+1][x+1] = Tetris.I;
                b[y+2][x+1] = Tetris.I;
                b[y+3][x+1] = Tetris.I;
            }
        } else if(type == 2) { // O
            b[y][x+1] = Tetris.O;
            b[y][x+2] = Tetris.O;
            b[y+1][x+1] = Tetris.O;
            b[y+1][x+2] = Tetris.O;
        } else if(type == 3) { // T
            if(rotation == 0) {
                b[y][x+1] = Tetris.T;
                b[y+1][x] = Tetris.T;
                b[y+1][x+1] = Tetris.T;
                b[y+1][x+2] = Tetris.T;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.T;
                b[y+1][x+1] = Tetris.T;
                b[y+2][x+1] = Tetris.T;
                b[y+1][x+2] = Tetris.T;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.T;
                b[y+1][x+1] = Tetris.T;
                b[y+1][x+2] = Tetris.T;
                b[y+2][x+1] = Tetris.T;
            } else { // rotation == 3
                b[y][x+1] = Tetris.T;
                b[y+1][x+1] = Tetris.T;
                b[y+2][x+1] = Tetris.T;
                b[y+1][x] = Tetris.T;
            }
        } else if(type == 4) { // S
            if(rotation == 0) {
                b[y+1][x] = Tetris.S;
                b[y+1][x+1] = Tetris.S;
                b[y][x+1] = Tetris.S;
                b[y][x+2] = Tetris.S;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.S;
                b[y+1][x+1] = Tetris.S;
                b[y+1][x+2] = Tetris.S;
                b[y+2][x+2] = Tetris.S;
            } else if(rotation == 2) {
                b[y+3][x] = Tetris.S;
                b[y+3][x+1] = Tetris.S;
                b[y+2][x+1] = Tetris.S;
                b[y+2][x+2] = Tetris.S;
            } else { // rotation == 3
                b[y][x] = Tetris.S;
                b[y+1][x] = Tetris.S;
                b[y+1][x+1] = Tetris.S;
                b[y+2][x+1] = Tetris.S;
            }
        } else if(type == 5) { // Z
            if(rotation == 0) {
                b[y][x] = Tetris.Z;
                b[y][x+1] = Tetris.Z;
                b[y+1][x+1] = Tetris.Z;
                b[y+1][x+2] = Tetris.Z;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.Z;
                b[y+1][x+2] = Tetris.Z;
                b[y+1][x+1] = Tetris.Z;
                b[y+2][x+1] = Tetris.Z;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.Z;
                b[y+1][x+1] = Tetris.Z;
                b[y+2][x+1] = Tetris.Z;
                b[y+2][x+2] = Tetris.Z;
            } else { // rotation == 3
                b[y][x+1] = Tetris.Z;
                b[y+1][x+1] = Tetris.Z;
                b[y+1][x] = Tetris.Z;
                b[y+2][x] = Tetris.Z;
            }
        } else if(type == 6) { // J
            if(rotation == 0) {
                b[y][x] = Tetris.J;
                b[y+1][x] = Tetris.J;
                b[y+1][x+1] = Tetris.J;
                b[y+1][x+2] = Tetris.J;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.J;
                b[y][x+1] = Tetris.J;
                b[y+1][x+1] = Tetris.J;
                b[y+2][x+1] = Tetris.J;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.J;
                b[y+1][x+1] = Tetris.J;
                b[y+1][x+2] = Tetris.J;
                b[y+2][x+2] = Tetris.J;
            } else { // rotation == 3
                b[y][x+1] = Tetris.J;
                b[y+1][x+1] = Tetris.J;
                b[y+2][x+1] = Tetris.J;
                b[y+2][x] = Tetris.J;
            }
        } else if(type == 7) { // L
            if(rotation == 0) {
                b[y+1][x] = Tetris.L;
                b[y+1][x+1] = Tetris.L;
                b[y+1][x+2] = Tetris.L;
                b[y][x+2] = Tetris.L;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.L;
                b[y+1][x+1] = Tetris.L;
                b[y+2][x+1] = Tetris.L;
                b[y+2][x+2] = Tetris.L;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.L;
                b[y+1][x+1] = Tetris.L;
                b[y+1][x+2] = Tetris.L;
                b[y+2][x] = Tetris.L;
            } else { // rotation == 3
                b[y][x] = Tetris.L;
                b[y][x+1] = Tetris.L;
                b[y+1][x+1] = Tetris.L;
                b[y+2][x+1] = Tetris.L;
            }
        }
    }
    // --------------------------------------------------------------
    public void removePiece(ImageIcon[][] b) { // removers piece based on x and y coords and rotation
        if(type == 1) { // I
            if(rotation == 0) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+1][x+3] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
                b[y+3][x+2] = Tetris.E;
            } else if(rotation == 2) {
                b[y+2][x] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
                b[y+2][x+3] = Tetris.E;
            } else { // rotation == 3
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+3][x+1] = Tetris.E;
            }
        } else if(type == 2) { // O
            b[y][x+1] = Tetris.E;
            b[y][x+2] = Tetris.E;
            b[y+1][x+1] = Tetris.E;
            b[y+1][x+2] = Tetris.E;
        } else if(type == 3) { // T
            if(rotation == 0) {
                b[y][x+1] = Tetris.E;
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
            } else { // rotation == 3
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+1][x] = Tetris.E;
            }
        } else if(type == 4) { // S
            if(rotation == 0) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y][x+1] = Tetris.E;
                b[y][x+2] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
            } else if(rotation == 2) {
                b[y+3][x] = Tetris.E;
                b[y+3][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
            } else { // rotation == 3
                b[y][x] = Tetris.E;
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
            }
        } else if(type == 5) { // Z
            if(rotation == 0) {
                b[y][x] = Tetris.E;
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
            } else { // rotation == 3
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x] = Tetris.E;
                b[y+2][x] = Tetris.E;
            }
        } else if(type == 6) { // J
            if(rotation == 0) {
                b[y][x] = Tetris.E;
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+2] = Tetris.E;
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
            } else { // rotation == 3
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+2][x] = Tetris.E;
            }
        } else if(type == 7) { // L
            if(rotation == 0) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y][x+2] = Tetris.E;
            } else if(rotation == 1) {
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
                b[y+2][x+2] = Tetris.E;
            } else if(rotation == 2) {
                b[y+1][x] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+1][x+2] = Tetris.E;
                b[y+2][x] = Tetris.E;
            } else { // rotation == 3
                b[y][x] = Tetris.E;
                b[y][x+1] = Tetris.E;
                b[y+1][x+1] = Tetris.E;
                b[y+2][x+1] = Tetris.E;
            }
        }
    }
    // ------------------------------------------------------------------
    public boolean fits(int xC, int yC) { // check if piece is in boundaries and does not overlap other pieces
        if(type == 1) { // I
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+3 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+1][xC+3] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+2 < 0 || yC < 0 || xC+2 > 9 || yC+3 > 23)
                    return false;
                if(Tetris.board[yC][xC+2] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E ||
                Tetris.board[yC+3][xC+2] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+2 < 0 || xC+3 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC+2][x] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC+3] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC+1 < 0 || yC < 0 || xC+1 > 9 || yC+3 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+3][xC+1] != Tetris.E) return false;
            }
        } else if(type == 2) { // O
            if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                return false;
            if(Tetris.board[yC][xC+1] != Tetris.E ||
            Tetris.board[yC][xC+2] != Tetris.E ||
            Tetris.board[yC+1][xC+1] != Tetris.E ||
            Tetris.board[yC+1][xC+2] != Tetris.E) return false;
        } else if(type == 3) { // T
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+1 < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC < 0 || yC < 0 || xC+1 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC] != Tetris.E) return false;
            }
        } else if(type == 4) { // S
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC][xC+2] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+2 < 0 || xC+2 > 9 || yC+3 > 23)
                    return false;
                if(Tetris.board[yC+3][x] != Tetris.E ||
                Tetris.board[yC+3][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC < 0 || yC < 0 || xC+1 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC] != Tetris.E ||
                Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E) return false;
            }
        } else if(type == 5) { // Z
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC][xC] != Tetris.E ||
                Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+2] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+1 < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC < 0 || yC < 0 || xC+1 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+2][xC] != Tetris.E) return false;
            }
        } else if(type == 6) { // J
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC][xC] != Tetris.E ||
                Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+2] != Tetris.E ||
                Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+1 < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC < 0 || yC < 0 || xC+1 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC] != Tetris.E) return false;
            }
        } else if(type == 7) { // L
            if(rotation == 0) {
                if(xC < 0 || yC < 0 || xC+2 > 9 || yC+1 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC][xC+2] != Tetris.E) return false;
            } else if(rotation == 1) {
                if(xC+1 < 0 || yC < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+2] != Tetris.E) return false;
            } else if(rotation == 2) {
                if(xC < 0 || yC+1 < 0 || xC+2 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC+1][xC] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+2] != Tetris.E ||
                Tetris.board[yC+2][xC] != Tetris.E) return false;
            } else { // rotation == 3
                if(xC < 0 || yC < 0 || xC+1 > 9 || yC+2 > 23)
                    return false;
                if(Tetris.board[yC][xC] != Tetris.E ||
                Tetris.board[yC][xC+1] != Tetris.E ||
                Tetris.board[yC+1][xC+1] != Tetris.E ||
                Tetris.board[yC+2][xC+1] != Tetris.E) return false;
            }
        }
        return true;
    }
}
