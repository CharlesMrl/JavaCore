/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacore;

import ArduinoCommunicator.*;
import DataModel.*;
import MoveController.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import wechesspresso.*;

/**
 *
 * @author Bastien
 */
public class JavaCore {
    
    public ArduinoCommunicator arduino;
    public Connection con;
    public Game current_game;
    public Player user;
    public final Scanner input = new Scanner(System.in);
    String arduino_output;

    //Affiche un echiquier en texte a partir d'une FEN
    static void printFEN(String fen) {
        String[] alines = fen.split("/");
        ArrayList<String> lines = new ArrayList();
        
        for (int i = 0; i < alines.length; i++) {
            lines.add(alines[i]);
        }
        String space = new String("");
        int lig = 8;
        System.out.println(" |abcdefgh|");
        System.out.println("------------");
        for (String s : lines) {
            space = "";
            System.out.print(lig + "|");
            for (int c = 1; c <= 8; c++) {
                space = space.concat(".");
                s = s.replace(String.valueOf(c), space);
            }
            
            s = s.substring(0, 8);
            
            System.out.print(s);
            System.out.println("|" + lig--);
        }
        
        System.out.println("------------");
        System.out.println(" |abcdefgh|");
    }
    
    public JavaCore(int user_id, int game_id) throws SQLException, ClassNotFoundException {
        //login db ECE
        //this.con = ConnectionManager.getConnection();

        //login db 192.168.80.17
        this.con = ConnectionManager.getConnectionLocal();
        
        this.user = null;
        this.current_game = null;
        user = Player.findById(String.valueOf(user_id));
        //selection partie
        current_game = Game.findById(game_id);
        current_game.print();
        
    }
    
    public void setCurrentGame(int game_id) {
        try {
            current_game = Game.findById(game_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setUser(int user_id) {
        try {
            user = Player.findById(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loop() throws Exception {
        String mon_coup = null;
        String newFen = null;
        boolean invalid;
        /*
        try {
            this.arduino = new ArduinoCommunicator();
        } catch (IOException ex) {
            System.out.println("Echec Initialisation Arduino! Branche le?");
            System.exit(-1);
        }*/
        //Si mon tour de jouer
        if (current_game.myTurn(user.get("id"))) {
            //if (true) {
            printFEN(current_game.get("fen"));
            //ArduinoCommunicator.init();
            // Attendre la detection d'un coup
            if (current_game.get("uidw").equals(user.get("id"))) {
                System.out.println("Your turn, you play WHITE : ");
                //ArduinoCommunicator.listen("white");
            } else {
                System.out.println("Your turn, you play BLACK : ");
                //ArduinoCommunicator.listen("black");
            }
            do {
                invalid = true;

                arduino_output = input.nextLine();
                //System.out.println("Reading ...");
                //arduino_output = ArduinoCommunicator.read();
                
                if (arduino_output != null) {
                    System.out.println("Arduino sent: '" + arduino_output + "'");
                    try {
                        newFen = null;
                        mon_coup = new MoveDecoder().decode(arduino_output);
                        //Verifier que le coup est valide
                        newFen = WeChesspresso.getNewFen(current_game.get("fen"), mon_coup);
                        invalid = false;
                    } catch (Exception ex) {
                        //ArduinoCommunicator.invalid();
                        System.out.println("Invalid move !");
                        WeChesspresso.printAllMovesVerbose(current_game.get("fen"));
                        //Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            } while (invalid );//&& current_game.myTurn(user.get("id")));
            
            if (newFen != null) {
                //ArduinoCommunicator.valid();
                printFEN(newFen);
                //Envoi des infos du coup
                Move m = Move.make(current_game.get("id"),
                        user.get("id"),
                        newFen,
                        mon_coup.substring(0, 2),
                        mon_coup.substring(2, 4),
                        WeChesspresso.getMoveType(current_game.get("fen"), mon_coup),
                        Move.getTimeStamp());
                m.print();
                //affecter le move a la partie en cours
                current_game.assignNewMove(m);
                // LOUIS : Verifier si echec et mat
            } else {
                current_game.sync();
                printFEN(current_game.get("fen"));
            }
            if (current_game.isCheck()) {
                System.out.println("CHECK!");
            }
            if (current_game.isCheckmate()) {
                //Envoi info victoire
                System.out.println("You won!");
                current_game.set("winner", user.get("id"));
            }
        } //Si tour de l'adversaire
        else {
            //Attendre un nouveau coup
            System.out.print("Waiting for the opponent move...");
            while (!current_game.myTurn(user.get("id"))) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Effectuer deplacements du coup
            Move m = Move.getLastFromGame(current_game);
            System.out.print(" Move received : ");
            m.print();

            /* Code de déplacement des moteurs
             * pour effectuer le move recu
             * de l'adversaire.
             *
             * // Decomposition du DataModel.Move en MoveController.Path
             ArrayList<Path> path_list = PathGenerator.getPathList(m, current_game.get("fen"));
             MotorController.run(path_list);
             */
            ArrayList<Path> path_list = PathGenerator.getPathList(m, current_game.getPreviousFen());
            MotorController.run(path_list);
            //Si echec
            if (current_game.isCheck()) {
                System.out.println("CHECK! ... *stress*");
            }
            //Si coup gagnant
            if (current_game.isCheckmate()) {
                //Informer joueur de la defaite
                System.out.println("You lost!...loser");
            }
        }
    }
    
}
