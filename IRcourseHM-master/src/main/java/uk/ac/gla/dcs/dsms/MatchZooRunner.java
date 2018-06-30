/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import org.terrier.indexing.tokenisation.EnglishTokeniser;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;

/**
 *
 * @author Joseph Moukarzel
 */
public class MatchZooRunner {

    public static void main(String[] args) throws IOException {

        MatchZooIntegration mz = new MatchZooIntegration("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\index",
                "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\qrels.robust2004.txt",
                "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\relation.txt");

        Index index = mz.getIndex();
        MetaIndex mi = index.getMetaIndex();
        File file = new File(mz.getPahToQrels());
        Reader csvData = new BufferedReader(new FileReader(file));
        CSVParser parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        StringBuilder sb = new StringBuilder();
        DocumentIndex di = index.getDocumentIndex();
        PrintWriter pw = new PrintWriter(new File(mz.getOutputRelation()));
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> docnos = new HashMap<>();
        PostingIndex<Pointer> dir = (PostingIndex<Pointer>) index.getDirectIndex();
//        for (int i = 0; i < di.getNumberOfDocuments(); ++i) {
//            docnos.put(mi.getItem("docno", i), i);
//        }
//        for (int j = 0; j < records.size(); ++j) {
//            int doc = docnos.get(records.get(j).get(2));
//            sb.append(records.get(j).get(3));
//            sb.append(' ');
//            sb.append("Q");
//            sb.append(records.get(j).get(0));
//            sb.append(' ');
//            sb.append("D");
//            sb.append(doc);
//            sb.append('\n');
//        }
//        pw.write(sb.toString());
//        pw.flush();
//        pw.close();

        int[] docids = new int[di.getNumberOfDocuments()];
        for (int i = 0; i < docids.length; ++i) {
            docids[i] = i;
        }

        FileWriter fw = new FileWriter(new File("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\documents_preprocessed.txt"), true);
        BufferedWriter pw2 = new BufferedWriter(fw);
        StringBuilder sb2 = new StringBuilder();
        MatchZooDocumentRepresentor mzdr = new MatchZooDocumentRepresentor(index, pw2,docids, new EnglishTokeniser(),
           "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\tmp_file.txt" ,false,50);
        mzdr.writeRepresentation();

    }

}
