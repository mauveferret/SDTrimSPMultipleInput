package ru.mauveferret;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Input
{
    public static void main( String[] args ) throws URISyntaxException
    {

        String currentJar = Input.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String currentPath = new File(currentJar).getParent();

        System.out.println(currentPath);

        ArrayList<Tri> inputList= new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(lookForInputTask(new File(currentPath))));
            while (reader.ready()){
                String line = reader.readLine();
                String[] parametra = line.split(" ");
                inputList.add(new Tri(Integer.parseInt(parametra[0]), Double.parseDouble(parametra[1]),
                        Double.parseDouble(parametra[2]), Integer.parseInt(parametra[3])));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

		// folder with all input files for a calculation
		new File(currentPath + File.separator + args[0]).mkdirs();
		
		//create folders for SDTrimSP


        try {
            for (Tri tri : inputList) {
                new File(currentPath + File.separator + args[0]+ File.separator+tri.createDir()).mkdirs();
                File f = new File(currentPath + File.separator +args[0]+ File.separator+tri.createDir() + File.separator + "tri.inp");
                FileOutputStream writer = new FileOutputStream(f);
                writer.write(tri.createTri().getBytes());
                writer.close();
            }
        }
        catch (Exception e){System.out.println(e.getMessage());}
		
		String gccCommandFile="";
		for (Tri tri : inputList) {
		
			
		
		}


        System.out.println( "Hello World!" );
        //create class "calc" which containes variables for the tri.inp file
        //create there method which creates tri.inp file

    }

    private static String lookForInputTask(File rootDir) {
        List<String> result = new ArrayList<>();

        LinkedList<File> dirList = new LinkedList<>();
        if (rootDir.isDirectory()) {
            dirList.addLast(rootDir);
        }

        while (dirList.size() > 0) {
            File[] filesList = dirList.getFirst().listFiles();
            if (filesList != null) {
                for (File path : filesList) {
                    if (path.isDirectory()) {
                        //wouldn't watch inside folders
                        //dirList.addLast(path);
                    } else {
                        String simpleFileName = path.getName();

                        if (simpleFileName.endsWith(".inp1")) {
                            return path.getAbsolutePath().toString();
                        }
                    }
                }
            }
            dirList.removeFirst();
        }
        System.out.println("Holy shit! No config 'inp1' file was found.");
        return null;
    }

}
