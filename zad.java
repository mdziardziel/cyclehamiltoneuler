import java.util.*;
import java.io.*;

class Graf {
	private final int elements;
	private final double nasycenie;
	private int[] ham;
	public int[][] macierz;
	private int krawedzie;
	
	public Graf(int elements, double nasycenie) {
		this.elements = elements;
		this.nasycenie = nasycenie;
		ham = new int[elements];
		macierz = new int[elements][elements];
		krawedzie = (int)(elements*(elements-1)/2*nasycenie);
	}
	
	public void read() {
		FileReader fr = null;
		String linia = "";

		// OTWIERANIE PLIKU:
		try {
			fr = new FileReader("plik.txt");
		}catch (FileNotFoundException e) {
		System.out.println("BLADD PRZY OTWIERANIU PLIKU!");
		System.exit(1);
		}
		BufferedReader bfr = new BufferedReader(fr);
		// ODCZYT KOLEJNYCH LINII Z PLIKU:
		try {

				for(int i = 0; i < elements; i++) {
					for(int j = 0; j < elements; j++) {
						linia = bfr.readLine();
						if(Integer.parseInt(linia)!= 0)
							macierz[i][j] = 1;	
						//System.out.println(macierz[i][j]);
					}
										
				}

			
		} catch (IOException e) {
			System.out.println("BLAD ODCZYTU Z PLIKU!");
			System.exit(2);
		}

		// ZAMYKANIE PLIKU
		try {
			fr.close();
		} catch (IOException e) {
			System.out.println("BLAD PRZY ZAMYKANIU PLIKU!");
			System.exit(3);
        }
	}
	
	private void wypelnij() {
		for(int i = 0; i < elements ; i++)
			ham[i] = i;	
	}
	
	private void mieszaj() {
		Random losowanie = new Random();
		for(int i = 0; i < elements; i++) {
			int pom = ham[i];
			int nr = losowanie.nextInt(elements);
			ham[i] = ham[nr];
			ham[nr] = pom;
		}		
	}
	
	private void dodaj(int x, int y) {
		macierz[x][y]++;
		macierz[y][x]++;
		macierz[x][x]++;
		macierz[y][y]++;
		krawedzie--;
	}
	
	private void uzupelnij_ham() {
		for(int i = 0; i < elements - 1; i++) 
			dodaj(ham[i],ham[i+1]);		
		dodaj(ham[0], ham[elements - 1]);			
	}
	
	private int nieparzysty(int start) {
		for(int i = start; i < elements; i++)
			if(macierz[i][i] % 2 == 1) 
				return i;
		return -1;
	}
	
	private void uzupelnij_losowo() {
		Random losowanie = new Random();
		int el1 = 0, el2 = 0, np;
		while(krawedzie > 0) {
			np = nieparzysty(0);
			do {
				el1 = losowanie.nextInt(elements);
				el2 = losowanie.nextInt(elements);
				if(np != -1) el1 = np;
			}while(el1 == el2); // || macierz[el1][el2] != 0
			dodaj(el1,el2);
		}	
		np = nieparzysty(0);
		if(np != -1) {
			int np2 = nieparzysty(np + 1);
			dodaj(np, np2);
		}
	}
	
	private void uzupelnij_losowo_v2() {
		Random losowanie = new Random();
		int el1 = 0, el2 = 0, np;
		while(krawedzie > 0) {
			np = nieparzysty(0);
			do {
				el1 = losowanie.nextInt(elements);
				el2 = losowanie.nextInt(elements);
				if(np != -1) el1 = np;
			}while(el1 == el2 || macierz[el1][el2] != 0 ); // wersja na tylko jedną krawędź między wierzchołkami
			dodaj(el1,el2);
		}	
		np = nieparzysty(0);
		if(np != -1) {
			int np2 = nieparzysty(np + 1);
			do {
				el1 = losowanie.nextInt(elements);
			}while(el1 == np || el1 == np2 || macierz[el1][np] != 0 || macierz[el1][np2] != 0 || macierz[np][el1] != 0 || macierz[np2][el1] != 0);
			dodaj(np, el1);
			dodaj(np2, el1);
		}
	}
	
	private void usun_przekatna() {
		for(int i = 0; i < elements; i++)
			macierz[i][i] = 0;
	}
	
	public void zbuduj_graf_eulera() {
		read();
		//wypelnij();
		//mieszaj();
		//uzupelnij_ham();
		//uzupelnij_losowo();
		//usun_przekatna();
	}
	
	public void wypisz_euler() {
		System.out.println();
		for(int i = 0; i < elements; i++)
			System.out.print(ham[i] + " > ");
		System.out.println();
	}
	
