
public class Individuo implements Comparable<Individuo>{
	private static int IdCount=0;
	private int id;
	public double[] x;
	private Double fitness;

	public Individuo(double[] x) {
		super();
		this.x = x;
		this.id = IdCount;
		IdCount++;
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public void calculateFitness(Function function) {
		this.fitness = function.getFx(this);
	}
	
	public double getOrCalculateFitness(Function function) {
		if(this.fitness==null) {
			this.calculateFitness(function);
		}
		return this.fitness;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("F(");
		for(int i=0; i<x.length; i++) {
			sb.append(x[i]);
			if(i<(x.length-1)) {
				sb.append(", ");
			}
		}
		sb.append(")");
		sb.append(" = ");
		sb.append(this.fitness);
		return sb.toString();
	}
	
	public int compareFitness(Individuo individuo, Function function) {
		double f1 = this.getOrCalculateFitness(function);
		double f2 = individuo.getOrCalculateFitness(function);
		if(f1>f2) {
			return 1;
		}else if(f1<f2) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public int compareTo(Individuo individuo) {
		if(this.getFitness()>individuo.getFitness()) {
			return 1;
		}else if (this.getFitness()<individuo.getFitness()) {
			return -1;
		}
		return 0;
	}
}
