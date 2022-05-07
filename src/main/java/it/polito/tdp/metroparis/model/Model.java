package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata,DefaultEdge> grafo;
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata,DefaultEdge>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		
		List<Fermata> fermate = dao.getAllFermate();
		Map<Integer,Fermata> fermateIdMap = new HashMap<Integer,Fermata>();
		for(Fermata f : fermate)
			fermateIdMap.put(f.getIdFermata(), f);
		
		Graphs.addAllVertices(this.grafo, fermate);
		
		/*
		//METODO 1: itero su ogni coppia di vertici (380000 query anche se semplici!!!!  619*619) n^2 query
		// su grafi piccoli conviene
		for(Fermata partenza : fermate) {
			for(Fermata arrivo : fermate) {
				if(dao.isFermateConnesse(partenza, arrivo)) { //esiste almeno una connessione tra partenza e arrivo
					this.grafo.addEdge(partenza, arrivo);
				}
			}
		}
		*/
		
		/*
		// METODO 2: dato ciascun vertice, trova i vertici ad esso adiacenti (619 query ma un po' più complicate :D !!!!) n query
		//Variante 2a: il dao restituisce un elenco di ID numerici
		
		// Nota: posso iterare su 'fermate' oppure su 'this.grafo.vertexSet()'
		for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(Integer id : idConnesse) {
//				Fermata arrivo = (fermata che possiede questo "id");
				Fermata arrivo=null;
				for(Fermata f : fermate) {
					if(f.getIdFermata()==id) {
						arrivo=f;
						break;
					}
				}
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		/*
		// METODO 2: dato ciascun vertice, trova i vertici ad esso adiacenti
		//Variante 2b: il dao restituisce un elenco oggetti Fermata
		
		for(Fermata partenza : fermate) {
			List<Fermata> arrivi = dao.getFermateConnesse(partenza);
			for(Fermata arrivo: arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		/*
		// METODO 2: dato ciascun vertice, trova i vertici ad esso adiacenti
		//Variante 2c: il dao restituisce un elenco di ID numerici che converto in oggetti tramite una Map<Integer,Fermata>
		// "Identity Map"
		for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(int id : idConnesse) {
				Fermata arrivo = fermateIdMap.get(id);
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		// METODO 3: faccio una sola query che mi restituisca le coppie
		// di fermate da collegare
		// (variante preferita: 3c - usare Identity Map
		List<CoppiaId> fermateDaCollegare = dao.getAllFermateConnesse();
		for(CoppiaId coppia : fermateDaCollegare) {
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));
		}
		
		System.out.println(this.grafo);
		System.out.println("Vertici = " + this.grafo.vertexSet().size());
		System.out.println("Archi = " +this.grafo.edgeSet().size());
		
		visitaGrafo(fermate.get(0));
	} 
	
	public void visitaGrafo(Fermata partenza) {
		GraphIterator<Fermata, DefaultEdge> visita = new DepthFirstIterator<>(this.grafo,partenza); //restituisce iteratore
		//                                           new BreadthFirstIterator<>(this.grafo,partenza);
		while(visita.hasNext()) {
			Fermata f = visita.next();
			System.out.println(f);
		}
	}

}