	public void wypisz_macierz() {
		System.out.println("Macierz sasiedztwa grafu: ");
		for(int i = 0; i < elements; i++) {
			for(int j = 0; j < elements; j++) {
				System.out.print(macierz[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void wypisz_wieszcholki() {
		int suma = 0;
		for(int i = 0; i < elements; i++) {
			for(int j = i +1; j < elements; j++) {
				int g = macierz[i][j];
				for(int l = 0; l < g; l++) {
					System.out.println(i + " " + j);
					System.out.println(j + " " + i);
					suma+=2;
				}
			}
		}
		System.out.println(suma);
		System.out.println(elements);
	}
	
	public int[][] get_macierz() {
		int[][] macierz_copy = new int[elements][elements];
		for(int i = 0; i < elements; i++)
			for(int j = 0; j < elements; j++)
				macierz_copy[i][j] = macierz[i][j];
		return macierz_copy;
	}
}

class Euler {
	private final int elements;
	private int[][] macierz;
	private ArrayList<Integer> wierzcholki = new ArrayList<Integer>();
	
	public Euler(int elements, int[][] macierz) {
		this.elements = elements;
		this.macierz = macierz;
	}
		
	private void cykl(int v) { //ArrayList<Integer>
		
		for(int i = 0; i < elements; i++) {
			if( i != v && macierz[v][i] != 0) {
				macierz[v][i]--; 
				macierz[i][v]--; 
				cykl(i);
			}
		}
		wierzcholki.add(v);
		//return wierzcholki;
	}
	
	public ArrayList<Integer> cykl() {
		cykl(0);
		return wierzcholki;
	}
	
	public void print() {
		System.out.println("Cykl Eulera: ");
		for(int v : wierzcholki)
			System.out.print((v+1) + " > ");
		System.out.println();
	}
}

class Hamilton {
	private final int elements;
	private int[][] macierz;
	private ArrayList<Integer> wierzcholki = new ArrayList<Integer>();
	private boolean done = false;
	
	public Hamilton(int elements, int[][] macierz) {
		this.elements = elements;
		this.macierz = macierz;
	}
	
	
	private void cykl(int v) {
		wierzcholki.add(v);
		macierz[v][v] = -1; 
		for(int i = 0; i < elements; i++) 
			if( i != v && macierz[i][i] != -1 && macierz[v][i] != 0) 				
				cykl(i);		
		if(wierzcholki.size() == elements && macierz[v][0] != 0) {
			done = true;
		}				 
		else if(!done) {
			wierzcholki.remove((Integer)v);
			macierz[v][v] = 0;
		}				
	}
	
	public ArrayList<Integer> cykl() {	
		cykl(0);
		return wierzcholki;
	}
	
	public void print() {
		System.out.println("Cykl Hamiltona: ");
		for(int v : wierzcholki)
			System.out.print(v + " > ");
		System.out.println();
	}
}

class Hamilton_wiele {
	private final int elements;
	private int[][] macierz;
	private ArrayList<Integer> wierzcholki = new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>> lista = new ArrayList<ArrayList<Integer>>();
	
	public Hamilton_wiele(int elements, int[][] macierz) {
		this.elements = elements;
		this.macierz = macierz;
	}
	
	private void cykle(int v) {
		wierzcholki.add(v);
		macierz[v][v] = -1; 
		for(int i = 0; i < elements; i++) 
			if( i != v && macierz[i][i] != -1 && macierz[v][i] != 0)
				cykle(i);								
		if(wierzcholki.size() == elements && macierz[v][0] != 0) {
			ArrayList<Integer> x = new ArrayList<Integer>();
			for(int y : wierzcholki) x.add(y);
			lista.add(x);
		}				 
		macierz[v][v] = 0;	
		wierzcholki.remove((Integer)v);			
	}
	
	public ArrayList<ArrayList<Integer>> cykle() {	
		cykle(0);
		return lista;
	}
	
	public void print() {
		int ll = 0;
		System.out.println("Cykle Hamiltona: ");
		for(ArrayList<Integer> v : lista) {
			for(int w : v)
				System.out.print((w+1) + " > ");
			System.out.println();
		}
		System.out.println();
	}		
}

class Test {
	private final int elements;
	private final double nasycenie;
	
	public Test(int elements, double nasycenie) {
		this.elements = elements;
		this.nasycenie = nasycenie;
	}
	
	public void tworzenie_grafu() {
		Graf graf = new Graf(elements, nasycenie);
		graf.zbuduj_graf_eulera();
		graf.wypisz_euler();
		graf.wypisz_macierz();
		
		Euler euler = new Euler(elements, graf.macierz);
		euler.cykl();
		euler.print();
		
		Hamilton hamilton = new Hamilton(elements, graf.get_macierz());
		hamilton.cykl();
		hamilton.print();
		
		Hamilton_wiele hamiltony = new Hamilton_wiele(elements, graf.get_macierz());
		hamiltony.cykle();
		hamiltony.print();
	}
	
	public void euler() {
		long start;
		Graf graf = new Graf(elements, nasycenie);
		graf.zbuduj_graf_eulera();
		//graf.wypisz_euler();
		graf.wypisz_macierz();
		start = System.nanoTime();
		Euler euler = new Euler(elements, graf.get_macierz());
		euler.cykl();
		System.out.println(System.nanoTime()-start);
		euler.print();
	}
	
	public void hamilton() {
		long start;
		Graf graf = new Graf(elements, nasycenie);
		graf.zbuduj_graf_eulera();
		//graf.wypisz_euler();
		//graf.wypisz_macierz();
		start = System.nanoTime();
		Hamilton hamilton = new Hamilton(elements, graf.get_macierz());
		hamilton.cykl();
		System.out.println(System.nanoTime()-start);
		//hamilton.print();
	}
	
	public void hamilton_wiele() {
		long start;
		Graf graf = new Graf(elements, nasycenie);
		graf.zbuduj_graf_eulera();
		//graf.wypisz_euler();
		//graf.wypisz_macierz();
		start = System.nanoTime();
		Hamilton_wiele hamiltony = new Hamilton_wiele(elements, graf.get_macierz());
		hamiltony.cykle();
		System.out.println(System.nanoTime()-start);
		hamiltony.print();
	}
	
}

class Pomiary {
	private final int start_first_30, start_first_70, start_second, step_first_30, step_first_70, step_second;
	long start;
	
	public Pomiary(int start_first_30, int start_first_70, int start_second, int step_first_30, int step_first_70, int step_second) {
		this.start_first_30 = start_first_30;
		this.start_first_70 = start_first_70;
		this.start_second = start_second;
		this.step_first_30 = step_first_30;
		this.step_first_70 = step_first_70;
		this.step_second = step_second;
	}
	
	public void first() throws FileNotFoundException{
		for(int j = 0; j < 15; j++) {
			PrintWriter zapis30 = new PrintWriter("dane30/seria_"+j+"_dane.txt");
			for(int i = start_first_30; i < start_first_30 + step_first_30*15; i+=step_first_30) {
				zapis30.println(i);
				System.out.println(j + " " +i + " 30");
				Graf graf = new Graf(i, 0.3);
				graf.zbuduj_graf_eulera();
				System.out.println("Euler");
				Euler euler = new Euler(i, graf.get_macierz());
				start = System.nanoTime();
				euler.cykl();
				zapis30.println(System.nanoTime()-start);
				//euler.print();
				System.out.println("Hamilton");
				Hamilton hamilton = new Hamilton(i, graf.get_macierz());
				start = System.nanoTime();
				hamilton.cykl();
				zapis30.println(System.nanoTime()-start);
				//hamilton.print();
			}
			zapis30.close();
			PrintWriter zapis70 = new PrintWriter("dane70/seria_"+j+"_dane.txt");
			for(int i = start_first_70; i < start_first_70 + step_first_70*15; i+=step_first_70) {
				zapis70.println(i);
				System.out.println(j + " " +i + " 70");
				Graf graf = new Graf(i, 0.7);
				graf.zbuduj_graf_eulera();
				System.out.println("Euler");
				Euler euler = new Euler(i, graf.get_macierz());
				start = System.nanoTime();
				euler.cykl();
				zapis70.println(System.nanoTime()-start);
				System.out.println("Hamilton");
				Hamilton hamilton = new Hamilton(i, graf.get_macierz());
				start = System.nanoTime();
				hamilton.cykl();
				zapis70.println(System.nanoTime()-start);
			}
			zapis70.close();
		}
	}
	
	public void second() throws FileNotFoundException{
		for(int j = 0; j < 15; j++) {
			PrintWriter zapis50 = new PrintWriter("dane50/seria_"+j+"_dane.txt");
			for(int i = start_second; i < start_second + step_second*15; i+=step_second) {
				System.out.println(j + " " +i + " 50");
				zapis50.println(i);
				Graf graf = new Graf(i, 0.5);
				graf.zbuduj_graf_eulera();
				
				Hamilton_wiele hamiltony = new Hamilton_wiele(i, graf.get_macierz());
				start = System.nanoTime();
				hamiltony.cykle();
				zapis50.println(System.nanoTime()-start);
				zapis50.println(" ");
			}
			zapis50.close();
		}	
	}
	
}


class zad {
	public static void main(String[] args) throws FileNotFoundException{
		//Scanner odczyt = new Scanner(System.in);
		//int ile = odczyt.nextInt();
		int ile = 6;
		Test test = new Test(ile, 0.3);
		test.hamilton_wiele();
		test.euler();
		//test.tworzenie_grafu();
		//Pomiary pomiary = new Pomiary(10,10,4,6,6,1);
		//pomiary.first();
		//pomiary.second();
		////Graf graf = new Graf(10, 0.5);
		//graf.zbuduj_graf_eulera();
		//graf.wypisz_wieszcholki();
		
	}
}