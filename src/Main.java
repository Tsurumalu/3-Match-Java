import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1200, 750);
            GameController gameController = new GameController(mainFrame, new Chessboard(0));
            mainFrame.setVisible(true);
        });
    }
}
