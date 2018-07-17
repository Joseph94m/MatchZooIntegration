/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private final String pathToAOLS;
    private final String pathToTopics;

    public AOLToTopics(String pathToAOLS, String pathToTopics) {
        this.pathToAOLS = pathToAOLS;
        this.pathToTopics = pathToTopics;
    }

    public String transform() throws FileNotFoundException, IOException {
        File file = new File(pathToAOLS);
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat('\t'));
        StringBuilder sb = new StringBuilder();
        List<CSVRecord> records = parser.getRecords();
        for (int j = 1; j < records.size(); ++j) {
            sb.append("<top>");
            sb.append('\n');
            sb.append('\n');
            sb.append("<num> Number: ");
            sb.append(j);
          //  sb.append("000");
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

        }
        return sb.toString();
    }

    public void writeTransform() throws FileNotFoundException, IOException {
        PrintWriter pw = new PrintWriter(new File(pathToTopics));
        pw.write(transform());

    }

    public static void main(String[] args) throws IOException {
        AOLToTopics aol = new AOLToTopics("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\2000-aol.txt", "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\aol-qrels.txt");
        aol.writeTransform();
    }

}
