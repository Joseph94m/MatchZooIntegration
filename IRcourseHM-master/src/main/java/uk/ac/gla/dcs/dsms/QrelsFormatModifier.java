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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.MetaIndex;

/**
 *
 * @author Joseph Moukarzel
 */
public class QrelsFormatModifier {

    private Index index;

    /*
    outputRelation should be in var\results\relation.txt
     */
    public QrelsFormatModifier(String pathToIndex) {
        index = Index.createIndex(pathToIndex, "data");
    }

    public QrelsFormatModifier(Index i) {
        index = i;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    private void emptyFile(String outputFile) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(outputFile);
        writer.print("");
        writer.close();
    }


    /*
    pathToQrels specifies the path to a file with the following format:
    301 0 FBIS3-10082 1
    outputRelation specifies the path to the outputfile where the results should be written, the results have the following format:
    Q301 0 D275787 1
    
     */
    public void writeToTerrierAndMZFormat(String pathToQrels, String outputRelation, boolean append) throws IOException {
        if (!append) {
            emptyFile(outputRelation);
        }
        MetaIndex mi = index.getMetaIndex();
        File file = new File(pathToQrels);
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        StringBuilder sb = new StringBuilder();
        DocumentIndex di = index.getDocumentIndex();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputRelation, true));
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> docnos = new HashMap<>();
        for (int i = 0; i < di.getNumberOfDocuments(); ++i) {
            docnos.put(mi.getItem("docno", i), i);
        }
        for (int j = 0; j < records.size(); ++j) {
            int doc = docnos.get(records.get(j).get(2));
            sb.append('Q');
            sb.append(records.get(j).get(0));
            sb.append(' ');
            sb.append("0");
            sb.append(' ');
            sb.append("D");
            sb.append(doc);
            sb.append(' ');
            sb.append(records.get(j).get(3));
            sb.append('\n');
            if (j % 10000 == 0) {
                bw.append(sb.toString());
                bw.flush();
                sb = new StringBuilder();
            }
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    /*
    public void writeQrelsToMZFormat(String pathToQrels, String outputRelation) throws FileNotFoundException, IOException {
        MetaIndex mi = index.getMetaIndex();
        File file = new File(pathToQrels);
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        StringBuilder sb = new StringBuilder();
        DocumentIndex di = index.getDocumentIndex();
        PrintWriter pw = new PrintWriter(new File(outputRelation));
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> docnos = new HashMap<>();
        for (int i = 0; i < di.getNumberOfDocuments(); ++i) {
            docnos.put(mi.getItem("docno", i), i);
        }
        for (int j = 0; j < records.size(); ++j) {
            int doc = docnos.get(records.get(j).get(2));
            sb.append(records.get(j).get(3));
            sb.append(' ');
            sb.append("Q");
            sb.append(records.get(j).get(0));
            sb.append(' ');
            sb.append("D");
            sb.append(doc);
            sb.append('\n');
        }
        pw.write(sb.toString());
        pw.flush();
        pw.close();

    }
     */
}
