package MoveController;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>{

	public final double x;
        public boolean occupied=false;
	public final double y;
	public ArrayList<Edge> adjacencies;
	public double minDistance = Double.POSITIVE_INFINITY;
	public Vertex previous;

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x+"-"+y;
	}

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
		weight = argWeight;
	}
}

public class Dijkstra{

	private static void computePaths(Vertex source){

		source.minDistance = 0.0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			// Visit each edge exiting u
			for (Edge e : u.adjacencies){
				Vertex v = e.target;
				
                                double weight;
                                //Ajout if Bastien
                                if(v.occupied){
                                    weight = Double.POSITIVE_INFINITY;
                                    //System.out.println(v.x+" "+v.y+" is occupied");
                                }
                                        else weight = e.weight;
				
                                double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					//System.out.println();
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU ;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	private static List<Vertex> getShortestPathTo(Vertex target){

		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}

        private static void markOccupied(Vertex[][] v1, List<Integer> occupied){
            
        }
        
	// Cree un quadrillage de Vertex reliés entre eux
	private static Vertex[][] makeVertexMap(int size, double shift){

		Vertex[][] vertexArray = new Vertex[size][size];
		Vertex v;
		String key;

		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){vertexArray[i][j]=new Vertex(i+shift,j+shift);
			}
		}

		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				vertexArray[i][j].adjacencies = new ArrayList<Edge>();
                                if(i+1<size)
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i+1][j], 1, i+1, j));
				if(i>0)
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i-1][j], 1, i-1, j));
				if(j+1<size)
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j+1], 1, i, j+1));
				if(j>0)
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j-1], 1, i, j-1));
			}
		}
		return vertexArray;
	}


	// Cree un quadrillage de Vertex reliés entre eux
	private static Vertex[][] makeVertexMap(int size, double shift, List<Integer> occupied){

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
				vertexArray[i][j].adjacencies = new ArrayList<Edge>();

				if(i+1<size && !occupied.contains((i+1)*size+j))
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i+1][j], 1, i+1, j));
				if(i>0 && !occupied.contains((i-1)*size+j))
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i-1][j], 1, i-1, j));
				if(j+1<size && !occupied.contains(i*size+j+1))
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j+1], 1, i, j+1));
				if(j>0 && !occupied.contains(i*size+j-1))
					vertexArray[i][j].adjacencies.add(new Edge(vertexArray[i][j-1], 1, i, j-1));
			}
		}
		return vertexArray;
	}

	// Relie 2 quadrillages de Vertex (1 quadrillage pour les arretes, 1 quadrillages pour les faces)
	private static void linkVertexMaps(Vertex[][] v1, Vertex[][] v2, int size1){

		//Estimation de sqrt(2)/2, la distance entre une arete et le milieu d'une face
		double sqrt2_2 = 0.70710678;

		for(Integer i=0 ; i<size1 ; i++){
			for(Integer j=0 ; j<size1 ; j++){
				/*
                                // Ne pas relier au maillage les cases occupées
				if(occupied.contains(i*size1+j)){
					//System.out.println("Case "+i+"-"+j+"occupee");
					//v1[i][j].adjacencies.clear();
					continue;
				}
                                */
                            
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

	public static Path getShortestPath(Position a, Position b, String fen){

		List<Position> lpos=null;
		try{
			lpos = getShortestPath(a.toInteger(), b.toInteger(), getOccupied(fen));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new Path(lpos);
	}


	private static List<Position> getShortestPath(int a, int b, List<Integer> occupied){

		Vertex[][] center = makeVertexMap(12,0.5,occupied);
		Vertex v_start =center[a%12][a/12];
		Vertex v_end = center[b%12][b/12];
		Vertex[][] corner = makeVertexMap(13,0);

		linkVertexMaps(center,corner,12);
                for(Integer i=0 ; i<12 ; i++){
			for(Integer j=0 ; j<12 ; j++){
				// Ne pas relier au maillage les cases occupées
				if(occupied.contains(i*12+j)){
					//System.out.println("Case "+i+"-"+j+"occupee");
					center[i][j].occupied=true;
                                }
                        }
                }
		computePaths(v_start); // run Dijkstra
		List<Vertex> vpath = getShortestPathTo(v_end);
                List<Position> path = vertex_to_position(vpath) ;
                //path = correctPath(path);
		return path;
	}

	private static List<Position> vertex_to_position(List<Vertex> l){
		List<Position> r = new ArrayList<Position>();
		for(Vertex v : l){
			r.add(new Position(v.x,v.y));
		}
		return r;
	}

	// reçoit une string fen et retourne un tableau de char représentant l'échiquier simple
	public static ArrayList<ArrayList<Character>> getTabFromFen(String fen){

		ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();
		String[] alines = fen.split("/");
		ArrayList<String> lines = new ArrayList<String>();
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

	// reçoit une fen et retourne un l'échiquier sous forme de tableau avec en plus les pièces capturées sur les côtés
	public static ArrayList<ArrayList<Character>> getFullBoard(String fen){

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
		return fullBoard;
	}

	// affiche le tableau représentant l'échiquier
	private static void printBoard(ArrayList<ArrayList<Character>> board){
		for(int i = 0; i < board.size(); i++){
                    System.out.print(i);
			for(int j = 0; j < board.get(0).size(); j++){
				System.out.print(board.get(i).get(j)+" ");
			}
			System.out.println();
		}
                for(int j = 0; j < board.get(0).size(); j++)
                {
                    System.out.print(" "+j%10);
                }
                System.out.println();
	}

	// reçoit une fen et retourne une liste des cases occupées sous la forme x*12+y
	public static ArrayList<Integer> getOccupied(String fen){
                
		ArrayList<ArrayList<Character>> fullBoard = getFullBoard(fen);
		ArrayList<Integer> liste = new ArrayList<Integer>();
		
                for(int i = fullBoard.size() - 1; i >= 0; i--){
			for(int j = 0; j < fullBoard.get(0).size(); j++){
				if(!fullBoard.get(i).get(j).equals('.')){
					//System.out.println(j+" "+(fullBoard.size()-i-1+" - case #"+(j+12*(fullBoard.size()-i-1))));
					liste.add(j+12*(fullBoard.size()-i-1));
				}
			}
		}
                
                /*
                for(int i = 0; i < 12 ; i++){
			for(int j = 0; j < 8; j++){
				if(!fullBoard.get(i).get(j).equals('.')){
					System.out.println(i+" "+j);
					liste.add(j*12+i);
				}
			}
		}
                */
		return liste;
	}

	public static ArrayList<Integer> getOccupied(ArrayList<ArrayList<Character>> fullBoard){

		ArrayList<Integer> liste = new ArrayList<Integer>();
		for(int i = fullBoard.size() - 1; i >= 0; i--){
			for(int j = 0; j < fullBoard.get(0).size(); j++){
				if(!fullBoard.get(i).get(j).equals('.')){
					//liste.add(j*12+(fullBoard.size()-i-1));
					liste.add(i*12+j);
				}
			}
		}
		return liste;
	}



	// Retourne board1 - board2
	public static ArrayList<ArrayList<Character>> substrctBoards(ArrayList<ArrayList<Character>> board1, ArrayList<ArrayList<Character>> board2){

		for(int i = 0; i < board1.size(); i++){
			for(int j = 0; j < board1.get(0).size(); j ++){
				if(board1.get(i).get(j).equals(board2.get(i).get(j))){
					board1.get(i).set(j,'.');
				}
			}
		}
		return board1;
	}

	public static ArrayList<Integer> getPossibleFrom(ArrayList<ArrayList<Character>> board, Character piece){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < board.size(); i++){
			for(int j = 0; j < board.get(0).size(); j++){
				if(board.get(i).get(j).equals(piece)){
					list.add(i*12+j);
					System.out.println("i = "+i+" j = "+j);
				}
			}
		}
		return list;
	}

	public static List<Position> correctPath(List<Position> path){
		List<Position> pos = new ArrayList<Position>();
		for(int i =0; i < path.size(); i++){
			pos.add(new Position(8 - path.get(i).x, path.get(i).y));
		}
		return pos;
	}


	public static ArrayList<Path> getAllPaths(String sourceFen, String targetFen){

		ArrayList<Path> paths = new ArrayList<Path>();
		ArrayList<Path> tmpaths = new ArrayList<Path>();
		ArrayList<ArrayList<Character>> sourceBoard = getFullBoard(sourceFen);
		ArrayList<ArrayList<Character>> targetBoard = getFullBoard(targetFen);
		ArrayList<ArrayList<Character>> needToMove = substrctBoards(getFullBoard(sourceFen), getFullBoard(targetFen));
		ArrayList<ArrayList<Character>> moveTo = substrctBoards(getFullBoard(targetFen), getFullBoard(sourceFen));
		ArrayList<ArrayList<Character>> varBoard = sourceBoard;
		
		System.out.println("Source Board");
		printBoard(sourceBoard);
		System.out.println("\nTarget Board");
		printBoard(targetBoard);
		System.out.println("\nNeed to move");
		printBoard(needToMove);
		System.out.println("\nMove To");
		printBoard(moveTo);
		System.out.println("");

		for(int i = 0; i < moveTo.size(); i++){
			for(int j = 0; j < moveTo.get(0).size(); j++){
				if(!moveTo.get(i).get(j).equals('.')){
					tmpaths.clear();
					System.out.println("Taking care of "+moveTo.get(i).get(j));
					ArrayList<Integer> possibleFrom = getPossibleFrom(needToMove, moveTo.get(i).get(j));
					for(Integer a : possibleFrom){
						tmpaths.add(new Path(getShortestPath(a,i*12+j, getOccupied(varBoard))));
					}
					Path tmpath = new Path(new ArrayList<Position>());
					tmpath.length=999999999;
					System.out.println("Possible paths");
					for(Path a : tmpaths){
						System.out.println(a);
						if(a.length < tmpath.length){
							tmpath = a;
						} 
					}
					
					paths.add(tmpath);
					System.out.println("Moving piece -> "+moveTo.get(i).get(j));
					System.out.println(tmpath);
					varBoard.get(i).set(j, moveTo.get(i).get(j));
					varBoard.get((int)(tmpath.getPositionati(0).getY()-0.5)).set((int)(tmpath.getPositionati(0).getX()-0.5),'.');
					//check/complete that
					needToMove.get((int)(tmpath.getPositionati(0).getY()-0.5)).set((int)(tmpath.getPositionati(0).getX()-0.5),'X');
				}
			}
		}
		System.out.println("\nNeed to move");
		printBoard(needToMove);
		System.out.println("\nFinal board");
		printBoard(varBoard);
		return paths;
	}





	public static void main(String[] args){

		// TO DO
		// dernier problème: adjacences
		
		String feninit = "rnbqkbnr/pppp2pp/8/4pp2/8/8/PPPPPPPP/RNBQKBNR w KQkq -";
		String newfen1 = "rnbqkbnr/pppp2pp/8/3pp3/8/8/PPPPPPPP/RNBQKBNR w KQkq -";
		String newfen2 = "rnbqkbnr/pppp2pp/8/3pp3/8/1P6/P1PPPPPP/RNBQKBNR w KQkq -";
		String newfen3 = "rnbqkbnr/pppp2pp/8/3pp3/1P6/8/P1PPPPPP/RNBQKBNR w KQkq -";


		ArrayList<Path> paths = getAllPaths(feninit, newfen2);



		
		// Simple dijkstra test
		List<Integer> cases_prises = new ArrayList<Integer>();
		int start_x = 6;
		int start_y = 3;
		int end_x = 8;
		int end_y = 3;
		int start = start_x*12+start_y;
		int end = end_x*12+end_y;

		String fen = "rnbqkbnr/pppp2pp/8/4pp2/8/8/PPPPPPPP/RNBQKBNR w KQkq -";
		ArrayList<ArrayList<Character>> teb = getFullBoard(fen);
		//printBoard(teb);
		cases_prises = getOccupied(fen);
                
		List<Position> path = getShortestPath(start, end, cases_prises);
		path = correctPath(path);
		System.out.println("\n"+path.toString());
		

		/*
		  TESTS
		String fen = "R1b1k2n/ppp5/4K2p/8/3p4/8/Pq6/3Q1Bb1 w KQkq - 0 5";

		ArrayList<ArrayList<Character>> tab = getTabFromFen(fen);
		for(ArrayList<Character> a : tab)
			System.out.println(a);
		System.out.println();
		ArrayList<ArrayList<Character>> teb = getFullBoard(fen);
		for(ArrayList<Character> a : teb)
			System.out.println(a);



		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		String fen1 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		String fen2 = "R1b1k2n/ppp5/4K2p/8/3p4/8/Pq6/3Q1Bb1 w KQkq - 0 5";
		String fen3 = "8/3k4/8/4K3/8/8/8/8 w KQkq - 0 5";
		 */
	}
}


