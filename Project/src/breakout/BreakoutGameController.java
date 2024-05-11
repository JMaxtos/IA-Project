package breakout;

import java.io.Serializable;

import genetic_neural.NeuralNetwork;
import utils.Commons;
import utils.GameController;

public class BreakoutGameController implements GameController, Serializable {
	private static final long serialVersionUID = 1L;
	private NeuralNetwork network;
	private double fitness;
	private int seed;

	// Construtores
	public BreakoutGameController(int inputLength, int outputLength, int hiddenLength) {
		this.network = new NeuralNetwork(inputLength, outputLength, hiddenLength);
	}

	public BreakoutGameController(NeuralNetwork network) {
		this.network = network;
	}

	// Setters e Getters

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public NeuralNetwork getNetwork() {
		return network;
	}

	// Função que define a próxima jogada
	@Override
	public int nextMove(int[] currentState) {
		return network.forward(currentState);
	}

	// Funções que mutam e realizam o crossover das redes neuronais

	public BreakoutGameController mutate(double mutation_rate) {
		return new BreakoutGameController(network.mutate(mutation_rate));

	}

	public BreakoutGameController[] crossover(BreakoutGameController controller) {
		NeuralNetwork[] newNetworks = network.crossover(controller.getNetwork());

		return new BreakoutGameController[] { new BreakoutGameController(newNetworks[0]),
				new BreakoutGameController(newNetworks[1]) };
	}
}
