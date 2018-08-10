/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.terrier.matching.MatchingQueryTerms;
import org.terrier.matching.ResultSet;
import org.terrier.matching.dsms.DocumentScoreModifier;
import org.terrier.querying.parser.Query;
import org.terrier.structures.Index;

/**
 *
 * @author Joseph
 */
public class NeuralScoreModifier implements DocumentScoreModifier {

    MZCommunicator mz;

    public NeuralScoreModifier() {
        System.out.println("NeuralScoreModifier started");
       
        mz = new MZCommunicator(InetAddress.getLoopbackAddress());
    }

    @Override
    public boolean modifyScores(Index index, MatchingQueryTerms queryTerms, ResultSet resultSet) {

        mz.contactMZ(queryTerms.getQueryId(), resultSet);
        return true;
    }

    @Override
    public String getName() {
        return "NeuralScoreModifier";
    }

    public Object clone() {
        return null;
    }

}
