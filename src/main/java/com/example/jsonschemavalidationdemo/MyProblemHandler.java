package com.example.jsonschemavalidationdemo;

import java.io.PrintStream;
import java.util.List;

import javax.json.stream.JsonLocation;

import org.leadpony.justify.api.Problem;
import org.leadpony.justify.api.ProblemHandler;

/**
 * A custom problem handler which will format the found problems in a HTML table.
 *
 * For the sake of simplicity, this handler ignores the branches of the problem
 * even if there were multiple solutions for the problem.
 *
 * @see ProblemHandler
 */
public class MyProblemHandler implements ProblemHandler {

    private final PrintStream out;

    public MyProblemHandler() {
        this.out = System.out;
    }

    /**
     * Handles the found problems.
     * We will output a row of the table for each problem.
     *
     * @param problems the found problems.
     */
    @Override
    public void handleProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            JsonLocation loc = problem.getLocation();
            out.print("[");
            out.print(loc.getLineNumber());
            out.print(":");
            out.print(loc.getColumnNumber());
            out.print("] ");
            out.print(problem.getMessage());
            out.print("[");
            out.print(problem.getKeyword());
            out.print("]");
            out.println();
        }
    }
}
