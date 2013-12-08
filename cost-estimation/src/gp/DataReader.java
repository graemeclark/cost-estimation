package gp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class DataReader
{

  static int varnumber, fitnesscases;
  static double[][] targets;

  public DataReader(int v, int f)
  {
    varnumber = v;
    fitnesscases = f;
    targets = new double[fitnesscases][varnumber + 1];
  }

  public double[][] setup_fitness(String fname)
  {
    try {
      int i, j;
      String line;

      BufferedReader in = new BufferedReader(new FileReader(fname));
      StringTokenizer tokens;

      for (i = 0; i < fitnesscases; i++) {
        line = in.readLine();
        tokens = new StringTokenizer(line, ",");
        for (j = 0; j <= varnumber; j++) {
          targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
        }
      }
      in.close();
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: Please provide a data file");
      System.exit(0);
    } catch (Exception e) {
      System.out.println("ERROR: Incorrect data format");
      System.exit(0);
    }
    return targets;
  }

}
