import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class MainWindow {

	JFrame frame;
	JPanel panel;
	JPanel titlePanel;
	JLabel titleLabel;
	JButton addTaskButton;
	
	ArrayList<JPanel> tasks = new ArrayList<>();
	

	public MainWindow() {
		initialize();
	}
	
	private void initialize() {
		
		//Initialize JFrame
		frame = new JFrame();
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Todo List");
		
		frame.setVisible(true);
		
		
		//Initialize Title Label
		titlePanel = new JPanel();
		titleLabel = new JLabel("Todo List");
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 40));
		
		titlePanel.add(titleLabel);
		frame.add(titlePanel, BorderLayout.NORTH);
		
		//Initialize Main Panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
			//Add New Task Panel To List
			tasks.add(newTask());
			//Add Task Panel To Main Panel
			panel.add(tasks.getFirst());
		
		frame.add(panel, BorderLayout.CENTER);
		
		//Initialize Add Task Button
		addTaskButton = new JButton("Add Task");
		addTaskButton.setFocusable(false);
		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Grey Out Old Task
				if(!tasks.isEmpty()) {
					tasks.getLast().getComponent(1).setEnabled(false);
				}
				
				//Add New Task Panel
				tasks.add(newTask());
				panel.add(tasks.getLast());
				panel.revalidate();
				panel.repaint();
				
				
		}});
		
		frame.add(addTaskButton,BorderLayout.SOUTH);
		
		//Add Scroll Pane
		 JScrollPane scrollPane = new JScrollPane(panel);
		 scrollPane.setBorder(null);
		 frame.add(scrollPane);
		
		
	}
	
	//Task Panel Template
	private JPanel newTask() {
		JPanel taskPanel = new JPanel();
		JCheckBox checkbox = new JCheckBox();
		JTextPane taskTextPane = new JTextPane();
		JButton removeButton = new JButton("X");
		
		taskPanel.setMaximumSize(new Dimension(500, 45));
		
		checkbox.addItemListener(new ItemListener() {
             @Override
			 public void itemStateChanged(ItemEvent e) {
	                
	            	StyledDocument doc = taskTextPane.getStyledDocument();
	  				Style style = taskTextPane.addStyle("StrikethroughStyle", null);
	                 if (e.getStateChange() == ItemEvent.SELECTED) {
	                	 try {
	     		        	StyleConstants.setStrikeThrough(style, true); 
	     		        	String text = taskTextPane.getText();
	     		        	taskTextPane.setText("");
	     		        	doc.insertString(doc.getLength(), text, style);
	     		            
	     		        } catch (Exception ex) {
	     		            ex.printStackTrace();
	     		        }
	     		       
	     			}
	             if (e.getStateChange() == ItemEvent.DESELECTED) {
	            	 try {
	  		        	StyleConstants.setStrikeThrough(style, false); 
	  		        	String text = taskTextPane.getText();
	  		            taskTextPane.setText("");
	  		            doc.insertString(doc.getLength(), text, style);
	  		            
	  		        } catch (Exception ex) {
	  		            ex.printStackTrace();
	  		        }
	             }
	                 
	             
	         }});
		
		removeButton.setFocusable(false);
		removeButton.setPreferredSize(new Dimension(50,30));
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				taskPanel.setVisible(false);
				tasks.remove(taskPanel);
                frame.revalidate();
                frame.repaint();
			}
			
		});
		
		//Customize Task Text Pane
		taskTextPane.setFont(new Font("Arial", Font.PLAIN, 25));
		taskTextPane.setPreferredSize(new Dimension(350,30));
		taskTextPane.setDisabledTextColor(Color.gray);
		taskTextPane.setBorder(new LineBorder(Color.black, 1));
		
		//Add Elements
		taskPanel.add(checkbox);
		taskPanel.add(taskTextPane);
		taskPanel.add(removeButton);

		return taskPanel;
		
		
		
	}


	//To Do: 
	//Tasks Speichern
	//Um auf Text zuzugreifen, maybe eine Klasse machen für den Typ der Liste, welche den Text des JTextPanes des JPanels speichert
	//wo die Tasks sind.
	//Oder den text getten indem man durch die Liste iterated, bis man sie findet und dann das Element jeweils findet und 
	//Das in einer JTextPane variable speichert und so den text kriegt.
	//Nur Task erstellen wenn textpane nicht leer ist.
		
	
	
	
}
