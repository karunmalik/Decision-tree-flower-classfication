	import java.util.*;
	import java.io.*;
	
	public class DecisionTree {
	
		private DecisionStump root;
		
		public DecisionTree(int dimension, int numberOfClasses) {
			DecisionStump.nClasses= numberOfClasses;
			DecisionStump.dim= dimension;
			root= new DecisionStump();
		}
		
		// returns the root of the decision tree
		public DecisionStump getRoot() {
		
			return root;
		}
		
		// get the class associated with this vector according to the decision tree
		public int getDecision(double[] vector){
			return root.getDecision(vector);
			
		}
		
		// replace a leaf node by a DecisionStump with two leaves
		public void replace(DecisionStump leaf, int featureIndex, double threshold){
			DecisionStump newleaf = new DecisionStump(featureIndex,threshold);
		    DecisionStump l = new DecisionStump();
		    DecisionStump r = new DecisionStump();
		    newleaf.setGreaterBranch(r);
		    newleaf.setSmallerBranch(l);
		    if(leaf.getParent()==null)
		    {
		    	this.root=newleaf;
		    	
		    }
		    else{
		    	
		    if( leaf.getParent().getSmallerBranch()==leaf)
			   leaf.getParent().setSmallerBranch(newleaf);
		   else
			   leaf.getParent().setGreaterBranch(newleaf);
		    }
		}
		
		// gets the leaf with the smallest maximal class probability
		public DecisionStump getSmallestMaxProb(){
			
			return getsmallestmaxprob(root);
	
		}
		private DecisionStump getsmallestmaxprob(DecisionStump root2){
			if (root2.isExternal()){
				
					return root2;
				}
				
			DecisionStump left=getsmallestmaxprob( root2.getSmallerBranch());
			
			DecisionStump right =getsmallestmaxprob( root2.getGreaterBranch());
				if( left.getMaxProb()<right.getMaxProb())
					return left;
				else
					return right;
			
		}
		// used to grow trees
		private void growtree(DecisionTree dt) 
		{
	
	
	double[] vector=new double[4];
		
		try{
		Scanner s = new Scanner(new File("iris.data.txt"));
		s.useDelimiter("[,\r\n]+");
		
		while(s.hasNextDouble()){
			
			
		vector[0]=s.nextDouble();
		
		vector[1]=s.nextDouble();
		vector[2]=s.nextDouble();
		vector[3]= s.nextDouble();
		String str= s.next();
		
	    if(str.equals("Iris-setosa")){
	   	
	    dt.train(vector,0);
	   	
	   	
	   		
	   	
	    
	    }
	    else if(str.equals("Iris-versicolor")){
	   	 dt.train(vector, 1);
		         }
	   	 
	    
	   else if(str.equals("Iris-virginica")){
	   	 dt.train(vector, 2);
	   	
	   }
	    
		}}
		
		catch (Exception e) {
			System.out.println("Error reading file...");
		}
		
		}
			
		
			
	
		// updates the probability count of each node of the decision tree
		// based on a sample for which the associated class is known
		public void train(double[] vector, int classNumber) {
		
			root.updateProbCount(vector, classNumber);
		}
		
		// reset the probability counts of all nodes
		public void resetAll() {
		
				reset(root);
		}
	
		private void reset(DecisionStump ds) {
		
			ds.resetProbCount();
			
			if (!ds.isExternal()) {
			
				reset(ds.getSmallerBranch()); // typo correction here
				reset(ds.getGreaterBranch()); // typo correction here
			}
		}
		
		private int getrandomindex()
		{
			return (int) (Math.random() * ( 4));
		}
		
		private double getrandomthreshold(int classtype)
		{
			if(classtype==0)
				return 4 + (int)(Math.random() * ((4)));
			else if(classtype==1)
				return 2 + (int)(Math.random() * ((3)));
			else if(classtype==2)
				return 1 + (int)(Math.random() * ((6)));
			else
				return 0 + (int)(Math.random() * ((3) ));
		}
	
		// pre-order print of all nodes
		public void print(){
			if(root!=null)
	        print(root);
		}
		private void print(DecisionStump current)
		{
			
			if(current!=null){
				
				
					System.out.println(current);	
	print( current.getSmallerBranch());
			
		print( current.getGreaterBranch());	
			}
			}
		public static void main(String[] args) {
			
		
			// here is how to read the file containing the pre-classified samples
			try {
				
				
	            Scanner scanner = new Scanner(new File("iris.data.txt"));
	          
				scanner.useDelimiter("[,\r\n]+");
				while(scanner.hasNextDouble()) {
					System.out.println("="+scanner.nextDouble());
					System.out.println("="+scanner.nextDouble());
					System.out.println("="+scanner.nextDouble());
					System.out.println("="+scanner.nextDouble());
					System.out.println("="+scanner.next());
				}
				scanner.close();
			} catch (Exception e) {
			   System.out.println("Error reading file...");
			}
			
			// ********* add code for part B and part C here *************** //
			System.out.println();
			DecisionTree dt= new DecisionTree(4, 3);
			dt.replace(dt.getRoot(), 0, 5.0);
			dt.growtree(dt);
			System.out.println("1 node decision tree");
			dt.print();
			System.out.println("unreliable node "+dt.getSmallestMaxProb());
			System.out.println();
			
			
			
			dt.replace(dt.getSmallestMaxProb(), 2, 2.5);
			dt.resetAll();
			dt.growtree(dt);
			System.out.println("2 node decision tree");
			dt.print();
			System.out.println("unreliable node "+dt.getSmallestMaxProb());
			System.out.println();
			
			dt.replace(dt.getSmallestMaxProb(), 1, 3.0);
			dt.resetAll();
			dt.growtree(dt);
			System.out.println("3 node decision tree");
			dt.print();
			System.out.println("unreliable node "+dt.getSmallestMaxProb());
			System.out.println();
		
			// Part C
			
			DecisionTree dtree= new DecisionTree(4,3);
			int random=dtree.getrandomindex();
			double random1= dtree.getrandomthreshold(random);
			dtree.replace(dtree.getRoot(), random, random1);
			dtree.resetAll();
			dtree.growtree(dtree);
			System.out.println();
			
			System.out.println();
			System.out.println("Before entering the loop the most unreliable node's probability:  "+dtree.getSmallestMaxProb().getMaxProb());
			for(int i=0;i<50 ;i++){
			
			random=dtree.getrandomindex();
			random1=dtree.getrandomthreshold(random);
			dtree.replace(dtree.getSmallestMaxProb(), random, random1);
			dtree.resetAll();
			dtree.growtree(dtree);
			
			}
			dtree.print();
			System.out.println("After feeding the tree for 50 times the probability of unreliable node:  "+dtree.getSmallestMaxProb().getMaxProb());
	
		
			
		}
		}
