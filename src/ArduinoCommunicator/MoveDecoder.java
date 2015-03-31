/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArduinoCommunicator;
/**
 *
 * @author charlesmariller
 */
public class MoveDecoder {
    
    
    public MoveDecoder()
    {
        
    }
    
    public static String decode(String arduinoCode) throws UnrecognizedMove
    {
        String[] mySplit = arduinoCode.split(" ");
        String coup, get;
        
        // roque
        if ((mySplit.length == 4)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'd')&&(mySplit[2].charAt(0) == 'u')&&(mySplit[3].charAt(0) == 'd')))
        {
            // Si 3 cases d'écart --> grand roque
            System.out.println("recu: " + mySplit[0]);
            if (((((mySplit[0].substring(1,3)).equals("a8"))&&((mySplit[1].substring(1,3)).equals("d8")))||(((mySplit[0].substring(1,3)).equals("a1"))&&((mySplit[1].substring(1,3)).equals("d1"))))
                    || ((((mySplit[2].substring(1,3)).equals("a8"))&&((mySplit[3].substring(1,3)).equals("d8")))||(((mySplit[2].substring(1,3)).equals("a1"))&&((mySplit[3].substring(1,3)).equals("d1")))))
                coup = "O-O-O";
            else 
                coup = "O-O";
        }
        
        //En passant
        else if ((mySplit.length == 3)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'd')&&(mySplit[2].charAt(0) == 'u')))
        {
            coup = mySplit[0].substring(1, 3) + mySplit[1].substring(1, 3);
        }
        
        //Deplacement normal
         else if ((mySplit.length == 2)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'd')))
        {
            coup = mySplit[0].substring(1, 3) + mySplit[1].substring(1, 3);
        }
        
        //Capture
        else if ((mySplit.length == 3)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'u')&&(mySplit[2].charAt(0) == 'd')))
        {
            coup = mySplit[0].substring(1, 3) + mySplit[2].substring(1, 3);
        }
        
        
        
        
        
        else  throw new  UnrecognizedMove("Coup impossible");
        System.out.println("Move translated: "+ coup);
        return coup;
    }
    
    public static class UnrecognizedMove extends Exception{
        
        public UnrecognizedMove(String message)
        {
            super(message);
        }
    }
}