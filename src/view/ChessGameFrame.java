package view;

import controller.GameController;
import model.ChessPiece;
import model.Chessboard;
import model.ChessboardPoint;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.SwingConstants.CENTER;

public class ChessGameFrame extends JFrame {
    public Dimension FRAME_SIZE;
    public String state;
    private final int WIDTH;
    private final int HEIGTH;
    public GamePanel gamePanel;
    public ImageIcon Background = new ImageIcon("src/resources/Image/bg1.png");
    public JLabel bg = new JLabel(Background);
    public int ONE_CHESS_SIZE;
    public Clip c;
    public AudioInputStream ais;
    public GameController gameController;
    public ChessboardComponent chessboardComponent;

    public ChessGameFrame(int width, int height) {
        setTitle("Match-3 Game");
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;
        FRAME_SIZE = new Dimension(width, height);

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        bg.setBounds(0, 0, getWidth(), getHeight());
        getLayeredPane().add(bg, Integer.valueOf(Integer.MIN_VALUE));
        JPanel content = (JPanel) getContentPane();
        content.setOpaque(false);
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setVisible(false);

        CreateOpening();
        addMusic();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (getWidth() == FRAME_SIZE.width) {
                    setSize(getHeight() * 16 / 10, getHeight());
                } else {
                    setSize(getWidth(), getWidth() * 10 / 16);
                }
                FRAME_SIZE = e.getComponent().getSize();
                ONE_CHESS_SIZE = (getHeight() * 4 / 5) / 9;
                getContentPane().revalidate();
                bg.setBounds(0, 0, getWidth(), getHeight());
                switch (state) {
                    case "Opening":
                        CreateOpening();
                        break;
                    case "ManualGaming":
                        gamePanel.resizePanel(FRAME_SIZE);
                        repaintChessboard(gameController.model);
                        break;
                    case "AutoGaming":
                        initLevelStartAuto();
                        break;
                    case "Lose":
                        clearLevelLose();
                        break;
                    case "EndGame":
                        endGame();
                        break;
                    case "SwitchLevel":
                        cleanLevelEnd();
                        break;
                }
            }
        });
    }

    public void CreateOpening() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        setTitle("Welcome to the Game!");
        add(new LoginPanel(this));
        state = "Opening";
        repaint();
    }

    public void initLevelStartAuto() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        gamePanel = new GamePanel(this);
        gamePanel.AutoPanel();
        add(gamePanel);
        state = "AutoGaming";
        repaint();
        setLocationRelativeTo(null);
    }

    public void initLevelStart() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        setLocationRelativeTo(null);
        gamePanel = new GamePanel(this);
        add(gamePanel);
        state = "ManualGaming";
        repaint();
    }

    public void repaintChessboard(Chessboard model) {
        gamePanel.updateLevel(model.level);
        gamePanel.updateScore(model.score);
        gamePanel.updateScoreNeed(model.scoreNeed[model.level]);
        gamePanel.updateStepLeft(model.stepMax - model.stepUse);
        gamePanel.updateShuffleLeft(model.shuffleLeft);
        gamePanel.chessboardComponent.rePaintAllChess(model);
    }

    public void repaintChessboardAuto(Chessboard model) {
        gamePanel.repaintAuto(model);
        paint(this.getGraphics());
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    public void addMusic() {

        File f = new File("src/resources/Audio/bgm.wav");
        try {
            ais = AudioSystem.getAudioInputStream(f);
            c = AudioSystem.getClip();
            c.open(ais);
            c.setFramePosition(0);
            c.loop(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playClearAudio() {
        File f = new File("src/resources/Audio/clear.wav");
        try {
            ais = AudioSystem.getAudioInputStream(f);
            c = AudioSystem.getClip();
            c.open(ais);
            c.setFramePosition(0);
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playComboAudio() {
        File f = new File("src/resources/Audio/combo.wav");
        try {
            ais = AudioSystem.getAudioInputStream(f);
            c = AudioSystem.getClip();
            c.open(ais);
            c.setFramePosition(0);
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playWinAudio() {
        File f = new File("src/resources/Audio/win.wav");
        try {
            c.stop();
            c.stop();
            ais = AudioSystem.getAudioInputStream(f);
            c = AudioSystem.getClip();
            c.open(ais);
            c.setFramePosition(0);
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLoseAudio() {
        File f = new File("src/resources/Audio/lose.wav");
        try {
            ais = AudioSystem.getAudioInputStream(f);
            c = AudioSystem.getClip();
            c.open(ais);
            c.setFramePosition(0);
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHint(String str) {
        gamePanel.showHints(str);
    }

    public void onPlayerBackToHomeAction() {
        gameController.backToHome();
    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
        chessboardComponent = gameController.view;

    }

    public void ShowMustShuffle() {
        gamePanel.addShuffleHint();
        repaint();
    }

    public void cleanLevelEnd() {
        getContentPane().removeAll();
        add(new SwitchLevelPanel(this));
        state = "SwitchLevel";
        repaint();
        setLocationRelativeTo(null);
    }

    public void clearLevelLose() {
        getContentPane().removeAll();
        add(new LosePanel(this));
        state = "Lose";
        repaint();
        setLocationRelativeTo(null);
    }

    public void endGame() {
        getContentPane().removeAll();
        add(new EndGamePanel(this));
        state = "EndGame";
        repaint();
        setLocationRelativeTo(null);
    }

    public void updateScore(int score) {
        gamePanel.Score.setText(String.format("Score:%d", score));
    }

    public void updateLevel(int level) {
        gamePanel.Level.setText(String.format("Level:%d", level));
    }

    public void updateScoreNeed(int scoreneed) {
        gamePanel.ScoreNeed.setText(String.format("Score Need:%d", scoreneed));
    }

    public void updateStepLeft(int steps) {
        gamePanel.Step.setText(String.format("Steps left:%d", steps));
    }

    public void updateShuffleLeft(int shuffle) {
        gamePanel.Shuffle.setText(String.format("Shuffle Left:%d", shuffle));
    }

    public void clearMustShuffle() {
        if (gamePanel.ShuffleHints != null) {
            gamePanel.remove(gamePanel.ShuffleHints);
        }
        repaint();
    }

    public void saveGame() {
        if (gameController.haveNext) {
            gameController.frame.showHint("Please take next swap!");
            return;
        }
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Save Game");

        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath() + ".txt";
            gameController.saveGameAt(path);
            JOptionPane.showMessageDialog(this, "Game Saved!");
        }

    }

    public int chooseLevelStart() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        Object init = JOptionPane.showInputDialog(null, "Level", "choose your Level",
                JOptionPane.INFORMATION_MESSAGE, null, new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }, "1");
        if (init == null)
            return 0;
        int initLevel = Integer.parseInt(String.valueOf(init));
        repaint();
        return initLevel;
    }

    public void cleanLevelEndAuto(int level) {
        getContentPane().remove(gamePanel);
        JLabel pass = new JLabel(String.format("Pass Level %d", level));
        pass.setForeground(Color.white);
        pass.setFont(new Font("Calibri", Font.BOLD, 50));
        pass.setLocation(getWidth() / 2 - 500, getHeight() / 2 - 25);
        pass.setSize(1000, 50);
        add(pass);
        paint(this.getGraphics());
        revalidate();
        try {
            Thread.sleep(2000);
            remove(pass);
        } catch (Exception e) {
        }

    }

    public int chooseBoardSize() {
        Object size = JOptionPane.showInputDialog(null, "Board size", "choose Board Size",
                JOptionPane.INFORMATION_MESSAGE, null, new String[] { "I", "H", "8", "5*5", "6*6", "7*7", "8*8" },
                "8*8");
        if (size == null)
            return 0;
        String BoardSize = String.valueOf(size);
        repaint();
        switch (BoardSize) {
            case "5*5":
                return 5;
            case "6*6":
                return 6;
            case "7*7":
                return 7;
            case "8*8":
                return 8;
            case "I":
                return -1;
            case "H":
                return -2;
            case "8":
                return -3;
        }
        return 0;
    }

    public void ShowFileFormatError(int code) {
        if (code == 101) {
            JOptionPane.showMessageDialog(this, "Error code:101", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (code == 102) {
            JOptionPane.showMessageDialog(this, "Error code:102", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (code == 103) {
            JOptionPane.showMessageDialog(this, "Error code:103", "Error", JOptionPane.ERROR_MESSAGE);
        }
        CreateOpening();
    }

}
