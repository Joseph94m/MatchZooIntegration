/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.IOException;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.PostingIndex;

/**
 *
 * @author Joseph
 */
public class IDToString {

    public static void main(String[] args) throws IOException {
        Index index = Index.createIndex("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\\\index", "data");
        DocumentIndex di = index.getDocumentIndex();
        MetaIndex mi = index.getMetaIndex();
        String[] doc = mi.getAllItems(374017);
        for (String d : doc) {
            System.out.println(d);
        }
    }

}
