/**
 * Author Name:
 * This program is a graphics program consisting GUI components and allows 
 * interaction with the user through GUI components.
 * The program reads user names from a file and populates the combo box with the names
 * in the file.
 * The file contains the names of people and their coordinates in X, Y. Once read the 
 * coordinates, the users are simulated with an image on the screen.
 */
import acm.graphics.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import acm.gui.*;
import acm.program.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class AquaProject extends GraphicsProgram {
	private Font font = new Font("Verdana", Font.PLAIN, 12);
	/**
	 * Class Instance Variables and Constants
	 */
	private JLabel title;
	private JLabel infoLabel;
	private JLabel nameLabel;
	private JLabel coordinates;
	private JComboBox<String> aquaSelectCombo;
	private JComboBox<String> fishSelectCombo;
	
	private DoubleField weightField;
	private DoubleField costField;
	private JTextField nameField;
	private JTextField coordinatesField;
	private JButton selectButton;
	private JButton editCoordinate;
	private JButton scaleButton;
	private JButton deleteButton;
	private JButton updateButton;
	private JSlider slider;
	private JButton newFish;
	private JButton animateButton;
	private JButton saveButton;
	private JButton reportButton;
	boolean animate = false;
	
	private int mouseX;
	private int mouseY;
	private String name="";
	private String currentJob="";
	int index = 0;
	boolean check=false;
	ArrayList<String> names;
	ArrayList<GPoint> locations;
	ArrayList<GRect> rects;
	ArrayList<String> infoCol;
	ArrayList<Double> weightList;
	ArrayList<Double> costList;
	ArrayList<Double> totalCostList;
	ArrayList<GImage> fishes;
	ArrayList<String> fishNames;
	ArrayList<String> fishFileNames;
	private static final String FISH_FILE = "fishList.txt";
	private static final String AQUA_FILE = "aquariumCol.txt";
	private static final String OUTPUT_FILE = "output.txt";
	//String nameList [];
	//DefaultComboBoxModel<String> model;
	public void init() {	
		setSize(1200,800);
		playThemeSong(THEME_SONG);// You can delete this line if you don't want to play the theme song.
		GImage back = new GImage("water_underwater.jpg");
		add(back);
		names = new ArrayList<String>();
		locations = new ArrayList<GPoint>();
		rects = new ArrayList<GRect>();
		fishes = new ArrayList<GImage>();
		infoCol = new ArrayList<String>();
		weightList = new ArrayList<Double>();
		costList = new ArrayList<Double>();
		totalCostList = new ArrayList<Double>();
		//Read Files
		readFishFile(FISH_FILE);
		readAquaFile(AQUA_FILE);
		
		
		//Creating GUI
		// north
		title = new JLabel ("Comp130 Aquarium");
		add (title, NORTH);
		
		
		// west gui
		add(new JLabel ("Fish Collection"), WEST);
		aquaSelectCombo = new JComboBox<String>();
		aquaSelectCombo.setEditable(true);
		add(aquaSelectCombo, WEST);
		createAquaList();
		selectButton=new JButton("Select");
		add(selectButton,WEST);
		selectButton=new JButton("Delete");
		add(selectButton,WEST);
		nameLabel = new JLabel("Name");
		add(nameLabel, WEST);
		nameField = new JTextField("Name");
		nameField.setColumns(10);
		nameField.setEditable(false);
		nameField.addActionListener(this);
		add(nameField, WEST);
		coordinates = new JLabel("Coordinates");
		add(coordinates, WEST);
		coordinatesField = new JTextField("Coordinates");
		coordinatesField.setColumns(10);
		coordinatesField.setEditable(true);
		add(coordinatesField, WEST);
		add(new JLabel ("Weight"), WEST);
		weightField = new DoubleField();
		weightField.setColumns(10);
		weightField.setEditable(false);
		add(weightField, WEST);
		add(new JLabel ("Cost"), WEST);
		costField = new DoubleField();
		costField.setColumns(10);
		costField.setEditable(false);
		add(costField, WEST);
		editCoordinate = new JButton("Edit Coordinates");
		add(editCoordinate, WEST);
		animateButton = new JButton("Animate");
		add(animateButton, WEST);
		updateButton=new JButton("Update");
		add(updateButton,WEST);	
		slider = new JSlider(1,3, 2);
		add(slider, WEST);
		scaleButton=new JButton("Scale");
		add(scaleButton,WEST);	
		
		// east gui buttons
		fishSelectCombo = new JComboBox<String>();
		add(fishSelectCombo, EAST);
		//createFishList();
		add (fishSelectCombo, EAST);
		newFish = new JButton("New Fish");
		add(newFish, EAST);
		createFishList();
		
		//south gui
		
		saveButton = new JButton("Save Aquairum");
		add(saveButton, SOUTH);
		reportButton = new JButton("Generate Report");
		add(reportButton, SOUTH);
		
		createFishes();
		// Your code ends here
		addActionListeners();
		addMouseListeners();
	}
	
	
	public void mouseClicked(MouseEvent e) {		
		mouseX=e.getX();
		mouseY=e.getY();	
		if(currentJob.equals("Edit Coordinates")) {	
			println("editing coor");
			GPoint temp = locations.get(index);
			temp.setLocation(mouseX, mouseY);
			coordinatesField.setText("X: " + mouseX + "\nY: " + mouseY);
			updateFishes();
		}
		reportButton.setEnabled(false);
	}
	public void updateCombo() {
		for (int i=0; i< names.size(); i++) {
			aquaSelectCombo.removeItemAt(i);
			println("adding" + names.get(i));
			aquaSelectCombo.addItem(names.get(i));
		}
		aquaSelectCombo.repaint();
		repaint();
	}
	public void actionPerformed(ActionEvent e) {
		currentJob= e.getActionCommand();
		name=(String) aquaSelectCombo.getSelectedItem();
		if (e.getSource() == nameField) {
			String name = nameField.getText();
			names.set(index, name);
			aquaSelectCombo.removeItemAt(index);
			//aquaSelectCombo.removeItem(name);
			aquaSelectCombo.addItem(name);
		} else if(currentJob.equals("Update")){
			//println("old value " + names.get(index));
			names.set(index, nameField.getText());
			weightList.set(index,  weightField.getValue());
			totalCostList.set(index,  costField.getValue());
			
			//nameList[index] = nameField.getText();
			//println("new value " + nameField.getText());
			//aquaSelectCombo.repaint();
			fishes.get(index).setColor(null);
			repaint();
			//aquaSelectCombo.
			//aquaSelectCombo.removeAll();
			//updateCombo();
			
			//aquaSelectCombo.removeItemAt(index);
			//aquaSelectCombo.removeItem(name);
			//aquaSelectCombo.addItem(name);
			//infoCol.set(index, infoField.getText());

		}else if(currentJob.equals("Select")) {
			//GPoint tempPoint=nameTable.get(name);
			animate = false;
			index = aquaSelectCombo.getSelectedIndex();
			GPoint tempPoint = locations.get(index);
			String name = names.get(index);
			
			Double weight = weightList.get(index);
			Double cost = totalCostList.get(index);
			setInfoField(name, tempPoint.getX(), tempPoint.getY(), weight, cost);
			fishes.get(index).setColor(Color.LIGHT_GRAY);
				
			reportButton.setEnabled(true);
		}else if(currentJob.equals("Delete")) {
			index = aquaSelectCombo.getSelectedIndex();
			aquaSelectCombo.removeItemAt(index);
			//aquaSelectCombo.setSelectedIndex(index-1);
			names.remove(index);
			weightList.remove(index);
			totalCostList.remove(index);
			//setInfoField(name, tempPoint.getX(), tempPoint.getY(), tempString, weight, cost);
			fishes.remove(index);	
			repaint();
			reportButton.setEnabled(true);
		}else if(e.getSource() == animateButton) {
			println("animate");
			fishes.get(index).setColor(null);
			for (int i=0; i<10; i++) {
				println("moving");
				fishes.get(index).move(20, 0);
				repaint();
				pause(200);
			}
		} else if(currentJob.equals("New Fish")) {
			String filename = getFileName();	
			String fishName = getFishName();
			int aquaSize = fishes.size()+1;
			GImage fish = new GImage(filename);
			add(fish, 0, 0);
			fishes.add(fish);
			GPoint loc = new GPoint (0, 0);
			locations.add(loc);
			names.add(aquaSize + "-"+ fishName);
			infoCol.add("");
			aquaSelectCombo.addItem(aquaSize + "-"+ fishName);	
			weightList.add(Double.valueOf(1));
			fishes.get(index).setColor(null);
			repaint();
			Double cost = costList.get(fishSelectCombo.getSelectedIndex());
			totalCostList.add(cost);	
		}else if(currentJob.equals("Scale")) {
			int size = slider.getValue();
			index = aquaSelectCombo.getSelectedIndex();
			fishes.get(index).setColor(Color.LIGHT_GRAY);
			double scaleFactor = 1.0;
			switch(size) {
			case 1:
				scaleFactor = 0.5;
				break;
			case 2:
				scaleFactor = 1.0;
				break;
			case 3:
				scaleFactor = 2.0;
				break;
			}
			fishes.get(index).scale(scaleFactor);
			Double d = weightField.getValue();
			weightList.set(index, d*scaleFactor);
			weightField.setValue(d*scaleFactor);
			reportButton.setEnabled(true);
			fishes.get(index).setColor(null);
			repaint();
		} else if(currentJob.equals("Save Aquairum")) {
			saveAquarium();
		} else if(currentJob.equals("Generate Report")) {	
			generateReport();	
		}
	
	}
	private String getFileName() {
		String file = "fish2";
		int index = fishSelectCombo.getSelectedIndex();
		file = fishFileNames.get(index);
		//file = (String) fishSelectCombo.getSelectedItem();
		return file;
	}
	private String getFishName() {
		String name = "fish2";
		name = (String) fishSelectCombo.getSelectedItem();
		return name;
	}

	public void createFishes() {	
		for (int i =0; i<locations.size(); i++){
			GPoint p = new GPoint(locations.get(i));
			double dx = p.getX();
			double dy = p.getY();
			String file = fishFileNames.get(i);
			GImage fish = new GImage (file);
			add(fish, dx, dy);
			fishes.add(fish);	
			
		}	
	}


	public void updateFishes() {
		fishes.get(index).setLocation(locations.get(index).getX(),locations.get(index).getY() );
		fishes.get(index).setColor(null);
		repaint();
	}
	private void generateReport() {
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter("reportFile.txt"));
			double cost = 0;
			bw.write("_________Report for Comp130 Aquarium_________");
			for (int i =0; i< names.size(); i++) {
				cost += weightList.get(i)*costList.get(i);
				String reportInfo = "Price of " + names.get(i) + " is "  + weightList.get(i)*costList.get(i) + ".\n";
				bw.write(reportInfo);
			}
			bw.write("Total cost of the aquarium is " + cost);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private void saveAquarium() {
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter("savedFile.txt"));
			for (int i =0; i< names.size(); i++) {
				String fishInfo = names.get(i) + "=" + locations.get(i).getX() + "=" + locations.get(i).getY() + "=" + weightList.get(i) + "\n";
				bw.write(fishInfo);
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	///helper methods
	/**
	 * This method updates the Information text field 
	 * @param user
	 * @param xCoor
	 * @param yCoor
	 * @param contact
	 */

	private void setInfoField (String fish, double xCoor, double yCoor,  Double weight, Double cost) {
		//String str = "Info\n" + user + "\n X: " + xCoor + "\nY: " + yCoor
		//		+ "\nContacts: " + contact;
		//infoField.setText(str);
		
		nameField.setText(fish);
		//infoField.setText(info);
		coordinatesField.setText("X: " + xCoor + "\nY: " + yCoor);
		//contactsField.setText("Contacts: " + contact);
		//infoField.setText(info);
		weightField.setValue(weight);
		println("cost is " + cost);
		costField.setValue(cost);
	}

	private void createAquaList() {
		
		for (int i =0; i< names.size(); i++) {
			aquaSelectCombo.addItem(names.get(i));
		}
		aquaSelectCombo.setEditable(true);
	}

	private void createFishList() {
		for (int i =0; i< fishNames.size(); i++) {
			fishSelectCombo.addItem(fishNames.get(i));
		}
		aquaSelectCombo.setEditable(false);
	}

	private void readAquaFile(String fileName) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(fileName));
			
			int fishCount = 1;
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				StringTokenizer token = new StringTokenizer(line, "=");
				String name = "";
				if (token.hasMoreTokens()) {
					name = token.nextToken();
				}
				names.add(fishCount + "-"+ name);
				totalCostList.add(getFishCost(name));
				fishCount++;
				int xCoo=0;
				int yCoo=0;
				if (token.hasMoreTokens()) {
					xCoo = Integer.parseInt(token.nextToken());
					yCoo = Integer.parseInt(token.nextToken());
					GPoint loc = new GPoint (xCoo, yCoo);
					locations.add(loc);
				}
				if (token.hasMoreTokens()) {
					String weight = token.nextToken();	
					Double d = Double.valueOf(weight);
					println("weight " +  weight + " = " + d);
					weightList.add(d);
				}
							
				//nameTable.put(name, new GPoint(xCoo,yCoo));
				
			}
			/*
			nameList = new String[names.size()];
			for (int i =0; i< nameList.length;i++) {
				nameList[i] = names.get(i);
			}
			model = new DefaultComboBoxModel<String>(nameList);
			*/
			rd.close();
		} catch (IOException ex) {
			System.out.println("Invalid file");;
		}
	}
	private Double getFishCost(String name) {
		Double result = new Double(0);
		for (int i=0; i< fishNames.size(); i++) {
			if (fishNames.get(i).equals(name)) {
				result = costList.get(i);
			}			
		}
		return result;
		}
	private void readFishFile(String fileName) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(fileName));
			fishNames= new ArrayList<String>();
			fishFileNames= new ArrayList<String>();
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				StringTokenizer token = new StringTokenizer(line, ";");
				if (token.hasMoreTokens()) {
					fishNames.add(token.nextToken());
				}
				if (token.hasMoreTokens()) {
					fishFileNames.add(token.nextToken());
				}
				if (token.hasMoreTokens()) {
					String cost = token.nextToken();	
					Double d = Double.valueOf(cost);
					//println("cost after file read " + d);
					costList.add(d);
				}
			}	
			rd.close();
		} catch (IOException ex) {
			System.out.println("Invalid file");;
		}
	}
	
	///////////
	/* Standard Java entry point */
	/* This method can be eliminated in most Java environments */
	public static void main(String[] args) {
		new AquaProject().start(args);
	}
	private void playThemeSong(String fileLocation) {
		try {
			inputStream = AudioSystem.getAudioInputStream(new File(fileLocation));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* Constant Instant Variable */
	private static final String THEME_SONG = "Underwater_Pool.wav";
	/* Class Instance Variables */
	private Clip clip;
	private AudioInputStream inputStream;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}