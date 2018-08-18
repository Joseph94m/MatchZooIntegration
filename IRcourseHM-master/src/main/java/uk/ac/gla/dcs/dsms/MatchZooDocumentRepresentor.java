/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.IterablePosting;

/**
 *
 * @author Joseph
 */

/*
*This class appens to a file specified by @param bw, the following line: D+Docid #of_words list_of_word_ids
*This is used to create the corpus_preprocessed file
 */
public class MatchZooDocumentRepresentor {

    final private Index index;
    final private Writer bw;
    final int docSize;
    boolean[] iterated;

    public MatchZooDocumentRepresentor(Index index, Writer bw, int docSize, boolean[] iterated) throws FileNotFoundException, IOException {
        this.index = index;
        this.bw = bw;
        this.docSize = docSize;
        this.iterated = iterated;
    }

    public void writeRepresentation(int[] docids) throws IOException {
        StringBuilder sb;
        IterablePosting postings;
        int[] text;
        int doc_length;
        int term_id;
        //Check if document is already present in the file. If yes, then skip doc.
        for (int i = 0; i < docids.length; ++i) {
            if (iterated[docids[i]] == true) {
                continue;
            }
            //If document is not  in file, get the first @docSize tokens/words and append them to a string builder
            iterated[docids[i]] = true;
            sb = new StringBuilder();

            PostingIndex<?> di = index.getDirectIndex();
            DocumentIndex doi = index.getDocumentIndex();
            doc_length = doi.getDocumentLength(docids[i]);

            if (doc_length > docSize) {
                text = new int[docSize];
                doc_length = docSize;
            } else {
                text = new int[doc_length];
            }

            postings = di.getPostings(doi.getDocumentEntry(docids[i]));
            while (postings.next() != IterablePosting.EOL) {
                term_id = postings.getId();
                int[] pos = ((BlockPosting) postings).getPositions();
                for (int position : pos) {
                    if (position < docSize) {
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
            //Append to file
            bw.write(sb.toString());
            bw.flush();
        }
    }

    public String[] getRepresentation(int[] docids) throws IOException {
        StringBuilder sb = null;
        IterablePosting postings;
        String [] ret = new String[docids.length];
        int[] text;
        int doc_length;
        int term_id;
        //Check if document is already present in the file. If yes, then skip doc.
        for (int i = 0; i < docids.length; ++i) {
            if (iterated[docids[i]] == true) {
                continue;
            }
            //If document is not  in file, get the first @docSize tokens/words and append them to a string builder
            iterated[docids[i]] = true;
            sb= new StringBuilder();
            PostingIndex<?> di = index.getDirectIndex();
            DocumentIndex doi = index.getDocumentIndex();
            doc_length = doi.getDocumentLength(docids[i]);

            
            if (doc_length > docSize) {
                text = new int[docSize];
                doc_length = docSize;
            } else {
                text = new int[doc_length];
            }

            postings = di.getPostings(doi.getDocumentEntry(docids[i]));
            while (postings.next() != IterablePosting.EOL) {
                term_id = postings.getId();
                int[] pos = ((BlockPosting) postings).getPositions();
                for (int position : pos) {
                    if (position < docSize) {
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
            ret[i]=sb.toString();

        }
        return ret;
    }

}
