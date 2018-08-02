/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terrier.matching.ResultSet;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.outputformat.*;
import org.terrier.utility.ApplicationSetup;

/**
 *
 * @author Joseph Moukarzel
 */
public class MatchZooOutputFormat implements OutputFormat {

    /**
     * The logger used
     */
    protected static final Logger logger = LoggerFactory.getLogger(TRECDocidOutputFormat.class);
    private boolean[] iterated;
    private String path;
    private String path_to_results;
    private FileWriter fw;
    private BufferedWriter bw;
    private Lexicon<String> lex;
    private MatchZooDocumentRepresentor mzdr;

    /**
     * Creates a new TRECDocidOutputFormat. The index object is ignored
     */
    public MatchZooOutputFormat(Index index) throws IOException {
        lex = index.getLexicon();
        iterated = new boolean[index.getDocumentIndex().getNumberOfDocuments()];

        path_to_results = ApplicationSetup.TERRIER_HOME + ApplicationSetup.FILE_SEPARATOR + "var" + ApplicationSetup.FILE_SEPARATOR + "results" + ApplicationSetup.FILE_SEPARATOR;
        System.out.println(path_to_results);
        PrintWriter writer = new PrintWriter(path_to_results + "corpus_preprocessed.txt");
        writer.print("");
        writer.close();
        fw = new FileWriter(path_to_results + "corpus_preprocessed.txt", true);
        bw = new BufferedWriter(fw);
        mzdr = new MatchZooDocumentRepresentor(
                index,
                bw,
                254,
                iterated);
    }

    /**
     * Prints the results for the given search request, using the specified
     * destination.
     *
     * @param pw PrintWriter the destination where to save the results.
     * @param q SearchRequest the object encapsulating the query and the
     * results.
     */
    public void printResults(final PrintWriter pw, final SearchRequest q,
            String method, String iteration, int _RESULTS_LENGTH) throws IOException {

        final ResultSet set = q.getResultSet();
        int count_tokens = 0;
        String[] query_terms = q.getQuery().toString().split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("Q");
        sb.append(q.getQueryID());
        sb.append(" ");
        fw = new FileWriter(path_to_results + "corpus_preprocessed.txt", true);
        bw = new BufferedWriter(fw);
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
        sb.append('\n');
        bw.write(sb.toString());
        bw.flush();
        final int[] docids = set.getDocids();

        mzdr.writeRepresentation(docids);
        final double[] scores = set.getScores();
        if (set.getResultSize() == 0) {
            logger.warn("No results retrieved for query " + q.getQueryID());
            return;
        }

        final int maximum = _RESULTS_LENGTH > set.getResultSize()
                || _RESULTS_LENGTH == 0 ? set.getResultSize()
                        : _RESULTS_LENGTH;
        final String queryIdExpanded = "Q" + q.getQueryID();
        StringBuilder sbuffer = new StringBuilder();
        int limit = 10000;
        int ctr = 0;
        for (int i = 0; i < maximum; i++) {
            if (scores[i] == Double.NEGATIVE_INFINITY) {
                continue;
            }
            sbuffer.append(scores[i]);
            sbuffer.append(" ");
            sbuffer.append(queryIdExpanded);
            sbuffer.append(" ");
            sbuffer.append("D");
            sbuffer.append(docids[i]);
            sbuffer.append(ApplicationSetup.EOL);
            ctr++;
            if (ctr % limit == 0) {
                pw.write(sbuffer.toString());
                sbuffer = null;
                sbuffer = new StringBuilder();
                pw.flush();
            }
        }
        pw.write(sbuffer.toString());
        pw.flush();
    }

}
