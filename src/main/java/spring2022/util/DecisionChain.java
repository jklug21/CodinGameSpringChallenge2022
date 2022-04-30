package spring2022.util;

public class DecisionChain {
    public static int of(Decision... decisions) {
        for (Decision decisionSupplier : decisions) {
            Integer decision = decisionSupplier.takeDecision();
            if (decision != 0) {
                return decision;
            }
        }
        return 0;
    }
}
