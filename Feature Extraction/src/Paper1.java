import java.util.*;

/*
 * Just an unfinished skeleton of the algorithm implemented in Paper 1 for feature extraction.
 * The work will involve building the graph generated from the dependency parsing of the text,
 * building clusters with their heads as the candidate features,
 * associating every word not in the candidate feature to a cluster,
 * then merging clusters that are near to each other (distance < theta).
 */
public class Paper1 {
	public static void main (String[]args){
		
		//ArrayList<Triple> edgeList
		
		ArrayList<String> sentences = new ArrayList();
		
		String[] features = extractFeatures();
		int n = features.length;
		UnionFind clusters = new UnionFind(n, features);
		
		
		
		for(int i=0; i<n; i++){
			for(int j=1; j<n; j++){
				//if(graph.minFeatureDist(i,j) < 3)
					clusters.join(i, j);
			}
		}
		
	}
	
	public static String[] extractFeatures(){
		return new String[]{"camera", "screen", "touch", "os"};
	}
}


class UnionFind{
	
	int n;
	int[] parent;
	String[] feature;
	ArrayList<String> words[];
	
	public UnionFind(int n, String[] arr){
		this.n = n;
		parent = new int[n];
		feature = new String[n];
		words = new ArrayList[n];
		for(int i=0; i<n; i++){
			parent[i] = i;
			feature[i] = arr[i];
			words[i] = new ArrayList<String>();
		}
	}
	
	public void addWord (int i, String word){
		words[i].add(word);
	}
	
	public int findSet(int i){
		if(parent[i]==i) return i;
		else return parent[i] = findSet(parent[i]);
	}
	
	/*
	 * Cluster Head is always the first parameter to the join method.
	 */
	public void join(int a, int b){
		int p1 = findSet(a);
		int p2 = findSet(b);
		parent[p2] = p1;
	}
}
