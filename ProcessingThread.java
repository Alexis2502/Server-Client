package com.example.serverclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.Map;

public class ProcessingThread extends Thread{

    @FXML
    Label textserver;
    Map<Integer, String> quest;
    int questnum;

    public static boolean gameover=false;


    ProcessingThread(Map<Integer,String> quest, int questnum,Label textserver){
        this.quest=quest;
        this.questnum=questnum;
        this.textserver=textserver;
    }

    public void run(){//uruchomienie wątku przetwarzającego odpowiedzi
        while(questnum<= quest.size()) {
            String ans="";
            String name="";
            if (!ListeningThread.answerqueue.isEmpty()) {
                char[] answer;
                try {
                    answer = ListeningThread.answerqueue.take().toCharArray();//pobranie odpowiedzi z kolejki blokującej
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (answer.length > 0) {
                    int i = 0;
                    while (!String.valueOf(answer[i]).equals("|")) {//pobranie imienia
                        name = name.concat(String.valueOf(answer[i]));
                        i++;
                    }
                    for (int j = i + 1; j < answer.length; j++) {//pobranie odpowiedzi
                        ans = ans.concat(String.valueOf(answer[j]));
                    }
                    if (ans.equals(quest.get(questnum + 1))) {//sprawdzenie czy dobra odpowiedź i wypisanie komunikatu
                        String text = textserver.getText();
                        String won = "\nKlient ".concat(name).concat(" podał prawidłową odpowiedź: ").concat(ans).concat("\n");
                        Platform.runLater(() -> textserver.setText(text.concat(won)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        questnum += 2;
                        Platform.runLater(() -> textserver.setText(textserver.getText().concat(quest.get(questnum))));
                    } else {
                        String text = textserver.getText();
                        Platform.runLater(() -> textserver.setText(text.concat("\nNieprawidłowa odpowiedź\n")));
                    }
                    if (questnum > quest.size()) {//jeśli koniec gry
                        gameover = true;
                        Platform.runLater(() -> textserver.setText(textserver.getText().concat("\nGAME OVER!")));
                    }
                }
            }
        }
    }
}