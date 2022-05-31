package controller;

import model.ChessColor;
import view.Chessboard;
import model.ChessComponent;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class GameController {
    private Chessboard chessboard;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public List<String> loadGameFromFile(String path) {
        try {
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }//这个方法应该对写存读档有用

    public Chessboard getChessboard(){
        return chessboard;
    }

    public void saveDataToFile(String filePath) {
        //todo: write data into file
        String scan = "";
        for(int i=0;i<8;i++) {
            for (int j = 0; j < 8; j++) {
                if(getChessboard().getChessComponents()[i][j].getName() == "R"){
                    scan += "R";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "N"){
                    scan += "N";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "B"){
                    scan += "B";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "Q"){
                    scan += "Q";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "K") {
                    scan += "K";
                }
                else if ((getChessboard().getChessComponents()[i][j].getName() == "P")) {
                    scan += "P";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "_") {
                    scan += "_";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "r"){
                    scan += "r";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "n"){
                    scan += "n";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "b"){
                    scan += "b";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "q"){
                    scan += "q";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "k") {
                    scan += "k";
                }
                else if ((getChessboard().getChessComponents()[i][j].getName() == "p")) {
                    scan += "p";
                }
            }
            scan += "\n";
        }//把棋盘按八个一行扫进来！

        if(getChessboard().getCurrentColor() == ChessColor.BLACK){
            scan += "Current Player: " + ChessColor.BLACK.name()+"\n";
        }else if(getChessboard().getCurrentColor() == ChessColor.WHITE){
            scan += "Current Player: " + ChessColor.WHITE.name()+"\n";
        }



        txtExport output = new txtExport(filePath,scan,"load_chessboard\\");
    }//有点不妙，这个存档把它叉掉还是会自己存档！

    public void readFileData(String fileName) {
        //todo: read date from file
        List<String> fileData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);//读档时要写blahblah.txt

            if(fileName.lastIndexOf(".txt")!=(fileName.length()-4)){
                throw new IOException("出现文件格式错误");
            }//文件格式判断

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int rowNumber=0;
            List<String> stepList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if(rowNumber>8){
                    stepList.add(line);
                }
                fileData.add(line);
                rowNumber++;
            }//收集文件信息与步数信息

            for(int i=0;i<8;i++){
                if (fileData.get(i).length() !=8){
                    throw new IOException("出现棋盘错误");
                }
            }
            if(fileData.get(8).length()!=21&&(fileData.contains("Current Player: BLACK")||fileData.contains("Current Player: WHITE"))){
                throw new IOException("出现棋盘错误");
            }//棋盘错误判断

            for(int i=0;i<8;i++){
                for (int j=0;j<8;j++){
                    if(fileData.get(i).charAt(j) != 'R' && fileData.get(i).charAt(j) != 'N' && fileData.get(i).charAt(j) != 'B' && fileData.get(i).charAt(j) != 'Q' && fileData.get(i).charAt(j) != 'K' && fileData.get(i).charAt(j) != 'P' && fileData.get(i).charAt(j) != '_' && fileData.get(i).charAt(j) != 'r' && fileData.get(i).charAt(j) != 'n' && fileData.get(i).charAt(j) != 'b' && fileData.get(i).charAt(j) != 'q' && fileData.get(i).charAt(j) != 'k' && fileData.get(i).charAt(j) != 'p'){
                        throw new IOException("出现棋子错误");
                    }
                }
            } //棋子错误判定

            if(!fileData.contains("Current Player: BLACK") && !fileData.contains("Current Player: WHITE")){
                throw new IOException("缺少下一步行棋方");
            }
            //else if ((chessboard.getCurrentColor() == ChessColor.BLACK) && (fileData.contains("Current Player: WHITE"))) {
               // throw new IOException("出现行棋方错误 错误编码1032");
            //}
           // else if ((chessboard.getCurrentColor() == ChessColor.WHITE) && (fileData.contains("Current Player: BLACK"))) {
               // System.out.println(chessboard.getCurrentColor());
                //throw new IOException("出现行棋方错误 错误编码1033");
            //}
            // 行棋方错误判定

            //差了落子是否非法判定！

            fileData.forEach(System.out::println);
            for(int i=0;i<8;i++) {
                for (int j = 0; j < 8; j++) {
                    getChessboard().initiateEmptyChessboard();
                    getChessboard().getChessComponents()[i][j].repaint();
                }
            }
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(fileData.get(i).charAt(j) == 'R'){
                        getChessboard().initRookOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'N'){
                        getChessboard().initKnightOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'B'){
                        getChessboard().initBishopOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'Q'){
                        getChessboard().initQueenOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'K'){
                        getChessboard().initKingOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'P'){
                        getChessboard().initPawnOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'r'){
                        getChessboard().initRookOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'n'){
                        getChessboard().initKnightOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'b'){
                        getChessboard().initBishopOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'q'){
                        getChessboard().initQueenOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'k'){
                        getChessboard().initKingOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'p'){
                        getChessboard().initPawnOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == '_'){
                        getChessboard().initEmptyOnBoard(i, j, ChessColor.NONE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                }
            }

            if(fileData.get(8).equals("Current Player: WHITE")){
                getChessboard().setCurrentColor(ChessColor.WHITE);
            }else if(fileData.get(8).equals("Current Player: BLACK")){
                getChessboard().setCurrentColor(ChessColor.BLACK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getChessboard(),e,"WRONG!",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void helpUnDo(int counter) {
        saveDataToFile1(String.valueOf(counter));
    }

    public void undo(int counter) {
        readFileData1(String.valueOf(counter));
        ChessComponent.counter--;
    }//感觉这里有问题

    private void saveDataToFile1(String filePath) {
        String scan = "";
        for(int i=0;i<8;i++) {
            for (int j = 0; j < 8; j++) {
                if(getChessboard().getChessComponents()[i][j].getName() == "R"){
                    scan += "R";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "N"){
                    scan += "N";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "B"){
                    scan += "B";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "Q"){
                    scan += "Q";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "K") {
                    scan += "K";
                }
                else if ((getChessboard().getChessComponents()[i][j].getName() == "P")) {
                    scan += "P";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "_") {
                    scan += "_";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "r"){
                    scan += "r";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "n"){
                    scan += "n";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "b"){
                    scan += "b";
                }
                else if(getChessboard().getChessComponents()[i][j].getName() == "q"){
                    scan += "q";
                }
                else if (getChessboard().getChessComponents()[i][j].getName() == "k") {
                    scan += "k";
                }
                else if ((getChessboard().getChessComponents()[i][j].getName() == "p")) {
                    scan += "p";
                }
            }
            scan += "\n";
        }//把棋盘按八个一行扫进来！

        if(getChessboard().getCurrentColor() == ChessColor.BLACK){
            scan += "Current Player: " + ChessColor.BLACK.name()+"\n";
        }else if(getChessboard().getCurrentColor() == ChessColor.WHITE){
            scan += "Current Player: " + ChessColor.WHITE.name()+"\n";
        }

        txtExport output = new txtExport(filePath,scan,"Undo\\");
    }

    private void readFileData1(String fileName) {
        List<String> fileData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("Undo\\"+fileName + ".txt");

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int rowNumber=0;
            List<String> stepList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if(rowNumber>8){
                    stepList.add(line);
                }
                fileData.add(line);
                rowNumber++;
            }//收集文件信息与步数信息

            fileData.forEach(System.out::println);
            for(int i=0;i<8;i++) {
                for (int j = 0; j < 8; j++) {
                    getChessboard().initiateEmptyChessboard();
                    getChessboard().getChessComponents()[i][j].repaint();
                }
            }
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(fileData.get(i).charAt(j) == 'R'){
                        getChessboard().initRookOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'N'){
                        getChessboard().initKnightOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'B'){
                        getChessboard().initBishopOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'Q'){
                        getChessboard().initQueenOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'K'){
                        getChessboard().initKingOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'P'){
                        getChessboard().initPawnOnBoard(i, j, ChessColor.BLACK);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'r'){
                        getChessboard().initRookOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'n'){
                        getChessboard().initKnightOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'b'){
                        getChessboard().initBishopOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'q'){
                        getChessboard().initQueenOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'k'){
                        getChessboard().initKingOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == 'p'){
                        getChessboard().initPawnOnBoard(i, j, ChessColor.WHITE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                    if(fileData.get(i).charAt(j) == '_'){
                        getChessboard().initEmptyOnBoard(i, j, ChessColor.NONE);
                        getChessboard().getChessComponents()[i][j].repaint();
                    }
                }
            }

            if(fileData.get(8).equals("Current Player: WHITE")){
                getChessboard().setCurrentColor(ChessColor.WHITE);
            }else if(fileData.get(8).equals("Current Player: BLACK")){
                getChessboard().setCurrentColor(ChessColor.BLACK);
            }
        }catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getChessboard(),e,"WRONG!",JOptionPane.ERROR_MESSAGE);
        }
    }
}
