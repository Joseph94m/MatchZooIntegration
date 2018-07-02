/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;

/**
 *
 * @author Joseph
 */
public class MatchZooDocumentRepresentor {

    final private Index index;
    final private Writer bw;
    final private Tokeniser tk;
    private BufferedReader r;
    final int docSize;
    boolean[] iterated;

    public MatchZooDocumentRepresentor(Index index, Writer bw, Tokeniser tk, int docSize, boolean[] iterated) throws FileNotFoundException, IOException {
        this.index = index;
        this.bw = bw;
        this.tk = tk;
        this.docSize = docSize;
        this.iterated = iterated;
    }

    private void represent(int[] docids) throws IOException {
        MetaIndex mi = index.getMetaIndex();
        Lexicon<String> lex = index.getLexicon();
        StringBuilder sb;
        StringBuilder tmp_body;
        int token_count;
        for (int i = 0; i < docids.length; ++i) {
            if (iterated[docids[i]] == true) {
                continue;
            }
            iterated[docids[i]] = true;
            token_count = 0;
            tmp_body = new StringBuilder();
            sb = new StringBuilder();
            String body = mi.getItem("body", docids[i]);
            String[] tokenized_body = tk.getTokens(body);
            sb.append("D");
            sb.append(docids[i]);
            sb.append(" ");
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
            bw.write(sb.toString());
            bw.flush();
            

        }
    }

    public void writeRepresentation(int[] docids) throws IOException {
        represent(docids);
    }

}
