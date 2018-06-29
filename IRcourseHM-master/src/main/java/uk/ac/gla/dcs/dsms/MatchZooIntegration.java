/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import org.terrier.structures.*;

/**
 * 
 *
 *
 * @author Joseph Moukarzel
 */
public class MatchZooIntegration {

    private String pahToQrels;
    private String outputRelation;
    private Index index;

    public MatchZooIntegration(String pathToIndex, String pathToQrels, String outputRelation) {
        this.pahToQrels = pathToQrels;
        this.outputRelation = outputRelation;
        index = Index.createIndex(pathToIndex, "data");

    }

    public MatchZooIntegration(Index i, String pathToQrels, String outputRelation) {
        index = i;
        this.pahToQrels = pathToQrels;
        this.outputRelation = outputRelation;
    }

    public String getPahToQrels() {
        return pahToQrels;
    }

    public void setPahToQrels(String pahToQrels) {
        this.pahToQrels = pahToQrels;
    }

    public String getOutputRelation() {
        return outputRelation;
    }

    public void setOutputRelation(String outputRelation) {
        this.outputRelation = outputRelation;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

}
