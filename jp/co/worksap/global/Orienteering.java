package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.List;

public class Orienteering {

	public int MAP_WIDTH;
	public int MAP_HEIGHT;

	public Node GOAL;
	public Node START;
	public List<Node> NODES;

	public Orienteering() {
		
		System.gc();

		NODES = new ArrayList<Node>();
		GOAL = new Node();
		START = new Node();
	}

	public char[][] alt_map;

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
			if(LineCounter == 0){
				
				
				o.MAP_WIDTH = Integer.parseInt(s);
				o.MAP_HEIGHT = 7;
				o.alt_map = new char[o.MAP_HEIGHT][o.MAP_WIDTH];
				
				System.out.println("WIDTHMAP:: "+o.MAP_WIDTH);
				
				LineCounter++;
			}else{				
				for (int arr_w = 0; arr_w < o.MAP_WIDTH; arr_w++) {
					o.alt_map[arr_h][arr_w] = (char) s.charAt(arr_w);	
					//System.out.print(String.valueOf(s.charAt(arr_w)));
				}
				//System.out.println(" shit " + String.valueOf(arr_h));
				arr_h++;	
			}			
		}
		r.close();
		
		// debug shit		
		o.DrawMap(o.alt_map);		
		o.TraverseMap(o.alt_map);		
		
		System.out.print("GOAL:::");
		o.DrawNode(o.GOAL);
		
		System.out.print("START::");
		o.DrawNode(o.START);
		
		for(Node n : o.NODES){
			System.out.print(" NODE::");
			o.DrawNode(n);			
		}
				
	}

	public boolean FindPath(int x, int y) {
		return true;
	}
	
	public boolean TraverseMap(char[][] map) {
		boolean flag_goal = false;
		boolean flag_start = false;
//		char temp = 'z';

		for (int y = 0; y < this.MAP_HEIGHT; y++) {
			for (int x = 0; x < this.MAP_WIDTH; x++) {
				System.out.print(map[y][x]);
//				temp = map[y][x];

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
					this.NODES.add(new Node(x, y));
				}
			}
			System.out.println("");
		}
		System.out.println("-------- this is TraverseMap");
		return flag_goal && flag_start;
	}

	public boolean valid(int x, int y) {
		if (x > 0 && y > 0 && x < MAP_WIDTH && y < MAP_HEIGHT
				&& alt_map[x][y] != '#')
			return true;
		else
			return false;
	}

	public void DrawMap(char[][] map) {

		for (int y = 0; y < this.MAP_HEIGHT; y++) {
			for (int x = 0; x < this.MAP_WIDTH; x++) {
				System.out.print(map[y][x]);
			}
			System.out.println("");
		}
		System.out.println("-------- this is DrawMap");
		//System.out.println(map[1][3]);
	}
	
	public void DrawNode(Node n){
		System.out.println("[ "+n.x+" , " + n.y + "]");
	}

	public class Node {
		public Node(int x2, int y2) {
			x = x2;
			y = y2;
		}

		public Node() {
			// TODO Auto-generated constructor stub
		}

		public int x, y;
	}
	
	char[][] alt_map0 = {
			{ '#', '#', '#', '#', '#' },
			{ '#', '@', '@', 'G', '#' }, 
			{ '#', '.', '#', '.', '#' },
			{ '#', '.', '.', '.', '#' }, 
			{ '#', '@', '#', '.', '#' },
			{ '#', 'S', '#', '@', '#' }, 
			{ '#', '#', '#', '#', '#' } };	
}
