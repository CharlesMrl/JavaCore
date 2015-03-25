/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArduinoCommunicator;


import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bast
 */

public class ArduinoCommunicator implements SerialPortEventListener {
    SerialPort serialPort = null;
    boolean listening = false;
    boolean available = false;
    public String bufferedInput="";
    
    private static final String PORT_NAMES[] = { 
        "/dev/tty.usbmodem1411", // Mac OS X
    //        "/dev/usbdev", // Linux
    //        "/dev/tty", // Linux
    //        "/dev/serial", // Linux
            "COM3", // Windows
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port
    
    public ArduinoCommunicator() {
        appName = getClass().getName();
    }
    
    public void validate(){
        send("valid\n");
    }
    
    public void reject(){
        send("invalid\n");
    }
    
    public String read(){
        //System.out.println("Reading ...");
        while(bufferedInput.equals("")) try {
            Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ArduinoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        String tmp = bufferedInput;
        bufferedInput="";
        listening=false;
        available=false;
        return tmp;
    }
    
    public void sleep(){
        send("sleep\n");
    }
    
    public void listen(){
        listening=true;
        send("listen\n");
    }
    
    public void setBuffer(String b){
        this.bufferedInput = b;
    }
    
    public boolean initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate system ports and try connecting to Arduino over each
            System.out.println( "Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                //
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( "   port " + currPortId.getName() );
                for (String portName : PORT_NAMES) {
                    if ( currPortId.getName().equals(portName) 
                      || currPortId.getName().startsWith(portName)) {

                        // Try to connect to the Arduino on this port
                        //
                        // Open serial port
                        serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println( "Connected on port" + currPortId.getName() );
                        break;
                    }
                }
            }
        
            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return false;
            }
        
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            // Give the Arduino some time
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            
            return true;
        }
        catch ( Exception e ) { 
            e.printStackTrace();
        }
        return false;
    }
    
    private void send(String data) {
        try {
            System.out.print("Sent Arduino: " + data);
            
            // open the streams and send the "y" character
            output = serialPort.getOutputStream();
            output.write( data.getBytes() );
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }

    //
    // This should be called when you stop using the port
    //
    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    //
    // Handle serial port event
    //
    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
        
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE:
                    if ( input == null ) {
                        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                    }
                    
                    setBuffer(input.readLine());
                    //System.out.println("Arduino sent: '"+bufferedInput+"'");
                    
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public static void main(String[] args) {
        ArduinoCommunicator test = new ArduinoCommunicator();
        if ( test.initialize() ) {
            while(true){
                try { Thread.sleep(1000); } catch (InterruptedException ie) {}
            test.listen();
            String out = test.read();
            System.out.println("Received: "+out);
            test.validate();
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            }
        }

        // Wait 5 seconds then shutdown
        try { Thread.sleep(100000); } catch (InterruptedException ie) {}
    }
}
