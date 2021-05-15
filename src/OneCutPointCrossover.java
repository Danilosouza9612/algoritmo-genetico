import java.util.Random;

public class OneCutPointCrossover implements Crossover{

	@Override
	public Individuo cross(Individuo a, Individuo b) {
		Random random = new Random();
		int sorteio = random.nextInt(5);
		double[] x = new double[5];
		for(int i=0; i<x.length; i++) {
			if(i<=sorteio) {
				x[i]=a.x[i];
			}else {
				x[i]=b.x[i];
			}
		}
		return new Individuo(x);
	}

}
