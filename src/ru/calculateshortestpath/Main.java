package ru.calculateshortestpath;
	
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Main extends Application {
	Graph graph;
	BorderPane root;
	@Override
	
	public void start(Stage primaryStage) {
		try {
			graph = createGraph();

			root = new BorderPane();
			//HBox hBox = addHBox();
			GridPane gridTop = addGridTop();
			GridPane gridLeft = addGridLeft();
			GridPane gridBottom = addGridBottom();
			BorderPane centerPane = addBorderPaneCenter();
			
			//root.setTop(hBox);
			root.setTop(gridTop);
			root.setLeft(gridLeft);
			root.setCenter(centerPane);	
			root.setBottom(gridBottom);
			
			renderGraph2(graph, centerPane);
			
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public HBox addHBox() {
		HBox hBox = new HBox();
		
	    hBox.setPadding(new Insets(15, 12, 15, 12));
	    hBox.setSpacing(10);
	    hBox.setStyle("-fx-background-color: #336699;");
	    
	    Button buttonCalculateAllGraph = new Button("Calculate All Graph");
	    buttonCalculateAllGraph.setPrefSize(150, 20);		
	    buttonCalculateAllGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				calculateShortestPathAllGraph();
			}
	    	
	    });
	    hBox.getChildren().add(buttonCalculateAllGraph);
	    return hBox;
		
	}
	 
	private GridPane addGridTop() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(15, 12, 15, 12));
		gridPane.setStyle("-fx-background-color: #336699;");
		
		//Button CalculateAllGraph in column1, row 1
	    Button buttonCalculateAllGraph = new Button("Calculate All Graph");
	    buttonCalculateAllGraph.setPrefSize(150, 20);		
	    buttonCalculateAllGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				calculateShortestPathAllGraph();
			}
	    	
	    });
		gridPane.add(buttonCalculateAllGraph, 0, 0);

		//Label Start in column 2 row 1
	    Text lblStart = new Text("Start:");
	    lblStart.setFill(Color.WHITE);
	    lblStart.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblStart, 1, 0);
		
	    //Choice box for StartNode in column 3 row 1
	    ChoiceBox<String> cboxStart = new ChoiceBox<String>(); 
	    cboxStart.setId("cboxStart");
	    gridPane.add(cboxStart, 2, 0);
	    
		//Label Start in column 4 row 1
	    Text lblEnd = new Text("End:");
	    lblEnd.setFill(Color.WHITE);
	    lblEnd.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblEnd, 3, 0);

	    //Choice box for EndNode in column 5 row 1
	    ChoiceBox<String> cboxEnd = new ChoiceBox<String>(); 
	    cboxEnd.setId("cboxEnd");
	    gridPane.add(cboxEnd, 4, 0);

	    List<String> orderedList = sortNodeNames(graph.getNodes());
	    
	    /*
	    for(Node node: graph.getNodes()) {
	    	cboxStart.getItems().add(node.getName());
	    	cboxEnd.getItems().add(node.getName());
	    }
	    */
	    for(String str: orderedList) {
	    	cboxStart.getItems().add(str);
	    	cboxEnd.getItems().add(str);	    	
	    }
	    
	    cboxStart.setValue(cboxStart.getItems().get(0));
	    cboxEnd.setValue(cboxEnd.getItems().get(0));
	    
	    // Button for starting the calculation on column 6 row 1
	    Button buttonCalculateShortestPath = new Button("Calculate path");
	    buttonCalculateShortestPath.setPrefSize(150, 20);		
	    buttonCalculateShortestPath.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				calculateShortestPath();
			}
	    	
	    });
		gridPane.add(buttonCalculateShortestPath, 5, 0);
	    
	    return gridPane;
	}
	
	private GridPane addGridLeft() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(0, 10, 0, 10));
		
		// Label FromTo in column 1, row 1
	    Text lblFromTo = new Text("From/To:");
	    lblFromTo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblFromTo, 0, 0);
	    
		// Text From in column 2, row 1
	    Text txtFrom = new Text("#");
	    txtFrom.setId("txtFrom");
	    txtFrom.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtFrom, 1, 0);
	    
		// Text To in column 3, row 1
	    Text txtTo = new Text("#");
	    txtTo.setId("txtTo");
	    txtTo.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtTo, 2, 0);
	    
		// Label Dijkstra in column 1, row 2
	    Text lblDijkstra = new Text("Dijkstra:");
	    lblDijkstra.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblDijkstra, 0, 1);
	    
		// Text DijkstraStart in column 2, row 2
	    Text txtDijkstraStart = new Text("ms.");
	    txtDijkstraStart.setId("txtDijkstraStart");
	    txtDijkstraStart.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtDijkstraStart, 1, 1);
	    
		// Text DijkstraEnd in column 3, row 2
	    Text txtDijkstraEnd = new Text("#");
	    txtDijkstraEnd.setId("txtDijkstraEnd");
	    txtDijkstraEnd.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtDijkstraEnd, 2, 1);
		
		// Label AStar in column 1, row 3
	    Text lblAStar = new Text("A*:");
	    lblAStar.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblAStar, 0, 2);
	    
		// Text AStarStart in column 2, row 3
	    Text txtAStarStart = new Text("#");
	    txtAStarStart.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtAStarStart, 1, 2);
	    
		// Text AStarEnd in column 3, row 3
	    Text txtAStarEnd = new Text("#");
	    txtAStarEnd.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtAStarEnd, 2, 2);
		
		// Label BreadthFirst in column 1, row 4
	    Text lblBreadthFirst = new Text("Breadth First:");
	    lblBreadthFirst.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblBreadthFirst, 0, 3);
	    
		// Text BreadthFirstStart in column 2, row 4
	    Text txtBreadthFirstStart = new Text("#");
	    txtBreadthFirstStart.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtBreadthFirstStart, 1, 3);
	    
		// Text BreadthFirstEnd in column 3, row 4
	    Text txtBreadthFirstEnd = new Text("#");
	    txtBreadthFirstEnd.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtBreadthFirstEnd, 2, 3);
		
		return gridPane;
	}
	
	private GridPane addGridBottom() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(0, 10, 0, 10));
		
		// Label FromTo in column 1, row 1
	    Text lblPathDijkstra = new Text("Path Dijkstra:");
	    lblPathDijkstra.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    gridPane.add(lblPathDijkstra, 0, 0);
	    
		// Text From in column 2, row 1
	    Text txtPathDijkstra = new Text("#");
	    txtPathDijkstra.setId("txtPathdijkstra");
	    txtPathDijkstra.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
	    gridPane.add(txtPathDijkstra, 1, 0);
	    
		return gridPane;
	}
	
	private BorderPane addBorderPaneCenter() {
		BorderPane borderPane = new BorderPane();
		
		return borderPane;
	}

	
	private Graph createGraph() {
		Node nodeA01 = new Node("A01",new Point("A01", 20,20));
		Node nodeB02 = new Node("B02", new Point("B02", 120, 50));
		Node nodeC03 = new Node("C03", new Point("C03", 30, 80));
		Node nodeD04 = new Node("D04", new Point("D04", 100, 120)); 
		Node nodeE05 = new Node("E05", new Point("E05", 80, 150));
		Node nodeF06 = new Node("F06", new Point("F06", 160, 180));
		Node nodeG07 = new Node("G07", new Point("G07", 20, 230));
		Node nodeH08 = new Node("H08", new Point("H08", 100, 260)); 
		Node nodeI09 = new Node("I09", new Point("I09", 180, 240));
		Node nodeJ10 = new Node("J10", new Point("J10", 200, 90));
		
		nodeA01.addDestination(nodeB02, 10);
		nodeA01.addDestination(nodeC03, 15);
		
		nodeB02.addDestination(nodeC03, 4);
		nodeB02.addDestination(nodeD04, 6);
		
		nodeC03.addDestination(nodeD04, 5);
		nodeC03.addDestination(nodeE05, 12);
		
		nodeD04.addDestination(nodeF06, 3);
		
		nodeE05.addDestination(nodeG07, 5);
		
		nodeF06.addDestination(nodeH08, 14);
		nodeF06.addDestination(nodeJ10, 6);
		
		nodeG07.addDestination(nodeH08, 5);
		
		nodeH08.addDestination(nodeI09, 8);
		
		nodeI09.addDestination(nodeJ10, 2);
		
		Graph graph = new Graph();
		
		graph.addNode(nodeA01);
		graph.addNode(nodeB02);
		graph.addNode(nodeC03);
		graph.addNode(nodeD04);
		graph.addNode(nodeE05);
		graph.addNode(nodeF06);
		graph.addNode(nodeG07);
		graph.addNode(nodeH08);
		graph.addNode(nodeI09);
		graph.addNode(nodeJ10);
		
		return graph;
		
	}

	public void renderGraph(Graph graph, BorderPane borderPane) {
		Iterator<Node> i = graph.getNodes().iterator();
		ObservableList<javafx.scene.Node> list = borderPane.getChildren();

		//Create a Path
		Path path = new Path();
		
		path.setStroke(Color.BLACK);

		MoveTo moveTo;
		LineTo line;
		Text text;
		
		while(i.hasNext()) {
			Node node = i.next();
			
			float startX = node.getCoordinates().getX();
			float startY = node.getCoordinates().getY();
			
			Set<Node> keySet = node.getAdjacentNodes().keySet();
			Iterator<Node> k = keySet.iterator();
			
			//System.out.println(node.getName() + " " + keySet.size());
			text = new Text();
			text.setFont(new Font(12));
			text.setX(startX);
			text.setY(startY);
			text.setText(node.getName());
			list.add(text);
						
			while(k.hasNext()) {
				Node adjNode = k.next();
				float endX = adjNode.getCoordinates().getX();
				float endY = adjNode.getCoordinates().getY();
				
				//Set the starting point
				moveTo = new MoveTo(startX,startY);
				
				//Draw the line
				line = new LineTo(endX, endY);		
				
				path.getElements().add(moveTo);
				path.getElements().add(line);
			}
			
		}
		
		list.add(path);
		
	}
	private void renderGraph2(Graph graph, BorderPane borderPane) {
		Iterator<Node> i = graph.getNodes().iterator();
		ObservableList<javafx.scene.Node> list = borderPane.getChildren();
		Line line;
		Text text;
		Text lblDistance;
		
		while(i.hasNext()) {
			Node node = i.next();
			
			float startX = node.getCoordinates().getX();
			float startY = node.getCoordinates().getY();
			
			Set<Node> keySet = node.getAdjacentNodes().keySet();
			Iterator<Node> k = keySet.iterator();
			
			//System.out.println(node.getName() + " " + keySet.size());
			text = new Text();
			text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
			text.setX(startX);
			text.setY(startY);
			text.setText(node.getName());
			list.add(text);
						
			while(k.hasNext()) {
				Node adjNode = k.next();
				float endX = adjNode.getCoordinates().getX();
				float endY = adjNode.getCoordinates().getY();
				
				//Draw the line
				line = new Line(startX, startY, endX, endY);
				line.setStroke(Color.BLACK);
				list.add(line);
				
				//create label with distance
				lblDistance = new Text();
				float lblX = 0;
				float lblY = 0;
				if (endX > startX) {
					lblX = startX + ((endX - startX)/2);
				} else {
					lblX = startX - ((startX - endX)/2);
				}
				if (startY < endY) {
					lblY = startY + ((endY - startY)/2);
				} else {
					lblY = startY - ((startY - endY)/2);
				}
				
				lblDistance.setFont(new Font(12));
				lblDistance.setX(lblX);
				lblDistance.setY(lblY);
				lblDistance.setText(Integer.toString(node.getAdjacentNodes().get(adjNode)));
				list.add(lblDistance);
			}
		}
	}

	private void calculateShortestPathAllGraph() {
		
		Node firstNode = null;
		for (Node node: this.graph.getNodes()) {
			if (node.getName().equals("A01")) {
				firstNode = node;
				break;
			}
		}
		
		System.out.println("calculate " + firstNode.getName());
		//graph =graph.resetGraph(graph);
		
		LocalDateTime startTime = LocalDateTime.now();
		
		graph = graph.dijkstraCalculateShortestPathAll(graph, firstNode);
		
		LocalDateTime endTime = LocalDateTime.now();
		long milliSeconds = ChronoUnit.MILLIS.between(startTime, endTime);
		
		System.out.println("Start time: " + startTime);
		System.out.println("End   time: " + endTime);
		System.out.println("Duration:   " + milliSeconds);
		
		StringBuffer sb = new StringBuffer();
		int firstTime = 0;
		
		for (Node node: this.graph.getNodes()) {
			if (node.getName().equals("J10")) {
				List<Node> nodePath = node.getShortestPath();
				for(Node nn: nodePath) {
					sb.append((firstTime == 0) ? "" : " -> ");
					sb.append(nn.getName());
					firstTime = 1;
					System.out.println("Node: "+ nn.getName());
				}
				sb.append(" -> " + node.getName());				
			}
		}

		// Fill the GridPane
	
		ShowResultInLeftPane(root, "A01", "J10",milliSeconds);
		
		ShowPathInBottomPane(root, sb);
				
	}
	
	private void calculateShortestPath() {
		String strStartNode = "";
		String strEndNode = "";
		
		// Retrieve the being and the end of the path chosen
		GridPane gridPaneTop = (GridPane)root.getTop();

		ObservableList<javafx.scene.Node> listPaneTop = gridPaneTop.getChildren();
		Iterator<javafx.scene.Node> iPaneTop = listPaneTop.iterator();
		
		while (iPaneTop.hasNext()) {
			javafx.scene.Node node = iPaneTop.next();
			
			if(node instanceof ChoiceBox) {
				ChoiceBox<String> tmpChoiceBox = ((ChoiceBox<String>)node);
				if((tmpChoiceBox.getId() != null) && (tmpChoiceBox.getId() == "cboxStart")) {
					strStartNode = tmpChoiceBox.getValue();
				}
				if((tmpChoiceBox.getId() != null) && (tmpChoiceBox.getId() == "cboxEnd")) {
					strEndNode = tmpChoiceBox.getValue();
				}
			}
		}
		
		Node startNode = null;
		Node endNode = null;
		
		if(strStartNode.equals(strEndNode)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Start node can not be equal to End node");
			alert.setContentText("Start node: " + strStartNode + " - End node: " + strEndNode);
			alert.showAndWait();
		} else {
			
			// set the startNode and the endNode in the graph
			for (Node node: this.graph.getNodes()) {
				if (node.getName().equals(strStartNode)) {
					startNode = node;
				}
				if (node.getName().equals(strEndNode)) {
					endNode = node;
				}
			}
			
			LocalDateTime startTime = LocalDateTime.now();
			
			if (strStartNode.compareTo(strEndNode) < 0) {
				// forward
				graph = graph.dijkstraCalculateShortestPath(graph, startNode, endNode);
			} else {
				// backward
				graph = graph.dijkstraCalculateShortestPath(graph, endNode, startNode);
			}
			
			LocalDateTime endTime = LocalDateTime.now();
			long milliSeconds = ChronoUnit.MILLIS.between(startTime, endTime);
			
			System.out.println("Start time: " + startTime);
			System.out.println("End   time: " + endTime);
			System.out.println("Duration:   " + milliSeconds);
			
			StringBuffer sb = new StringBuffer();
			int firstTime = 0;
						
			if (strStartNode.compareTo(strEndNode) < 0) {
				for (Node node: this.graph.getNodes()) {
					if (node.getName().equals(strEndNode)) {
						List<Node> nodePath = node.getShortestPath();
						for(Node nn: nodePath) {
							sb.append((firstTime == 0) ? "" : " -> ");
							sb.append(nn.getName());
							firstTime = 1;
							System.out.println("Node: "+ nn.getName());
						}
						sb.append(" -> " + node.getName());				
					}
				}
			} else {
				for (Node node: this.graph.getNodes()) {
					if (node.getName().equals(strStartNode)) {
						sb.append(node.getName());				
						List<Node> nodePath = node.getShortestPath();
						ListIterator<Node> listIterator = node.getShortestPath().listIterator(node.getShortestPath().size());
						while(listIterator.hasPrevious()) {
							Node nn = listIterator.previous();
							sb.append(" -> " + nn.getName());
							firstTime = 1;
							System.out.println("Node bb: "+ nn.getName());							
						}
					}
				}				
			}

			// Fill the GridPane
		
			ShowResultInLeftPane(root, strStartNode, strEndNode, milliSeconds);
			
			ShowPathInBottomPane(root, sb);
		}
		
	}
	
	public void ShowResultInLeftPane(
			BorderPane root, String startNode, String endNode, double milliSeconds) {
		
		GridPane gridPaneLeft = (GridPane)root.getLeft();
		
		ObservableList<javafx.scene.Node> listPaneLeft = gridPaneLeft.getChildren();
		Iterator<javafx.scene.Node> iPaneLeft = listPaneLeft.iterator();
		
		while(iPaneLeft.hasNext()) {
			javafx.scene.Node node = iPaneLeft.next();
			
			if(node instanceof Text) {
				Text tmpText = ((Text)node);
				if ((tmpText.getId() != null) && (tmpText.getId().equals("txtFrom"))) {
					tmpText.setText(startNode);
				}
				if ((tmpText.getId() != null) && (tmpText.getId().equals("txtTo"))) {
					tmpText.setText(endNode);
				}
				if ((tmpText.getId() != null) && (tmpText.getId().equals("txtDijkstraEnd"))) {
					tmpText.setText(String.valueOf(milliSeconds));
				}
			}
		}
	}
	
	public void ShowPathInBottomPane(BorderPane root, StringBuffer sb) {
		GridPane gridPaneBottom = (GridPane)root.getBottom();

		ObservableList<javafx.scene.Node> listPaneBottom = gridPaneBottom.getChildren();
		Iterator<javafx.scene.Node> iPaneBottom = listPaneBottom.iterator();
		
		while(iPaneBottom.hasNext()) {
			javafx.scene.Node node = iPaneBottom.next();
			
			if(node instanceof Text) {
				Text tmpText = (Text)node;
				if((tmpText.getId()!= null && (tmpText.getId().equals("txtPathdijkstra")))) {
					tmpText.setText(sb.toString());
				}
			}
		}
				
		
	}
    public List<String> sortNodeNames(Set<Node> nodes) {
		Set<String> unorderedSet = new HashSet<String>();
		for (Node tmpNode: nodes) {
			unorderedSet.add(tmpNode.getName());
		}
		
		List<String> orderedList = new ArrayList<String>(unorderedSet);
		Collections.sort(orderedList);
		return orderedList;
    }

}