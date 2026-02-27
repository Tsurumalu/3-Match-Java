package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.SwingConstants.CENTER;

public class SwitchLevelPanel extends JPanel {
    public JButton nextLevel, BackToHome;
    public JLabel pass;
    public ChessGameFrame Frame;

    public SwitchLevelPanel(ChessGameFrame Frame) {
        this.Frame = Frame;
        setSize(Frame.getSize());
        setLayout(null);
        setOpaque(false);

        addPass();
        addNextLevel();
        addBackHomeButton();

    }

    public void addPass() {
        pass = new JLabel("  恭喜通过本关  ");
        pass.setSize(getWidth() / 3, getHeight() / 20);
        pass.setForeground(Color.white);
        pass.setLocation(getWidth() / 2 - getWidth() / 6, getHeight() / 5);
        pass.setFont(new Font("微软雅黑", Font.BOLD, getHeight() / 20));
        add(pass);
    }

    public void addNextLevel() {
        nextLevel = new JButton("Next Level");
        nextLevel.setHorizontalTextPosition(CENTER);
        nextLevel.setSize(getWidth() / 6, getHeight() / 10);
        nextLevel.setFont(new Font("Calibri", Font.BOLD, getHeight() / 30));
        nextLevel.setLocation(getWidth() / 2 - getWidth() / 12, getHeight() / 2 - getHeight() / 20);
        nextLevel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.gameController.initLevelBegin();
                repaint();
            }
        });
        add(nextLevel);
    }

    public void addBackHomeButton() {
        BackToHome = new JButton("Back to home");
        BackToHome.setLocation(getWidth() / 2 - getWidth() / 12, getHeight() / 2 + getHeight() / 20);
        BackToHome.setSize(getWidth() / 6, getHeight() / 10);
        BackToHome.setFont(new Font("Calibri", Font.BOLD, getHeight() / 30));
        BackToHome.addActionListener((e -> Frame.onPlayerBackToHomeAction()));
        add(BackToHome);
    }

}
