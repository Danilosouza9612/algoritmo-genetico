import java.util.Random;

public class UniformCrossover implements Crossover{

	@Override
	public Individuo cross(Individuo a, Individuo b) {
		// TODO Auto-generated method stub
		Random random = new Random();
		double[] x = new double[5];
		int sorteio;
		for(int i=0; i<x.length; i++) {
			sorteio = random.nextInt(2);
			if(sorteio==0) {
				x[i]=a.x[i];
			}else {
				x[i]=b.x[i];
			}
		}
		return new Individuo(x);
	}

}
