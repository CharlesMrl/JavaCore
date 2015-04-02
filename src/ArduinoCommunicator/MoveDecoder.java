/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArduinoCommunicator;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author charlesmariller
 */
public class MoveDecoder {
    
    /**
     * class containing the event of a piece (position and boolean for the event up or down)
     */
    private class pieceEvent{
        String pEv;
        String pos;
        boolean isUp;
        /**
         * 
         * @param in 
         */
        public pieceEvent(String in){
            pEv = in;
            if(in.charAt(0)!='u')isUp = false;
            else if(in.charAt(0)!='d'){
                isUp=true;
            }
            pos=in.substring(1, 3);
        }
        
        @Override
        public String toString()
        {
            return "Pos : " + pos + " ,pos char at 1 : " + pos.charAt(1) + ", Is up : " + isUp ;
        }
        
    }
    
    /**
     * 
     * @param code 
     */
    public MoveDecoder()
    {
    } 
    /**
     * gets all the pieceEvent down
     * @param list 
     * @return 
     */
    private ArrayList<pieceEvent> getDown(ArrayList<pieceEvent> list)
    {
        ArrayList<pieceEvent> downs = new ArrayList();
        
        for (pieceEvent list1 : list) {
            if (!(list1.isUp)) {
                System.out.println("Is down");
                downs.add(list1);
            }
        }
        return downs;
         
    }
    
    /**
     * gets all the pieceEvent up
     * @param list
     * @return 
     */
    private ArrayList<pieceEvent> getUp(ArrayList<pieceEvent> list)
    {
        ArrayList<pieceEvent> ups = new ArrayList();
        
        for (pieceEvent list1 : list) {
            if (list1.isUp) {
                ups.add(list1);
            }
        }
        
        return ups;
         
    }
    
    /**
     * get a list containing the pieceEvent up and down when doing a En Passant
     * @param list
     * @return 
     */
    private ArrayList<pieceEvent> getEPList(ArrayList<pieceEvent> list)
    {
        ArrayList<pieceEvent> EPlist = new ArrayList();
        ArrayList<pieceEvent> downs = new ArrayList();
        downs = getDown(list);
        
        String myPos = downs.get(0).pos;
        for (pieceEvent piece : list)
        {
            Character tmp = (piece.pos.charAt(0));
            
            if (((tmp.compareTo(myPos.charAt(0)))!=0)&&(piece.isUp))
            {
                EPlist.add(piece);
            }
        }
        EPlist.add(downs.get(0));
        return EPlist;
    }
    
    
    /**
     * boolean to know if a pieceEvent is contained inside the eventList
     * @param list
     * @param checkedPiece
     * @return 
     */
    
    private boolean doesMatchPiece(ArrayList<pieceEvent> list, pieceEvent checkedPiece)
    {
        boolean matcher = false;
        
        for(pieceEvent pieceEv : list)
        {
            if (pieceEv.pEv.equals(checkedPiece.pEv))
                matcher = true;
        }
        
        return matcher;
    }
    
    /**
     * decode the arduino code into a move
     * @return
     * @throws ArduinoCommunicator.MoveDecoder.UnrecognizedMove 
     */
    public String decode(String arduinoCode) throws UnrecognizedMove
    {
        String[] mySplit = arduinoCode.split(" ");
        String coup;
        
        ArrayList<pieceEvent> eventList = new ArrayList();
        ArrayList<pieceEvent> tmp = new ArrayList();
        int nbUp = 0, nbDown=0, size = mySplit.length;
        String myPos;
        
        for (int i = 0; i < size; i++)
        {
            pieceEvent newEvent = new pieceEvent(mySplit[i]);
            eventList.add(newEvent);
            if (newEvent.isUp)
                nbUp++;
            else nbDown++;
        }
        if (!(eventList.get(0).isUp))
            throw new  UnrecognizedMove("Coup impossible");
        //  castle
        else if ((eventList.size() == 4) && (nbUp == 2) && (nbDown == 2))
        {
            // long castle
            if ((doesMatchPiece(eventList, new pieceEvent("ua8"))&&(doesMatchPiece(eventList, new pieceEvent("ue8"))))
                    || ((doesMatchPiece(eventList, new pieceEvent("ua1"))&&(doesMatchPiece(eventList, new pieceEvent("ue1"))))))
                coup = "O-O-O";
            
            /* short castle */
            else if ((doesMatchPiece(eventList, new pieceEvent("ue8"))&&(doesMatchPiece(eventList, new pieceEvent("uh8"))))
                    || ((doesMatchPiece(eventList, new pieceEvent("ue1"))&&(doesMatchPiece(eventList, new pieceEvent("uh1"))))))
                coup = "O-O";
            
            else  throw new  UnrecognizedMove("Coup impossible");
        }

        // Normal move
        else if ((eventList.size() == 2) && (nbUp == 1) && (nbDown == 1))
        {
            coup = eventList.get(0).pos + eventList.get(1).pos;
        }

        // Capture
        else if ((eventList.size() == 3) && (nbUp == 2) && (nbDown == 1))
        {
            // verifier si capture normale
            if (!((!(eventList.get(0).pos.equals(eventList.get(1).pos)))
                &&(!(eventList.get(1).pos.equals(eventList.get(2).pos)))
                        &&(!(eventList.get(0).pos.equals(eventList.get(2).pos)))))
            {
                /* trouver le coup à envoyer */
                tmp = getDown(eventList);
                myPos = tmp.get(0).pos;
                
                if ((eventList.get(0).isUp)&&(eventList.get(0).pos.equals(myPos)))
                {
                    coup = eventList.get(1).pos + myPos;
                }
                else coup = eventList.get(0).pos + myPos;
                tmp.clear();
            }
            
            /* En Passant */
            else 
            {
                 tmp = getEPList(eventList);
                 System.out.println("pos1 "+tmp.get(0).pos + ", pos2 " + tmp.get(1).pos);
                 coup = tmp.get(0).pos + tmp.get(1).pos;
                 tmp.clear();
            }
        }
        
        else  throw new  UnrecognizedMove("Coup impossible");
        
        System.out.println("Move translated: "+ coup);
        System.out.println("Move decoder translated: "+ coup);
        return coup;
    }
    
    public static class UnrecognizedMove extends IOException{
        
        public UnrecognizedMove(String message)
        {
            super(message);
        }
    }
}
