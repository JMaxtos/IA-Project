package genetic_neural;

import java.util.ArrayList;
import java.util.Random;

public abstract class GeneticAlgorithm {

	protected int inputLength;
	protected int hiddenLength;
	protected int outputLength;
	protected int gameSeed;
	protected int numberOfParents;
	protected Random random;

	// Construtor

	public GeneticAlgorithm(int gameSeed, int numberOfParents, int inputLength, int hiddenLength, int outputLength) {
		this.gameSeed = gameSeed;
		this.random = new Random(gameSeed);
		this.numberOfParents = numberOfParents;
		this.inputLength = inputLength;
		this.hiddenLength = hiddenLength;
		this.outputLength = outputLength;

	}

	protected abstract void generatePopulation();

	// Função que verifica se o fitness é o mesmo nas últimas 5 gerações
	protected boolean isFitnessEqual(ArrayList<Double> fitness) {
		return fitness.get(0).equals(fitness.get(4));
	}

	public abstract void search();

}
