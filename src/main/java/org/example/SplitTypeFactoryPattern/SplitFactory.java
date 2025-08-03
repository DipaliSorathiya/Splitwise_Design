package org.example.SplitTypeFactoryPattern;

import org.example.SplitTypeFactoryPattern.ConcreteSplitClasses.EqualSplit;
import org.example.SplitTypeFactoryPattern.ConcreteSplitClasses.PercentageSplit;

public class SplitFactory {
    public static Split createSplit(String splitType) {
        switch (splitType) {
            case "EQUAL":
                return new EqualSplit();
            case "PERCENTAGE":
                return new PercentageSplit();
            default:
                throw new IllegalArgumentException("Unknown Split type:" +splitType);

        }
    }
}
