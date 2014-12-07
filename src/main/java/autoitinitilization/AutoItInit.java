package autoitinitilization;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import autoitx4java.AutoItX;

import com.jacob.com.LibraryLoader;

public class AutoItInit{
	
	public static void registerdlls() throws IOException {
	
		File dllfiles = new File("lib").getAbsoluteFile();
		FileFilter fileFilter = new WildcardFileFilter("*.dll");
		File[] files = dllfiles.listFiles(fileFilter);
		for (File file : files) {
			Runtime.getRuntime().exec("regsvr32 /s" + file.getAbsoluteFile());
		}

		/*
		 * This also works for nested folders Collection<File> dlls =
		 * FileUtils.listFiles(.getAbsoluteFile(),TrueFileFilter.INSTANCE,
		 * DirectoryFileFilter.DIRECTORY); for (File file : dlls) {
		 * if(file.getName().contains(".dll"))
		 * Runtime.getRuntime().exec("regsvr32 /s"+file.getAbsoluteFile()); }
		 */

	}

	public static void loadjacobx86dll(String filename) {
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				new File(filename).getAbsolutePath());
	}

	public static void loadjacobx64dll(String filename) {
		System.setProperty(LibraryLoader.JACOB_DLL_PATH,
				new File(filename).getAbsolutePath());
	}

	public static void loadjacobx64dll(double version) {
		loadjacobfiles(new WildcardFileFilter("jacob*" + version + "*x64.dll"));
	}

	public static void loadjacobx86dll(double version) {
		loadjacobfiles(new WildcardFileFilter("jacob*" + version + "*x86.dll"));
	}

	public static void loadjacobx86dll() {
		loadjacobfiles(new WildcardFileFilter("jacob*x86.dll"));
	}

	public static void loadjacobx64dll() {
		loadjacobfiles(new WildcardFileFilter("jacob*x64.dll"));
	}

	private static void loadjacobfiles(FileFilter jacobfilter) {
		File dllfiles = new File("lib").getAbsoluteFile();
		File[] files = dllfiles.listFiles(jacobfilter);
		for (File file : files) {
			System.out.println("Loading.."+file.getName());
			System.setProperty(LibraryLoader.JACOB_DLL_PATH,
					file.getAbsolutePath());
		}
	}
}
