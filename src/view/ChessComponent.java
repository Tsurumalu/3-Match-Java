package view;

import model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.font.LineMetrics;

public class ChessComponent extends JComponent {

    private boolean selected;

    public boolean hinted;
    public ChessboardComponent chessboardComponent;

    private ChessPiece chessPiece;

    public ChessComponent(int size, ChessPiece chessPiece, boolean selected, boolean hinted) {
        this.selected = selected;
        this.hinted = hinted;
        setSize(size / 2, size / 2);
        setLocation(0, 0);
        setVisible(true);
        this.chessPiece = chessPiece;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isHinted() {
        return hinted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Helvetica", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        if (this.chessPiece != null) {
            g2.setColor(this.chessPiece.getColor());
            g2.drawImage(this.chessPiece.getIcon().getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}
