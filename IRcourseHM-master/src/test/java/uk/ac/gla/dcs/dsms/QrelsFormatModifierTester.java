/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.terrier.indexing.IndexTestUtils;
import org.terrier.structures.Index;
import org.terrier.tests.ApplicationSetupBasedTest;
import org.terrier.utility.ApplicationSetup;

/**
 *
 * @author Joseph
 */
public class QrelsFormatModifierTester extends ApplicationSetupBasedTest {

    QrelsFormatModifier qm;
    Reader csvData;
    CSVParser parser;
    List<CSVRecord> records;
    Index index;

    @Test
    public void OneDoc() throws Exception {
        ApplicationSetup.setProperty("termpipelines", "");
        index = IndexTestUtils.makeIndexBlocks(
                new String[]{"doc1"},
                new String[]{"The quick brown fox jumps over the lazy dog"});

        qm = new QrelsFormatModifier(index);
        qm.writeToTerrierAndMZFormat("src\\test\\java\\testResources\\test2_qrels_format_modifier.txt",
                "src\\test\\java\\testResources\\test2_qrels_format_modifier_output.txt",
                false);
        csvData = new BufferedReader(new FileReader("src\\test\\java\\testResources\\test1_qrels_format_modifier_output.txt"));
        parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        records = parser.getRecords();

        assertEquals("0", records.get(0).get(0));
        assertEquals("Q1", records.get(0).get(1));
        assertEquals("D0", records.get(0).get(2));
        assertEquals(1, records.size());

    }

    @Test
    public void TwoDoc() throws Exception {
        ApplicationSetup.setProperty("termpipelines", "");
        index = IndexTestUtils.makeIndexBlocks(
                new String[]{"doc1", "doc2"},
                new String[]{"The quick brown fox jumps over the lazy dog", "ad vitam aeternam"}
        );

        qm = new QrelsFormatModifier(index);
        qm.writeToTerrierAndMZFormat("src\\test\\java\\testResources\\test2_qrels_format_modifier.txt",
                "src\\test\\java\\testResources\\test2_qrels_format_modifier_output.txt",
                false);
        csvData = new BufferedReader(new FileReader("src\\test\\java\\testResources\\test2_qrels_format_modifier_output.txt"));
        parser = new CSVParser(csvData, CSVFormat.newFormat(' '));
        records = parser.getRecords();

        assertEquals("0", records.get(0).get(0));
        assertEquals("Q1", records.get(0).get(1));
        assertEquals("D0", records.get(0).get(2));
        assertEquals("1", records.get(1).get(0));
        assertEquals("Q1", records.get(1).get(1));
        assertEquals("D1", records.get(1).get(2));
        assertEquals("1", records.get(2).get(0));
        assertEquals("Q2", records.get(2).get(1));
        assertEquals("D0", records.get(2).get(2));
        assertEquals("0", records.get(3).get(0));
        assertEquals("Q2", records.get(3).get(1));
        assertEquals("D1", records.get(3).get(2));
        assertEquals("1", records.get(4).get(0));
        assertEquals("Q3", records.get(4).get(1));
        assertEquals("D0", records.get(4).get(2));
        assertEquals("1", records.get(5).get(0));
        assertEquals("Q3", records.get(5).get(1));
        assertEquals("D1", records.get(5).get(2));
        assertEquals(6, records.size());

    }

}
