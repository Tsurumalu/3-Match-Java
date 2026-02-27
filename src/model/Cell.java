package model;

import java.io.Serializable;

/**
 * This class describe the slot for Chess in Chessboard
 */
public class Cell implements Serializable {
    private ChessPiece piece;
    private boolean selected;
    public boolean hinted;

    public Cell() {
    }

    public Cell(ChessPiece piece) {
        this.piece = piece;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public void setHinted(boolean hinted) {
        this.hinted = hinted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void removePiece() {
        this.piece = null;
    }

    public Cell clone() {
        if (piece == null)
            return new Cell();
        return new Cell(new ChessPiece(piece.getName()));
    }
}
