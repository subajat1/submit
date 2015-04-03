package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Orienteering {

	public int MAP_WIDTH;
	public int MAP_HEIGHT;

	public Node GOAL;
	public Node START;
	public List<Node> NODES;

	public int[][] T_look;
	public String[][] T_location;

	public int ch_point, n_step;

	public Orienteering() {

		System.gc();

		NODES = new ArrayList<Node>();
		GOAL = new Node();
		START = new Node();
	}

	public char[][] alt_map;
	public char[][] map;

	// MATRIX DISTANCE
	// ARRAY 2D

	public static void main(String[] args) throws java.lang.Exception {
		java.io.BufferedReader r = new java.io.BufferedReader(
				new java.io.InputStreamReader(System.in));
		String s;

		Orienteering o = new Orienteering();
		int LineCounter = 0;
		int arr_h = 0;
		while (r.ready()) {
			s = r.readLine();
			if (LineCounter == 0) {
				String w0 = "", h0 = "";
				boolean flag_wh = false;
				for (char c : s.toCharArray()) {
					if (c == ' ')
						flag_wh = true;
					if (!flag_wh && c != ' ')
						w0 += String.valueOf(c);
					else if (flag_wh && c != ' ')
						h0 += String.valueOf(c);
					System.out.println(String.valueOf(c));
				}

				o.MAP_WIDTH = Integer.parseInt(w0);
				o.MAP_HEIGHT = Integer.parseInt(h0);
				o.alt_map = new char[o.MAP_HEIGHT][o.MAP_WIDTH];

				System.out.println("WIDTHMAP::: " + o.MAP_WIDTH);
				System.out.println("HEIGHTMAP:: " + o.MAP_HEIGHT);

				LineCounter++;
			} else {
				for (int arr_w = 0; arr_w < o.MAP_WIDTH; arr_w++) {
					o.alt_map[arr_h][arr_w] = (char) s.charAt(arr_w);
					// System.out.print(String.valueOf(s.charAt(arr_w)));
				}
				// System.out.println(" shit " + String.valueOf(arr_h));
				arr_h++;
			}
		}
		r.close();

		// debug
		o.debugMap();
		o.map = new char[o.MAP_HEIGHT][o.MAP_WIDTH];
		o.map_init();
		// o.DrawMap(o.alt_map);
		if (o.ExploreMap(o.alt_map)) {

			o.START.ID = o.NODES.size();
			o.NODES.add(o.START);
			o.GOAL.ID = o.NODES.size();
			o.NODES.add(o.GOAL);

			// +2 means adding a START node & a GOAL node
			// and assumed that GOAL & START are always the last nodes.
			o.T_look = new int[o.NODES.size()][o.NODES.size()];
			o.T_location = new String[o.NODES.size()][o.NODES.size()];

			// filling matrix
			for (int j = 0; j < o.NODES.size(); j++) {
				for (int i = 0; i < j; i++) {
					// o.T_look[j][i] = o.NODES.get(i).x;
					// o.T_look[i][j] = o.NODES.get(i).x;

					System.out.println("ini::dari:" + o.NODES.get(j).x + ","
							+ o.NODES.get(j).y + " ke " + o.NODES.get(i).x + ","
							+ o.NODES.get(i).y);
					// o.traverse(o.START.y, o.START.x, o.NODES.get(i).y,
					// o.NODES.get(i).x);
					o.BFS(o.NODES.get(j).y, o.NODES.get(j).x, o.NODES.get(i).y,
							o.NODES.get(i).x, i, j);
					o.map_init();
					// o.DrawMap(o.alt_map);
				}
			}

			System.out.println("*************");
			for (int j = 0; j < o.NODES.size(); j++) {
				for (int i = 0; i < o.NODES.size(); i++) {
					System.out.print("["+o.T_look[j][i]+"]");
				}
				System.out.println("");
			}
			
			for (int j = 0; j < o.NODES.size(); j++) {
				for (int i = 0; i < o.NODES.size(); i++) {
					System.out.print("<"+o.T_location[j][i]+">");
				}
				System.out.println("");
			}
			
			o.BNB();

			// o.DrawMap(o.alt_map);
			// o.DrawMap(o.map);

			// o.BFS(o.START.y, o.START.x);

		} else {
			System.out.println("MAP NOT COMPLETE");
		}

		// System.out.print("GOAL:::");
		// o.DrawNode(o.GOAL);
		//
		// System.out.print("START::");
		// o.DrawNode(o.START);

		for (Node n : o.NODES) {
			System.out.print(" NODE " + n.ID + "::");
			o.DrawNode(n);
		}
		// o.DrawMap(o.map);
	}

	private void BNB() {
		Node best =new Node();
		int currentBest =Integer.MAX_VALUE;
		
		
		Node active=NODES.get(NODES.size()-2);
		
		Comparator<Node> comp = new StringLengthComparator();
		PriorityQueue<Node> pq = new PriorityQueue<Node>(10, comp);
		pq.add(active);
		
		while(pq.peek() != null){
			Node currentNode = pq.poll();
			
			for(Node ii : pq){
				System.out.println(ii.ID+" ID ");	
			}
			System.out.println(currentNode.ID+" <--");
			currentNode.used.add(currentNode.ID);
			if(currentNode.used.size() == NODES.size()-1){
				if(currentNode.distance_to_root + T_look[NODES.size()-1][currentNode.ID] < currentBest){
					best = currentNode;
					currentBest = currentNode.distance_to_root + T_look[NODES.size()-1][currentNode.ID];
				}
			}else{
				for(int i=0;i<NODES.size()-2;i++){
					if(currentNode.used.contains(i)){
						continue;
					}else{
						if(currentNode.distance_to_root + T_look[i][currentNode.ID] > currentBest){
							// do nothing (X)
						}else{
							Node newNode = new Node(i);
							newNode.distance_to_root = currentNode.distance_to_root + T_look[i][currentNode.ID];
							newNode.used = new ArrayList<Integer>(currentNode.used);
							pq.add(newNode);
						}
					}
				}	
			}
		}
		for(int ii : best.used){
			System.out.println(ii+" -> ");	
		}
		System.out.println("dist::"+currentBest);
		
		
	}

	public class StringLengthComparator implements Comparator<Node>
	{
	    @Override
	    public int compare(Node x, Node y)
	    {
	        // Assume neither string is null. Real code should
	        // probably be more robust
	        // You could also just return x.length() - y.length(),
	        // which would be more efficient.
	        if (x.distance_to_root < y.distance_to_root)
	        {
	            return -1;
	        }
	        if (x.distance_to_root > y.distance_to_root)
	        {
	            return 1;
	        }
	        return 0;
	    }
	}
	
	private void map_init() {
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				map[y][x] = '.';
			}
		}
	}

	public boolean BFS(int i, int j, int i2, int j2, int iii, int jjj) {
		if (!isValid(i, j)) {
			return false;
		}

		LinkedList<Node> queue = new LinkedList<Node>();
		n_step = 0;
		Node startNode = new Node(i, j, n_step);
		startNode.level = 0;
		queue.add(startNode);
		// map[i][j] = 'X';

		boolean f_ketemu = false;
		
		while (queue.peek() != null && !f_ketemu) {
			Node node_act = queue.removeFirst();
			if (map[node_act.x][node_act.y] == 'X') {
				continue;
			}
			map[node_act.x][node_act.y] = '+';			
			System.out.println("level::"+node_act.level);
			// map[node_act.x][node_act.y]='X';
			n_step++;
			// { 0, 1 } EAST { 1, 0 } SOUTH { 0, -1 } WEST { -1, 0 } NORTH
			int[][] points = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
			int[][] points_n = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
			int[][] points_w = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
			int[][] points_e = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

			for (int ii = 0; ii < 4; ++ii) {
				if (node_act.x + points[ii][0] == i2
						&& node_act.y + points[ii][1] == j2) {
					T_look[iii][jjj]= node_act.level+1;
					T_look[jjj][iii]= node_act.level+1;
					T_location[iii][jjj]= j2+","+i2+",";
					T_location[jjj][iii]= j2+","+i2+",";
					while (node_act.parent.ID!=0){
						T_location[iii][jjj]+= node_act.y+","+node_act.x+",";
						T_location[jjj][iii]+= node_act.y+","+node_act.x+",";
						node_act = node_act.parent;
						
					}
					T_location[iii][jjj]+= node_act.y+","+node_act.x+",";
					T_location[iii][jjj]+= node_act.parent.y+","+node_act.parent.x;
					T_location[jjj][iii]+= node_act.y+","+node_act.x+",";
					T_location[jjj][iii]+= node_act.parent.y+","+node_act.parent.x;
					f_ketemu = true;
					break;
				}

				Node gNode = new Node(node_act.x + points[ii][0], node_act.y
						+ points[ii][1], n_step);

				if (isValid(gNode.x, gNode.y) && map[gNode.x][gNode.y] != 'X') {
					gNode.level = node_act.level+1;
					gNode.parent = node_act;
					queue.add(gNode);
					map[gNode.x][gNode.y] = '%';
				}
			}
			// alt_map[node_act.x][node_act.y] =
			// String.valueOf(n_step).charAt(0);
			DrawMap(map);
			System.out.println("size:" + queue.size());
			// DrawMap(alt_map);
			map[node_act.x][node_act.y] = 'X';
			// maze[x][y]='#';
		}

		return false;
	}

	public boolean traverse(int i, int j, int i2, int j2) {
		if (!isValid(i, j)) {
			return false;
		}

		DrawMap(map);

		if (isEnd(i, j, i2, j2)) {
			map[i][j] = 'O';
			System.out.println("== end ===");
			map_init();
			// DrawMap(map);
			return true;
		} else {
			map[i][j] = 'X';
			System.out.println("curr: " + j + "::" + i);
			System.out.println("cur2: " + j2 + "::" + i2);
			// DrawMap(map);
		}

		if (j2 - j == 0 && i2 - i > 0) {
			// South
			if (traverse(i + 1, j, i2, j2)) {
				map[i + 1][j] = 's';
				return true;
			}
		} else if (j2 - j == 0 && i2 - i < 0) {
			// North
			if (traverse(i - 1, j, i2, j2)) {
				map[i - 1][j] = 'n';
				return true;
			}
		} else if (j2 - j > 0 && i2 - i == 0) {
			// East
			if (traverse(i, j + 1, i2, j2)) {
				map[i][j + 1] = 'e';
				return true;
			}
		} else if (j2 - j < 0 && i2 - i == 0) {
			// West
			if (traverse(i, j - 1, i2, j2)) {
				map[i][j - 1] = 'w';
				return true;
			}
		} else

		if (j2 - j > 0 && i2 - i > 0) {
			// s-e-n-w
			System.out.println("s-e-n-w");
			// South
			if (traverse(i + 1, j, i2, j2)) {
				map[i + 1][j] = 's';
				return true;
			}
			// East
			if (traverse(i, j + 1, i2, j2)) {
				map[i][j + 1] = 'e';
				return true;
			}
			// North
			if (traverse(i - 1, j, i2, j2)) {
				map[i - 1][j] = 'n';
				return true;
			}
			// West
			if (traverse(i, j - 1, i2, j2)) {
				map[i][j - 1] = 'w';
				return true;
			}
		} else if (j2 - j > 0 && i2 - i < 0) {

			// N-E-s-W
			System.out.println("N-E-s-W");
			// North
			if (traverse(i - 1, j, i2, j2)) {
				map[i - 1][j] = 'n';
				return true;
			}
			// East
			if (traverse(i, j + 1, i2, j2)) {
				map[i][j + 1] = 'e';
				return true;
			}
			// South
			if (traverse(i + 1, j, i2, j2)) {
				map[i + 1][j] = 's';
				return true;
			}
			// West
			if (traverse(i, j - 1, i2, j2)) {
				map[i][j - 1] = 'w';
				return true;
			}
		} else if (j2 - j < 0 && i2 - i > 0) {
			// s-w-n-e
			System.out.println("s-w-n-e");
			// South
			if (traverse(i + 1, j, i2, j2)) {
				map[i + 1][j] = 's';
				return true;
			}
			// West
			if (traverse(i, j - 1, i2, j2)) {
				map[i][j - 1] = 'w';
				return true;
			}
			// North
			if (traverse(i - 1, j, i2, j2)) {
				map[i - 1][j] = 'n';
				return true;
			}
			// East
			if (traverse(i, j + 1, i2, j2)) {
				map[i][j + 1] = 'e';
				return true;
			}

		} else if (j2 - j < 0 && i2 - i < 0) {
			// n-w-s-e
			System.out.println("N-w-s-e");
			// North
			if (traverse(i - 1, j, i2, j2)) {
				map[i - 1][j] = 'n';
				return true;
			}
			// West
			if (traverse(i, j - 1, i2, j2)) {
				map[i][j - 1] = 'w';
				return true;
			}
			// South
			if (traverse(i + 1, j, i2, j2)) {
				map[i + 1][j] = 's';
				return true;
			}
			// East
			if (traverse(i, j + 1, i2, j2)) {
				map[i][j + 1] = 'e';
				return true;
			}
		}
		return false;
	}

	private void debugMap() {
		MAP_WIDTH = 7;
		MAP_HEIGHT = 7;
		alt_map = new char[MAP_HEIGHT][MAP_WIDTH];
		for (int y = 0; y < this.MAP_HEIGHT; y++) {
			for (int x = 0; x < this.MAP_WIDTH; x++) {
				alt_map[y][x] = alt_map0[y][x];
			}
		}
	}

	private boolean isEnd(int i, int j, int i2, int j2) {
		if ((alt_map[i][j] == 'G' || alt_map[i][j] == '@') && i == i2
				&& j == j2) {
			System.out.println("KETEMU");
			return true;
		} else {
			return false;
		}
		// i == MAP_HEIGHT - 1 && j == MAP_WIDTH - 1;
	}

	private boolean isValid(int i, int j) {
		if (inRange(i, j) && isOpen(i, j) && !isTried(i, j)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean inRange(int i, int j) {
		return inHeight(i) && inWidth(j);
	}

	private boolean inHeight(int i) {
		return i >= 0 && i < MAP_HEIGHT;
	}

	private boolean inWidth(int j) {
		return j >= 0 && j < MAP_WIDTH;
	}

	private boolean isOpen(int i, int j) {
		return alt_map[i][j] == '.' || alt_map[i][j] == 'S'
				|| alt_map[i][j] == 'G' || alt_map[i][j] == '@';
	}

	private boolean isTried(int i, int j) {
		return map[i][j] == 'X';
	}

	public boolean ExploreMap(char[][] map) {
		boolean flag_goal = false;
		boolean flag_start = false;
		// char temp = 'z';

		for (int y = 0; y < this.MAP_HEIGHT; y++) {
			for (int x = 0; x < this.MAP_WIDTH; x++) {
				// System.out.print(map[y][x]);
				// temp = map[y][x];

				if (map[y][x] == 'G') {
					this.GOAL.x = x;
					this.GOAL.y = y;
					flag_goal = true;
				}
				if (map[y][x] == 'S') {
					this.START.x = x;
					this.START.y = y;
					flag_start = true;
				}
				if (map[y][x] == '@') {
					this.NODES.add(new Node(x, y, NODES.size()));
				}
			}
			// System.out.println("");
		}
		// System.out.println("-------- this is TraverseMap");
		return flag_goal && flag_start;
	}

	public void DrawMap(char[][] map) {

		for (int y = 0; y < this.MAP_HEIGHT; y++) {
			for (int x = 0; x < this.MAP_WIDTH; x++) {
				System.out.print(map[y][x]);
			}
			System.out.println("");
		}
		System.out.println("-------- this is DrawMap");
		// System.out.println(map[1][3]);
	}

	public void DrawNode(Node n) {
		System.out.println("[ " + n.x + " , " + n.y + "]");
	}

	public class Node {
		public Node(int x, int y, int id) {
			this.x = x;
			this.y = y;
			this.ID = id;
			parent = null;
			used = new ArrayList<Integer>();			
		}

		public Node() {
			parent = null;
			used = new ArrayList<Integer>();
		}

		public Node(int id) {
			this.ID = id;
			parent = null;
			used = new ArrayList<Integer>();
		}

		public int x, y, ID, level, distance_to_root;
		public Node parent;
		public List<Integer> used;
	}

	char[][] alt_map0 = { 
			{ '#', '#', '#', '#', '#', '#', '#' },
			{ '#', '.', '.', '.', '.', '.', '#' },
			{ '#', '.', 'S', '.', '#', '.', '#' },
			{ '#', 'G', '.', '.', '#', '.', '#' },
			{ '#', '#', '#', '#', '.', '.', '#' },
			{ '#', '@', '.', '@', '.', '.', '#' },
			{ '#', '#', '#', '#', '#', '#', '#' } };
}
