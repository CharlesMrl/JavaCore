/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArduinoCommunicator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ArduinoCommunicator {

    private static Process pythonProcessReader;
    private static Process pythonProcessWriter;
    private static BufferedReader inp;
    private static BufferedWriter outp;
    private static final String cmdWriter = "/home/pi/Desktop/wechess/JavaCore/python/writer.py";
    private static final String cmdReader = "/home/pi/Desktop/wechess/JavaCore/python/reader.py";
    private static String readLine;
    private static String linetoWrite;
    private static ProcessBuilder pyPRBuilder;
    private static ProcessBuilder pyPWBuilder;

    public ArduinoCommunicator() throws IOException {
        pyPRBuilder = new ProcessBuilder(cmdReader);
        pyPWBuilder = new ProcessBuilder(cmdWriter);
        readLine = null;
        linetoWrite = null;
        this.runPythonReader();
        this.runPythonWriter();
    }

    public static void runPythonWriter() throws IOException {
        pythonProcessWriter = pyPWBuilder.start();
        outp = new BufferedWriter(new OutputStreamWriter(pythonProcessWriter.getOutputStream()));
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (linetoWrite!=null) {
                        try {
                            System.out.print("pythonWriter sends :"+linetoWrite);
                            ArduinoCommunicator.outp.write(linetoWrite+"\n");
                            ArduinoCommunicator.outp.flush();
                            System.out.println(" -> OK");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        linetoWrite=null;
                    }
                    
                }
            }
        });
        t.start();
    }

    public void runPythonReader() throws IOException {
        pythonProcessReader = pyPRBuilder.start();
        inp = new BufferedReader(new InputStreamReader(pythonProcessReader.getInputStream()));
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while ((readLine = ArduinoCommunicator.inp.readLine()) != null) {
                        System.out.println("pythonReader received :"+readLine);
						//System.out.println(readLine);
                        // CALL FUNCTION THAT NEDD TO PROCESS READ DATA HERE INSTEAD
                        //SerialPython.setReadLine("nope");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Reader is dead");
            }
        });
        t.start();
    }

    public static void send(String m) {
        linetoWrite = m;
    }

    public static String read() {
        String tmp = readLine;
        readLine = null;
        return readLine;
    }

    public static void valid() {
        send("valid");
    }

    public static void invalid() {
        send("invalid");
    }

    public static void sleep() {
        send("sleep");
    }

    public static void listen(String player) {
        switch (player) {
            case "white":
                send("listen 1");
                break;
            case "black":
                send("listen 2");
                break;
        }
    }

    public static void init() {
        send("init");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        SerialPython sp = new SerialPython();
        while(true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                
                }
            
            sp.listen("white");
            String out;
            do{
                out = sp.read();
            }while(out==null);
            
            
            System.out.println("Received: "+out);
            sp.valid();
        }
    }

}
