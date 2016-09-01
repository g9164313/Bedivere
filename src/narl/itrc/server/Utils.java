package narl.itrc.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
	
	
	/**
	 * Execute script or command. Another override function
	 * @param txt - use '@' sign to seperate command and arguments 
	 * @return standard/screen output 
	 */
	public static String Exec(String txt){
		String[] col = txt.split("@");
		for(int i=0; i<col.length; i++){
			col[i] = col[i].trim();
		}		
		return Exec(Arrays.asList(col));
	}
	
	/**
	 * Execute script or command. Another override function
	 * @param cmd - command
	 * @param arg - arguments
	 * @return standard/screen output 
	 */
	public static String Exec(String cmd,String... arg){
		ArrayList<String> lst = new ArrayList<String>();
		lst.add(cmd);
		lst.addAll(Arrays.asList(arg));
		return Exec(lst);
	}
	
	/**
	 * Execute script or command
	 * @param cmd_arg - command and arguments
	 * @return standard/screen output
	 */
	public static String Exec(List<String> cmd_arg){		
		String stdout,stderr;
		try {
			ProcessBuilder pb = new ProcessBuilder(cmd_arg);
			pb.redirectOutput();
			pb.redirectError();
			Process p = pb.start();
			p.waitFor();
			
			byte[] buf = new byte[1024*10];
			
			p.getInputStream().read(buf);
			stdout = new String(buf);
			
			/*p.getErrorStream().read(buf);
			stderr = new String(buf);
			
			if(stderr.length()!=0){
				stdout = stdout + "\n[ERROR]:\n" + stderr;
			}*/			
			p.destroy();
		} catch (IOException e) {
			stdout = "[ERROR]: "+e.getMessage();
		} catch (InterruptedException e) {
			stdout = "[ERROR]: "+e.getMessage();
		}		
		if(stdout.length()==0){
			return null;
		}		
		return stdout;
	}
}
