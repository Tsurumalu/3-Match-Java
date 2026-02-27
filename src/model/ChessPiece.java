package model;

import javax.swing.*;
import java.awt.*;

public class ChessPiece {
    private String name;

    private Color color;

    public ChessPiece(String name) {
        this.name = name;
        this.color = Constant.colorMap.get(name);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    final ImageIcon image1 = new ImageIcon("src/resources/image/piece/1.png");
    final ImageIcon image2 = new ImageIcon("src/resources/image/piece/2.png");
    final ImageIcon image3 = new ImageIcon("src/resources/image/piece/3.png");
    final ImageIcon image4 = new ImageIcon("src/resources/image/piece/4.png");
    final ImageIcon image5 = new ImageIcon("src/resources/image/piece/5.png");

    public ImageIcon getIcon() {
        switch (name) {
            case "💎":
                return image1;
            case "⚪":
                return image2;
            case "▲":
                return image3;
            case "🔶":
                return image4;
            case "x":
                return image5;
        }
        return null;
    }

}
