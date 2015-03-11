/*
Cette classe se charge
 */
package MoveController;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>{
	public final double x;
	public final double y;
	public ArrayList<Edge> adjacencies;
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() { return x+"-"+y; }

	@Override
	public int compareTo(Vertex other){
		return Double.compare(minDistance, other.minDistance);
	}

}


class Edge{
	public final Vertex target;
	public final double weight;
	public Edge(Vertex argTarget, double argWeight, int i, int j){
		if(argTarget==null) {
			System.out.println("arg problem "+i+" - "+j);
		}
		target = argTarget;
		weight = argWeight; }
}

public class Dijkstra{
	private static void computePaths(Vertex source){
		source.minDistance = 0.0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			// Visit each edge exiting u
			for (Edge e : u.adjacencies){
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					System.out.println();
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU ;
					v.previous = u;
					vertexQueue.add(v);
				}

			}
		}

	}

	private static List<Vertex> getShortestPathTo(Vertex target){
		List<Vertex> path = new ArrayList();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}



	private static Vertex[][] makeVertexMap(int size, double shift){
		Vertex[][] vertexArray = new Vertex[size][size];
		Vertex v;
		String key;

		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				vertexArray[i][j]=new Vertex(i+shift,j+shift);
			}
		}

		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				vertexArray[i][j].adjacencies = new ArrayList();

				if(i+1<size) vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i+1][j], 1, i+1, j));
				if(i>0) vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i-1][j], 1, i-1, j));
				if(j+1<size) vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j+1], 1, i, j+1));
				if(j>0) vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j-1], 1, i, j-1));

			}
		}


		return vertexArray;
	}



	private static void linkVertexMaps(Vertex[][] v1, Vertex[][] v2, int size1, List<Integer> occupied){
		double sqrt2_2 = 0.7071;
		for(Integer i=0 ; i<size1 ; i++){
			for(Integer j=0 ; j<size1 ; j++){
				if(occupied.contains(i*size1+j)){
					//System.out.println("Case "+i+"-"+j+"occupee");
					continue;
				}
				v1[i][j].adjacencies.add(new Edge(v2[i][j],sqrt2_2,i,j));
				v1[i][j].adjacencies.add(new Edge(v2[i+1][j],sqrt2_2,i,j));
				v1[i][j].adjacencies.add(new Edge(v2[i][j+1],sqrt2_2,i,j));
				v1[i][j].adjacencies.add(new Edge(v2[i+1][j+1],sqrt2_2,i,j));

				v2[i][j].adjacencies.add(new Edge(v1[i][j],sqrt2_2,i,j));
				v2[i+1][j].adjacencies.add(new Edge(v1[i][j],sqrt2_2,i,j));
				v2[i][j+1].adjacencies.add(new Edge(v1[i][j],sqrt2_2,i,j));
				v2[i+1][j+1].adjacencies.add(new Edge(v1[i][j],sqrt2_2,i,j));
			}
		}
	}


	private static List<Position> getShortestPath(int a, int b, List<Integer> occupied){
		Vertex[][] center = makeVertexMap(12,0.5);
		Vertex v_start =center[a%12][a/12];
		Vertex v_end = center[b%12][b/12];
		Vertex[][] corner = makeVertexMap(13,0);

		linkVertexMaps(center,corner,12,occupied);
		computePaths(v_start); // run Dijkstra
		List<Vertex> path = getShortestPathTo(v_end);
		return vertex_to_position(path);
	}

	public static ArrayList<Integer> getOccupied(String fen){
		ArrayList<Integer> liste = new ArrayList();
		ArrayList<ArrayList<Character>> chessBoard = new ArrayList<ArrayList<Character>>();
		ArrayList<ArrayList<Character>> fullBoard = new ArrayList<ArrayList<Character>>();
		chessBoard = getTabFromFen(fen);

		// remove captured piece to tmp fen
		fen = fen.substring(0, fen.indexOf(' '));
		String tmp="rnbqkbnr/pppppppp/PPPPPPPP/RNBQKBNR";
		int a = 0;
		char c = ' ';
		for(int i = 0; i < fen.length(); i++){
			c = fen.charAt(i);
			a = 0;
			do{
				if(c != '/' && tmp.charAt(a) == c){
					tmp = tmp.substring(0, a) + '.' + tmp.substring(a + 1);
					c='Z';
				}
				else{
					a++;
				}
			}while(c != 'Z' && a < tmp.length());
		}
		
		// add tmp fen to sides
		String[] initFen=tmp.split("/");
		for(int i = 0; i < 8; i++){
			fullBoard.add(new ArrayList<Character>());
			for(int j = 0; j < 12; j++)
				fullBoard.get(i).add('.');
			fullBoard.get(i).set(0, initFen[0].charAt(i));
			fullBoard.get(i).set(1, initFen[1].charAt(i));
			fullBoard.get(i).set(10, initFen[2].charAt(initFen[2].length() - i - 1));
			fullBoard.get(i).set(11, initFen[3].charAt(initFen[3].length() - i - 1));
		}

		// add current chessboard
		for(int i = 0; i < 8; i++){

			for(int j = 0; j < 12; j++){
				if(j == 0 || j == 1 || j == 10 || j == 11){
					//fullBoard.get(i).set(j,'.');
				}
				else{
					fullBoard.get(i).set(j,chessBoard.get(i).get(j - 2));
				}
			}
		}
		printBoard(fullBoard);
		return liste;
	}

	public static ArrayList<ArrayList<Character>> getTabFromFen(String fen){
		ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();
		String[] alines = fen.split("/");
		ArrayList<String> lines = new ArrayList();
		int k = 0, v = 0;

		for(int i=0;i<8;i++){
			if(i == 7)
				lines.add(alines[i].substring(0, alines[i].indexOf(' ')));
			else
				lines.add(alines[i]);
		}

		for(int i = 0; i < 8; i++){
			board.add(new ArrayList<Character>());
			for(int j = 0; j < 8 ; j++)
				board.get(i).add('.');
		}

		for(String s : lines){
			v = 0;
			char[] a = s.toCharArray();
			for(int i = 0; i < a.length; i++){
				if(Character.isDigit(a[i])){
					v += Character.getNumericValue(a[i]);
				}
				else{
					board.get(k).set(v, a[i]);
					v++;
				}
			}
			k++;
		}
		return board;
	}

	private static void printBoard(ArrayList<ArrayList<Character>> board){
		for(int i = 0; i < board.size(); i++){
			for(int j = 0; j < board.get(0).size(); j++){
				System.out.print(board.get(i).get(j));
			}
			System.out.println();
		}
	}

	private static List<Position> vertex_to_position(List<Vertex> l){
		List<Position> r = new ArrayList();
		for(Vertex v : l){
			r.add(new Position(v.x,v.y));
		}
		return r;
	}

	public static void main(String[] args){

		/*List<Integer> cases_prises = new ArrayList();
		int start_x = 2;
		int start_y = 2;
		int end_x = 6;
		int end_y = 6;
		int start = start_x*12+start_y;
		int end = end_x*12+end_y;

		cases_prises.add(3*12+3);
		List<Position> path = getShortestPath(start, end, cases_prises);
		System.out.println("Path: " + path.toString());*/
		//String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		//String fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		//String fen = "R1b1k2n/ppp5/4K2p/8/3p4/8/Pq6/3Q1Bb1 w KQkq - 0 5";
		String fen = "8/3k4/8/4K3/8/8/8/8 w KQkq - 0 5";
		ArrayList<Integer> test = getOccupied(fen);
	}
}

