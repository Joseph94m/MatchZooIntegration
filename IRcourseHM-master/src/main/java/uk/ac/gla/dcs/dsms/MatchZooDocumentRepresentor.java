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
    final int docSize;
    boolean[] iterated;

    public MatchZooDocumentRepresentor(Index index, Writer bw, int[] docids, Tokeniser tk, int docSize, boolean[] iterated) throws FileNotFoundException, IOException {
        this.index = index;
        this.bw = bw;
        this.docids = docids;
        this.tk = tk;
        this.docSize = docSize;
        this.iterated = iterated;
    }

    private String represent() throws IOException {

        MetaIndex mi = index.getMetaIndex();
        Lexicon<String> lex = index.getLexicon();
        StringBuilder sb = new StringBuilder();
        StringBuilder tmp_body;
        StringBuilder sb2 = new StringBuilder();
        int token_count;
        for (int i = 0; i < docids.length; ++i) {
            if (iterated[docids[i]] == true) {
                continue;
            }
            iterated[docids[i]] = true;
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
