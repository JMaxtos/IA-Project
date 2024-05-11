package genetic_neural;

import java.io.Serializable;
import java.util.Random;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private double bias;
	private double[] weights;
	private Random random;

	// Construtor
	public Node(double[] weights, double bias) {
		this.bias = bias;
		this.weights = weights;
		random = new Random();
	}

	// Getters e Setters
	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double getBias() {
		return bias;
	}

	// Função que calcula os outputs da camada intermédia
	public double forward(int[] inputs) {

		double result = 0.0;
		for (int i = 0; i < inputs.length; i++) {
			result += inputs[i] * weights[i];
		}
		result += bias;
		return sigmoid(result);
	}

	// Função que calcula os outputs finais
	public double forward(double[] inputs) {

		double result = 0.0;
		for (int i = 0; i < inputs.length; i++) {
			result += inputs[i] * weights[i];
		}
		result += bias;
		return sigmoid(result);
	}

	// Função sigmoid
	private double sigmoid(double input) {
		return 1 / (1 + Math.exp(-input));
	}

	// Função que muta os nós (neurónios)
	public Node mutate(double mutate_rate) {
		double[] newWeights = new double[weights.length];
		double newbias = bias;
		for (int i = 0; i < weights.length; i++) {
			if (random.nextDouble() <= mutate_rate) {

				newWeights[i] = weights[i] + (random.nextDouble() * 2 - 1);

			} else {
				newWeights[i] = weights[i];
			}
		}

		if (random.nextDouble() <= mutate_rate) {
			newbias = bias + (random.nextDouble() * 2 - 1);
		}
		return new Node(newWeights, newbias);
	}

	// Função que realiza o two-point crossover entre 2 nós (neurónios)
	public Node[] crossover(Node othernode) {

		int crossoverPoint1 = random.nextInt(weights.length);

		int crossoverPoint2 = random.nextInt(weights.length);

		if (crossoverPoint1 > crossoverPoint2) {
			int tmp = crossoverPoint1;
			crossoverPoint1 = crossoverPoint2;
			crossoverPoint2 = tmp;
		}

		int remainingLength1 = crossoverPoint2 - crossoverPoint1;
		int remainingLength2 = weights.length - crossoverPoint2;
		double[] weights1 = new double[weights.length];
		double[] weights2 = new double[weights.length];

		// Realização do crossover dos pesos entre o início do array e o primeiro ponto
		// de crossover, e depois do primeiro ponto de corssover até ao segundo ponto de
		// crossover
		System.arraycopy(this.weights, 0, weights1, 0, crossoverPoint1);
		System.arraycopy(othernode.getWeights(), crossoverPoint1, weights1, crossoverPoint1, remainingLength1);
		System.arraycopy(this.weights, crossoverPoint2, weights1, crossoverPoint2, remainingLength2);

		System.arraycopy(othernode.getWeights(), 0, weights2, 0, crossoverPoint1);
		System.arraycopy(this.weights, crossoverPoint1, weights2, crossoverPoint1, remainingLength1);
		System.arraycopy(othernode.getWeights(), crossoverPoint2, weights2, crossoverPoint2, remainingLength2);

		double bias1 = 0.0;
		double bias2 = 0.0;

		// Bias podem ou não serem trocadas
		if (random.nextBoolean()) {
			bias1 = bias;
			bias2 = othernode.getBias();
		} else {
			bias1 = othernode.getBias();
			bias2 = bias;

		}

		return new Node[] { new Node(weights1, bias1), new Node(weights2, bias2) };
	}

}
