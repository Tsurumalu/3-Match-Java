package view;

import model.Chessboard;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.jar.JarEntry;

public class GamePanel extends JPanel {
    public JLabel Score = new JLabel(), Level = new JLabel(), ScoreNeed = new JLabel(), Step = new JLabel(),
            Shuffle = new JLabel(), ShuffleHints = new JLabel(), Hints = new JLabel();
    public JButton SaveButton, Swap, NextStep, shuffleButton, HintButton, BackToHome, Withdraw;
    public ArrayList<ChessComponent> chess = new ArrayList<>();
    public ChessGameFrame Frame;
    public ChessboardComponent chessboardComponent;

    public GamePanel(ChessGameFrame Frame) {
        this.Frame = Frame;

        setSize(Frame.getSize());
        setLayout(null);
        setLocation(0, 0);
        setOpaque(false);

        addCurrentScore();
        addLevel();
        addScoreNeed();
        addStep();
        addSaveButton();
        addSwapConfirmButton();
        addNextStepButton();
        addWithdrawButton();
        addShuffle();
        addHintButton();
        addBackHomeButton();
        addChessboard(Frame.gameController.model);
        addHints();
        setVisible(true);

    }

    public void resizePanel(Dimension dimension) {
        setSize(dimension);
        revalidate();
        chessboardComponent.setLocation(getWidth() / 6, getHeight() / 10);
        chessboardComponent.setSize(Frame.ONE_CHESS_SIZE * 8, Frame.ONE_CHESS_SIZE * 8);
        chessboardComponent.CHESS_SIZE = Frame.ONE_CHESS_SIZE;
        chessboardComponent.rePaintAllChess(Frame.gameController.model);
        chessboardComponent.resizeChessboard(Frame.gameController.model);
        chessboardComponent.removeAll();
        chessboardComponent.revalidate();
        chessboardComponent.initiateGridComponents(Frame.gameController.model);

        Score.setLocation(getWidth() * 7 / 10, getHeight() * 4 / 40);
        Score.setSize(getWidth() / 3, getHeight() / 20);
        Level.setLocation(getWidth() * 7 / 10, getHeight() * 7 / 40);
        Level.setSize(getWidth() / 3, getHeight() / 20);
        ScoreNeed.setLocation(getWidth() * 7 / 10, getHeight() * 10 / 40);
        ScoreNeed.setSize(getWidth() / 3, getHeight() / 20);
        Step.setLocation(getWidth() * 7 / 10, getHeight() * 13 / 40);
        Step.setSize(getWidth() / 3, getHeight() / 20);

        Score.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        Level.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        ScoreNeed.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        Step.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));

        HintButton.setBounds(getWidth() / 30, getHeight() / 10, getWidth() / 10, getHeight() / 20);
        HintButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        shuffleButton.setBounds(getWidth() / 30, getHeight() * 7 / 40, getWidth() / 10, getHeight() / 20);
        shuffleButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        Shuffle.setBounds(getWidth() / 30, getHeight() * 9 / 40, getWidth() / 3, getHeight() / 25);
        Withdraw.setBounds(getWidth() / 30, getHeight() * 12 / 40, getWidth() / 10, getHeight() / 20);
        Withdraw.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));

        SaveButton.setBounds(getWidth() * 7 / 10, getHeight() * 16 / 40, getWidth() / 6, getHeight() / 15);
        Swap.setBounds(getWidth() * 7 / 10, getHeight() * 21 / 40, getWidth() / 6, getHeight() / 15);
        NextStep.setBounds(getWidth() * 7 / 10, getHeight() * 26 / 40, getWidth() / 6, getHeight() / 15);
        BackToHome.setBounds(getWidth() * 7 / 10, getHeight() * 31 / 40, getWidth() / 6, getHeight() / 15);
        ShuffleHints.setBounds(getWidth() / 3, getHeight() * 36 / 40, getWidth() / 3, getHeight() / 15);
        Hints.setBounds(getWidth() / 2 - getWidth() / 6, getHeight() / 20, getWidth() / 3, getHeight() / 15);
        NextStep.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        BackToHome.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        Swap.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        SaveButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));

        repaint();
    }

    public void addChessboard(Chessboard model) {
        chessboardComponent = new ChessboardComponent(Frame.ONE_CHESS_SIZE);
        chessboardComponent.initiateGridComponents(model);
        chessboardComponent.setLocation(getWidth() / 6, getHeight() / 10);
        add(chessboardComponent);
        chessboardComponent.setVisible(true);
        repaint();
    }

    public void addCurrentScore() {
        Score.setText("Score:0");
        Score.setLocation(getWidth() * 7 / 10, getHeight() * 4 / 40);
        Score.setSize(getWidth() / 3, getHeight() / 10);
        Score.setForeground(Color.white);
        Score.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        add(Score);
        repaint();
    }

    public void updateScore(int score) {
        Score.setText(String.format("Score:%d", score));
    }

    public void addLevel() {
        Level = new JLabel("Level:");
        Level.setLocation(getWidth() * 7 / 10, getHeight() * 7 / 40);
        Level.setSize(getWidth() / 3, getHeight() / 10);
        Level.setForeground(Color.white);
        Level.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        add(Level);
    }

    public void updateLevel(int level) {
        Level.setText(String.format("Level:%d", level));
    }

    public void addScoreNeed() {
        ScoreNeed = new JLabel("Score Need:");
        ScoreNeed.setLocation(getWidth() * 7 / 10, getHeight() * 10 / 40);
        ScoreNeed.setSize(getWidth() / 3, getHeight() / 10);
        ScoreNeed.setForeground(Color.white);
        ScoreNeed.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        add(ScoreNeed);
    }

    public void updateScoreNeed(int scoreneed) {
        ScoreNeed.setText(String.format("Score Need:%d", scoreneed));

    }

    public void addStep() {
        Step = new JLabel("Steps Left:_");
        Step.setLocation(getWidth() * 7 / 10, getHeight() * 13 / 40);
        Step.setSize(getWidth() / 3, getHeight() / 10);
        Step.setForeground(Color.white);
        Step.setFont(new Font("Calibri", Font.BOLD, getHeight() / 20));
        add(Step);
        repaint();
    }

    public void updateStepLeft(int steps) {
        Step.setText(String.format("Steps left:%d", steps));
    }

    public void addHintButton() {
        HintButton = new JButton("Hint");
        HintButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        HintButton.setBounds(getWidth() / 30, getHeight() / 10, getWidth() / 10, getHeight() / 20);
        HintButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hintButtonAction();
            }
        });
        add(HintButton);
    }

    public void hintButtonAction() {
        Frame.gameController.onPlayerHint();
    }

    public void addShuffleButton() {
        shuffleButton = new JButton("Shuffle");
        shuffleButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        shuffleButton.setBounds(getWidth() / 30, getHeight() * 7 / 40, getWidth() / 10, getHeight() / 20);
        shuffleButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleButtonAction();
            }
        });
        add(shuffleButton);
    }

    public void addShuffle() {
        Shuffle = new JLabel("Shuffle Left:");
        Shuffle.setForeground(Color.white);
        Shuffle.setBounds(getWidth() / 30, getHeight() * 9 / 40, getWidth() / 3, getHeight() / 25);
        Shuffle.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        add(Shuffle);
        addShuffleButton();
        repaint();
    }

    public void shuffleButtonAction() {
        Frame.gameController.onPlayerShuffle();
    }

    public void updateShuffleLeft(int shuffle) {
        Shuffle.setText(String.format("Shuffle Left:%d", shuffle));
    }

    public void addWithdrawButton() {
        Withdraw = new JButton("Withdraw");
        Withdraw.addActionListener((e) -> WithdrawButtonAction());
        Withdraw.setBounds(getWidth() / 30, getHeight() * 12 / 40, getWidth() / 10, getHeight() / 20);
        Withdraw.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        add(Withdraw);
    }

    public void WithdrawButtonAction() {
        Frame.gameController.onPlayerWithdraw();
    }

    private void addSaveButton() {
        SaveButton = new JButton("Save Game");
        SaveButton.addActionListener((e) -> Frame.saveGame());
        SaveButton.setBounds(getWidth() * 7 / 10, getHeight() * 16 / 40, getWidth() / 6, getHeight() / 15);
        SaveButton.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        add(SaveButton);
    }

    public void addSwapConfirmButton() {
        Swap = new JButton("Confirm Swap");
        Swap.addActionListener((e) -> SwapConfirmButtonAction());
        Swap.setBounds(getWidth() * 7 / 10, getHeight() * 21 / 40, getWidth() / 6, getHeight() / 15);
        Swap.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        add(Swap);
    }

    public void SwapConfirmButtonAction() {
        Frame.gameController.onPlayerSwapConfirm();
    }

    public void addNextStepButton() {
        NextStep = new JButton("Next Step");
        NextStep.addActionListener((e) -> NextSwapButtonAction());
        NextStep.setBounds(getWidth() * 7 / 10, getHeight() * 26 / 40, getWidth() / 6, getHeight() / 15);
        NextStep.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        add(NextStep);
    }

    public void NextSwapButtonAction() {
        Frame.gameController.onPlayerNextSwap();
    }

    public void addBackHomeButton() {
        BackToHome = new JButton("Back to home");
        BackToHome.setBounds(getWidth() * 7 / 10, getHeight() * 31 / 40, getWidth() / 6, getHeight() / 15);
        BackToHome.setFont(new Font("Calibri", Font.BOLD, getHeight() / 40));
        BackToHome.addActionListener((e -> Frame.onPlayerBackToHomeAction()));
        add(BackToHome);
    }

    public void addShuffleHint() {
        ShuffleHints.setText("Shuffle Only!");
        ShuffleHints.setFont(new Font("Calibri", Font.BOLD, 40));
        ShuffleHints.setBounds(getWidth() / 3, getHeight() * 36 / 40, getWidth() / 3, getHeight() / 15);
        add(ShuffleHints);
    }

    public void addHints() {
        Hints.setText("");
        Hints.setFont(new Font("Calibri", Font.BOLD, 20));
        Hints.setForeground(Color.white);
        Hints.setLocation(getWidth() / 2 - 200, getHeight() / 20);
        Hints.setSize(400, 60);
        Hints.setBounds(getWidth() / 2 - getWidth() / 6, getHeight() / 20, getWidth() / 3, getHeight() / 15);
        add(Hints);
    }

    public void showHints(String string) {
        Hints.setText(string);
    }

    public void repaintAuto(Chessboard model) {
        Frame.repaintChessboard(model);
        Frame.revalidate();

    }

    public void AutoPanel() {
        remove(Swap);
        remove(NextStep);
        remove(SaveButton);
        remove(HintButton);
        remove(Withdraw);
        BackToHome.setBounds(getWidth() * 7 / 10, getHeight() * 16 / 40, getWidth() / 6, getHeight() / 15);
    }

}
