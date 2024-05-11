package genetic_neural;

import java.io.Serializable;
import java.util.Random;

public class NeuralNetwork implements Serializable {

	private static final long serialVersionUID = 1L;
	private Random r;
	private Node[] hiddenLayer;
	private Node[] outputLayer;
	private int hidden_dim;

	// Construtores

	public NeuralNetwork(int inputLength, int actionsLength, int hiddenLength) {
		this.r = new Random();
		this.hidden_dim = hiddenLength;
		this.hiddenLayer = new Node[hidden_dim];
		this.outputLayer = new Node[actionsLength];

		for (int i = 0; i < hidden_dim; i++) {
			double[] hiddenWeights = generateWeights(inputLength);
			double hiddenBias = generateBias();
			hiddenLayer[i] = new Node(hiddenWeights, hiddenBias);
		}

		for (int i = 0; i < actionsLength; i++) {
			double[] outputWeights = generateWeights(hidden_dim);
			double outputBias = generateBias();
			outputLayer[i] = new Node(outputWeights, outputBias);
		}
	}

	public NeuralNetwork(Node[] hiddenLayer, Node[] outputLayer) {
		this.r = new Random();
		this.hidden_dim = hiddenLayer.length;
		this.hiddenLayer = hiddenLayer;
		this.outputLayer = outputLayer;
	}

	// Getters

	public Node[] getHiddenLayer() {
		return hiddenLayer;
	}

	public Node[] getOutputLayer() {
		return outputLayer;
	}

	// Função que determina o próximo movimento do paddle

	public int forward(int[] gameState) {
		double[] resultsHidden = new double[hidden_dim];
		double[] resultsOutput = new double[outputLayer.length];

		for (int i = 0; i < hidden_dim; i++) {
			resultsHidden[i] = hiddenLayer[i].forward(gameState);
		}
		for (int i = 0; i < outputLayer.length; i++) {

			resultsOutput[i] = outputLayer[i].forward(resultsHidden);
		}
		return getMaxIndex(resultsOutput);
	}

	// Função que determina o índice com maior valor
	private int getMaxIndex(double[] results) {
		int index = 0;
		double value = results[0];
		for (int i = 1; i < results.length; i++) {
			if (value < results[i]) {
				index = i;
			}
		}
		return index;
	}

	// Função que gera pesos para as camadas da rede neuronal
	private double[] generateWeights(int length) {
		double[] weights = new double[length];
		for (int i = 0; i < length; i++)
			weights[i] = r.nextDouble() * 2 - 1;
		return weights;
	}

	// Função que gera as bias para as camadas da rede neuronal
	private double generateBias() {
		return r.nextDouble() * 2 - 1;
	}

	// Função que muta os nós (neurónios) da rede neuronal
	public NeuralNetwork mutate(double mutation_rate) {
		Node[] newhiddenLayer = new Node[hiddenLayer.length];
		Node[] newoutputLayer = new Node[outputLayer.length];
		for (int i = 0; i < hiddenLayer.length; i++)
			newhiddenLayer[i] = hiddenLayer[i].mutate(mutation_rate);
		for (int i = 0; i < outputLayer.length; i++)
			newoutputLayer[i] = outputLayer[i].mutate(mutation_rate);
		return new NeuralNetwork(newhiddenLayer, newoutputLayer);
	}

	// Função que realiza o crossover dos nós (neurónios) da rede neuronal
	public NeuralNetwork[] crossover(NeuralNetwork otherNetwork) {

		Node[] otherNetworkHidden = otherNetwork.getHiddenLayer();
		Node[] otherNetworkOutput = otherNetwork.getOutputLayer();

		Node[] child1Hidden = new Node[hidden_dim];
		Node[] child2Hidden = new Node[hidden_dim];
		Node[] child1Output = new Node[outputLayer.length];
		Node[] child2Output = new Node[outputLayer.length];

		for (int i = 0; i < hidden_dim; i++) {
			Node[] nodes = this.hiddenLayer[i].crossover(otherNetworkHidden[i]);
			child1Hidden[i] = nodes[0];
			child2Hidden[i] = nodes[1];
		}
		for (int i = 0; i < outputLayer.length; i++) {
			Node[] nodes = this.outputLayer[i].crossover(otherNetworkOutput[i]);
			child1Output[i] = nodes[0];
			child2Output[i] = nodes[1];
		}

		return new NeuralNetwork[] { new NeuralNetwork(child1Hidden, child1Output),
				new NeuralNetwork(child2Hidden, child2Output) };
	}

}
