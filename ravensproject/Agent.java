package ravensproject;

// Uncomment these lines to access image processing.
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
	
	//list of images needed?
	ArrayList<RavensFigure> answerFigures;
	
	private Frame hor1;
	private Frame hor2;
	private Frame ver1;
	private Frame ver2;
	private Frame dia1;
	private Frame dia2;
	
	private Frame expected;
	private Frame cannotContainThisShape;
	private int expectedSize;
	
	private boolean takeIntoAccountPixelCount;
	private int[] restrictedPixels;
	
	private RavensProblem problem;
	
	Transformation horizontalTransformation;
	Transformation verticalTransformation;
	Transformation diagonalTransformation;
	
	public static final int green = Color.GREEN.getRGB();
	public static final int white = Color.WHITE.getRGB();

	
	
    public Agent() {
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
        reset(problem);
        
        //first we will check if the solution to the problem is trivial
        //basically if there is no transformation required in one of the 
        //directions
        
        //*************************************************************
        //Testing Figures
        if (problem.getName().contains("")){
        	
        	
        	//This is our horizontal Transformation
        	RavensFigure g = problem.getFigures().get("G");
        	RavensFigure h = problem.getFigures().get("H");
        	BufferedImage imageG, imageH;
			try {
				imageG = ImageIO.read(new File(g.getVisual()));
				imageH = ImageIO.read(new File(h.getVisual()));
				createFrame(imageG, hor1);
				createFrame(imageH, hor2);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			if (hor1.equals(hor2)) System.out.println("We have matching hor frames");
			
			//This is our vertical Transformation
        	RavensFigure c = problem.getFigures().get("C");
        	RavensFigure f = problem.getFigures().get("F");
        	BufferedImage imageC, imageF;
			try {
				imageC = ImageIO.read(new File(c.getVisual()));
				imageF = ImageIO.read(new File(f.getVisual()));
				createFrame(imageC, ver1);
				createFrame(imageF, ver2);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			if (ver1.equals(ver2)) System.out.println("We have matching ver frames");
			
			//This is our diagonal Transformation
        	RavensFigure a = problem.getFigures().get("A");
        	RavensFigure e = problem.getFigures().get("E");
        	BufferedImage imageA, imageE;
			try {
				imageA = ImageIO.read(new File(a.getVisual()));
				imageE = ImageIO.read(new File(e.getVisual()));
				createFrame(imageA, dia1);
				createFrame(imageE, dia2);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
//			if (dia1.equals(dia2)) System.out.println("We have matching dia frames");
			
			int answer = -1;

			
			if (problem.getName().contains("Basic Problem D-")){
				answer = solveProblem();
				
			}

			if (problem.getName().contains("Basic Problem E-")){
	        	RavensFigure d = problem.getFigures().get("D");
	        	BufferedImage imageD;
	        	
	        	try{
	        		imageD = ImageIO.read(new File(d.getVisual()));
	        		Frame ver3 = new Frame();
		        	createFrame(imageD, ver3);
		        	
		        	int frameASum = 0;
			    	for (Figure f1 : dia1.figures) frameASum += f1.pixelCount;
			    	
			    	int frameDSum = 0;
			    	for (Figure f1 : ver3.figures) frameDSum += f1.pixelCount;
			    	
			    	int frameGSum = 0;
			    	for (Figure f1 : hor1.figures) frameGSum += f1.pixelCount;
			    	
//			    	System.out.println("The sums");
//			    	System.out.println(frameASum);
//			    	System.out.println(frameDSum);
//			    	System.out.println(frameGSum);

			    	
			    	if (frameASum + frameDSum - frameGSum <= 50){
//			    		System.out.println("Success!!");
						for (int i = 0; i < 8; i++){
							BufferedImage image;
							try {
								image = ImageIO.read(new File(answerFigures.get(i).getVisual()));
								Frame temp = new Frame();
								createFrame(image, temp);
								
								double answerSum = 0;
						    	for (Figure f1 : temp.figures) answerSum += f1.pixelCount;
						    	
						    	int frameCSum = 0;
						    	for (Figure f1 : ver1.figures) frameCSum += f1.pixelCount;
						    	
						    	int frameFSum = 0;
						    	for (Figure f1 : ver2.figures) frameFSum += f1.pixelCount;
						    	
						    	if (frameCSum + frameFSum - answerSum <= 70){
//					    			System.out.println("We found an answer");
					    			return i + 1;
					    		}
							} catch (Exception ex){
								ex.printStackTrace();
							}
						}
			    	}
	        	} catch(Exception ex){
	        		ex.printStackTrace();
	        	}
				
			}
//			System.out.println("The answer being returned here is: " + answer);
			return answer;
        }
        
        
        //*************************************************************
        
        //if no solution, return -1
        return -1;
    }
    
//    private boolean pixelSumIntegerMultiple(Frame f1, Frame f2){
//    	double sum1 = 0;
//    	for (Figure f : f1.figures) sum1 += f.pixelCount;
//    	double sum2 = 0;
//    	for (Figure f: f2.figures) sum2 += f.pixelCount;
//    	
//    	System.out.println("Sum1 = " + sum1);
//    	System.out.println("Sum2 = " + sum2);
//    	
//    	if (sum2 > sum1){
//    		if (sum2/sum1 <= 2.05 && sum2/sum1 >= 1.95){
//    			return true;
//    		}
//    	}
//    	else{
//    		if (sum1/sum2 <= 2.05 && sum1/sum2 >= 1.95){
//    			return true;
//    		}
//    	}
//    	return false;
//    }
    
    /*
     * This method resets all of our class variables for Agent
     */
    private void reset(RavensProblem problem){
    	//getting all the answers
    	answerFigures = new ArrayList<>();
    	answerFigures.add(problem.getFigures().get("1"));
    	answerFigures.add(problem.getFigures().get("2"));
    	answerFigures.add(problem.getFigures().get("3"));
    	answerFigures.add(problem.getFigures().get("4"));
    	answerFigures.add(problem.getFigures().get("5"));
    	answerFigures.add(problem.getFigures().get("6"));
    	answerFigures.add(problem.getFigures().get("7"));
    	answerFigures.add(problem.getFigures().get("8"));
    	
    	//reset the transformations
    	horizontalTransformation = new Transformation();
    	verticalTransformation = new Transformation();
    	diagonalTransformation = new Transformation();
    	
    	expected = new Frame();
    	cannotContainThisShape = new Frame();
    	expectedSize = 0;
    	
    	//reset each frame
    	hor1 = new Frame();
    	hor2 = new Frame();
    	ver1 = new Frame();
    	ver2 = new Frame();
    	dia1 = new Frame();
    	dia2 = new Frame();
    	
    	//reset the problem
    	this.problem = problem;
    	
    	takeIntoAccountPixelCount = false;
    }
    
    /*
     * This method will be used to create the horizontal and vertical transformations.
     * In doing so, it will also collect details about the figures it is determining
     * a transformation between.
     */
    private void createTransformation(Transformation t, String keyImage1, String keyImage2, RavensProblem problem){
    	//loading the raven's figures
    	RavensFigure a = problem.getFigures().get(keyImage1);
    	RavensFigure b = problem.getFigures().get(keyImage2);
    	
    	try{
    		t.image1 = ImageIO.read(new File(a.getVisual()));
    		t.image2 = ImageIO.read(new File(b.getVisual()));
    		
//    		//this was used to map the difference between the two images
//    		BufferedImage output = new BufferedImage(hor1.image1.getWidth(), hor1.image1.getHeight(), BufferedImage.TYPE_INT_RGB);
//    		output.getGraphics().setColor(new Color(255,255,255));
//    		output.getGraphics().fillRect(0, 0, output.getWidth(), output.getHeight());
    		
    		for (int i = 0; i < t.image1.getHeight(); i++){
    			for (int j = 0; j < t.image1.getWidth(); j++){
    				Color p1 = new Color(t.image1.getRGB(j, i));
    				Color p2 = new Color(t.image2.getRGB(j, i));
    				
    				//check if there is anything drawn on that pixel
    				if (checkColorPresence(p1)){
    					updateMinMaxDimensions(t.fig1, j, i);
    				}
    				if (checkColorPresence(p2)){
    					updateMinMaxDimensions(t.fig2, j, i);
    				}
    			}
    		}
    		t.pixelDifferenceCounter = Math.abs(t.fig1.pixelCount - t.fig2.pixelCount);
    		
//    		System.out.println("NEXT STAGE");
//    		System.out.println(t.fig1.getHeight());
//    		System.out.println(t.fig2.getHeight());
//    		System.out.println(t.fig1.getWidth());
//    		System.out.println(t.fig2.getWidth());
//    		System.out.println(t.pixelDifferenceCounter);
    		
    		
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    /*
     * This method is used when we are expecting the answer to be a figure already
     * in our problem. This is usually the case when there is no transformation 
     * horizontally or vertically
     */
    private int findSameAnswer(Transformation t, RavensProblem problem){
    	boolean answerFound = false;
    	int answer = -1;
    	int k = 1;
    	
    	while (k <= 8 && !answerFound){
    		Transformation temp = new Transformation();
    		try{
        		temp.image1 = t.image1;
        		temp.fig1 = t.fig1;
        		
        		String answerIndex = "" + k;
        		RavensFigure tempFigure = problem.getFigures().get(answerIndex);
        		
        		temp.image2 = ImageIO.read(new File(tempFigure.getVisual()));
        		
        		for (int i = 0; i < temp.image2.getHeight(); i++){
        			for (int j = 0; j < temp.image2.getWidth(); j++){
        				Color p2 = new Color(temp.image2.getRGB(j, i));
        				
        				//check if there is anything drawn on that pixel
        				if (checkColorPresence(p2)){
        					updateMinMaxDimensions(temp.fig2, j, i);
        				}
        			}
        		}
        		temp.pixelDifferenceCounter = Math.abs(temp.fig1.pixelCount - temp.fig2.pixelCount);
        		
//        		System.out.println("Comparing Pixels with Answer " + k);
//        		System.out.println("The result is: " + temp.pixelDifferenceCounter);
        		
        		if (temp.pixelDifferenceCounter <= 20){ //giving a small margin of error
//        			System.out.println("We found the answer: " + k);
        			answerFound = true;
        			answer = k;
        		}
        	}
        	catch (Exception e){
        		e.printStackTrace();
        	}
    		k++;
    	}
    	
    	return answer;
    }  
    
    /*
     * This method is used to collect details about the figure. It capture the 
     * maximum and minimum height and width of the pixels as well as the number
     * of colored pixels in the figure. This way, we are able to differentiate 
     * between figures with no fill and figures with fill if they have the same
     * dimensions
     */
	private void updateMinMaxDimensions(Figure f, int x, int y){
		//check min and max
		if (x < f.minWidth) f.minWidth = x;
		if (x > f.maxWidth) f.maxWidth = x;
		if (y < f.minHeight) f.minHeight = y;
		if (y > f.maxHeight) f.maxHeight = y;
		if (x < f.leftMostPixel[0]){
			f.leftMostPixel[0] = x;
			f.leftMostPixel[1] = y;
		}
		
		//update counter
		f.pixelCount++;
	}
	
	/*
	 * This method is used to determine whether the pixel is blank or has something
	 * drawn on it. 
	 */
	private boolean checkColorPresence(Color c){
		//something would have to be very close to white to return false
		if (c.getBlue() < 220 && c.getGreen() < 220 && c.getRed() < 220) return true;
		else return false;
	}
	
	/*
	 * This method will help us see if there is any pixel near by that is active.
	 * If there is in fact an active pixel, then it will too become active. This 
	 * method basically checks the continuity of the objects.
	 */
	private boolean nextToActivePixel(int x, int y, BufferedImage image){
		//this will check if the pixels next to this current pixel is activated
		if(image.getRGB(x-1, y-1) == green || image.getRGB(x-1, y-1) == green || 
				image.getRGB(x-1, y-1) == green || image.getRGB(x, y-1) == green || 
				image.getRGB(x+1, y-1) == green || image.getRGB(x-1, y) == green ||
				image.getRGB(x, y) == green || image.getRGB(x+1, y) == green ||
				image.getRGB(x-1, y+1) == green || image.getRGB(x, y+1) == green ||
				image.getRGB(x+1, y+1) == green){
			return true;
		}
		return false;
	}
	
	/*
	 * This method is used to convert the frame into its respective figures.
	 */
	private void createFrame(BufferedImage image, Frame frame){
//		System.out.println("Creating a new Frame for new Figure:");
		while(checkForColoredPixels(image)){
			Figure figure = new Figure();
			recognizeFigure(figure, image);
			removeActivePixels(image);
			frame.figures.add(figure);
		}
//		System.out.println("Done creating figures. There are " + frame.figures.size() + " figures");
	}
	
	/*
	 * This method iteratively goes through each pixel of the frame and finds
	 * consecutive pixels in order to determine what shapes are in the figure
	 */
	private void recognizeFigure(Figure figure, BufferedImage image){
		boolean firstFound = false;
        	
    	//first round of coloring
    	for (int i = 0; i < image.getHeight(); i++){
			for (int j = 0; j < image.getWidth(); j++){
				Color p = new Color(image.getRGB(j, i));
				if (checkColorPresence(p)){
					if (nextToActivePixel(j, i, image)){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
					}
					else if (!firstFound){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
						firstFound = true;
					}
				}
			}
    	}
    	
    	//need to make another round and go backwards to color everything
    	for (int i = 0; i < image.getHeight(); i++){
			for (int j = image.getHeight() - 1; j > 0; j--){
				Color p = new Color(image.getRGB(j, i));
				if (checkColorPresence(p)){
					if (nextToActivePixel(j, i, image)){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
					}
					else if (!firstFound){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
						firstFound = true;
					}
				}
			}
    	}
    	
    	//need to go upwards 
    	for (int i = image.getHeight() - 1; i > 0; i--){
			for (int j = 0; j < image.getWidth(); j++){
				Color p = new Color(image.getRGB(j, i));
				if (checkColorPresence(p)){
					if (nextToActivePixel(j, i, image)){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
					}
					else if (!firstFound){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
						firstFound = true;
					}
				}
			}
    	}
    	
    	//last iteration
    	for (int i = image.getHeight() - 1; i > 0; i--){
			for (int j = image.getHeight() - 1; j > 0; j--){
				Color p = new Color(image.getRGB(j, i));
				if (checkColorPresence(p)){
					if (nextToActivePixel(j, i, image)){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
					}
					else if (!firstFound){
						image.setRGB(j, i, green);
						updateMinMaxDimensions(figure, j, i);
						firstFound = true;
					}
				}
			}
    	}
    	
    	figure.leftMostPixel[0] = figure.leftMostPixel[0] - figure.minWidth;
    	figure.leftMostPixel[1] = figure.leftMostPixel[1] - figure.minHeight;
    	
//    	System.out.println("Height of Figure is :" + figure.getHeight());
//    	System.out.println("Width of Figure is :" + figure.getWidth());
//    	System.out.println("Count of Figure is :" + figure.pixelCount);
    	
//    	//TODO
//    	//delete later *********************************
//    	try {
//			ImageIO.write(image, "png", new File("TestImage.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	//delete later *********************************
	}
	
	/*
	 * This method removes the active pixels after the figure has been extracted.
	 * It is used to detect other figures (each figure is continuous)
	 */
	private void removeActivePixels(BufferedImage image){
		for (int i = 0; i < image.getHeight(); i++){
			for (int j = 0; j < image.getWidth(); j++){
				Color p = new Color(image.getRGB(j, i));
				if (image.getRGB(j, i) == green) image.setRGB(j, i, white);
			}
		}
	}
	
	/*
	 * This method is used to detect if any more pixels with drawing on it remain 
	 * in the figure
	 */
	private boolean checkForColoredPixels(BufferedImage image){
		for (int i = 0; i < image.getHeight(); i++){
			for (int j = 0; j < image.getWidth(); j++){
				Color p = new Color(image.getRGB(j, i));
				if (checkColorPresence(p)) return true;
			}
		}
		return false;
	}
	
	private int solveProblem(){
		//check trivial solution
		if (hor1.equals(hor2)){
			expected = hor1;
			expectedSize = hor1.figures.size();
		}
		else if (ver1.equals(ver2)){
			expected = ver1;
			expectedSize = ver1.figures.size();
		}
		else if (dia1.equals(dia2)){
			expected = dia1;
			expectedSize = dia1.figures.size();
		}
		else{
			expectedFrame();
		}
		//now we have non-trivial solution
		
//		System.out.println("Size of Expected: " + expectedSize);
//		System.out.println("It must have" + expected.figures.toString());
		if (cannotContainThisShape != null && cannotContainThisShape.figures.size() != 0){
//			System.out.println("It cannot contain: " + cannotContainThisShape.figures.toString());
		}
//		System.out.println();
		
		
		int answer = -1;
		for (int i = 0; i < 8; i++){
			BufferedImage image;
			try {
				image = ImageIO.read(new File(answerFigures.get(i).getVisual()));
				Frame temp = new Frame();
				createFrame(image, temp);
//				System.out.println("Our answer " + (i+1) + " looks like: ");
//				System.out.println(temp.figures.toString());	
				//New way to find the answer
				if (compareAnswer(temp)){
					answer = i+1;
					break;
				}
//				else{
//					System.out.println("We get here");
//					image = ImageIO.read(new File(answerFigures.get(i).getVisual()));
//					temp = new Frame();
//					createFrame(image, temp);
//					System.out.println(temp == null);
//					System.out.println(temp.figures.get(0));
//					Figure fig = temp.figures.get(0);
//					if (expected.figures.contains(fig)
//							&& fig.pixelCount != restrictedPixels[0] && 
//							fig.pixelCount != restrictedPixels[1]){
//						answer = i + 1;
//						break;
//					}
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	private boolean compareAnswer(Frame frame){
		//int counter = 0;
		Frame temp = new Frame();
		for (Figure f : frame.figures){
			temp.figures.add(f);
		}
		
		if (temp.figures.size() != expectedSize) return false;
		for (Figure f : expected.figures){
			if (temp.figures.contains(f)){
				temp.figures.remove(f);
			}
			else return false;
		}
		if (cannotContainThisShape != null && cannotContainThisShape.figures.size() != 0){
			for (Figure f : cannotContainThisShape.figures){
				if (temp.figures.contains(f)) return false;
			}
		}
		return true;
	}
	
	private void expectedFrame(){
		
		//check if the number of figures in each way is the same or not
		if (hor1.figures.size() == ver1.figures.size() && hor1.figures.size() == dia1.figures.size()
				&& ver1.figures.size() == dia1.figures.size() && somethingInCommon(hor1, hor2)
				&& somethingInCommon(ver1, ver2) && somethingInCommon(dia1, dia2)){
			expectedSize = hor1.figures.size();
//			System.out.println("Commonality in overal size of transformation");
			for (Figure a : hor1.figures){
				if (hor2.figures.contains(a)){
					expected.figures.add(a);
					hor2.figures.remove(a);
				}
			}
			
			//check what frames are there in the vertical transformation
			for (Figure a : ver1.figures){
				if (ver2.figures.contains(a)){
					expected.figures.add(a);
					ver2.figures.remove(a);
				}
			}
			
			//check what frames are there in the vertical transformation
			for (Figure a : dia1.figures){
				if (dia2.figures.contains(a)){
					expected.figures.add(a);
					dia2.figures.remove(a);
				}
			}
		}
		else {
			//check which one has the common size
//			System.out.println("No commonality in overal size of transformation");
			expectedSize = 0;

			if (hor1.figures.size() == hor2.figures.size() && somethingInCommon(hor1, hor2)){
//				System.out.println("Horizontal Transformation to go");
				expectedSize = hor1.figures.size();
				for (Figure a : hor1.figures){
					if (hor2.figures.contains(a)){
						expected.figures.add(a);
						hor2.figures.remove(a);
					}
					else {
						cannotContainThisShape.figures.add(a);
					}
				}
				for (Figure a : hor2.figures){
					cannotContainThisShape.figures.add(a);
				}
				//expectedSize += cannotContainThisShape.figures.size();
			}
			else if (ver1.figures.size() == ver2.figures.size() && somethingInCommon(ver1, ver2)){
//				System.out.println("Vertical Transformation to go");
				expectedSize = ver1.figures.size();
				for (Figure a : ver1.figures){
					if (ver2.figures.contains(a)){
						expected.figures.add(a);
						ver2.figures.remove(a);
					}
					else {
						cannotContainThisShape.figures.add(a);
					}
				}
				for (Figure a : ver2.figures){
					cannotContainThisShape.figures.add(a);
				}
				//expectedSize += cannotContainThisShape.figures.size();
			}
			else if (dia1.figures.size() == dia2.figures.size() && somethingInCommon(dia1, dia2)){
//				System.out.println("Diagonal Transformation to go");
				expectedSize = dia1.figures.size();
				for (Figure a : dia1.figures){
					if (dia2.figures.contains(a)){
						expected.figures.add(a);
						dia2.figures.remove(a);
					}
					else {
						cannotContainThisShape.figures.add(a);
					}
				}
				for (Figure a : dia2.figures){
					cannotContainThisShape.figures.add(a);
				}
				//expectedSize += cannotContainThisShape.figures.size();
			}
			
			//This means we have to look at the diagonal
			else{
//				System.out.println("This is a crazy transformation!");
				RavensFigure b = problem.getFigures().get("B");
	        	RavensFigure d = problem.getFigures().get("D");
	        	
	        	Frame dia3 = new Frame();
	        	Frame dia4 = new Frame();

	        	BufferedImage imageB, imageD;
				try {
					imageB = ImageIO.read(new File(b.getVisual()));
					imageD = ImageIO.read(new File(d.getVisual()));
					createFrame(imageB, dia3);
					createFrame(imageD, dia4);
//					
//					System.out.println(dia3.figures.size());
//					System.out.println(dia4.figures.size());
//					
//					System.out.println(dia3.figures.toString());
//					System.out.println(dia4.figures.toString());
					
					if (dia1.figures.size() == dia2.figures.size() && somethingInCommon(dia3, dia4) && 
							dia3.figures.size() != dia4.figures.size()){
						expectedSize = dia1.figures.size();
						getMissingFigure(dia3, dia4);
					}
					else {
						expectedSize = dia3.figures.size();
						takeIntoAccountPixelCount = true;
						restrictedPixels = new int[2];
						restrictedPixels[0] = dia3.figures.get(0).pixelCount;
						restrictedPixels[1] = dia4.figures.get(0).pixelCount;
						expected.figures.add(dia3.figures.get(0));
					}
					
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}	
		}
	}
	
	private void getMissingFigure(Frame dia3, Frame dia4){	
		if (dia3.figures.size() != dia4.figures.size()){
			for (Figure f : dia3.figures){
				if (dia4.figures.contains(f)){

					int pixelOfDia3 = f.pixelCount;
					
					int index = dia4.figures.indexOf(f);
					int pixelOfDia4 = dia4.figures.get(index).pixelCount;
					
					int pixelOfDia = dia1.figures.get(0).pixelCount;
					
					if (Math.abs(pixelOfDia - pixelOfDia3) < Math.abs(pixelOfDia - pixelOfDia4)){
//						System.out.println("Dia3 is chosen");
						expected.figures.add(f);
					}
					else{
//						System.out.println("Dia4 is chosen");
						expected.figures.add(dia4.figures.get(index));
					}
				}
			}
		}
	}
	
	private boolean somethingInCommon(Frame f1, Frame f2){
		for (Figure a : f1.figures){
			if (f2.figures.contains(a)){
				return true;
			}
		}
		return false;
	}
	
    //Private class Figure
    private class Figure{
    	
    	//protected int size;
    	protected int pixelCount;
    	protected int maxHeight;
    	protected int minHeight;
    	protected int maxWidth;
    	protected int minWidth;
    	protected int[] leftMostPixel;
    	
    	
    	public Figure(){
    		maxHeight = -1;
        	minHeight = 100000;
        	maxWidth = -1;
        	minWidth = 100000;
        	pixelCount = 0;
        	leftMostPixel = new int[2];
        	leftMostPixel[0] = 100000;
        	leftMostPixel[1] = 100000;
    	}
    	
    	public int getHeight(){ return maxHeight - minHeight; }
    	public int getWidth(){ return maxWidth - minWidth; }
    	
    	@Override
    	public boolean equals(Object o){
    		if (o == null) return false;
    		if (o == this) return true;
    		if (!(o instanceof Figure)) return false;
    		//test attributes here
    		Figure obj = (Figure) o;
    		if (Math.abs(obj.getHeight() - this.getHeight()) <= 5 && Math.abs(obj.getWidth() - this.getWidth()) <= 5
    				&& (obj.leftMostPixel[0] - this.leftMostPixel[0]) <= 2
    						&& Math.abs(obj.leftMostPixel[1] - this.leftMostPixel[1]) <= 2){
    			return true;
    		}
    		else {
				return false;
			}
    	}
    	
    	@Override
    	public String toString(){
    		return "Figure Height: " + this.getHeight() + " ,Figure Width: " + this.getWidth()
    				+ " Figure Pixels: " + this.pixelCount + " Left-Most: " + this.leftMostPixel[0] 
    						+ ", " + this.leftMostPixel[1];
    	}
    }
    
    private class Frame{
    	
    	protected ArrayList<Figure> figures;
    	
    	public Frame(){
    		figures = new ArrayList<>();
    	}
    	
    	@Override
    	public boolean equals(Object o){
    		if (o == null) return false;
    		if (o == this) return true;
    		if (!(o instanceof Frame)) return false;
    		//test attributes here
    		Frame obj = (Frame) o;
    		if (obj.figures.size() != this.figures.size()) return false;
    		
    		for (Figure f : obj.figures){
    			if (!this.figures.contains(f)) return false;
    		}
    		return true;
    	}
    }
    
    private class Transformation{
    	protected int pixelDifferenceCounter;
    	protected Figure fig1;
    	protected Figure fig2;
    	
    	protected BufferedImage image1;
    	protected BufferedImage image2;
    	
    	public Transformation(){
    		fig1 = new Figure();
    		fig2 = new Figure();
    	}
    	
    }
}
