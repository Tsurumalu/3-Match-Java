package view;

import controller.GameController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

public class ChessboardComponent extends JPanel {
    public CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE
            .getNum()];
    public int CHESS_SIZE;
    private final Set<ChessboardPoint> riverCell = new HashSet<>();

    private GameController gameController;

    public ChessboardComponent(int chessSize) {
        CHESS_SIZE = chessSize;
        int width = CHESS_SIZE * 8;
        int height = CHESS_SIZE * 8;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLayout(null);
        setSize(width, height);
    }

    public void resizeChessboard(Chessboard model) {
        for (int i = 0; i < model.n; i++) {
            for (int j = 0; j < model.m; j++) {
                gridComponents[i][j].setSize(CHESS_SIZE, CHESS_SIZE);
                gridComponents[i][j].revalidate();
                gridComponents[i][j].repaint();
            }
        }
    }

    public void rePaintAllChess(Chessboard model) {
        Cell[][] grid = model.getGrid();
        for (int i = 0; i < model.n; i++) {
            for (int j = 0; j < model.m; j++) {
                gridComponents[i][j].removeAll();
                gridComponents[i][j].revalidate();
                if (model.haveCell[i][j]) {
                    if (model.del[i][j])
                        gridComponents[i][j].background = Color.CYAN;
                    else if (grid[i][j].getSelected() && grid[i][j].getPiece() != null)
                        gridComponents[i][j].background = Color.GRAY;
                    else if (grid[i][j].getPiece() != null && grid[i][j].hinted)
                        gridComponents[i][j].background = Color.RED;
                    else
                        gridComponents[i][j].background = Color.LIGHT_GRAY;
                    if (grid[i][j].getPiece() != null) {
                        gridComponents[i][j].add(new ChessComponent(CHESS_SIZE, grid[i][j].getPiece(),
                                grid[i][j].getSelected(), grid[i][j].hinted));
                    }
                }
                repaint();
            }
        }
        repaint();
    }

    public void initiateGridComponents(Chessboard model) {
        for (int i = 0; i < model.n; i++) {
            for (int j = 0; j < model.m; j++) {
                ChessboardPoint temp = new ChessboardPoint(i, j);
                CellComponent cell;
                if (riverCell.contains(temp)) {
                    cell = new CellComponent(Color.CYAN, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cell);
                } else {
                    cell = new CellComponent(Color.LIGHT_GRAY, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cell);
                }
                gridComponents[i][j] = cell;
                if (!model.haveCell[i][j]) {
                    gridComponents[i][j].setVisible(false);
                }

            }
        }
    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setChessComponentAtGrid(ChessboardPoint point, ChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }

    public ChessComponent removeChessComponentAtGrid(ChessboardPoint point) {
        ChessComponent chess = (ChessComponent) getGridComponentAt(point).getComponents()[0];
        getGridComponentAt(point).removeAll();
        getGridComponentAt(point).revalidate();
        chess.setSelected(false);
        return chess;
    }

    public CellComponent getGridComponentAt(ChessboardPoint point) {
        return gridComponents[point.getRow()][point.getCol()];
    }

    private ChessboardPoint getChessboardPoint(Point point) {
        return new ChessboardPoint(point.y / CHESS_SIZE, point.x / CHESS_SIZE);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());
            if (clickedComponent.getComponentCount() == 0) {
                gameController.onPlayerClickCell(getChessboardPoint(e.getPoint()), (CellComponent) clickedComponent);
            } else {
                gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()));
            }
        }
    }

}
