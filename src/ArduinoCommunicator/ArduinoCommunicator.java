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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArduinoCommunicator {

	private static Process pythonProcessReader;
	private static Process pythonProcessWriter;
	private static BufferedReader inp;
	private static BufferedWriter outp;
	private static final String cmdWriter = "/home/pi/Desktop/wechess/JavaCore/python/writer.py";
	private static final String cmdReader = "/home/pi/Desktop/wechess/JavaCore/python/reader.py";
	private static String readLine;
	private static String linetoWrite = null;
	private static ProcessBuilder pyPRBuilder;
	private static ProcessBuilder pyPWBuilder;

	public ArduinoCommunicator() throws IOException {
		ArduinoCommunicator.pyPRBuilder = new ProcessBuilder(cmdReader);
		ArduinoCommunicator.pyPWBuilder = new ProcessBuilder(cmdWriter);
		ArduinoCommunicator.readLine = null;
		ArduinoCommunicator.linetoWrite = null;
		ArduinoCommunicator.runPythonReader();
		//ArduinoCommunicator.runPythonWriter();
	}

	public static void runPythonWriter() throws IOException {
		ArduinoCommunicator.pythonProcessWriter = ArduinoCommunicator.pyPWBuilder.start();
		ArduinoCommunicator.outp = new BufferedWriter(new OutputStreamWriter(ArduinoCommunicator.pythonProcessWriter.getOutputStream()));
		Thread t = new Thread(new Runnable() {
			public void run() {
				//while (true) {
					try {
							System.out.print("pythonWriter sends :"+ArduinoCommunicator.linetoWrite);
							ArduinoCommunicator.outp.write(ArduinoCommunicator.linetoWrite);
                                                        ArduinoCommunicator.outp.newLine();
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
						//ArduinoCommunicator.linetoWrite=null;
					

				}
			//}
		});
		t.start();
	}

	public static void runPythonReader() throws IOException {
		ArduinoCommunicator.pythonProcessReader = ArduinoCommunicator.pyPRBuilder.start();
		ArduinoCommunicator.inp = new BufferedReader(new InputStreamReader(ArduinoCommunicator.pythonProcessReader.getInputStream()));
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true){
                                                ArduinoCommunicator.readLine = ArduinoCommunicator.inp.readLine();
						System.out.println("pythonReader received :"+ArduinoCommunicator.readLine);
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
		ArduinoCommunicator.linetoWrite = m;
            try {
                ArduinoCommunicator.runPythonWriter();
            } catch (IOException ex) {
                Logger.getLogger(ArduinoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public static String read() {
		String tmp = ArduinoCommunicator.readLine;
		ArduinoCommunicator.readLine = null;
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
