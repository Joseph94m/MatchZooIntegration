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
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import org.terrier.indexing.tokenisation.EnglishTokeniser;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.IterablePosting;

/**
 *
 * @author Joseph
 */
public class MatchZooDocumentRepresentor {

    final private Index index;
    final private Writer bw;
    final private int[] docids;
    final private Tokeniser tk;
    private BufferedReader r;
    private BufferedWriter w;
    final String tmp_file;
    final private boolean checkDuplicates;
    final int docSize;

    public MatchZooDocumentRepresentor(Index index, Writer bw, int[] docids, Tokeniser tk, String tmp_file, boolean checkDuplicates, int docSize) throws FileNotFoundException, IOException {
        this.index = index;
        this.bw = bw;
        this.docids = docids;
        this.tk = tk;
        this.tmp_file = tmp_file;
        this.checkDuplicates=checkDuplicates;
        this.docSize=docSize;
    }

    private String represent() throws IOException {
        
        MetaIndex mi = index.getMetaIndex();
        Lexicon<String> lex = index.getLexicon();
        StringBuilder sb = new StringBuilder();
        StringBuilder tmp_body;
        StringBuilder sb2 = new StringBuilder();
        int token_count;
        String line;
        Set<String> docs = new HashSet<String>();
        File tmpDir = new File(tmp_file);
        if (!tmpDir.exists()) {
            w = new BufferedWriter(new FileWriter(tmp_file, true));
            w.close();
        }
        if(checkDuplicates){
        r = new BufferedReader(new FileReader(tmp_file));

        while ((line = r.readLine()) != null) {
            docs.add(line);
        }
        r.close();
        }
        long now = System.nanoTime();
        for (int i = 0; i < docids.length; ++i) {
            if(i % 1000 == 0){
                System.out.println(i);
                System.out.println((System.nanoTime() - now)*1.667 * 0.00000000001);
            }
            if (docs.contains("D" + docids[i])) {
                break;
            }
            token_count = 0;
            tmp_body = new StringBuilder();
            String body = mi.getItem("body", docids[i]);
            String[] tokenized_body = tk.getTokens(body);
            sb.append("D");
            sb.append(docids[i]);
            sb.append(" ");
            sb2.append("D");
            sb2.append(docids[i]);
            sb2.append('\n');
            for (String t : tokenized_body) {
                if (token_count == docSize) {
                    break;
                }
                LexiconEntry le = lex.getLexiconEntry(t);
                if (le == null) {
                } else {
                    ++token_count;
                    tmp_body.append(" ");
                    tmp_body.append(le.getTermId());

                }
            }
            sb.append(token_count);
            sb.append(tmp_body.toString());
            sb.append('\n');

        }
        w = new BufferedWriter(new FileWriter(tmp_file, true));
        w.write(sb2.toString());
        w.flush();
        w.close();
        return sb.toString();
    }

    public void writeRepresentation() throws IOException {
        bw.write(represent());
        bw.flush();
    }

    public String getRepresentation() throws IOException {
        return represent();
    }

}
