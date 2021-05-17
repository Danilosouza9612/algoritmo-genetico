import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class AlgoritmoGenetico {
	private IntervaloBusca intervalo;
	private double taxaMutacao;
	private int nTorneio;
	private int generations;
	private Individuo[] individuos;
	private Function function;
	private int nElitism;
	private Crossover crossover;
	private int qtdFilhos;
	private GenerationOptions options;
	
	public AlgoritmoGenetico(
			IntervaloBusca intervalo,
			double taxaMutacao, 
			int nTorneio, 
			int population, 
			int generations, 
			int nElitism, 
			Function function, 
			Crossover crossover,
			GenerationOptions options,
			int qtdFilhos
		) {
		super();
		this.intervalo = intervalo;
		this.taxaMutacao = taxaMutacao;
		this.nTorneio = nTorneio;
		this.generations = generations;
		this.individuos = new Individuo[population];
		this.function = function;
		this.nElitism = nElitism;
		this.crossover = crossover;
		this.qtdFilhos = qtdFilhos;
		this.options = options;
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
		double t1 = random.nextDouble()*this.intervalo.getMaior();
		double t2 = -random.nextDouble()*this.intervalo.getMenor();
		return t1-t2;
	}
	
	public Individuo torneio() {
		Individuo[] selected = new Individuo[this.nTorneio];
		Random random = new Random();
		Individuo maior;
		for(int i=0; i<selected.length; i++) {
			selected[i]=this.individuos[random.nextInt(this.individuos.length)];
		}
		maior=selected[0];
		for(int i=1; i<selected.length; i++) {
			if(maior.compareFitness(selected[i], this.function)==-1) {
				maior=selected[i];
			}
		}
		return maior;
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
		Individuo[] novaGeracao;
		Individuo[] elite;
		for(int i=1; i<this.generations; i++) {
			if(i==20) {
				this.printPopulacao();
			}
			novaGeracao = new Individuo[this.individuos.length];
			elite = this.elitism();
			System.out.println(elite[0]+" para K = "+i);
			for(int j=0; j<elite.length; j++) {
				novaGeracao[j]=elite[j];
			}
			if(this.options == GenerationOptions.CROSSOVER_AND_MUTATE) {
				this.crossoverAndMutate(novaGeracao);
			}else {
				this.crossoverOrMutate(novaGeracao);
			}
			this.individuos = novaGeracao;
		}
		elite = this.elitism();
		System.out.println(elite[0]+" para K = "+this.generations);
	}
	
	private void crossoverAndMutate(Individuo[] novaGeracao) {
		Individuo a, b, novo;
		int count=0;
		for(int j=3; j<this.individuos.length; j++) {
			a = this.torneio();
			b = this.torneio();
			for(int k=0; k<this.qtdFilhos; k++) {
				if(j==this.individuos.length) break;
				novo = this.crossover.cross(a, b);
				if(count==this.taxaMutacao*100) {
					novo = this.mutation(novo);
					count=0;
				}else {
					count++;
				}
				novo.calculateFitness(this.function);
				novaGeracao[j]=novo;
				j++;
			}
			j--;
		}
	}
	
	private void crossoverOrMutate(Individuo[] novaGeracao) {
		Individuo a, b, novo;
		int count=0;
		for(int j=3; j<this.individuos.length; j++) {
			a = this.torneio();
			b = this.torneio();
			if(count<this.taxaMutacao*100) {
				for(int k=0; k<this.qtdFilhos; k++) {
					if(j==this.individuos.length) break;
					novo = this.crossover.cross(a, b);
					novo.calculateFitness(this.function);
					novaGeracao[j]=novo;
					j++;
					count++;
				}
				j--;
			}else {
				novo = this.mutation(a);
				novo.calculateFitness(this.function);
				novaGeracao[j]=novo;
				count=0;
			}
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
			System.out.println(individuo);
		}
	}
}
