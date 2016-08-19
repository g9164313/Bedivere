package narl.itrc.server;

import java.io.IOException;

public class Utils {
	
	public static String Exec(String... command){
		String stdout;
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectOutput();
			pb.redirectError();
			Process p = pb.start();
			p.waitFor();
			byte[] buf = new byte[1024*10];
			p.getInputStream().read(buf);
			stdout = new String(buf);
			//try standard error stream~~~
			/*if(stdout.length()==0){				
				p.getErrorStream().read(buf);
				for(byte bb:buf){							
					if(bb==0){
						break;
					}
					stdout = stdout + (char)bb;
				}
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
