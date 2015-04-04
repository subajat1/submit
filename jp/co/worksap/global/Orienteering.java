package jp.co.worksap.global;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Orienteering {

	public static void main(String[] args) throws java.lang.Exception {
		Orienteering o = new Orienteering();

		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				System.in));
		String readLine;
		int readLineCounter = 0;
		int readLine_ptr = 0;
		while (bufferReader.ready()) {
			readLine = bufferReader.readLine();
			if (readLineCounter == 0) {
				String readWidth = "", readHeight = "";
				boolean blankFlag = false;
				for (char readCharacter : readLine.toCharArray()) {
					if (readCharacter == ' ')
						blankFlag = true;

					if (!blankFlag && readCharacter != ' ')
						readWidth += String.valueOf(readCharacter);
					else if (blankFlag && readCharacter != ' ')
						readHeight += String.valueOf(readCharacter);
				}

				readLineCounter++;

				o.readMap_Width = Integer.parseInt(readWidth);
				o.readMap_Height = Integer.parseInt(readHeight);
				o.readMap = new char[o.readMap_Height][o.readMap_Width];
			} else {
				for (int readCharacter_ptr = 0; readCharacter_ptr < o.readMap_Width; readCharacter_ptr++)
					o.readMap[readLine_ptr][readCharacter_ptr] = (char) readLine
							.charAt(readCharacter_ptr);
				readLine_ptr++;
			}
		}
		bufferReader.close();

		// debug
		o.debugMap();

		//drawDetailedPoints(o);

		// / Starting algorithm
		o.map = new char[o.readMap_Height][o.readMap_Width];
		o.initializeMap(o.map);

		// / Traversing the readMap
		// / I.S.: Traversing readMap, loaded from given txt file
		// / F.S.: Getting valid readMap, if it is then, getting the G 'Goal',
		// S 'Start', & @ 'Checkpoint's
		if (o.traverseMap(o.readMap)) {

			System.out.println(o.readMap_Width+ " " + o.readMap_Height);
			o.DrawMap(o.readMap);
			
			// Adding a startPoint & a goalPoint into o.points collection.
			// Assumed that a startPoint is always as the second-last point
			// and a goalPoint is always as the last point, in o.points.
			o.startPoint.id = o.points.size();
			o.points.add(o.startPoint);
			o.goalPoint.id = o.points.size();
			o.points.add(o.goalPoint);

			// MASIH PROBLEM, TABLE PATHS SEGITIGA BAWAH BELUM REVERSE
			o.table_paths = new String[o.points.size()][o.points.size()];
			o.table_lookUpDistance = new int[o.points.size()][o.points.size()];

			// / Finding all possible distance & path from each point
			// respectively
			// / I.S.: Valid readMap
			// / F.S.: Gaining each distance & path
			for (int h_ptr = 0; h_ptr < o.points.size(); h_ptr++) {
				for (int w_ptr = 0; w_ptr < h_ptr; w_ptr++) {

					// System.out.println("dari:" +
					// o.points.get(h_ptr).xPosition
					// + "," + o.points.get(h_ptr).yPosition + " ke "
					// + o.points.get(w_ptr).xPosition + ","
					// + o.points.get(w_ptr).yPosition);

					o.findingDistancePath(o.points.get(h_ptr).yPosition,
							o.points.get(h_ptr).xPosition,
							o.points.get(w_ptr).yPosition,
							o.points.get(w_ptr).xPosition, w_ptr, h_ptr);
					o.initializeMap(o.map);
				}
			}
			System.out.println("PATHFINDING IS FINISH *************");
			o.reversingTablePath();
			System.out.println("REVERSE IS FINISH *************");
			//draw_tableLookUp(o); // out string
			//draw_tablePaths(o); // out string

			// / Finding the shortest distance & path from 'Start' to 'Goal' by
			// passing all 'Checkpoint'
			// / I.S.: Having table_lookUp (distance values from each pairs)
			// / F.S.: Getting shortest distance that satisfying the condition
			o.findingShortestDistance();
			System.out.println("B&B IS FINISH *************");
		} else {
			System.out.println(-1);
		}
	}

	// / scanning readMap
	public boolean traverseMap(char[][] map) {
		boolean flag_goal = false;
		boolean flag_start = false;
		boolean flag_checkPointLessThan18 = true;

		for (int y = 0; y < this.readMap_Height; y++) {
			for (int x = 0; x < this.readMap_Width; x++) {
				if(this.points.size()>18){
					flag_checkPointLessThan18 = false;
					break;
				}
				if (map[y][x] == '@') {
					this.points.add(new Point(x, y, points.size()));
				} else if (map[y][x] == 'G') {
					this.goalPoint.xPosition = x;
					this.goalPoint.yPosition = y;
					flag_goal = true;
				} else if (map[y][x] == 'S') {
					this.startPoint.xPosition = x;
					this.startPoint.yPosition = y;
					flag_start = true;
				}
			}
		}
		return flag_goal && flag_start && flag_checkPointLessThan18;
	}

	// / Finding paths from each point in points{'S','G','@'}
	public boolean findingDistancePath(int i, int j, int i2, int j2,
			int counter_i, int counter_j) {
		if (!isValid(i, j))
			return false;

		boolean isFound = false;
		int n_step = 0;

		LinkedList<Point> activeSet = new LinkedList<Point>();

		Point startNode = new Point(i, j, n_step);
		startNode.level = 0;
		activeSet.add(startNode);

		while (activeSet.peek() != null && !isFound) {
			Point activePoint = activeSet.removeFirst();
			if (map[activePoint.xPosition][activePoint.yPosition] == 'X')
				continue;

			// / '+' means active point
			map[activePoint.xPosition][activePoint.yPosition] = '+';
			n_step++;
			
			int[][] points = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

			for (int i_adj = 0; i_adj < 4; ++i_adj) {
				if (activePoint.xPosition + points[i_adj][0] == i2
						&& activePoint.yPosition + points[i_adj][1] == j2) {

					table_lookUpDistance[counter_i][counter_j] = activePoint.level + 1;
					table_lookUpDistance[counter_j][counter_i] = activePoint.level + 1;

					table_paths[counter_i][counter_j] = j2 + "," + i2 + ",";
					// table_paths[counter_j][counter_i] = j2 + "," + i2 + ",";
					
					while (activePoint._parent != null && activePoint._parent.id != 0) {
						
						table_paths[counter_i][counter_j] += activePoint.yPosition
								+ "," + activePoint.xPosition + ",";

						activePoint = activePoint._parent;
						// table_paths[counter_j][counter_i] +=
						// activePoint.yPosition
						// + "," + activePoint.xPosition + ",";
					}
					table_paths[counter_i][counter_j] += activePoint.yPosition
							+ "," + activePoint.xPosition + ",";
					if(activePoint._parent != null){
						table_paths[counter_i][counter_j] += activePoint._parent.yPosition
							+ "," + activePoint._parent.xPosition;
					}
					// table_paths[counter_j][counter_i] +=
					// activePoint.yPosition
					// + "," + activePoint.xPosition + ",";
					// table_paths[counter_j][counter_i] +=
					// activePoint._parent.yPosition
					// + "," + activePoint._parent.xPosition;
					isFound = true;
					break;
				}

				Point generatedPoint = new Point(activePoint.xPosition
						+ points[i_adj][0], activePoint.yPosition
						+ points[i_adj][1], n_step);

				if (isValid(generatedPoint.xPosition, generatedPoint.yPosition)
						&& map[generatedPoint.xPosition][generatedPoint.yPosition] != 'X') {
					generatedPoint.level = activePoint.level + 1;
					generatedPoint._parent = activePoint;
					activeSet.add(generatedPoint);
					map[generatedPoint.xPosition][generatedPoint.yPosition] = '%';
				}
			}
			map[activePoint.xPosition][activePoint.yPosition] = 'X';

			//DrawMap(map);
			System.out.println("  ActiveSet current-size:" + activeSet.size());
		}
		return true;
	}

	public void reversingTablePath() {
		for (int h_ptr = 0; h_ptr < points.size(); h_ptr++) {
			for (int w_ptr = 0; w_ptr < h_ptr; w_ptr++) {
				String tempPath = table_paths[w_ptr][h_ptr];
				String reversedPath = "";
				String[] splitPath = tempPath.split(",");

				for (int i = splitPath.length - 1; i > 0; i -= 2) {
					reversedPath += splitPath[i - 1] + "," + splitPath[i] + ",";
				}

				table_paths[h_ptr][w_ptr] = reversedPath;
			}
		}
	}

	// / by adopting Branch & Bound algorithm
	private void findingShortestDistance() {
		Point best = new Point();
		int currentBest = Integer.MAX_VALUE;

		Point active = points.get(points.size() - 2);

		Comparator<Point> comparator = new DistanceComparator();
		PriorityQueue<Point> prioQueue = new PriorityQueue<Point>(1,
				comparator);
		prioQueue.add(active);

		while (prioQueue.peek() != null) {
			Point currentNode = prioQueue.poll();
			//System.out.println(" current node :: " + currentNode.id);
//			for (Point node : prioQueue) {
//				System.out.print(node.id);
//			}

			currentNode.usedIdCollection.add(currentNode.id);
			if (currentNode.usedIdCollection.size() == points.size() - 1) {
				if (currentNode.distanceToRootPoint
						+ table_lookUpDistance[currentNode.id][points.size() - 1] < currentBest) {
					best = currentNode;
					best.usedIdCollection.add(points.get(points.size() - 1).id);

					currentBest = currentNode.distanceToRootPoint
							+ table_lookUpDistance[currentNode.id][points
									.size() - 1];
				}
			} else {
				for (int i = 0; i < points.size() - 2; i++) {
//					System.out.println("generated "+i);
					if (currentNode.usedIdCollection.contains(i)) {
						continue;
					} else {
						if (currentNode.distanceToRootPoint
								+ table_lookUpDistance[i][currentNode.id] > currentBest) {
							// do nothing here (X)
						} else {
							Point generatedNode = new Point(i);
							generatedNode.distanceToRootPoint = currentNode.distanceToRootPoint
									+ table_lookUpDistance[i][currentNode.id];
							generatedNode.usedIdCollection = new ArrayList<Integer>(
									currentNode.usedIdCollection);
							prioQueue.add(generatedNode);
						}
					}
				}
			}
		}

		// System.out.println("Best Node");
		// for (int usedCounter : best.usedIdCollection) {
		for (int i = 0; i < best.usedIdCollection.size() - 1; i++) {
//			System.out.println(" >> "
//							+ best.usedIdCollection.get(i)
//							+ " ("
//							+ table_lookUpDistance[best.usedIdCollection.get(i)][best.usedIdCollection
//									.get(i + 1)] + ")");
			//System.out.println(table_paths[best.usedIdCollection.get(i)][best.usedIdCollection.get(i+1)]);
		}
//		System.out
//				.println(" >> "+ best.usedIdCollection.get(best.usedIdCollection.size() - 1));						
		System.out.println(currentBest);
	}

	// /=====================================================	
	private void initializeMap(char[][] map) {
		for (int y = 0; y < readMap_Height; y++)
			for (int x = 0; x < readMap_Width; x++)
				map[y][x] = '.';
	}

	private boolean isValid(int i, int j) {
		if (isPassable(i, j) && !isTried(i, j) && isInsideTheMap(i, j)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isInsideTheMap(int i, int j) {
		return i >= 0 && i < readMap_Height && j >= 0 && j < readMap_Width;
	}

	private boolean isPassable(int i, int j) {
		return readMap[i][j] == '.' || readMap[i][j] == 'S'
				|| readMap[i][j] == 'G' || readMap[i][j] == '@';
	}

	private boolean isTried(int i, int j) {
		return map[i][j] == 'X';
	}

	// ==========================================================

	// / Class for PriorityQueue Comparator
	public class DistanceComparator implements Comparator<Point> {
		@Override
		public int compare(Point point1, Point point2) {
			if (point1.distanceToRootPoint < point2.distanceToRootPoint) {
				return -1;
			}
			if (point1.distanceToRootPoint > point2.distanceToRootPoint) {
				return 1;
			}
			return 0;
		}
	}

	// / Class for representing each point in readMap
	public class Point {
		public Point(int x, int y, int id) {
			this.xPosition = x;
			this.yPosition = y;
			this.id = id;
			this._parent = null;
			this.usedIdCollection = new ArrayList<Integer>();
		}

		public Point(int id) {
			this.id = id;
			this._parent = null;
			this.usedIdCollection = new ArrayList<Integer>();
		}

		public Point() {
			this._parent = null;
			this.usedIdCollection = new ArrayList<Integer>();
		}

		public int xPosition;
		public int yPosition;
		public int id;
		public int level;
		public int distanceToRootPoint;
		public Point _parent;
		public List<Integer> usedIdCollection;
	}

	private static void drawDetailedPoints(Orienteering o) {
		for (Point n : o.points) {
			System.out.print(" NODE " + n.id + "::");
			o.DrawNode(n);
		}
	}

	private static void draw_tablePaths(Orienteering o) {
		for (int j = 0; j < o.points.size(); j++) {
			for (int i = 0; i < o.points.size(); i++) {
				System.out.print("<" + o.table_paths[j][i] + ">");
			}
			System.out.println("");
		}
	}

	private static void draw_tableLookUp(Orienteering o) {
		for (int j = 0; j < o.points.size(); j++) {
			for (int i = 0; i < o.points.size(); i++) {
				System.out.print("[" + o.table_lookUpDistance[j][i] + "]");
			}
			System.out.println("");
		}
	}

	public void DrawMap(char[][] map) {
		for (int y = 0; y < this.readMap_Height; y++) {
			for (int x = 0; x < this.readMap_Width; x++) {
				System.out.print(map[y][x]);
			}
			System.out.println("");
		}
//		System.out.println("-- DrawMap");
	}

	public void DrawNode(Point n) {
		System.out.println("[ " + n.xPosition + " , " + n.yPosition + "]");
	}

	// / Constructor
	public Orienteering() {
		points = new ArrayList<Point>();
		goalPoint = new Point();
		startPoint = new Point();
	}

	public int readMap_Width;
	public int readMap_Height;

	public Point goalPoint;
	public Point startPoint;
	public List<Point> points;

	public int[][] table_lookUpDistance;
	public String[][] table_paths;

	public int ch_point;

	public char[][] readMap;
	public char[][] map;
	
	private void debugMap() {
		readMap_Width = 5;
		readMap_Height = 7;
		readMap = new char[readMap_Height][readMap_Width];
		for (int y = 0; y < this.readMap_Height; y++) {
			for (int x = 0; x < this.readMap_Width; x++) {
				readMap[y][x] = alt_mapDummy[y][x];
			}
		}
	}

	char[][] alt_mapDummy = {
			{ '#', '#', '#', '#', '#' },
			{ '#', '.', '.', '.', '#' },
			{ '#', '.', '.', '.', '#' },
			{ '#', '.', '.', '.', '#' },
			{ '#', '.', '.', '.', '#' },
			{ '#', 'S', '@', 'G', '#' },
			{ '#', '#', '#', '#', '#' } };
			
	}
//			{ '#', '#', '#', '#', '#' },
//			{ '#', '@', '@', '@', '#' },
//			{ '#', '@', '@', '@', '#' },
//			{ '#', '@', '@', '@', '#' },
//			{ '#', '@', '@', '@', '#' },
//			//{ '#', '@', '@', '@', '#' },
//			{ '#', 'S', 'G', '@', '#' },
//			{ '#', '#', '#', '#', '#' } };
//}
