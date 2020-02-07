package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.component.evaluation.impl.SequentialEvaluation;
import org.uma.jmetal.component.initialsolutioncreation.InitialSolutionsCreation;
import org.uma.jmetal.component.initialsolutioncreation.impl.RandomSolutionsCreation;
import org.uma.jmetal.component.replacement.impl.MOEADReplacement;
import org.uma.jmetal.component.selection.impl.PopulationAndNeighborhoodMatingPoolSelection;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.termination.impl.TerminationByEvaluations;
import org.uma.jmetal.component.variation.impl.DifferentialCrossoverVariation;
import org.uma.jmetal.operator.crossover.impl.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.aggregativefunction.AggregativeFunction;
import org.uma.jmetal.util.archive.Archive;
import org.uma.jmetal.util.neighborhood.impl.WeightVectorNeighborhood;
import org.uma.jmetal.util.observable.impl.DefaultObservable;
import org.uma.jmetal.util.sequencegenerator.SequenceGenerator;
import org.uma.jmetal.util.sequencegenerator.impl.IntegerPermutationGenerator;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * This class is intended to provide an implementation of the MOEA/D-DE algorithm including a
 * constructor with the typical parameters.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class MOEADDEWithArchive extends MOEADDE {
  private Archive<DoubleSolution> archive;
  private int numberOfSolutionsToTakeFromTheArchive;

  /**
   * Constructor with the parameters used in the paper describing MOEA/D-DE.
   *
   * @param problem
   * @param populationSize
   * @param maxNumberOfEvaluations
   * @param f
   * @param cr
   * @param neighborhoodSelectionProbability
   * @param maximumNumberOfReplacedSolutions
   * @param neighborhoodSize
   */
  public MOEADDEWithArchive(
      Problem<DoubleSolution> problem,
      int populationSize,
      int maxNumberOfEvaluations,
      double cr,
      double f,
      AggregativeFunction aggregativeFunction,
      double neighborhoodSelectionProbability,
      int maximumNumberOfReplacedSolutions,
      int neighborhoodSize,
      String weightVectorDirectory, Archive<DoubleSolution> archive, int numberOfSolutionsToTakeFromTheArchive) {
    super(problem, populationSize, maxNumberOfEvaluations, cr, f, aggregativeFunction, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions, neighborhoodSize, weightVectorDirectory) ;

    this.archive = archive ;
    this.numberOfSolutionsToTakeFromTheArchive = numberOfSolutionsToTakeFromTheArchive ;
  }

  @Override
  protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
    List<DoubleSolution> evaluatedSolutionList = super.evaluatePopulation(population);
    for (DoubleSolution solution : evaluatedSolutionList) {
      archive.add(solution);
    }

    return evaluatedSolutionList;
  }

  @Override
  public List<DoubleSolution> getResult() {
    return SolutionListUtils.distanceBasedSubsetSelection(
            archive.getSolutionList(), numberOfSolutionsToTakeFromTheArchive);
  }

  public Archive<DoubleSolution> getArchive() {
    return archive;
  }
}
