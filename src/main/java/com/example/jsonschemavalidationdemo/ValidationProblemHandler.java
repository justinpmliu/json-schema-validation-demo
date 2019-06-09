package com.example.jsonschemavalidationdemo;

import lombok.extern.log4j.Log4j2;
import org.leadpony.justify.api.Problem;
import org.leadpony.justify.api.ProblemHandler;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A custom problem handler which will format the found problems in a HTML table.
 *
 * For the sake of simplicity, this handler ignores the branches of the problem
 * even if there were multiple solutions for the problem.
 *
 * @see ProblemHandler
 */
@Log4j2
public class ValidationProblemHandler implements ProblemHandler {

    private Set<String> missingFields;

    public ValidationProblemHandler() {
        missingFields = new TreeSet<>();
    }

    /**
     * Handles the found problems.
     * We will output a row of the table for each problem.
     *
     * @param problems the found problems.
     */
    @Override
    public void handleProblems(List<Problem> problems) {
        StringBuilder builder = new StringBuilder();

        for (Problem problem : problems) {
            String keyword = problem.getKeyword();

            builder.append("[");
            if (!problem.getPointer().isEmpty()) {
                builder.append(problem.getPointer());
                builder.append(", ");
            }
            builder.append(keyword);
            builder.append("] ");
            builder.append(problem.getMessage());

            if ("required".equals(keyword)) {
                missingFields.add(this.getMissingField(problem.getMessage()));
            }
        }

        log.error(builder.toString());
    }

    public void flush() {
        if (!missingFields.isEmpty()) {
            log.error(String.format("Missing fields: %s", missingFields));
        }
    }

    private String getMissingField(String message) {
        int startIndex = message.indexOf('"');
        int endIndex = message.lastIndexOf('"');
        return message.substring(startIndex + 1, endIndex);
    }
}
