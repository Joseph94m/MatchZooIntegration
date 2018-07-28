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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
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
    private FileWriter fw;
    private BufferedWriter bw;
    private File file;
    private int q = 1;

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
        fw = new FileWriter(pathToTopics, true);
        bw = new BufferedWriter(fw);
    }

    public AOLToTopics(String pathToAOLS, String pathToTopics) throws IOException {
        this.pathToAOLS = pathToAOLS;
        this.pathToTopics = pathToTopics;
        PrintWriter writer = new PrintWriter(pathToTopics);
        writer.print("");
        writer.close();
        fw = new FileWriter(pathToTopics, true);
        bw = new BufferedWriter(fw);
        file = new File(pathToAOLS);
    }

    public void writeTransform() throws FileNotFoundException, IOException {
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat('\t'));
        StringBuilder sb;
        List<CSVRecord> records = parser.getRecords();

        for (int j = 1; j < records.size(); ++j) {
            sb = new StringBuilder();
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
            bw.write(sb.toString());
            bw.flush();
        }

    }


}
