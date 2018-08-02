/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package learn.to.rank;

import java.io.IOException;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author Joseph
 */
public class MZModel {

    public static void main(String[] args) throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
        //  KerasModelImport.importKerasModelAndWeights("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1.model", "C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1");
        //KerasModelImport.importKerasModelAndWeights("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1.model");
        KerasModelImport.importKerasModelAndWeights("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\knrm.Robust.444.50.false.50.weights.1.model", false);
    }
}
