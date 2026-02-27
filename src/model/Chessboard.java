package model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import controller.GameController;

/**
 * This class store the real chess information.
 * The Chessboard has 8 * 8 cells, and each cell has a position for chess
 */
public class Chessboard {
    public GameController gameController;

    public int n, m;
    public boolean[][] del;
    public Cell[][] grid;
    public Cell[][][] hisGrid;
    public int[] hisScore;
    public boolean[][] haveCell;

    public int level, score, combo, stage, stepMax, stepUse, shuffleLeft;

    /*
     * stage:
     * 0 normal
     * 1 after confirm swap and match clear
     * 2 after blocks fall
     * 3 generateNew (back to 0 or 1)
     */

    public Chessboard(int randomSeed) {

    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }

    public int[] stepBegin = { 0, 12, 12, 12, 10, 10, 8, 8, 8 };
    public int[] scoreNeed = { 0, 360, 500, 800, 800, 1000, 1000, 1500, 1500 };
    public int[] obsNeed = { 0, 1, 1, 1, 1, 2, 2, 2, 3 };

    public void setSize(int d) {
        if (d > 0) {
            n = m = d;
        } else {
            n = m = 8;
        }
        grid = new Cell[n][m];
        haveCell = new boolean[n][m];
        del = new boolean[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                haveCell[i][j] = true;
        if (d == -1) {
            for (int i = 2; i <= 5; i++)
                for (int j = 0; j < 8; j++)
                    if (j <= 1 || j >= 6)
                        haveCell[i][j] = false;
        }
        if (d == -2) {
            for (int i = 0; i < 8; i++)
                if (i <= 1 || i >= 6)
                    for (int j = 2; j <= 5; j++)
                        haveCell[i][j] = false;
        }
        if (d == -3) {
            int len = 0;
            for (int i = 1; i <= 6; i++) {
                if (i <= 3)
                    len++;
                if (i > 4)
                    len--;
                for (int j = 0; j < len; j++)
                    haveCell[i][j] = false;
                for (int j = 8 - len; j < 8; j++)
                    haveCell[i][j] = false;
            }
        }
    }

    public void init() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    grid[i][j] = new Cell();
        generateBlank();
        level++;
        generateObs(obsNeed[level]);
        shuffleLeft++;
        score = 0;
        stage = 0;
        stepMax = stepBegin[level];
        stepUse = 0;
        hisGrid = new Cell[stepMax + 1][][];
        hisScore = new int[stepMax + 1];
        gameController.frame.repaintChessboard(this);
        saveVersion();
        checkStatus();
    }

