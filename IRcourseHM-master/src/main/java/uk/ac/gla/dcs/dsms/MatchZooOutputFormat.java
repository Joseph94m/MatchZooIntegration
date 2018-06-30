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
import org.terrier.indexing.tokenisation.EnglishTokeniser;
import org.terrier.matching.ResultSet;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
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
    final Index index;

    /**
     * Creates a new TRECDocidOutputFormat. The index object is ignored
     */
    public MatchZooOutputFormat(Index index) {
        this.index = index;
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
        PostingIndex<Pointer> dir = (PostingIndex<Pointer>) index.getDirectIndex();
        String path = new File("").getAbsolutePath();
        String bin = "";
        DocumentIndex doi = index.getDocumentIndex();
        StringTokenizer st = new StringTokenizer(path, "" + File.separatorChar);
        while (st.hasMoreTokens()) {
            bin = st.nextToken();
        }
        String path_to_results;
        if (bin.equals("bin")) {
            path_to_results = path + File.separatorChar + ".." + File.separatorChar + "var" + File.separatorChar + "results" + File.separatorChar;
        } else {
            path_to_results = path + File.separatorChar + "var" + File.separatorChar + "results" + File.separatorChar;
        }

        FileWriter fw = new FileWriter(path_to_results + "corpus_preprocessed.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        Lexicon<String> lex = index.getLexicon();
        String[] query_terms = q.getQuery().toString().split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("Q");
        sb.append(q.getQueryID());
        sb.append(" ");
        sb.append("" + query_terms.length);
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

        //MatchZooDocumentRepresentor mzdr = new MatchZooDocumentRepresentor(index, bw, docids, new EnglishTokeniser(), path_to_results + "tmp_file.txt",true,50);
       // mzdr.writeRepresentation();

        bw.close();

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
