package gp;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.terminal.Variable;

public class CostEstimationFitnessFunction extends GPFitnessFunction
{

  private double[][] targets;
  private Variable[] variables;

  private static Object[] NO_ARGS = new Object[0];

  public CostEstimationFitnessFunction(double t[][], Variable[] x)
  {
    targets = t;
    variables = x;
  }

  @Override
  protected double evaluate(final IGPProgram program)
  {
    double result = 0.0;

    double longResult = 0.0;
    for (int i = 0; i < targets.length; i++) {
      for (int j = 0; j < variables.length; j++) {
        // Set the input values
        variables[j].set(targets[i][j]);
      }
      // Execute the genetically engineered algorithm
      double value = program.execute_double(0, NO_ARGS);

      // The closer longResult gets to 0 the better the algorithm.
      longResult += Math.abs(value - targets[i][variables.length]);
    }

    result = longResult;

    return result;
  }

}