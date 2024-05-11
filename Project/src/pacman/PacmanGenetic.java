package pacman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import genetic_neural.GeneticAlgorithm;

public class PacmanGenetic extends GeneticAlgorithm {

	private final int POPULATION_SIZE = 100;
	private final int NUM_GENERATIONS = 1000;
	private final double MUTATION_RATE = 0.02;
	private static final String filename = "bestPacman.txt";
	private ArrayList<PacmanController> population;

	// Construtor
	public PacmanGenetic(int randomSeed, int numberOfParents, int inputLength, int hiddenLength, int outputLength) {
		super(randomSeed, numberOfParents, inputLength, hiddenLength, outputLength);
		generatePopulation();
	}

	// Escolha dos pais para a mutação
	private PacmanController selectParent() {
		return population.get(super.random.nextInt(super.numberOfParents));
	}

	// Geração de uma população de controladores do Pacman
	@Override
	protected void generatePopulation() {
		this.population = new ArrayList<>(POPULATION_SIZE);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(new PacmanController(super.inputLength, super.outputLength, super.hiddenLength));
		}

	}

	// Função que treina e evolui as redes neuronais
	@Override
	public void search() {
		int timesNoEvol = 0;
		PacmanController best = new PacmanController(super.inputLength, super.outputLength, super.hiddenLength);
		ArrayList<Double> lastFiveGenerations = new ArrayList(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0));

		for (int i = 0; i < NUM_GENERATIONS; i++) {
			System.out.println("Geração" + i);

			for (int j = 0; j < POPULATION_SIZE; j++) {
				// Avaliação do fitness do controlador
				PacmanBoard b = new PacmanBoard(population.get(j), false, super.gameSeed);
				b.runSimulation();
				population.get(j).setFitness(b.getFitness());

				if (best.getFitness() < population.get(j).getFitness()) {
					best = population.get(j);
					best.setSeed(gameSeed);
				}
			}

			lastFiveGenerations.remove(0);
			lastFiveGenerations.add(population.get(0).getFitness());

			// Ordenar por maior Fitness
			population.sort((a, b) -> (int) (b.getFitness() - a.getFitness()));
			System.out.println("\n Fitness: " + population.get(0).getFitness());

			// Nova Geração com os melhores pais da geração anterior
			ArrayList<PacmanController> newgeneration = new ArrayList<>(population.subList(0, numberOfParents));

			for (int k = 0; k < (POPULATION_SIZE - numberOfParents) / 2; k++) {
				PacmanController parent1;
				PacmanController parent2;

				PacmanController[] childs;
				if (k == 0) {
					parent1 = population.get(0);
					parent2 = population.get(1);

					childs = parent1.crossover(parent2);
				} else if (k < 3) {
					// Criação de filhos através dos pais mutados
					childs = new PacmanController[] { selectParent(), selectParent() };
				} else {
					parent1 = selectParent();
					parent2 = selectParent();

					childs = parent1.crossover(parent2);
				}
				// Incremento da percentagem de mutação caso
				// não exista uma alteração de fitness
				double newmutation_rate = MUTATION_RATE + timesNoEvol * 0.002;

				newgeneration.add(childs[0].mutate(newmutation_rate));
				newgeneration.add(childs[1].mutate(newmutation_rate));
			}

			population = newgeneration;

			if (isFitnessEqual(lastFiveGenerations) == true) {
				timesNoEvol++;
			} else {
				timesNoEvol = 0;
			}

			if (i != 0 && i % 200 == 0) {
				super.gameSeed = super.random.nextInt(Integer.MAX_VALUE);
			}
		}

		System.out.println("Melhor Fitness:" + best.getFitness() + " Seed:" + super.gameSeed);
		checkbest((PacmanController) best, filename);
		System.out.println("Para visualizar o melhor Pacman gerado. Prima Enter: ");
		Scanner s = new Scanner(System.in);

		if (s.nextLine() != null) {
			Pacman b = new Pacman(best, true, super.gameSeed);
		}
		s.close();
	}

	// Função que verifica se o melhor controlador gerado é melhor que o já guardado
	// no ficheiro anteriormente
	protected void checkbest(PacmanController best, String filename) {
		try {

			FileInputStream file = new FileInputStream(new File(filename));
			ObjectInputStream oi = new ObjectInputStream(file);
			PacmanController pacmanC = (PacmanController) oi.readObject();

			if (pacmanC.getFitness() < best.getFitness()) {
				writeBest(best, filename);
			}
			oi.close();
			file.close();

		} catch (FileNotFoundException e) {
			writeBest(best, filename);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Função que escreve o melhor controlador de todos no ficheiro
	private void writeBest(PacmanController best, String filename) {
		try {

			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(file);
			o.writeObject(best);
			o.close();
			file.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
