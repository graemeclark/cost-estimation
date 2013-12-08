package gp;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

/**
 * @author carlos
 * 
 */
public class CostEstimation extends GPProblem
{

  // ALBRECHT
  //private final int variableNum = 7; private final int fitnessNum = 24; private final String fileName = "albrecht.dat";
  
  //MAXWELL
  private final int variableNum = 26; private final int fitnessNum = 47; private final String fileName = "maxwell.dat";
  
  //CHINA
  //private final int variableNum = 18; private final int fitnessNum = 499; private final String fileName = "china.dat";
    
  private double[][] targets = new double[fitnessNum][variableNum + 1];
  private Variable[] variables = new Variable[variableNum];

  private DataReader dr;

  public CostEstimation() throws InvalidConfigurationException
  {
    super(new GPConfiguration());

    GPConfiguration config = getGPConfiguration();

    dr = new DataReader(variableNum, fitnessNum);
    targets = dr.setup_fitness(fileName);

    for (int i = 0; i < variables.length; i++) {
      variables[i] = Variable.create(config, "X" + i, CommandGene.DoubleClass);
    }

    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config.setMaxInitDepth(10);
    config.setPopulationSize(1000);
    config.setMaxCrossoverDepth(8);
    config.setFitnessFunction(new CostEstimationFitnessFunction(targets,
        variables));
    config.setStrictProgramCreation(true);

  }

  @Override
  public GPGenotype create() throws InvalidConfigurationException
  {
    GPConfiguration config = getGPConfiguration();

    // The return type of the GP program.
    Class[] types = { CommandGene.DoubleClass };

    // Arguments of result-producing chromosome: none
    Class[][] argTypes = { {} };

    // Next, we define the set of available GP commands and terminals to
    // use.
    CommandGene[][] nodeSets = new CommandGene[1][variables.length + 10];

    for (int i = 0; i < variables.length; i++) {
      nodeSets[0][i] = variables[i];
    }

    nodeSets[0][variables.length] = new Add(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 1] = new Subtract(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 2] = new Multiply(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 3] = new Divide(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 4] = new Sine(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 5] = new Cosine(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 6] = new Tangent(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 7] = new Ceil(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 8] = new Floor(config,
        CommandGene.DoubleClass);
    nodeSets[0][variables.length + 9] = new Terminal(config,
        CommandGene.DoubleClass, 0.0, 10.0, true);

    GPGenotype result = GPGenotype.randomInitialGenotype(config, types,
        argTypes, nodeSets, 20, true);

    return result;
  }

  public static void main(String[] args) throws Exception
  {
    GPProblem problem = new CostEstimation();

    GPGenotype gp = problem.create();
    gp.setVerboseOutput(true);
    gp.evolve(1000);

    // System.out.println("Formula to discover: x^2 + 2y + 3x + 5");
    gp.outputSolution(gp.getAllTimeBest());
    
    Object[] NO_ARGS = new Object[0];
    String fname = "maxwell1.dat";
    double actual = 0.0;
    double mre = 0.0;
    DataReader dr2 = new DataReader(7, 15);
    double [][] _inputs = dr2.setup_fitness(fname);
    
    for (int i = 0; i < _inputs.length; i++) { 
        // Set the input values 
        for (int j = 0;j < _inputs[i].length - 1; j++) { 
            Variable t = Variable.create(gp.getGPConfiguration(), "X" + (i + 1), CommandGene.DoubleClass);
            System.out.println(_inputs[i][j]);
            t.set(_inputs[i][j]);
            gp.putVariable(t);
        }
        // Execute the genetically engineered algorithm 
        double value = gp.getAllTimeBest().execute_double(0, NO_ARGS);
        actual = _inputs[i][_inputs[i].length - 1];
        System.out.println("VALUE: " + value);
        System.out.println("ACTUAL: " + actual);
        mre += Math.abs((actual - value) / actual);
        System.out.println("\n" + (i + 1) + "\nMRE: " + Math.abs((actual - value) / actual));
    }
    System.out.println("MMRE: " + mre / _inputs.length);
  }

}