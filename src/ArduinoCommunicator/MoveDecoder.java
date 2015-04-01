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
    
    private class pieceEvent{
        
        String pos;
        boolean isUp;
        
        public pieceEvent(String in){
            if(in.charAt(0)!='u')isUp = false;
            else if(in.charAt(0)!='d'){
                isUp=true;
            }
            pos=in.substring(1, 3);
        }
    }
    
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
            System.out.println("roque !!");
            // Si 3 cases d'écart --> grand roque
            if (((((mySplit[0].substring(1,3)).equals("a8"))&&((mySplit[1].substring(1,3)).equals("d8")))||(((mySplit[0].substring(1,3)).equals("a1"))&&((mySplit[1].substring(1,3)).equals("d1"))))
                    || ((((mySplit[2].substring(1,3)).equals("a8"))&&((mySplit[3].substring(1,3)).equals("d8")))||(((mySplit[2].substring(1,3)).equals("a1"))&&((mySplit[3].substring(1,3)).equals("d1")))))
            {
                System.out.println("Grand roque!!");
                coup = "O-O-O";
            }
            else 
            {
                coup = "O-O";
                System.out.println("Petit roque !!");
            }
        }
        
        //En passant
        else if ((mySplit.length == 3)&&((mySplit[0].charAt(0) == 'u')&&(mySplit[1].charAt(0) == 'd')&&(mySplit[2].charAt(0) == 'u')))
        {
            System.out.println("en passant!!!");
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
