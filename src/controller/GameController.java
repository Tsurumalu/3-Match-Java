package controller;

import model.Constant;
import model.Chessboard;
import model.ChessboardPoint;
import model.Util;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;
import view.ChessGameFrame;

import java.io.*;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 *
 */
public class GameController {

    public Chessboard model;
    public ChessGameFrame frame;
    public ChessboardComponent view;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint selectedPoint2;

    public boolean haveNext;

    public GameController(ChessGameFrame frame, Chessboard model) {
        this.frame = frame;
        this.model = model;

        frame.registerController(this);
        model.registerController(this);
    }

    public void backToHome() {
        frame.CreateOpening();
    }

    public void levelWin() {
        frame.playWinAudio();
        if (model.level == 8)
            frame.endGame();
        else
            frame.cleanLevelEnd();
    }

    public void levelWinAuto() {
        frame.playWinAudio();
        if (model.level == 8)
            frame.endGame();
        else {
            frame.cleanLevelEndAuto(model.level);
            model.initLevelBeginAuto();
        }
    }

    public void gameLose() {
        frame.playLoseAudio();
        frame.clearLevelLose();
    }

    public void gameLoseAuto() {
        frame.playLoseAudio();
        frame.clearLevelLose();
    }

    public void startManualGame() {
        int boardSize = frame.chooseBoardSize();
        if (boardSize == 0) {
            backToHome();
            return;
        }
        model.setSize(boardSize);
        int initLevel = frame.chooseLevelStart();
        if (initLevel == 0) {
            backToHome();
            return;
        }
        model.level = initLevel - 1;
        model.shuffleLeft = 3;
        Util.init((int) (System.currentTimeMillis()));
        initLevelBegin();
    }

    public void startAutoGame() {
        int boardSize = frame.chooseBoardSize();
        if (boardSize == 0) {
            backToHome();
            return;
        }
        model.setSize(boardSize);
        model.level = 0;
        model.shuffleLeft = 3;
        Util.init((int) (System.currentTimeMillis()));
        model.initLevelBeginAuto();
    }

    public void initLevelBegin() {
        frame.initLevelStart();
        model.init();
        initNew();
    }

    public void loadGameFromFile(String Path) {
        if (Path == null || !Path.substring(Path.length() - 4).equals(new String(".txt"))) {
            frame.ShowFileFormatError(101);
            return;
        }
        int errorCode = model.initFromFile(Path);
        if (errorCode != 0) {
            frame.ShowFileFormatError(errorCode);
            return;
        }
        initNew();
        Util.init(511);
    }

    public void initNew() {
        this.view = frame.gamePanel.chessboardComponent;
        view.registerController(this);
        selectedPoint = null;
        selectedPoint2 = null;
        haveNext = (model.stage != 0);
        view.rePaintAllChess(model);
    }

    public void saveGameAt(String Path) {
        try {
            File file = new File(Path);
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(model.toString());
            bufferedWriter.close();
        } catch (IOException e) {

        }
    }

    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
    }

    public void onPlayerSwapConfirm() {
        if (haveNext) {
            frame.showHint("Please take next swap!");
            return;
        }
        if (model.validSwap(selectedPoint, selectedPoint2)) {
            view.rePaintAllChess(model);
            frame.playClearAudio();
            haveNext = true;
        } else {
            frame.showHint("Error:Can't swap!");
        }
        if (selectedPoint != null) {
            model.getGridAt(selectedPoint).setSelected(false);
            selectedPoint = null;
        }
        if (selectedPoint2 != null) {
            model.getGridAt(selectedPoint2).setSelected(false);
            selectedPoint2 = null;
        }
    }

    public void onPlayerNextSwap() {
        if (!haveNext) {
            frame.showHint("Error:No Next Swap!");
            return;
        }
        model.goNextStep();
        view.rePaintAllChess(model);
        if (model.stage == 0)
            haveNext = false;
        model.checkStatus();
    }

    public void onPlayerClickChessPiece(ChessboardPoint point) {
        if (haveNext) {
            frame.showHint("Please take next swap!");
            return;
        }
        if (model.getColor(point.getRow(), point.getCol()) == 5) {
            frame.showHint("Can not swap obstacles");
            return;
        }
        if (selectedPoint2 != null) {
            var distance2point1 = Math.abs(selectedPoint.getCol() - point.getCol())
                    + Math.abs(selectedPoint.getRow() - point.getRow());
            var distance2point2 = Math.abs(selectedPoint2.getCol() - point.getCol())
                    + Math.abs(selectedPoint2.getRow() - point.getRow());

            if (distance2point1 == 0) {
                model.getGridAt(selectedPoint).setSelected(false);
                selectedPoint = selectedPoint2;
                selectedPoint2 = null;
            } else if (distance2point2 == 0) {
                model.getGridAt(selectedPoint2).setSelected(false);
                selectedPoint2 = null;
            } else if (distance2point1 == 1) {
                model.getGridAt(selectedPoint2).setSelected(false);
                selectedPoint2 = point;
                model.getGridAt(selectedPoint2).setSelected(true);
            } else {
                model.getGridAt(selectedPoint).setSelected(false);
                selectedPoint = selectedPoint2;
                selectedPoint2 = point;
                model.getGridAt(selectedPoint2).setSelected(true);
            }
        } else {
            if (selectedPoint == null) {
                selectedPoint = point;
                model.getGridAt(selectedPoint).setSelected(true);
            } else {
                var distance2point1 = Math.abs(selectedPoint.getCol() - point.getCol())
                        + Math.abs(selectedPoint.getRow() - point.getRow());
                if (distance2point1 == 0) {
                    model.getGridAt(selectedPoint).setSelected(false);
                    selectedPoint = null;
                } else {
                    if (distance2point1 == 1) {
                        selectedPoint2 = point;
                        model.getGridAt(selectedPoint2).setSelected(true);
                    } else {
                        if (selectedPoint != null)
                            model.getGridAt(selectedPoint).setSelected(false);
                        if (selectedPoint2 != null)
                            model.getGridAt(selectedPoint2).setSelected(false);
                        selectedPoint = point;
                        model.getGridAt(selectedPoint).setSelected(true);
                    }
                }
            }
        }
        view.rePaintAllChess(model);
    }

    public void onPlayerHint() {
        if (haveNext) {
            frame.showHint("Please take next swap!");
            return;
        }
        model.generateHint();
        view.rePaintAllChess(model);
    }

    public void onPlayerShuffle() {
        if (haveNext) {
            frame.showHint("Please take next swap!");
            return;
        }
        frame.clearMustShuffle();
        model.shuffle();
        view.rePaintAllChess(model);
    }

    public void onPlayerWithdraw() {
        if (haveNext) {
            frame.showHint("Please take next swap!");
            return;
        }
        if (model.stepUse == 0) {
            frame.showHint("No previous steps so can not Withdraw!");
            return;
        }
        model.withdraw();
    }
}
