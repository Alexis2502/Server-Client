package com.example.serverclient;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ListeningThread extends Thread{
    public static BlockingQueue<String> answerqueue= new LinkedBlockingQueue<>(10);
    Map<Integer, String> quest;
    int questnum;
    @FXML
    Label textserver;

    ListeningThread(Map<Integer,String> quest, int questnum, Label textserver){
        this.quest=quest;
        this.questnum=questnum;
        this.textserver=textserver;
    }

    @Override
    public void run(){ //uruchomienie nasłuchiwania na porcie
        Socket socket;
        ProcessingThread pt = new ProcessingThread(quest, questnum, textserver);
        pt.start();
        try (ServerSocket serverSocket = new ServerSocket(1012)){
            while(!ProcessingThread.gameover){
                socket = serverSocket.accept();
                getAnswer(socket);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAnswer(Socket socket) throws IOException, InterruptedException {//zczytanie odpowiedzi z portu
        byte[] dataBytes = socket.getInputStream().readAllBytes();
        String data = new String(dataBytes, Charset.defaultCharset());
        answerqueue.add(data);
    }
}
