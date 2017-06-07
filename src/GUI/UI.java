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
    private JPanel panelButton, filterOptions, panelVideoButtons, panelLabels, panelProgress, panelPreview, panelProcessing; 
    private JButton buttonPlayStop, buttonPlay, buttonPause, buttonNormal, buttonPluginGray, buttonPluginSepia, buttonPluginInvert, 
                    buttonPluginPixelize, buttonThresholding, buttonPluginHalftone, buttonPluginMinimum, 
                    buttonPluginMaximum, buttonPluginVintage, buttonPluginTelevision, buttonPluginEdgeDetector,
                    buttonPluginDifference, buttonOpenFile, buttonSave, buttonCancel, buttonParallel;
    private JLabel labelCurrentFilter, labelProcessing;
    private Thread  thread;
    private int vidWidth, vidHeight;
    private boolean playing, saving, parallel;
    public boolean cancelled;
    public ArrayList<JProgressBar> progressBars;
    public ArrayList<JLabel> processingInfo;
    public ArrayList<VideoProcessor> processors;
    public ArrayList<ParallelProcessor> Paraprocessors;
    private File file;
    private Container container;
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
        processors = new ArrayList<VideoProcessor>();
        Paraprocessors = new ArrayList<ParallelProcessor>();
        progressBars = new ArrayList<JProgressBar>();
        processingInfo = new ArrayList<JLabel>();
        imageproc = new ImageProcessing();
        setLayout(new BorderLayout());
 
    }
     
    private void loadGUI() {
        setTitle("Video Editor"); 
        
        // Labels
        labelCurrentFilter = new JLabel("Current filter: None");
        labelProcessing = new JLabel("");
         
        // Buttons 
        ButtonHandler l_handler = new ButtonHandler();
        buttonPlay = new JButton("Play");
        buttonPlayStop = new JButton("Stop");
        buttonPause = new JButton("Pause");
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
        buttonPluginVintage = new JButton("Vintage"); 
        buttonPluginTelevision = new JButton("Television"); 
        buttonPluginEdgeDetector = new JButton("Edge Detector"); 
        buttonPluginDifference = new JButton("Difference"); 
        
        buttonPlay.addActionListener(l_handler);
        buttonPause.addActionListener(l_handler); 
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
        buttonPluginVintage.addActionListener(l_handler); 
        buttonPluginTelevision.addActionListener(l_handler); 
        buttonPluginEdgeDetector.addActionListener(l_handler); 
        buttonPluginDifference.addActionListener(l_handler);      

        // Panels 
        panelButton = new JPanel(); 
        panelButton.add(buttonPlay);
        panelButton.add(buttonPause);
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
        filterOptions.add(buttonPluginVintage); 
        filterOptions.add(buttonPluginTelevision); 
        filterOptions.add(buttonPluginEdgeDetector); 
        filterOptions.add(buttonPluginDifference);
        
        panelLabels = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLabels.add(labelCurrentFilter);
        
        panelProgress = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelProgress.add(labelProcessing);
        
        panelPreview = new JPanel(new BorderLayout());
        panelProcessing = new JPanel(new GridLayout(10,1));
        panelPreview.add(panelProcessing, BorderLayout.NORTH);
        
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
		File directory = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(directory);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		  
		if (result == JFileChooser.CANCEL_OPTION) {
			// If the user clicks cancel
		} else {
			// If the user chooses a file
			file = fileChooser.getSelectedFile();
			
			// Creating a resized video for the player
			/*String commandLine = "ffmpeg.exe -i "+file.getName()+" -ss  -c copy -t  SubVideos\\sub_video_.mp4";
			try {
				Runtime.getRuntime().exec(commandLine);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//File resizedVideo = new File("resized");
			
			media = new Media(file.toURI().toString());
	        player = new MediaPlayer(media);
	        view = new MediaView(player);
	        BorderPane root = new BorderPane();
	        root.getChildren().add(view);
	        
	        Platform.runLater(new Runnable() {
	            @Override 
	            public void run() {
	            	Scene scene = new Scene(root);
	            	panelPlayer.setScene(scene); 
	            }
	        });
	        
	        container.add(panelPlayer, BorderLayout.CENTER);
	       
			pic = imageproc.getFrame(file, 100);
			if(pic != null) {
				vidHeight = pic.getHeight();
				vidWidth = pic.getWidth();
				resized = imageproc.scale(pic,vidWidth/2,vidHeight/2);
				ImageIcon image = new ImageIcon(resized);
				JLabel picLabel = new JLabel(image);
				container.setLayout(new BorderLayout());
				panelPlayer.setSize(vidWidth,vidHeight);
				System.out.println("width: " + vidWidth+ ", height: " + vidHeight);
				panelPlayer.setBorder(null);
				vidWidth = pic.getWidth() + pic.getWidth()/2;
				setSize(vidWidth+120,vidHeight+100);
				container.remove(panelPreview);
				container.add(panelPreview, BorderLayout.EAST);
				container.add(filterOptions, BorderLayout.WEST);
				container.add(panelVideoButtons, BorderLayout.SOUTH);
				container.remove(panelPreview);
				panelPreview = new JPanel(new BorderLayout());
				panelPreview.add(picLabel, BorderLayout.NORTH);
				panelPreview.add(panelProcessing, BorderLayout.SOUTH);
				panelPreview.setSize(pic.getWidth()/2, pic.getHeight());
				container.add(panelPreview, BorderLayout.EAST);
				panelPreview.revalidate();
				panelPreview.repaint();
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
    
    public void updateProcessing() {
    	panelProcessing = new JPanel(new GridLayout(10,1));
    	for (int i=0; i < processors.size(); i++) {
    		panelProcessing.add(processingInfo.get(i), BorderLayout.NORTH);
    		panelProcessing.add(progressBars.get(i), BorderLayout.NORTH);
    	}
    	for (int i=0; i < Paraprocessors.size(); i++) {
    		panelProcessing.add(processingInfo.get(i), BorderLayout.NORTH);
    		panelProcessing.add(progressBars.get(i), BorderLayout.NORTH);
    	}
    	ImageIcon image = new ImageIcon(resized);
		JLabel picLabel = new JLabel(image);
		container.remove(panelPreview);
		panelPreview = new JPanel(new BorderLayout());
		panelPreview.add(picLabel, BorderLayout.NORTH);
		panelPreview.add(panelProcessing, BorderLayout.SOUTH);
		panelPreview.setSize(pic.getWidth()/2, pic.getHeight());
		container.add(panelPreview, BorderLayout.EAST);
		panelPreview.revalidate();
		panelPreview.repaint();
    	
    }
    
    public void updatePreview() {
    	filteredPreview = imageproc.filterImage(pic, filter);
    	resized = imageproc.scale(filteredPreview,pic.getWidth()/2,pic.getHeight()/2);
		ImageIcon image = new ImageIcon(resized);
		JLabel picLabel = new JLabel(image);
		container.remove(panelPreview);
		panelPreview = new JPanel(new BorderLayout());
		panelPreview.add(picLabel, BorderLayout.NORTH);
		panelPreview.add(panelProcessing, BorderLayout.SOUTH);
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
        	} else if (a_event.getSource() == buttonPause) {
        		if (media != null) {
	        		if(playing){ 
	                    playing = false;       
	                    player.pause();
	                }
        		} else {
        			JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
        		}
        	} else if(a_event.getSource() == buttonPlayStop) {
        		if (media != null) {
	                playing = false;       
	                player.stop();
        		} else {
        			JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
        		}
            } else if (a_event.getSource() == buttonOpenFile) {
            	openFile();
            } else if (a_event.getSource() == buttonSave) {
            	if (file == null) {
            		JOptionPane.showMessageDialog(ui, "You have not selected a file yet.");
            	} else {
            		if (filter == null) {
            			JOptionPane.showMessageDialog(ui, "You have not selected a filter.");
            		} else {
            			outputVideo = (String)JOptionPane.showInputDialog(ui,"Enter output file name:\n","File Name",JOptionPane.PLAIN_MESSAGE);
            			updateLabel("Saving Video......");
            			cancelled = false;
            			saving = true;
            			JProgressBar progressBar = new JProgressBar(0, 100);
            			int id = progressBars.size();
            			progressBars.add(progressBar);
            	        progressBars.get(id).setValue(0);
            	        progressBars.get(id).setStringPainted(true);
            			if (!parallel) {
            				JLabel label = new JLabel("ID: "+id+", Output: "+ outputVideo+"\n, Filter: "+filterName+", Sequential");
            				processingInfo.add(label);
            				panelProcessing.add(label, BorderLayout.NORTH);
            	    		panelProcessing.add(progressBar, BorderLayout.NORTH);
	            			VideoProcessor processor = new VideoProcessor(file.getAbsolutePath(), ui, id);
	            			processors.add(processor);
	            			updateProcessing();
	            			processor.initializeFilter(filter);
	            			processor.execute();
            			} else {
            				if (Paraprocessors.size()==0) {
	            				JLabel label = new JLabel("ID: "+id+", Output: "+ outputVideo+"\n, Filter: "+filterName+", Parallel");
	            				processingInfo.add(label);
	            				panelProcessing.add(label, BorderLayout.NORTH);
	            	    		panelProcessing.add(progressBar, BorderLayout.NORTH);
	            	    		Thread t = new Thread(new Runnable() {
		        	    		    @Override
		        	    		    public void run() {
		        	    		    	ParallelProcessor processor = new ParallelProcessor(file.getAbsolutePath(),filter, ui, id);
		        	    				Paraprocessors.add(processor);
		        	    				processor.execute(); 
		    	            			updateProcessing();	      
		        	    		    }
	            	    		});
	            	    		t.start();
            				} else {
            					JOptionPane.showMessageDialog(ui, "There is already a parallel processor running.");
            				}
            			}	
            		}
            	}
            } else if(a_event.getSource() == buttonCancel){
                if (saving) {
                	cancelled = true;
                	updateLabel("");
                	System.out.println("Video processing has been cancelled.");
                	JOptionPane.showMessageDialog(ui, "File saving has been cancelled.");
                	saving = false;
                	progressBars.removeAll(progressBars);
                	processingInfo.removeAll(processingInfo);
                	processors.removeAll(processors);
                	updateProcessing();
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
        		panelPreview.add(panelProcessing, BorderLayout.SOUTH);
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
                filter = "colorlevels=rimin=0.039:gimin=0.039:bimin=0.039:rimax=0.96:gimax=0.96:bimax=0.96";
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
            else if(a_event.getSource() == buttonPluginVintage){ 
            	filterName = "Vintage";
                labelCurrentFilter.setText("Current filter: Vintage");
                filter = "curves=vintage";
                updatePreview();
            } 
            else if(a_event.getSource() == buttonPluginTelevision){ 
            	filterName = "Television";
                labelCurrentFilter.setText("Current filter: Television"); 
            } 
            else if(a_event.getSource() == buttonPluginEdgeDetector){
            	filterName = "Edge Detector";
                labelCurrentFilter.setText("Current filter: Edge Detector");
                filter = "edgedetect=mode=colormix:high=0";
                updatePreview();
            }     
            else if(a_event.getSource() == buttonPluginDifference){
            	filterName = "Difference";
                labelCurrentFilter.setText("Current filter: Difference"); 
            } 
        } 
    }
}
