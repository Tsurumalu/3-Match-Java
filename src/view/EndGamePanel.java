package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EndGamePanel extends JPanel {
    public JLabel pass;
    public JButton BackToHome;
    public Clip c;

    public AudioInputStream ais;
    public ChessGameFrame Frame;

    public EndGamePanel(ChessGameFrame Frame) {
        this.Frame = Frame;
        setSize(Frame.getSize());
        setLayout(null);
        setOpaque(false);
        addMusic();
        addPass();
        addBackHomeButton();
    }

    public void addPass() {
        pass = new JLabel("恭喜通关!");
        pass.setSize(400, 40);
        pass.setLocation(getWidth() / 2 - 200, getHeight() / 2 - 200);
        pass.setForeground(Color.white);
        pass.setFont(new Font("微软雅黑", Font.BOLD, 40));
        add(pass);
    }

    public void addMusic() {
        File f = new File("src/resources/Audio/win.wav");
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

    public void addBackHomeButton() {
        BackToHome = new JButton("Back to home");
        BackToHome.setLocation(getWidth() / 2 - 100, getHeight() / 2 - 30);
        BackToHome.setSize(200, 60);
        BackToHome.setFont(new Font("Calibri", Font.BOLD, 20));
        BackToHome.addActionListener((e -> Frame.onPlayerBackToHomeAction()));
        add(BackToHome);
    }
}
