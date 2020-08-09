package com.fatmanur.unittest.junit.model;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

public class DropBookingConditionExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {

        if (List.of("member").containsAll(context.getTags()) || List.of("member", "dropBooking").containsAll(context.getTags())) {
            return ConditionEvaluationResult.enabled("Drop booking is enabled");
        }

        return ConditionEvaluationResult.disabled("Only drop booking allowed to run");
    }
}