    public int initFromFile(String Path) {
        try {
            File file = new File(Path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            s = reader.readLine();
            Scanner rd = new Scanner(s);
            n = rd.nextInt();
            m = rd.nextInt();
            level = rd.nextInt();
            stepMax = stepBegin[level];
            score = rd.nextInt();
            stepUse = rd.nextInt();
            shuffleLeft = rd.nextInt();
            grid = new Cell[n][m];
            haveCell = new boolean[n][m];
            del = new boolean[n][m];
            hisGrid = new Cell[stepMax + 1][][];
            hisScore = new int[stepMax + 1];
            for (int i = 0; i < n; i++) {
                s = reader.readLine();
                rd = new Scanner(s);
                for (int j = 0; j < m; j++) {
                    int x;
                    x = rd.nextInt();
                    if (x != 0 && x != 1)
                        return 103;
                    haveCell[i][j] = (x == 1);
                }
            }
            String[] pic = { "", "💎", "⚪", "▲", "🔶", "x" };

            for (int i = 0; i < n; i++) {
                s = reader.readLine();
                if (s == null)
                    return 102;
                rd = new Scanner(s);
                for (int j = 0; j < m; j++) {
                    int x;
                    x = rd.nextInt();
                    if (!haveCell[i][j]) {
                        if (x != 0)
                            return 103;
                    } else {
                        if (x < 1 || x > 5)
                            return 103;
                    }
                    grid[i][j] = new Cell(new ChessPiece(pic[x]));
                }
            }
            for (int t = 0; t <= stepUse; t++) {
                hisGrid[t] = new Cell[n][m];
                for (int i = 0; i < n; i++) {
                    s = reader.readLine();
                    if (s == null)
                        return 102;
                    rd = new Scanner(s);
                    for (int j = 0; j < m; j++) {
                        int x = rd.nextInt();
                        if (!haveCell[i][j]) {
                            if (x != 0)
                                return 103;
                        } else {
                            if (x < 1 || x > 5)
                                return 103;
                        }
                        hisGrid[t][i][j] = new Cell(new ChessPiece(pic[x]));
                    }
                }
            }
            s = reader.readLine();
            rd = new Scanner(s);
            for (int t = 0; t <= stepUse; t++) {
                int x = rd.nextInt();
                hisScore[t] = x;
            }
        } catch (Exception e) {
            return 102;
        }
        gameController.frame.initLevelStart();
        gameController.frame.updateLevel(level);
        gameController.frame.updateScoreNeed(scoreNeed[level]);
        gameController.frame.updateStepLeft(stepMax - stepUse);
        gameController.frame.updateShuffleLeft(shuffleLeft);
        gameController.frame.updateScore(score);
        checkStatus();
        return 0;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("%d %d %d %d %d %d\n", n, m, level, score, stepUse, shuffleLeft));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                res.append(String.format("%d ", (haveCell[i][j] ? 1 : 0)));
            res.append("\n");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                res.append(String.format("%d ", getColor(i, j)));
            res.append("\n");
        }
        for (int t = 0; t <= stepUse; t++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++)
                    res.append(String.format("%d ", getColor(i, j)));
                res.append("\n");
            }
        }
        for (int t = 0; t <= stepUse; t++) {
            res.append(String.format("%d ", hisScore[t]));
        }
        res.append("\n");
        return res.toString();
    }

    class SwapOperate {
        int x1, y1, x2, y2, c;

        public SwapOperate(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            c = trySwapAuto(x1, y1, x2, y2);
        }
    }

    class findBestStep {
        Cell[][] a, b;
        boolean[][] del;

        public SwapOperate cal(int x1, int y1, int x2, int y2) {
            SwapOperate v = new SwapOperate(x1, y1, x2, y2);
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (haveCell[i][j])
                        b[i][j] = a[i][j].clone();
            if (getColor(x1, y1) == 0 || getColor(x2, y2) == 0 || getColor(x1, y1) == 5 || getColor(x2, y2) == 5) {
                v.c = 0;
                return v;
            }
            Cell t = b[x1][y1].clone();
            b[x1][y1] = b[x2][y2];
            b[x2][y2] = t;
            del = new boolean[n][m];
            int combo = 0, score = 0;
            while (checkDel()) {
                int x = deleteMatch();
                score += (int) ((Math.pow(1.2, combo) * x * 10));
                combo++;
                blockFall();
            }
            v.c = score;
            return v;
        }

        public SwapOperate sol() {
            a = new Cell[n][m];
            b = new Cell[n][m];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (haveCell[i][j])
                        a[i][j] = grid[i][j].clone();
            SwapOperate mx = null;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i + 1 < n) {
                        SwapOperate t = cal(i, j, i + 1, j);
                        if (mx == null || t.c > mx.c)
                            mx = t;
                    }
                    if (j + 1 < m) {
                        SwapOperate t = cal(i, j, i, j + 1);
                        if (mx == null || t.c > mx.c)
                            mx = t;
                    }
                }
            }
            return mx;
        }

        public boolean haveMatch(int x, int y) {
            if (getColor(x, y) == 0)
                return false;
            if (x - 2 >= 0 && getColor(x, y) == getColor(x - 2, y) && getColor(x, y) == getColor(x - 1, y))
                return true;
            if (x - 1 >= 0 && x + 1 < n && getColor(x, y) == getColor(x - 1, y) && getColor(x, y) == getColor(x + 1, y))
                return true;
            if (x + 2 < n && getColor(x, y) == getColor(x + 1, y) && getColor(x, y) == getColor(x + 2, y))
                return true;
            if (y - 2 >= 0 && getColor(x, y) == getColor(x, y - 2) && getColor(x, y) == getColor(x, y - 1))
                return true;
            if (y - 1 >= 0 && y + 1 < n && getColor(x, y) == getColor(x, y - 1) && getColor(x, y) == getColor(x, y + 1))
                return true;
            if (y + 2 < m && getColor(x, y) == getColor(x, y + 1) && getColor(x, y) == getColor(x, y + 2))
                return true;
            return false;
        }

        public int getColor(int x, int y) {
            if (!haveCell[x][y] || b[x][y].getPiece() == null)
                return 0;
            ChessPiece p = b[x][y].getPiece();
            if (p == null)
                return 0;
            if (p.getColor() == Color.blue)
                return 1;
            if (p.getColor() == Color.white)
                return 2;
            if (p.getColor() == Color.green)
                return 3;
            if (p.getColor() == Color.orange)
                return 4;
            if (p.getColor() == Color.black)
                return 5;
            return -1;
        }

        public boolean checkDel() {
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (haveCell[i][j])
                        del[i][j] = haveMatch(i, j);
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (haveCell[i][j])
                        if (del[i][j])
                            return true;
            return false;
        }

        public int deleteMatch() {
            int tot = 0;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (haveCell[i][j] && del[i][j]) {
                        tot++;
                        b[i][j].setPiece(null);
                        del[i][j] = false;
                    }
            return tot;
        }

        public void blockFall() {
            for (int i = 0; i < m; i++) {
                int[] q = new int[n];
                int bg = 0, ed = 0;
                for (int j = n - 1; j >= 0; j--) {
                    if (haveCell[j][i] && getColor(j, i) == 5)
                        continue;
                    if (haveCell[j][i])
                        q[ed++] = j;
                    if (haveCell[j][i] && b[j][i].getPiece() != null) {
                        int x = q[bg++];
                        b[x][i].setPiece(b[j][i].getPiece());
                    }
                }
                while (bg < ed) {
                    int x = q[bg++];
                    b[x][i].removePiece();
                }
            }
        }

    }

    public void initLevelBeginAuto() {
        gameController.frame.initLevelStartAuto();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    grid[i][j] = new Cell();
        generateBlank();
        level++;
        generateObs(obsNeed[level]);
        shuffleLeft++;
        score = 0;
        stage = 0;
        stepMax = stepBegin[level];
        stepUse = 0;
        gameController.frame.repaintChessboardAuto(this);
        while (true) {
            if (score >= scoreNeed[level]) {
                gameController.levelWinAuto();
                return;
            }
            if (stepUse == stepMax) {
                gameController.gameLoseAuto();
                return;
            }
            if (detectMustShuffle()) {
                if (shuffleLeft == 0) {
                    gameController.gameLoseAuto();
                    return;
                }
                shuffleAuto();
                continue;
            }
            findBestStep cal = new findBestStep();
            SwapOperate mx = cal.sol();
            int x1 = mx.x1, y1 = mx.y1, x2 = mx.x2, y2 = mx.y2;
            gameController.frame.showHint(String.format("Swap (%d,%d) and (%d,%d)", x1, y1, x2, y2));
            grid[x1][y1].setSelected(true);
            grid[x2][y2].setSelected(true);
            gameController.frame.repaintChessboardAuto(this);
            Swap(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2));
            grid[x1][y1].setSelected(false);
            grid[x2][y2].setSelected(false);
            gameController.frame.repaintChessboardAuto(this);
            combo = 0;
            while (checkDel()) {
                gameController.frame.repaintChessboardAuto(this);
                int x = deleteMatch();
                int v = (int) (Math.pow(1.2, combo) * 10 * x);
                score += v;
                gameController.frame.showHint(String.format("Score add %d, with combo %d", v, combo));
                gameController.frame.repaintChessboardAuto(this);
                combo++;
                blockFall();
                gameController.frame.repaintChessboardAuto(this);
                generateBlank();
                gameController.frame.repaintChessboardAuto(this);
                if (score >= scoreNeed[level])
                    break;
            }
            stepUse++;
        }
    }

    public void checkStatus() {
        if (score >= scoreNeed[level]) {
            gameController.levelWin();
            return;
        }
        if (stage != 0)
            return;
        if (stepUse == stepMax) {
            gameController.gameLose();
            return;
        }
        if (detectMustShuffle()) {
            if (shuffleLeft == 0) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gameController.frame.ShowMustShuffle();
                        try {
                            Thread.sleep(2000);
                            gameController.gameLose();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            } else
                gameController.frame.ShowMustShuffle();
        }
    }

    public boolean haveMatch(int x, int y) {
        if (getColor(x, y) == 0 || getColor(x, y) == 5)
            return false;
        if (x - 2 >= 0 && getColor(x, y) == getColor(x - 2, y) && getColor(x, y) == getColor(x - 1, y))
            return true;
        if (x - 1 >= 0 && x + 1 < n && getColor(x, y) == getColor(x - 1, y) && getColor(x, y) == getColor(x + 1, y))
            return true;
        if (x + 2 < n && getColor(x, y) == getColor(x + 1, y) && getColor(x, y) == getColor(x + 2, y))
            return true;
        if (y - 2 >= 0 && getColor(x, y) == getColor(x, y - 2) && getColor(x, y) == getColor(x, y - 1))
            return true;
        if (y - 1 >= 0 && y + 1 < n && getColor(x, y) == getColor(x, y - 1) && getColor(x, y) == getColor(x, y + 1))
            return true;
        if (y + 2 < m && getColor(x, y) == getColor(x, y + 1) && getColor(x, y) == getColor(x, y + 2))
            return true;
        return false;
    }

    public void generateBlank() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!haveCell[i][j])
                    continue;
                if (grid[i][j].getPiece() == null) {
                    generatePiece(i, j);
                    while (haveMatch(i, j))
                        generatePiece(i, j);
                }
            }
        }
    }

    public void generateObs(int c) {
        Random rnd = new Random(System.currentTimeMillis());
        while (c > 0) {
            int x, y;
            do {
                x = rnd.nextInt(n);
                y = rnd.nextInt(m);
            } while (!(getColor(x, y) >= 1) || !(getColor(x, y) <= 4));
            grid[x][y] = new Cell(new ChessPiece("x"));
            c--;
        }
    }

    public boolean checkDel() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    del[i][j] = haveMatch(i, j);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    if (del[i][j])
                        return true;
        return false;
    }

    public int deleteMatch() {
        int tot = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j] && del[i][j]) {
                    tot++;
                    grid[i][j].setPiece(null);
                    del[i][j] = false;
                }
        return tot;
    }

    public void blockFall() {
        Chessboard h = this.Clone();
        ArrayList<Integer> x1, y1, x2, y2;
        x1 = new ArrayList<>();
        y1 = new ArrayList<>();
        x2 = new ArrayList<>();
        y2 = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] q = new int[n];
            int bg = 0, ed = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (haveCell[j][i] && getColor(j, i) == 5)
                    continue;
                if (haveCell[j][i])
                    q[ed++] = j;
                if (haveCell[j][i] && grid[j][i].getPiece() != null) {
                    int x = q[bg++];
                    if (j != x) {
                        x1.add(j);
                        y1.add(i);
                        x2.add(x);
                        y2.add(i);
                    }
                    grid[x][i].setPiece(grid[j][i].getPiece());
                }
            }
            while (bg < ed) {
                int x = q[bg++];
                grid[x][i].removePiece();
            }
        }
    }

    public void Swap(ChessboardPoint p1, ChessboardPoint p2) {
        int x1 = p1.getRow(), y1 = p1.getCol(), x2 = p2.getRow(), y2 = p2.getCol();
        Cell t = grid[x1][y1].clone();
        grid[x1][y1] = grid[x2][y2];
        grid[x2][y2] = t;
    }

    public boolean validSwap(ChessboardPoint p1, ChessboardPoint p2) {
        clearHint();
        if (p1 == null || p2 == null || calculateDistance(p1, p2) != 1)
            return false;
        Swap(p1, p2);
        if (checkDel()) {
            Swap(p1, p2);
            Swap(p1, p2);
            gameController.frame.repaintChessboardAuto(this);
            int x = deleteMatch();
            combo = 0;
            gameController.frame.showHint(String.format("%d Combo, add %d Points", combo, x * 10));
            score += x * 10;
            gameController.frame.repaintChessboard(this);
            stage++;
            stepUse++;
            gameController.frame.repaintChessboard(this);
            return true;
        } else {
            Swap(p1, p2);
            return false;
        }
    }

    public void goNextStep() {
        if (stage == 1) {
            blockFall();
            stage++;
        } else if (stage == 2) {
            generateBlank();
            stage++;
            if (!checkDel()) {
                stage = 0;
                saveVersion();
            } else {
                gameController.frame.showHint("Combo exists:Please press Next Step!");
                gameController.frame.playComboAudio();
            }
        } else if (stage == 3) {
            int x = deleteMatch();
            combo++;
            int v = (int) (Math.pow(1.2, combo) * 10 * x);
            score += v;
            gameController.frame.showHint(String.format("%d Combo, add %d Points", combo, v));
            gameController.frame.repaintChessboard(this);
            stage = 1;
        }
    }

    boolean trySwap(int x1, int y1, int x2, int y2) {
        if (!haveCell[x1][y1] || !haveCell[x2][y2])
            return false;
        if (getColor(x1, y1) == 5 || getColor(x2, y2) == 5)
            return false;
        ChessboardPoint p1 = new ChessboardPoint(x1, y1);
        ChessboardPoint p2 = new ChessboardPoint(x2, y2);
        Swap(p1, p2);
        boolean ok = haveMatch(x1, y1) || haveMatch(x2, y2);
        Swap(p1, p2);
        return ok;
    }

    int trySwapAuto(int x1, int y1, int x2, int y2) {
        if (!haveCell[x1][y1] || !haveCell[x2][y2])
            return 0;
        ChessboardPoint p1 = new ChessboardPoint(x1, y1);
        ChessboardPoint p2 = new ChessboardPoint(x2, y2);
        Swap(p1, p2);
        checkDel();
        int c = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j] && del[i][j])
                    c++;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    del[i][j] = false;
        Swap(p1, p2);
        return c;
    }

    public void generateHint() {
        SwapOperate op = new findBestStep().sol();
        int x1 = op.x1, y1 = op.y1, x2 = op.x2, y2 = op.y2;
        grid[x1][y1].setHinted(true);
        grid[x2][y2].setHinted(true);
    }

    public void clearHint() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    grid[i][j].setHinted(false);
        gameController.view.rePaintAllChess(this);
    }

    public boolean detectMustShuffle() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i + 1 < n && trySwap(i, j, i + 1, j))
                    return false;
                if (j + 1 < m && trySwap(i, j, i, j + 1))
                    return false;
            }
        }
        return true;
    }

    public void shuffle() {
        clearHint();
        if (shuffleLeft == 0) {
            gameController.frame.showHint("No more shuffle chances");
            return;
        }
        shuffleLeft--;
        gameController.frame.updateShuffleLeft(shuffleLeft);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j] && getColor(i, j) != 5)
                    grid[i][j].setPiece(null);
        generateBlank();
    }

    public void shuffleAuto() {
        gameController.frame.showHint("Shuffle");
        shuffleLeft--;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    grid[i][j].setPiece(null);
        generateBlank();
        gameController.frame.repaintChessboardAuto(this);
    }

    public void saveVersion() {
        hisGrid[stepUse] = new Cell[n][m];
        hisScore[stepUse] = score;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    hisGrid[stepUse][i][j] = grid[i][j].clone();
    }

    public void withdraw() {
        stepUse--;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    grid[i][j] = hisGrid[stepUse][i][j].clone();
        score = hisScore[stepUse];
        gameController.frame.repaintChessboard(this);
    }

    public void generatePiece(int x, int y) {
        grid[x][y].setPiece(new ChessPiece(Util.RandomPick(new String[] { "💎", "⚪", "▲", "🔶" })));
    }

    public int getColor(int x, int y) {
        if (!haveCell[x][y])
            return 0;
        ChessPiece p = grid[x][y].getPiece();
        if (p == null)
            return 0;
        if (p.getColor() == Color.blue)
            return 1;
        if (p.getColor() == Color.white)
            return 2;
        if (p.getColor() == Color.green)
            return 3;
        if (p.getColor() == Color.orange)
            return 4;
        if (p.getColor() == Color.black)
            return 5;
        return -1;
    }

    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    public Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    public int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Chessboard Clone() {
        Chessboard c = new Chessboard(0);
        c.n = this.n;
        c.m = this.m;
        c.grid = new Cell[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (haveCell[i][j])
                    c.grid[i][j] = this.grid[i][j].clone();
        c.haveCell = new boolean[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                c.haveCell[i][j] = this.haveCell[i][j];
        return c;
    }
}
