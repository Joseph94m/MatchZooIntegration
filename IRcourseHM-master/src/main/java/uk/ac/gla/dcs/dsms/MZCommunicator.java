/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.Query;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;

/**
 *
 * @author Joseph
 */
public class MZCommunicator {

    InetAddress server;
    DatagramSocket ss = null;

    String[] newScores;

    public MZCommunicator(InetAddress host) {
        server = host;
        System.out.println(server);
        try {
            ss = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void contactMZ(Index index, Query query, ResultSet resultSet, String queryID) throws IOException {
        Lexicon<String> lex = index.getLexicon();
        int[] docids = resultSet.getDocids();
        double[] scores = resultSet.getScores();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < docids.length; ++i) {
            sb.append(scores[i]);
            sb.append(" ");
            sb.append("Q");
            sb.append(queryID);
            sb.append(" ");
            sb.append("D");
            sb.append(docids[i]);
            sb.append('\n');
        }

        String ligne = sb.toString();
        int longueur = ligne.length();
        byte[] message = new byte[longueur];
        message = ligne.getBytes();

        DatagramPacket rel = new DatagramPacket(message, longueur, server, 6776);
        try {
            ss.send(rel);
        } catch (IOException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

        sb = new StringBuilder();
        sb.append("Q");
        sb.append(queryID);
        int count_tokens = 0;
        String[] query_terms = query.toString().split(" ");
        for (String s : query_terms) {

            if (lex.getLexiconEntry(s) != null) {
                ++count_tokens;
            }
        }

        sb.append(count_tokens);
        for (String s : query_terms) {

            if (lex.getLexiconEntry(s) != null) {
                sb.append(" ");
                sb.append(lex.getLexiconEntry(s).getTermId());
            }
        }

        ligne = sb.toString();
        longueur = ligne.length();
        message = new byte[longueur];
        message = ligne.getBytes();
        DatagramPacket q = new DatagramPacket(message, longueur, server, 6776);
        System.out.println(longueur);
        try {
            ss.send(q);
        } catch (IOException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatchZooDocumentRepresentor mzdr = new MatchZooDocumentRepresentor(
                index,
                null,
                254,
                new boolean[index.getDocumentIndex().getNumberOfDocuments()]);
    
        ligne = mzdr.getRepresentation(docids);
                longueur = ligne.length();
        message = new byte[longueur];
        message = ligne.getBytes();
        DatagramPacket d = new DatagramPacket(message, longueur, server, 6776);
        System.out.println(longueur);
        try {
            ss.send(d);
        } catch (IOException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }


    byte[] receivedMessage = new byte[longueur];
    DatagramPacket recept = new DatagramPacket(receivedMessage, receivedMessage.length);

    
        try {
            ss.receive(recept);
        String s2 = new String(receivedMessage);
        System.out.println(s2);
        StringTokenizer tk = new StringTokenizer(s2, " ");
        int i = 0;
        String newscore;
        String strippedScore;
        while (tk.hasMoreTokens() && i < scores.length) {
            newscore = tk.nextToken();
            strippedScore = newscore.substring(1, newscore.length() - 1);
            if (i == scores.length - 1) {
                strippedScore = strippedScore.trim();
                strippedScore = strippedScore.substring(0, strippedScore.length() - 1);
            }

            scores[i++] = Double.parseDouble(strippedScore);
        }
    }
    catch (IOException ex

    
        ) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
    }

}

}