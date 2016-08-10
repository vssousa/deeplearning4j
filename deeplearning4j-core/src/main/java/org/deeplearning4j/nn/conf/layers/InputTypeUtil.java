package org.deeplearning4j.nn.conf.layers;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.conf.InputPreProcessor;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.preprocessor.FeedForwardToCnnPreProcessor;
import org.deeplearning4j.nn.conf.preprocessor.RnnToCnnPreProcessor;

/**
 * Utilities for calculating input types
 *
 * @author Alex Black
 */
@Slf4j
public class InputTypeUtil {


    public static InputType getOutputTypeCnnLayers(InputType inputType, int[] kernelSize, int[] stride, int[] padding,
                                                   int outputDepth, String layerName) {

        InputType.InputTypeConvolutional i = (InputType.InputTypeConvolutional) inputType;
        int inHeight = i.getHeight();
        int inWidth = i.getWidth();
        int padH = padding[0];
        int padW = padding[1];
        int kH = kernelSize[0];
        int kW = kernelSize[1];
        int sH = stride[0];
        int sW = stride[1];

        if ((inHeight - kH + 2 * padH) % sH != 0) {
            throw new IllegalStateException("Invalid input configuration (layer name = \"" + layerName + "\") for height: inHeight=" + inHeight + ", kernelH="
                    + kH + ", padH=" + padH + ", strideH=" + sH + "; (" + inHeight + "-" + kH + "+2*" + padH + ")/" + sH
                    + " is not an integer");
        }
        if ((inWidth - kW + 2 * padW) % sW != 0) {
            throw new IllegalStateException("Invalid input configuration (layer name = \"" + layerName + "\") for width: inWidth=" + inWidth + ", kernelW="
                    + kW + ", padW=" + padW + ", strideW=" + sW + "; (" + inWidth + "-" + kW + "+2*" + padW + ")/" + sW
                    + " is not an integer");
        }

        int hOut = (inHeight - kH + 2 * padH) / sH + 1;
        int wOut = (inWidth - kW + 2 * padW) / sH + 1;
        return InputType.convolutional(hOut, wOut, outputDepth);
    }

    /**
     * Utility method for determining the appropriate preprocessor for CNN layers, such as {@link ConvolutionLayer} and
     * {@link SubsamplingLayer}
     *
     * @param inputType     Input type to get the preprocessor for
     * @return              Null if no preprocessor is required; otherwise the appropriate preprocessor for the given input type
     */
    public static InputPreProcessor getPreProcessorForInputTypeCnnLayers(InputType inputType, String layerName){

        //To add x-to-CNN preprocessor: need to know image depth/width/height after reshaping
        //But this can't be inferred from the FF/RNN activations directly (could be anything)

        switch (inputType.getType()){
            case FF:
                //FF -> CNN
//                return new FeedForwardToCnnPreProcessor(inputSize[0], inputSize[1], inputDepth);
                log.info("Automatic addition of FF -> CNN preprocessors: not yet implemented (layer name: \"" + layerName + "\")");
                return null;
            case RNN:
                //RNN -> CNN
//                return new RnnToCnnPreProcessor(inputSize[0], inputSize[1], inputDepth);
                log.warn("Automatic addition of RNN -> CNN preprocessors: not yet implemented (layer name: \"" + layerName + "\")");
                return null;
            case CNN:
                //CNN -> CNN: no preprocessor required
                return null;
            default:
                throw new RuntimeException("Unknown input type: " + inputType);
        }

    }

}
