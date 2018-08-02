/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.gla.dcs.dsms;

import java.io.IOException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;





/**
 *
 * @author Joseph
 */
public class MZModel {

    public static void main(String[] args) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        //  KerasModelImport.importKerasModelAndWeights("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1.model", "C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1");
        //KerasModelImport.importKerasModelAndWeights("C:\\Users\\Joseph\\Desktop\\Studies\\Project\\MatchZoo\\examples\\Robust\\weights\\nmws.Robust.444.50.false.254.weights.1.model");
        KerasModelImport.importKerasModelAndWeights("D:\\weights\\knrm.Robust.444.50.false.50.weights.1.model", false);
    }
}
