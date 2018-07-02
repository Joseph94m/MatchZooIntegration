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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
/**
 *
 * @author Joseph
 */
public class QrelsToDocumentRepresentor {

    private String pathToMZFormattedQrels;
    private boolean[] iterated;

    public QrelsToDocumentRepresentor(String pathToMZFormattedQrels, int size) {
        this.pathToMZFormattedQrels = pathToMZFormattedQrels;
        this.iterated = new boolean[size];
    }

    public int[] getQrelsDocs() throws FileNotFoundException, IOException {
        List<Integer> list = new ArrayList<>();
        File file = new File(pathToMZFormattedQrels);
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        List<CSVRecord> records = parser.getRecords();
        int docid;
        String docid_;
        for (CSVRecord r : records) {
            docid_ = r.get(2);
            docid = Integer.parseInt(docid_.substring(1));
            if (iterated[docid] == true) {
                continue;
            }
            iterated[docid] = true;
            list.add(docid);

        }
        int[] docids = list.stream().mapToInt(i -> i).toArray();
        return docids;
    }

    public static void main(String[] args) throws IOException {
QrelsToDocumentRepresentor qr = new QrelsToDocumentRepresentor("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\results\\relation.txt",1);
        System.out.println("Did it");
    }

}
