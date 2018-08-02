/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Joseph
 */
public class AOLToTopics {

    private String pathToAOLS;
    private String pathToTopics;
    private FileOutputStream fw;
    private BufferedWriter bw;
    private File file;
    private int q = 1;
    private OutputStreamWriter osw;
    private GZIPOutputStream gz;

    public String getPathToAOLS() {
        return pathToAOLS;
    }

    public void setPathToAOLS(String pathToAOLS) {
        this.pathToAOLS = pathToAOLS;
        file = new File(pathToAOLS);
    }

    public String getPathToTopics() {
        return pathToTopics;
    }

    public void setPathToTopics(String pathToTopics) throws IOException {
        this.pathToTopics = pathToTopics;
    }

    public AOLToTopics(String pathToAOLS, String pathToTopics) throws IOException {
        this.pathToAOLS = pathToAOLS;
        this.pathToTopics = pathToTopics;
        PrintWriter writer = new PrintWriter(pathToTopics);
        writer.print("");
        writer.close();
        file = new File(pathToAOLS);
    }

    public void writeTransform() throws FileNotFoundException, IOException {
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat('\t'));
        StringBuilder sb = new StringBuilder();
        List<CSVRecord> records = parser.getRecords();
        fw = new FileOutputStream(pathToTopics,true);
        gz = new GZIPOutputStream(fw);
        osw = new OutputStreamWriter(gz, "UTF-8");
        bw = new BufferedWriter(osw);
        for (int j = 1; j < records.size(); ++j) {
            sb.append("<top>");
            sb.append('\n');
            sb.append('\n');
            sb.append("<num> Number: ");
            sb.append(q);
            sb.append('\n');
            sb.append("<title> ");
            sb.append(records.get(j).get(1));
            sb.append('\n');
            sb.append('\n');
            sb.append("<desc> Description:");
            sb.append('\n');
            for (int i = 2; i < records.get(j).size(); ++i) {
                sb.append(records.get(j).get(i));
                sb.append(' ');
            }
            sb.append('\n');
            sb.append('\n');
            sb.append("<narr> Narrative:");
            sb.append('\n');
            sb.append("NADA");
            sb.append('\n');
            sb.append('\n');
            sb.append("</top>");
            sb.append('\n');
            sb.append('\n');
            sb.append('\n');
            ++q;
            if (j % 10000 == 0) {
                bw.write(sb.toString());
                bw.flush();
                sb = new StringBuilder();
            }
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        osw.close();
        gz.close();
        fw.close();
    }

}
