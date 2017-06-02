package GUI; 

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import Processing.ImageProcessing;
import Processing.ParallelProcessor;
import Processing.VideoProcessor;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class UI extends JFrame implements Runnable {
    // GUI 
    private JPanel panelButton, filterOptions, panelVideoButtons, panelLabels, panelProgress, panelPreview; 
    private JButton buttonPlayStop, buttonPlay, buttonNormal, buttonPluginGray, buttonPluginSepia, buttonPluginInvert, 
                    buttonPluginPixelize, buttonThresholding, buttonPluginHalftone, buttonPluginMinimum, 
                    buttonPluginMaximum, buttonPluginFlip, buttonPluginTelevision, buttonPluginEdgeDetector,
                    buttonPluginDifference, buttonOpenFile, buttonSave, buttonCancel, buttonParallel;
    private JLabel labelCurrentFilter, labelProcessing;
    public JLabel labelProcessInfo;
    private Thread  thread;
    private int vidWidth, vidHeight;
    private boolean playing, saving, parallel;
    public boolean cancelled;
    public ArrayList<JProgressBar> progressBars;
    public JProgressBar progressBar;
    private File file;
    private Container container;
    private VideoProcessor processor;
    private ParallelProcessor ParaProcessor;
    private ImageProcessing imageproc;
    private String filter, filterName;
    private UI ui;
    private BufferedImage pic, filteredPreview, resized;
    public String outputVideo;
    
    //---@Rain---
    private Media media;
    private MediaPlayer player;
    private MediaView view;
    private JFXPanel panelPlayer;
    //---@Rain---
         
    public UI() { 
        ui = this;
        loadGUI(); 
         
        thread = new Thread(this);
        thread.start();
        playing = false;
        parallel = false;
        setLayout(new BorderLayout());
 
    }
     
    private void loadGUI() {
        setTitle("Video Editor"); 
        
        // Labels
        labelCurrentFilter = new JLabel("Current filter: None");
        labelProcessing = new JLabel("");
        labelProcessInfo = new JLabel("");
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
         
        // Buttons 
        ButtonHandler l_handler = new ButtonHandler();
        buttonPlay = new JButton("Play");
        buttonPlayStop = new JButton("Stop");
        buttonOpenFile = new JButton("Open file");
        buttonSave = new JButton("Save");
        buttonCancel = new JButton("Cancel");
        buttonParallel = new JButton("Parallel/Sequential");
        buttonNormal = new JButton("Normal"); 
        buttonPluginGray = new JButton("Gray Scale"); 
        buttonPluginSepia = new JButton("Sepia"); 
        buttonPluginInvert = new JButton("Invert Colors");     
        buttonPluginPixelize = new JButton("Pixelize"); 
        buttonThresholding = new JButton("Thresholding"); 
        buttonPluginHalftone = new JButton("Halftone"); 
        buttonPluginMinimum = new JButton("Minimum"); 
        buttonPluginMaximum = new JButton("Maximum");         
        buttonPluginFlip = new JButton("Flip"); 
        buttonPluginTelevision = new JButton("Television"); 
        buttonPluginEdgeDetector = new JButton("Edge Detector"); 
        buttonPluginDifference = new JButton("Difference"); 
        
        buttonPlay.addActionListener(l_handler); 
        buttonPlayStop.addActionListener(l_handler);
        buttonOpenFile.addActionListener(l_handler);
        buttonSave.addActionListener(l_handler);
        buttonCancel.addActionListener(l_handler);
        buttonParallel.addActionListener(l_handler);
        buttonPluginGray.addActionListener(l_handler); 
        buttonNormal.addActionListener(l_handler); 
        buttonPluginSepia.addActionListener(l_handler); 
        buttonPluginInvert.addActionListener(l_handler); 
        buttonPluginPixelize.addActionListener(l_handler); 
        buttonThresholding.addActionListener(l_handler); 
        buttonPluginHalftone.addActionListener(l_handler);         
        buttonPluginMinimum.addActionListener(l_handler); 
        buttonPluginMaximum.addActionListener(l_handler);         
        buttonPluginFlip.addActionListener(l_handler); 
        buttonPluginTelevision.addActionListener(l_handler); 
        buttonPluginEdgeDetector.addActionListener(l_handler); 
        buttonPluginDifference.addActionListener(l_handler);      

        // Panels 
        panelButton = new JPanel(); 
        panelButton.add(buttonPlay);
        panelButton.add(buttonPlayStop);
        panelButton.add(buttonOpenFile);
        panelButton.add(buttonSave);
        panelButton.add(buttonCancel);
        panelButton.add(buttonParallel);
         
        filterOptions = new JPanel(); 
        filterOptions.setLayout(new GridLayout(15,1)); 
        filterOptions.add(buttonNormal); 
        filterOptions.add(buttonPluginGray); 
        filterOptions.add(buttonPluginSepia); 
        filterOptions.add(buttonPluginInvert);         
        filterOptions.add(buttonPluginPixelize); 
        filterOptions.add(buttonThresholding); 
        filterOptions.add(buttonPluginHalftone);         
        filterOptions.add(buttonPluginMinimum); 
        filterOptions.add(buttonPluginMaximum);         
        filterOptions.add(buttonPluginFlip); 
        filterOptions.add(buttonPluginTelevision); 
        filterOptions.add(buttonPluginEdgeDetector); 
        filterOptions.add(buttonPluginDifference);
        
        panelLabels = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLabels.add(labelCurrentFilter);
        
        panelProgress = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelProgress.add(labelProcessing);
        panelProgress.add(progressBar);
        
        panelPreview = new JPanel(new BorderLayout());
        panelPreview.add(labelProcessInfo, BorderLayout.NORTH);
        
        panelVideoButtons = new JPanel(new BorderLayout());
        panelVideoButtons.add(panelButton, BorderLayout.SOUTH);
        panelVideoButtons.add(panelLabels, BorderLayout.WEST);
        panelVideoButtons.add(panelProgress, BorderLayout.EAST);
        panelVideoButtons.setSize(200,200);
        panelButton.setBorder(BorderFactory.createLineBorder(Color.black));
        
        container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panelPreview);
        container.add(filterOptions, BorderLayout.WEST);
        container.add(panelVideoButtons, BorderLayout.SOUTH);
        
        vidHeight = 400;
        vidWidth = 720;
        panelPlayer = new JFXPanel();
        panelPlayer.setBorder(BorderFactory.createLineBorder(Color.black));
        panelPlayer.setBackground(Color.black);
        container.add(panelPlayer, BorderLayout.CENTER);      
        
        setSize(vidWidth+125,vidHeight+100); 
        setResizable(false); 
        setVisible(true);      

    }
  
    private void openFile() {
    	if (playing) {
    		player.stop();
    	}
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = fileChooser.showOpenDialog( this );
		  
		if (result == JFileChooser.CANCEL_OPTION) {
			// If the user clicks cancel
		} else {
			// If the user chooses a file
			file = fileChooser.getSelectedFile();
			media = new Media(file.toURI().toString());
	        player = new MediaPlayer(media);
	        view = new MediaView(player);
	        BorderPane root = new BorderPane();
	        root.getChildren().add(view);
	        
	        Platform.runLater(new Runnable() {
	            @Override 
	            public void run() {
	            	Scene scene = new Scene(root);
	            	panelPlayer.setSize(1000,1000);
	            	panelPlayer.setScene(scene); 
	            }
	        });
	        
			imageproc = new ImageProcessing();
			pic = imageproc.getFrame(file, 100);
			if(pic != null) {
				vidHeight = pic.getHeight();
				vidWidth = pic.getWidth();
				resized = imageproc.scale(pic,vidWidth/2,vidHeight/2);
				ImageIcon image = new ImageIcon(resized);
				JLabel picLabel = new JLabel(image);
				
				panelPreview.add(picLabel, BorderLayout.NORTH);
				container.setLayout(new BorderLayout());
				
				panelPlayer.setSize(vidWidth,vidHeight);
				panelPlayer.setBorder(null);
				panelPreview.setSize(vidWidth/2,vidHeight);
				vidWidth = pic.getWidth() + pic.getWidth()/2;
				setSize(vidWidth+120,vidHeight+100);
				//container.removeAll();;
				container.add(filterOptions, BorderLayout.WEST);
				container.add(panelPlayer, BorderLayout.CENTER);
				container.add(panelVideoButtons, BorderLayout.SOUTH);
				container.add(panelPreview, BorderLayout.EAST);
				this.invalidate();
		        this.validate();
		        this.repaint();
			}
		}  
    }
     
    public void run() {
        while(true){             
            if(playing) 
            {
                
            } 
        }  
    }
    
    public void updateLabel(String text) {
    	labelProcessing.setText(text);
		panelLabels.validate();
        panelLabels.repaint();
    }
    
    public void updatePreview() {
    	filteredPreview = imageproc.filterImage(pic, filter);
    	resized = imageproc.scale(filteredPreview,pic.getWidth()/2,pic.getHeight()/2);
		ImageIcon image = new ImageIcon(resized);
		JLabel picLabel = new JLabel(image);
		container.remove(panelPreview);
		panelPreview = new JPanel(new BorderLayout());
		panelPreview.add(picLabel, BorderLayout.NORTH);
		panelPreview.add(labelProcessInfo, BorderLayout.SOUTH);
		panelPreview.setSize(pic.getWidth()/2, pic.getHeight());
		container.add(panelPreview, BorderLayout.EAST);
		panelPreview.revalidate();
		panelPreview.repaint();
    }
     
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent a_event) {
        	if (a_event.getSource() == buttonPlay) {
        		if (media != null) {
	        		if(!playing){
	                    playing = true;
	                    player.play();
	                }
        		} else {
        			JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
        		}
        	} else if(a_event.getSource() == buttonPlayStop) {
                if(playing){ 
                    playing = false;    
                    player.stop();
                } 
        		if (media != null) {
	        		if(playing){ 
	                    playing = false;       
	                    player.stop();
	                }
        		} else {
        			JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
        		}
            } else if (a_event.getSource() == buttonOpenFile) {
            	openFile();
            	progressBar.setValue(0);
            } else if (a_event.getSource() == buttonSave) {
            	if (file == null) {
            		JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
            	} else {
            		if (filter == null) {
            			JOptionPane.showMessageDialog(ui, "You have not selected a filter.");
            		} else {
            			outputVideo = (String)JOptionPane.showInputDialog(ui,"Enter output file name:\n","File Name",JOptionPane.PLAIN_MESSAGE);
            			System.out.println(outputVideo);
            			updateLabel("Saving Video......");
            			cancelled = false;
            			saving = true;
            			if (!parallel) {
            				labelProcessInfo.setText("Output: "+ outputVideo+"\n, Filter: "+filterName+", Sequential");
	            			processor = new VideoProcessor(file.getAbsolutePath(), ui);
	            			processor.initializeFilter(filter);
	            			processor.execute();
	            		// Need to add functionality to process in parallel.
            			} else {
            				updateLabel("");
            				//ParaProcessor = new ParallelProcessor(file.getAbsolutePath(), ui);
                			//ParaProcessor.initializeFilter(filter);
                			//ParaProcessor.execute();
            			}	
            		}
            	}
            } else if(a_event.getSource() == buttonCancel){
                if (saving) {
                	cancelled = true;
                	processor.cancel(true);
                	updateLabel("");
                	System.out.println("Video processing has been cancelled.");
                	JOptionPane.showMessageDialog(ui, "File saving has been cancelled.");
                	if (processor.getFile().delete()) {
                		System.out.println("File successfully deleted");
                	}
                	saving = false;
                	progressBar.setValue(0);
                } else {
                	JOptionPane.showMessageDialog(ui, "No file is being saved.");
	            }
            } else if(a_event.getSource() == buttonParallel) {
            	if(parallel){
                    parallel = false;
                    JOptionPane.showMessageDialog(ui, "Processing will now be done sequentially.");
                } else {
                	parallel = true;
                	JOptionPane.showMessageDialog(ui, "Processing will now be done in parallel.");
                }
	        }
            else if(a_event.getSource() == buttonNormal){ 
                labelCurrentFilter.setText("Current filter: None");
                filter = null;
                resized = imageproc.scale(pic,pic.getWidth()/2,pic.getHeight()/2);
				ImageIcon image = new ImageIcon(resized);
        		JLabel picLabel = new JLabel(image);
        		container.remove(panelPreview);
        		panelPreview = new JPanel(new BorderLayout());
        		panelPreview.add(picLabel, BorderLayout.NORTH);
        		panelPreview.add(labelProcessInfo, BorderLayout.SOUTH);
        		container.add(panelPreview, BorderLayout.EAST);
        		container.validate();
                container.repaint();
            } 
            else if(a_event.getSource() == buttonPluginGray){ 
            	filterName = "Gray Scale";
                labelCurrentFilter.setText("Current filter: Gray Scale");
                filter = "colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3";
                updatePreview();
            } 
            else if(a_event.getSource() == buttonPluginSepia){ 
            	filterName = "Sepia";
                labelCurrentFilter.setText("Current filter: Sepia");
                filter = "colorchannelmixer=.393:.769:.189:0:.349:.686:.168:0:.272:.534:.131";
                updatePreview();
            } 
            else if(a_event.getSource() == buttonPluginInvert){ 
            	filterName = "Negative";
                labelCurrentFilter.setText("Current filter: Negative");
                filter = "lutrgb='r=negval:g=negval:b=negval'lutyuv='y=negval:u=negval:v=negval'";
                updatePreview();
            } 
            else if(a_event.getSource() == buttonPluginPixelize){ 
            	filterName = "Pixelize";
                labelCurrentFilter.setText("Current filter: Pixelize");
                filter = "boxblur=5:1";
                updatePreview();
            } 
            else if(a_event.getSource() == buttonThresholding){ 
            	filterName = "Thresholding";
                labelCurrentFilter.setText("Current filter: Thresholding"); 
            } 
            else if(a_event.getSource() == buttonPluginHalftone){
            	filterName = "Halftone";
                labelCurrentFilter.setText("Current filter: Halftone"); 
            } 
            else if(a_event.getSource() == buttonPluginMinimum){ 
            	filterName = "Minimum";
                labelCurrentFilter.setText("Current filter: Minimum"); 
            } 
            else if(a_event.getSource() == buttonPluginMaximum){ 
            	filterName = "Maximum";
                labelCurrentFilter.setText("Current filter: Maximum"); 
            } 
            else if(a_event.getSource() == buttonPluginFlip){ 
            	filterName = "Flip";
                labelCurrentFilter.setText("Current filter: Flip");
            } 
            else if(a_event.getSource() == buttonPluginTelevision){ 
            	filterName = "Television";
                labelCurrentFilter.setText("Current filter: Television"); 
            } 
            else if(a_event.getSource() == buttonPluginEdgeDetector){
            	filterName = "Edge Detector";
                labelCurrentFilter.setText("Current filter: Edge Detector"); 
            }     
            else if(a_event.getSource() == buttonPluginDifference){
            	filterName = "Difference";
                labelCurrentFilter.setText("Current filter: Difference"); 
            } 
        } 
    }
}
