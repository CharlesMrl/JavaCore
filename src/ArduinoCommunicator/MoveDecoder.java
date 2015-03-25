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
        String coup;
        
        if ((mySplit.length == 2)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'd')))
        {
            coup = mySplit[0].substring(1, 3) + mySplit[1].substring(1, 3);
        }
        
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
