import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SerialPython {

	private Process pythonProcessReader;
	private Process pythonProcessWriter;
	private static BufferedReader inp;
	private static BufferedWriter outp;
	private static final String cmdWriter = "/home/louis/workspace/IPCPython/writer.py";
	private static final String cmdReader = "/home/louis/workspace/IPCPython/reader.py";
	private static String readLine;
	private static String linetoWrite;
	private static ProcessBuilder pyPRBuilder;
	private static ProcessBuilder pyPWBuilder;

	public SerialPython() throws IOException{
		pyPRBuilder = new ProcessBuilder(cmdReader);
		pyPRBuilder = new ProcessBuilder(cmdWriter);
		readLine = "nope";
		linetoWrite = "nope";
		this.runPythonReader();
		this.runPythonWriter();
	}

	public void runPythonWriter() throws IOException{
		this.pythonProcessWriter = this.pyPWBuilder.start();
		outp = new BufferedWriter( new OutputStreamWriter(pythonProcessWriter.getOutputStream()) );
		Thread t = new Thread(new Runnable() {
			public void run() {
				while(true){
					if(!SerialPython.getLinetoWrite().equals("nope")){
						try {
							SerialPython.outp.write(SerialPython.getLinetoWrite() + "$");
							SerialPython.outp.flush();

						} catch (IOException e1) { }
						try { Thread.sleep(500); } catch (InterruptedException e) { }
						SerialPython.setLinetoWrite("nope");
					}
				}
			}
		});  
		t.start();
	}

	public void runPythonReader() throws IOException{
		this.pythonProcessReader = this.pyPRBuilder.start();
		inp = new BufferedReader( new InputStreamReader(pythonProcessReader.getInputStream()) );
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while((readLine = SerialPython.inp.readLine()) != null){
						System.out.println(readLine);
						// CALL FUNCTION THAT NEDD TO PROCESS READ DATA HERE INSTEAD
						SerialPython.setReadLine("nope");
					}
				} catch (IOException e) { }
			}
		});  
		t.start();
	}

	public static String getLinetoWrite(){
		return linetoWrite;
	}

	public static void setLinetoWrite(String tmp){
		linetoWrite = tmp;
	}

	public static String getReadLine(){
		return readLine;
	}

	public static void setReadLine(String tmp){
		readLine = tmp;
	}



	public static void main(String[] args) throws IOException {
		//SerialPython sp = new SerialPython();
	}

}
