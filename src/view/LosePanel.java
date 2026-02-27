package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LosePanel extends JPanel {
    public JButton BackToHome;
    public JLabel lose;
    public Clip c;
    public AudioInputStream ais;
    public ChessGameFrame Frame;

    public LosePanel(ChessGameFrame Frame) {
        this.Frame = Frame;
        setSize(Frame.getSize());
        setLayout(null);
        setOpaque(false);

        addMusic();
        addLose();
        addBackHomeButton();

    }

    public void addMusic() {
        File f = new File("src/resources/Audio/lose.wav");
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

    public void addLose() {
        lose = new JLabel("You lose!");
        lose.setSize(400, 40);
        lose.setLocation(getWidth() / 2 - 200, getHeight() / 2 - 20);
        lose.setForeground(Color.white);
        lose.setFont(new Font("Calibri", Font.BOLD, 20));
        add(lose);
    }

    public void addBackHomeButton() {
        BackToHome = new JButton("Back to home");
        BackToHome.setLocation(getWidth() / 2 - 100, getHeight() / 2 - 30);
        BackToHome.setSize(200, 60);
        BackToHome.setFont(new Font("Calibri", Font.BOLD, 20));
        BackToHome.addActionListener((e -> Frame.onPlayerBackToHomeAction()));
        add(BackToHome);
    }
}
