import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

public class ReadFile {

    ParseUnit Parse = new ParseUnit();

    public ReadFile(String path) {
        List<File> allFiles = null;
        int counter =0;
        try {
            // Read all files from path
            allFiles = Files.walk(Paths.get(path)).
                    filter(Files::isRegularFile).
                    map(Path::toFile).
                    collect(Collectors.toList());

            for (File file : allFiles) {
                //System.out.println(file.getName());
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Document doc = Jsoup.parse(new String(Files.readAllBytes(file.toPath())));
                    Elements elements = doc.getElementsByTag("DOC");

                    // For every doc in the file
                    // Cut all the string from <TEXT> until </TEXT>
                    // Send it to ParseUnit
                    for (Element element : elements) {
                        if(true) {
                            String docText = element.getElementsByTag("TEXT").text();
                            String docName = element.getElementsByTag("DOCNO").text();
                            String[] withoutSpaceText = docText.split(" "); // split the text by " "(space) into array
                            //System.out.println("~~~~~" + docName + "~~~~~~");
                            Parse.parse(withoutSpaceText, docName);

                        }
                        else{ // for debug
                            int doNothing;
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                counter++;
                Parse.post.createPostingFileFirstTime(Parse.allWordsDic);
                Parse.allWordsDic.clear();
                if(counter==16)
                    break;



            }
        } catch (IOException e) { }
    }



    public static void main(String[]args) {
        long start = System.nanoTime();
        //ReadFile rf = new ReadFile("C:\\Users\\USER\\Desktop\\search2018\\corpus\\FB496139");

        ReadFile rf = new ReadFile("D:\\documents\\users\\dorlev\\Downloads\\corpus");
        //rf.p.printDic();
        long finish = System.nanoTime();
        System.out.println(finish-start);
        //convertTo();

    }

    public static void convertTo(){
        List<String> lines = new ArrayList<>();
        Stack<String> lineInStack = new Stack<>();
        File f = new File("C:\\Users\\glazersh\\IdeaProjects\\SearchEngineJ\\src\\main\\java\\postings\\post15");
        try (GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(f));
             BufferedReader br = new BufferedReader(new InputStreamReader(gzip))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            File f2 = new File("C:\\Users\\glazersh\\IdeaProjects\\SearchEngineJ\\src\\main\\java\\postings\\post16");
            FileOutputStream out = new FileOutputStream(f2);
            Writer writer = new OutputStreamWriter(out);
            writer.write(lines.toString());
            writer.close();
            out.close();


        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
