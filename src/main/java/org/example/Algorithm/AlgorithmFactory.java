package org.example.Algorithm;

import org.example.Algorithm.Impl.BestFitAlgorithm;
import org.example.Algorithm.Impl.FirstFitAlgorithm;
import org.example.Algorithm.Impl.WorstFitAlgorithm;
import org.example.Enum.AlgorithmEnum;

public class AlgorithmFactory {
    public static IAlgorithmStrategy getAlgorithm(AlgorithmEnum algorithmEnum) {
        return switch (algorithmEnum) {
            case FIRST -> new FirstFitAlgorithm();
            case BEST -> new BestFitAlgorithm();
            case WORST -> new WorstFitAlgorithm();
        };
    }
}
