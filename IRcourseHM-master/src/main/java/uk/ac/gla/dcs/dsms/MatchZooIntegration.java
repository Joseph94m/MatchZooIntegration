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
        QrelsFormatModifier mz = new QrelsFormatModifier("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\index",
                "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\qrels.robust2004.txt",
                "C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\terrier-core-4.2\\var\\results\\relation.txt");
        mz.wroteToMZFormat();
        mz.setOutputRelation("C:\\Users\\Joseph\\Desktop\\Studies\\Semester2\\IR\\qrels\\mz-terrier-qrels.txt");
        mz.writeToTerrierAndMZFormat();

    }

}
