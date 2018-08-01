/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.IOException;

/**
 *
 *
 *
 * @author Joseph Moukarzel
 */
public class MatchZooIntegration {

    public static void main(String[] args) throws IOException {
        QrelsFormatModifier mz = new QrelsFormatModifier("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\index");
        mz.writeToTerrierAndMZFormat("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\qrels.robust2004.txt", "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\mz-terrier-qrels.txt",false);

        AOLToTopics aol = new AOLToTopics("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\user-ct-test-collection-01.txt", "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\aol-qrels.txt");
        // aol.writeTransform();
        for (int i = 2; i < 10; ++i) {
            System.out.println(i);
            //   aol.setPathToAOLS("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\user-ct-test-collection-0" + i + ".txt");
            // aol.writeTransform();
        }
        // aol.setPathToAOLS("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\queries\\user-ct-test-collection-10.txt");
        aol.writeTransform();

        Vocabulary wg = new Vocabulary("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\index");
        wg.getWordDict("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\word_dict.txt", false);
        wg.getWordStats("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\word_stats.txt", false);

    }

}
