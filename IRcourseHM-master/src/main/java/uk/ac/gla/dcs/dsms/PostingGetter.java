/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.IOException;
import java.util.Arrays;
import org.terrier.structures.BitIndexPointer;
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
public class PostingGetter {

    public static void main(String[] args) throws IOException {
        Index index = Index.createIndex("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\index", "data");
        PostingIndex<Pointer> inv = (PostingIndex<Pointer>) index.getInvertedIndex();
        MetaIndex meta = index.getMetaIndex();
        Lexicon<String> lex = index.getLexicon();
        LexiconEntry le = lex.getLexiconEntry("monday");
        IterablePosting postings = inv.getPostings((BitIndexPointer) le);
        while (postings.next() != IterablePosting.EOL) {
            String docno = meta.getItem("docno", postings.getId());
            int[] positions = ((BlockPosting) postings).getPositions();
            System.out.println(docno + " with frequency " + postings.getFrequency() + " and positions " + Arrays.toString(positions));
        }
    }

}
