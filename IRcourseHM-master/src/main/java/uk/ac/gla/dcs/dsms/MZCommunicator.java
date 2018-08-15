/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.terrier.matching.ResultSet;
import org.terrier.querying.parser.Query;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;

/**
 *
 * @author Joseph
 */
public class MZCommunicator {

    private InetAddress server;
    public static final int PORT_NUMBER = 6776;
    public static final String ENCODING = "UTF-8";
    private Socket clientSocket;

    public MZCommunicator(InetAddress host) {
        server = host;
        System.out.println(server);

    }

    public void contactMZ(Index index, Query query, ResultSet resultSet, String queryID) throws IOException {

        try {
            clientSocket = new Socket(server.getHostName(), PORT_NUMBER);
            System.out.println(server.getHostName());
        } catch (SocketException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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

        String line = sb.toString();
        outToServer.write(line.getBytes(ENCODING));
        String fromServer = inFromServer.readLine();
        sb = new StringBuilder();
        sb.append("Q");
        sb.append(queryID);
        sb.append(" ");
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

        line = sb.toString();
        outToServer.write(line.getBytes(ENCODING));
        fromServer = inFromServer.readLine();
        int setSize = docids.length;
        line = "" + setSize;

        MatchZooDocumentRepresentor mzdr = new MatchZooDocumentRepresentor(
                index,
                null,
                254,
                new boolean[index.getDocumentIndex().getNumberOfDocuments()]);

        String[] docs = mzdr.getRepresentation(docids);
        outToServer.write(line.getBytes(ENCODING));
        for (int i = 0; i < docs.length; ++i) {
            fromServer = inFromServer.readLine();
            outToServer.write(docs[i].getBytes(ENCODING));

        }
        fromServer = inFromServer.readLine();
        String s2 = fromServer;
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

            scores[i] = Double.parseDouble(strippedScore); // + scores[i];
            ++i;
        }

    }

}
