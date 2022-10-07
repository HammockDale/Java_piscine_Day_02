import java.io.IOException;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program {


	 static long getSize(Path path) {
		long size = 0;
		try {
			if (Files.isDirectory(path) && Files.isReadable(path)) {
				List<Path> collect = Files.list(path).collect(Collectors.toList());
				for (Path p : collect) {
					size += getSize(p);
				}
			}
			return size + Files.size(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	static void ft_ls(URI uriPath) throws IOException {
		Files.list(Paths.get(uriPath)).forEach(file -> {
			File fil = new File(file.toString());
			System.out.println(file.getFileName() + "   " + (long)Math.ceil((getSize(Paths.get(uriPath)) / 1024)) + " KB");
		});
	}

	static void renameFile(String path, String pathFrom, String pathTo) throws IOException {
		String folderFrom = path + "/" + pathFrom;
		String folderTo = path + "/" + pathTo;
		Path newPathFrom = Paths.get(URI.create(folderFrom)).normalize();
		Path newPathTo = Paths.get(URI.create(folderTo)).normalize();
		File file1 = new File(newPathFrom.toString());
		File file2 = new File(newPathTo.toString());
		if (file2.isDirectory()) {
			if (file2.exists())
				file1.renameTo(new File(file2.getCanonicalPath() + "/" + file1.getName()));
			else
				System.err.println("No such directory");
		} else {
			boolean success = file1.renameTo(file2);
			if (!success)
				System.err.println("File was not successfully renamed");
		}
	}

	static String ft_cd(String path, String folder) {
		String newFolder = path + "/" + folder;
		Path newPath = Paths.get(URI.create(newFolder)).normalize();
		File file = new File(newPath.toString());
		if (file.isDirectory()) {
			System.out.println(newPath);
			return  newFolder;
		}
		System.err.println("Wrong path " + newPath + " Path is " + path);
		return path;
	}

	public static void main(String[] args) throws IOException{
		String path = "";
		if (args.length != 1) {
			System.err.println("Wrong argument");
			return;
		}

		if (args[0].startsWith("--current-folder=")) {
			path = "file:" + args[0].substring(17);
		} else {
			System.err.println("Wrong argument");
			return;
		}

		URI uriPath = URI.create(path).normalize();

		if (Paths.get((uriPath)).isAbsolute() == false) {
			System.err.println("Wrong argument");
			return;
		}

		if (Files.exists(Paths.get(uriPath)) == false || Files.isDirectory(Paths.get(uriPath)) == false) {
			System.err.println("Wrong argument");
			return;
		}

		Scanner sc = new Scanner(System.in);
		String str;
		while (sc.hasNext()) {
			str = sc.next();
			if (str.equals("exit")) {
				return;
			}
			else if (str.equals("ls")) {
				ft_ls(uriPath);
			}
			else if (str.equals("cd")) {
				str = sc.next();
				path = ft_cd(path, str);
				uriPath = URI.create(path).normalize();
			}
			else if (str.equals("mv")) {
				String pathFrom = sc.next();
				String pathTo = sc.next();
				renameFile(path, pathFrom, pathTo);
			}
			else {
				System.err.println("wrong argument");
			}

		}

	}

}
