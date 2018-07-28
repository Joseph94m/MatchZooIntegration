/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import org.terrier.matching.models.WeightingModelLibrary;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.PostingIndex;

/**
 *
 * @author Joseph
 */
/*
This class generates word_stats.txt and word_dict.txt
 */
public class WordGen {

    private final Index index;

    public WordGen(String index) {
        this.index = Index.createIndex(index, "data");;
    }

    public WordGen(Index index) {
        this.index = index;
    }

    public void wordDict(String output) throws IOException {
        PrintWriter writer = new PrintWriter(output);
        writer.print("");
        writer.close();
        FileWriter fw = new FileWriter(output, true);
        BufferedWriter bw = new BufferedWriter(fw);
        Lexicon<String> lex = index.getLexicon();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lex.numberOfEntries(); ++i) {
            Entry<String, LexiconEntry> entry = lex.getLexiconEntry(i);
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(i);
            sb.append('\n');
            if (i % 5000 == 0) {
                bw.append(sb.toString());
                bw.flush();
                sb = new StringBuilder();
            }
        }
        bw.append(sb.toString());
        bw.flush();
        bw.close();
    }

    public void wordStats(String output) throws IOException {
        PrintWriter writer = new PrintWriter(output);
        writer.print("");
        writer.close();
        FileWriter fw = new FileWriter(output, true);
        BufferedWriter bw = new BufferedWriter(fw);
        Lexicon<String> lex = index.getLexicon();
        StringBuilder sb = new StringBuilder();
        PostingIndex<?> inv = index.getInvertedIndex();

        for (int i = 0; i < lex.numberOfEntries(); ++i) {
            Entry<String, LexiconEntry> entry = lex.getLexiconEntry(i);

            sb.append(i);
            sb.append(" ");
            sb.append(entry.getValue().getNumberOfEntries());
            sb.append(" ");
            sb.append(entry.getValue().getDocumentFrequency());
            sb.append(" ");
            sb.append(WeightingModelLibrary.log(index.getDocumentIndex().getNumberOfDocuments() / (entry.getValue().getNumberOfEntries() + 0.0)));
            sb.append('\n');
            if (i % 5000 == 0) {
                bw.append(sb.toString());
                bw.flush();
                sb = new StringBuilder();
            }
        }
        bw.append(sb.toString());
        bw.flush();
        bw.close();
    }



}
