package net.launcher.run;
import java.io.File;
import java.util.ArrayList;

import net.launcher.utils.BaseUtils;

public class Starter
{
	public static void main(String[] args) throws Exception
	{	
		try {

			String jarpath = Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			int memory = BaseUtils.getPropertyInt("memory", 512);
			
			ArrayList<String> params = new ArrayList<String>();
           
			params.add("java");
			params.add("-Xmx"+memory+"m");
			params.add("-Xms"+memory+"m");
			params.add("-XX:MaxPermSize=128m");
			params.add("-Dfile.encoding=UTF-8");
			if(System.getProperty("os.name").toLowerCase().startsWith("mac"))
			{
				params.add("-Xdock:name=Minecraft");
				params.add("-Xdock:icon="+BaseUtils.getAssetsDir().toString()+"/favicon.png");
			}
			params.add("-classpath");
			params.add(jarpath);
			params.add(Mainclass.class.getCanonicalName());

			ProcessBuilder pb = new ProcessBuilder(params);
			pb.directory(new File(BaseUtils.getAssetsDir().toString()));
			Process process = pb.start();
			if (process == null) throw new Exception("Launcher can't be started!");
			System.exit(0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
