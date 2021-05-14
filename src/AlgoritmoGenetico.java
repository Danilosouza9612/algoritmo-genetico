import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class AlgoritmoGenetico {
	private double taxaMutacao;
	private int nTorneio;
	private int generations;
	private Individuo[] individuos;
	private Function function;
	private int nElitism;
	
	public AlgoritmoGenetico(double taxaMutacao, int nTorneio, int population, int generations, int nElitism, Function function) {
		super();
		this.taxaMutacao = taxaMutacao;
		this.nTorneio = nTorneio;
		this.generations = generations;
		this.individuos = new Individuo[population];
		this.function = function;
		this.nElitism = nElitism;
		this.populate();
	}
	
	private void populate() {
		double[] x;
		for(int i=0; i<individuos.length; i++) {
			x = new double[5];
			for(int j=0; j<x.length; j++) {
				x[j] = this.generateNumber();
			}
			this.individuos[i] = new Individuo(x);
			this.individuos[i].calculateFitness(this.function);
		}
	}
	
	private double generateNumber() {
		Random random = new Random();
		double t1 = random.nextDouble()*100;
		double t2 = random.nextDouble()*100;
		return t1-t2;
	}
	
	public Individuo torneio() {
		Individuo[] selected = new Individuo[this.nTorneio];
		Random random = new Random();
		Individuo maior;
		for(int i=0; i<selected.length; i++) {
			selected[i]=this.individuos[random.nextInt(999)];
		}
		maior=selected[0];
		for(int i=1; i<selected.length; i++) {
			if(maior.compareFitness(selected[i], this.function)==-1) {
				maior=selected[i];
			}
		}
		return maior;
	}
	
	public Individuo uniformCrossover(Individuo a, Individuo b) {
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
	
	public Individuo mutation(Individuo a) {
		double newValue = this.generateNumber();
		double pos = new Random().nextInt(5);
		double[] x = new double[5];
		for(int i=0; i<x.length; i++) {
			if(i==pos) {
				x[i] = newValue;
			}else {
				x[i] = a.x[i];
			}
		}
		return new Individuo(x);
	}
	
	public void run() {
		Individuo a, b, novo;
		Individuo[] novaGeracao;
		Individuo[] elite;
		Random random = new Random();
		double sorteioProb;
		for(int i=1; i<this.generations; i++) {
			novaGeracao = new Individuo[this.individuos.length];
			elite = this.elitism();
			for(int j=0; j<elite.length; j++) {
				novaGeracao[j]=elite[j];
			}
			for(int j=3; j<this.individuos.length; j++) {
				a = this.torneio();
				b = this.torneio();
				novo = this.uniformCrossover(a, b);
				sorteioProb = random.nextDouble();
				if(sorteioProb<this.taxaMutacao) {
					novo = this.mutation(novo);
				}
				novo.calculateFitness(this.function);
				novaGeracao[j]=novo;
			}
			this.individuos = novaGeracao;
			this.printPopulacao();
		}
	}
	
	public Individuo[] elitism() {
		Individuo[] individuos = Arrays.copyOf(this.individuos, this.individuos.length);
		Individuo[] elite = new Individuo[this.nElitism];
		Arrays.sort(individuos, new Comparator<Individuo>() {
			@Override
			public int compare(Individuo o1, Individuo o2) {
				// TODO Auto-generated method stub
				return -o1.compareTo(o2);
			}
			
		});
		for(int i=0; i<this.nElitism; i++) {
			elite[i]=individuos[i];
		}
		return elite;
	}
	
	public void printPopulacao() {
		for(Individuo individuo : this.individuos) {
			System.out.println(individuo.getFitness());
		}
	}
}
