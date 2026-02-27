package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static javax.swing.SwingConstants.CENTER;

public class LoginPanel extends JPanel {
    public JButton LoadButton, start, Auto;
    public JLabel jLabel;
    public Image backgroundImage;

    public ChessGameFrame jFrame;

    public LoginPanel(ChessGameFrame jFrame) {
        setSize(jFrame.getSize());
        setLayout(null);
        setOpaque(false);

        this.jFrame = jFrame;
        backgroundImage = new ImageIcon("C:/Users/48946/IdeaProjects/Project5.0/background.jpg").getImage();
        addManual();
        addTitle();
        addLoad();
        addAuto();

    }

    public void addTitle() {
        jLabel = new JLabel("Match-3");
        jLabel.setHorizontalAlignment(CENTER);
        jLabel.setBounds(getWidth() / 2 - getWidth() / 6, getHeight() / 4, getWidth() / 3, getHeight() / 10);
        jLabel.setForeground(Color.white);
        jLabel.setFont(new Font("Rockwell", Font.BOLD, getHeight() / 16));
        add(jLabel);
    }

    public void addManual() {
        start = new JButton("Manual Mode");
        start.setHorizontalTextPosition(CENTER);
        start.setFont(new Font("Rockwell", Font.BOLD, getHeight() / 40));
        start.setBounds(getWidth() / 2 - getWidth() / 12, getHeight() * 8 / 15, getWidth() / 6, getHeight() / 15);
        start.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.gameController.startManualGame();
            }
        });
        add(start);
    }

    public void addLoad() {
        LoadButton = new JButton("Load");
        LoadButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        LoadButton.setBounds(getWidth() / 2 - getWidth() / 12, getHeight() * 6 / 15, getWidth() / 6, getHeight() / 15);
        LoadButton.addActionListener(e -> {
            String path = load();
            jFrame.gameController.loadGameFromFile(path);

        });
        add(LoadButton);
    }

    public String load() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            return filePath;
        }
        return null;
    }

    public void addAuto() {
        Auto = new JButton("Auto Mode");
        Auto.setSize(200, 60);
        Auto.setLocation(getWidth() / 2 - 100, getHeight() / 2 + 30);
        Auto.setBounds(getWidth() / 2 - getWidth() / 12, getHeight() * 10 / 15, getWidth() / 6, getHeight() / 15);
        Auto.setFont(new Font("Rockwell", Font.BOLD, 20));
        Auto.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.gameController.startAutoGame();
            }
        });
        add(Auto);
    }

    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
