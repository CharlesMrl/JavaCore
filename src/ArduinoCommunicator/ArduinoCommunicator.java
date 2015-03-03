/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArduinoCommunicator;

/**
 *
 * @author Bast
 */
public class ArduinoCommunicator {
    
    public void sleep(){
        write("SLEEP");
    }
    
    public void write(String cmd){
        //Envoyer cmd sur port de communication vers l'arduino
    }
    
    public String read(){
        write("READ");
        String msg = new String();
        
        return msg;
    }
}
