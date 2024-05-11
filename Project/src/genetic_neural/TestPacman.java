package genetic_neural;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import pacman.Pacman;
import pacman.PacmanController;
import pacman.PacmanGenetic;
import utils.Commons;

public class TestPacman {

	private static PacmanController checkbest() {
		try {

			FileInputStream file = new FileInputStream(new File("bestPacman.txt"));
			ObjectInputStream oi = new ObjectInputStream(file);
			PacmanController pc = (PacmanController) oi.readObject();

			oi.close();
			file.close();
			return pc;

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		PacmanGenetic pg = new PacmanGenetic(15784533, 20, Commons.PACMAN_STATE_SIZE, 200,
				Commons.PACMAN_NUM_ACTIONS + 1);
		pg.search();

		/* O seguinte código serve para correr o melhor Pacman obtido */
//		PacmanController bestPacman = checkbest();
//		System.out.println(bestPacman.getFitness() + "");
//		Pacman pacman = new Pacman(bestPacman, true, bestPacman.getSeed());
	}
}
