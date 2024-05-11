package genetic_neural;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import breakout.Breakout;
import breakout.BreakoutGameController;
import breakout.BreakoutGenetic;
import pacman.PacmanGenetic;
import utils.Commons;

public class Test {
	public static void main(String[] args) {

		BreakoutGenetic bg = new BreakoutGenetic(15784533, 20, Commons.BREAKOUT_STATE_SIZE, 50,
				Commons.BREAKOUT_NUM_ACTIONS + 1);
		bg.search();

		/* O seguinte código serve para correr o melhor Breakout obtido */

//		BreakoutGameController bestBreakout = checkbest();
//		System.out.println(bestBreakout.getFitness() + "");
//		Breakout bestb = new Breakout(bestBreakout,bestBreakout.getSeed() );
	}

	private static BreakoutGameController checkbest() {
		try {

			FileInputStream file = new FileInputStream(new File("bestBreakout.txt"));
			ObjectInputStream oi = new ObjectInputStream(file);
			BreakoutGameController bgc = (BreakoutGameController) oi.readObject();

			oi.close();
			file.close();
			return bgc;

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
