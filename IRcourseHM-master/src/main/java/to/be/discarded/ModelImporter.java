/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.be.discarded;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author Joseph
 */
public class ModelImporter {

    public static ComputationGraph modelImport(String pathToModel, String pathToWeights) {
        ComputationGraph cg = null;
        try {

            cg = KerasModelImport.importKerasModelAndWeights(pathToModel, pathToWeights);
        } catch (IOException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedKerasConfigurationException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKerasConfigurationException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cg;
    }

    public static ComputationGraph modelImport(String pathToModel) {
        ComputationGraph cg = null;
        try {
            cg = KerasModelImport.importKerasModelAndWeights(pathToModel, false);
        } catch (IOException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedKerasConfigurationException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKerasConfigurationException ex) {
            Logger.getLogger(ModelImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cg;

    }
}
