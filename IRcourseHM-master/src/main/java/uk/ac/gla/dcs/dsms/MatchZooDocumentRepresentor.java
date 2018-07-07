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
import java.util.Arrays;
import java.util.Map;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
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
        StringBuilder sb;
        IterablePosting postings;
        int[] text;
        int doc_length;
        int term_id;
        for (int i = 0; i < docids.length; ++i) {
            if (iterated[docids[i]] == true) {
                continue;
            }
            iterated[docids[i]] = true;
            sb = new StringBuilder();

            PostingIndex<?> di = index.getDirectIndex();
            DocumentIndex doi = index.getDocumentIndex();
            doc_length = doi.getDocumentLength(docids[i]);

            if (doc_length > 200) {
                text = new int[200];
                doc_length = 200;
            } else {
                text = new int[doc_length];
            }

            postings = di.getPostings(doi.getDocumentEntry(docids[i]));
            while (postings.next() != IterablePosting.EOL) {
                term_id = postings.getId();
                int[] pos = ((BlockPosting) postings).getPositions();

                for (int position : pos) {
                    if (position < 200) {
                        text[position] = term_id;
                    }
                }

            }
            sb.append("D");
            sb.append(docids[i]);
            sb.append(" ");
            sb.append(doc_length);
            for (int t : text) {
                sb.append(" ");
                sb.append(t);
            }
            sb.append('\n');
            bw.write(sb.toString());
            bw.flush();

//            String body = mi.getItem("body", docids[i]);
//            String[] tokenized_body = tk.getTokens(body);
//            sb.append("D");
//            sb.append(docids[i]);
//            sb.append(" ");
//            for (String t : tokenized_body) {
//                if (token_count == docSize) {
//                    break;
//                }
//                LexiconEntry le = lex.getLexiconEntry(t);
//                if (le == null) {
//                } else {
//                    ++token_count;
//                    tmp_body.append(" ");
//                    tmp_body.append(le.getTermId());
//                }
//            }
//
//            sb.append(token_count);
//            sb.append(tmp_body.toString());
//            sb.append('\n');
//            bw.write(sb.toString());
//            bw.flush();
        }
    }

    public void writeRepresentation(int[] docids) throws IOException {
        represent(docids);
    }

}
