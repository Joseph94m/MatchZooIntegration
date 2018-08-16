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
import org.terrier.utility.ApplicationSetup;
import org.terrier.utility.ArrayUtils;

/**
 *
 * @author Joseph
 */
public class MZCommunicator {

    public static final String ENCODING = "UTF-8";
    private Socket clientSocket;
    private final double alpha;
    private final String serverName;
    private final int serverPort;

    public MZCommunicator() {
        
        String[] coeff
                = ArrayUtils.parseCommaDelimitedString(
                        ApplicationSetup.getProperty("neural.modifier.alpha", ""));
        alpha = Double.parseDouble(coeff[0]);
        
                String[] name
                = ArrayUtils.parseCommaDelimitedString(
                        ApplicationSetup.getProperty("neural.server.name", ""));
        serverName = name[0];
        
                String[] port
                = ArrayUtils.parseCommaDelimitedString(
                        ApplicationSetup.getProperty("neural.server.port", ""));
        serverPort = Integer.parseInt(port[0]);
        System.out.println(serverName);
        System.out.println(serverPort);
        System.out.println(alpha);

    }

    public void contactMZ(Index index, Query query, ResultSet resultSet, String queryID) throws IOException {

        try {
            clientSocket = new Socket(serverName, serverPort);
        } catch (SocketException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MZCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),ENCODING));
        
        Lexicon<String> lex = index.getLexicon();
        int[] docids = resultSet.getDocids();

        double[] scores = resultSet.getScores();
        StringBuilder sb = new StringBuilder();

//        if (docids.length > 1000) {
//            int messageNumber = docids.length / 1000;
//            String msg = "" + messageNumber;
//            outToServer.write(msg.getBytes(ENCODING));
//            String fromServer = inFromServer.readLine();
//            
//        }

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

            
            
            scores[i] = (alpha) * Double.parseDouble(strippedScore) + (1 - alpha) * scores[i];
            ++i;
        }

    }

}
