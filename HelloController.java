package com.example.serverclient;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    public Label textserver;

    @FXML
    public void initialize() throws Exception {
        Map<Integer, String> quest = new HashMap<>(); //mapa z pytaniami i odpowiedziami
        int questnum = 1;
        int i = 1;
        //zczytanie pytań i odpowiedzi
        File questions = new File("Pytania.txt");
        FileReader freader = new FileReader(questions);
        BufferedReader breader = new BufferedReader(freader);
        String line;
        while ((line = breader.readLine()) != null) {
            quest.put(i, line);
            line = breader.readLine();
            quest.put(i + 1, line);
            i += 2;
        }
        //wypisanie pierwszego pytania
        textserver.setText(quest.get(questnum));
        //uruchomienie wątku nasłuchującego odpowiedzi
        ListeningThread lt = new ListeningThread(quest, questnum, textserver);
        lt.start();
    }
}

