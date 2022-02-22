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

        if (args[0]==null) {
            System.out.println("no task name. Enter it after jar name");
            System.exit(0);
        }
        String currentJar = Input.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String currentPath = new File(currentJar).getParent();

        //System.out.println(currentPath);

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
		
		//create multiple folders with input parametra for SDTrimSP calcs
        try {
            for (Tri tri : inputList) {
                new File(currentPath + File.separator + args[0]+ File.separator+tri.getDir()).mkdirs();
                File f = new File(currentPath + File.separator +args[0]+ File.separator+tri.getDir() + File.separator + "tri.inp");
                FileOutputStream writer = new FileOutputStream(f);
                writer.write(tri.createTri().getBytes());
                writer.close();
            }
        }
        catch (Exception e){System.out.println(e.getMessage());}

        // create folder with scrypts
        new File(currentPath + File.separator + args[0]+File.separator+File.separator+"launches"+
                File.separator+args[0]).mkdirs();
        String bashFile="#!/bin/bash\n" +
                "exec 3>&1 4>&2\n" +
                "trap 'exec 2>&4 1>&3' 0 1 2 3\n" +
                "exec 1> >( tee -ia log.out) 2>&1\n\n";
        boolean isFirstWritten=false;
		for (Tri tri : inputList) {
            bashFile+= (!isFirstWritten) ?
                    "cd ../../"+tri.getDir()+"\nmpirun -np 16  --oversubscribe ../../bin/ubuntu.PPROJ/SDTrimSP.exe\n" :
                    "cd ../"+tri.getDir()+"\nmpirun -np 16  --oversubscribe ../../bin/ubuntu.PPROJ/SDTrimSP.exe\n";
            isFirstWritten=true;
		}
        bashFile+="cd ..\njava -jar ISInCa.jar -c launches/"+args[0]+"/isinca-"+args[0]+".xml";
        try {
             File f = new File(currentPath + File.separator + args[0] + File.separator +
                    File.separator + "launches" + File.separator + args[0] + File.separator + "start.sh");
            FileOutputStream writer = new FileOutputStream(f);
            writer.write(bashFile.getBytes());
            writer.close();
        }
        catch (Exception e){System.out.println(e.getMessage());}

        //create file for ISINCA

        String isincaXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ISInCa>\n" +
                "\t<pref>\n" +
                "\t    <getTXT>false</getTXT>\n" +
                "        <getSummary>true</getSummary>\n" +
                "        <visualize>false</visualize>\n" +
                "        <combine>true</combine>\n" +
                "\t\t<dirSubname>angle</dirSubname>\n" +
                "\t\t<combSum>Fe,Cr,Ni,Ti</combSum>\n" +
                "    </pref>\n" +
                "    <calcs>\n" +
                "        <calc id=\"0\">\n";
        for (Tri tri: inputList){
            isincaXML+="\t\t\t<dir>"+tri.getDir()+"</dir>\n";
        }
        isincaXML+="            <N_E>\n" +
                "                <sort>B</sort>\n" +
                "                <phi>0</phi>\n" +
                "                <deltaPhi>3</deltaPhi>\n" +
                "                <deltaTheta>3</deltaTheta>\n" +
                "            </N_E>\n" +
                "            <N_Theta>\n" +
                "                <sort>S</sort>\n" +
                "                <phi>0</phi>\n" +
                "                <deltaPhi>3</deltaPhi>\n" +
                "                <deltaTheta>3</deltaTheta>\n" +
                "            </N_Theta>\n" +
                "            <polar_Map>\n" +
                "                <sort>S</sort>\n" +
                "                <delta>3</delta>\n" +
                "            </polar_Map>\n" +
                "        </calc>\n" +
                "    </calcs>\n" +
                "</ISInCa>";
        try {
            File f = new File(currentPath + File.separator + args[0] + File.separator +
                    File.separator + "launches" + File.separator + args[0] + File.separator + "isinca-"+args[0]+".xml");
            FileOutputStream writer = new FileOutputStream(f);
            writer.write(isincaXML.getBytes());
            writer.close();
        }
        catch (Exception e){System.out.println(e.getMessage());}
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
