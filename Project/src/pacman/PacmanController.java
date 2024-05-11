package pacman;

import java.io.Serializable;

import genetic_neural.NeuralNetwork;
import utils.GameController;

public class PacmanController implements GameController, Serializable {

	private static final long serialVersionUID = 1L;
	private NeuralNetwork network;
	private double fitness;
	private int seed;

	// Construtores
	public PacmanController(int inputLength, int outputLength, int hiddenLength) {
		this.network = new NeuralNetwork(inputLength, outputLength, hiddenLength);
	}

	public PacmanController(NeuralNetwork network) {
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

	//
	@Override
	public int nextMove(int[] currentState) {
		return network.forward(currentState);
	}

	// Função que mutam e realizam o crossover das redes neuronais
	public PacmanController mutate(double mutation_rate) {
		return new PacmanController(network.mutate(mutation_rate));

	}

	public PacmanController[] crossover(PacmanController controller) {
		NeuralNetwork[] newNetworks = network.crossover(controller.getNetwork());

		return new PacmanController[] { new PacmanController(newNetworks[0]), new PacmanController(newNetworks[1]) };
	}

}
