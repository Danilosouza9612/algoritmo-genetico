
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Function function = new Function() {

			@Override
			public double getFx(Individuo individuo) {
				// TODO Auto-generated method stub
				return individuo.x[0] + 2*individuo.x[1] - Math.pow(individuo.x[2], 2) - Math.cos(individuo.x[3]); 
			}
			
		};
		
		AlgoritmoGenetico algoritmo = new AlgoritmoGenetico(0.1, 2, 1000, 1000, 3, function, new OneCutPointCrossover(), GenerationOptions.CROSSOVER_AND_MUTATE, 2);
		algoritmo.run();
	}
}
