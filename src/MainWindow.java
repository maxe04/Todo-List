import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainWindow {

	JFrame frame;
	JPanel mainPanel;

	JPanel titlePanel;
	JLabel titleLabel;

	JButton newTaskButton;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem saveMenuItem;
	JMenuItem exitMenuItem;

	enum STATE {
		CHECKED, UNCHECKED
	}

	ArrayList<JPanel> tasks = new ArrayList<>();

	public MainWindow() {
		initialize();
	}

	private void initialize() {

		// Initialize Main Frame
		frame = new JFrame();
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Todo List");
		frame.setVisible(true);

		// Initialize Title Panel
		// Initialize Title Label
		titlePanel = new JPanel();
		titleLabel = new JLabel("Todo List");

		titleLabel.setFont(new Font("Arial", Font.PLAIN, 40));

		titlePanel.add(titleLabel);
		frame.add(titlePanel, BorderLayout.NORTH);

		// Initialize Main Panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.add(mainPanel, BorderLayout.CENTER);

		// Initialize New Task Button
		newTaskButton = new JButton("New Task");
		newTaskButton.setFocusable(false);

		// New Task Button Functionality
		newTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Add New Task
				tasks.add(addTask("", STATE.UNCHECKED));
				mainPanel.add(tasks.getLast());
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		frame.add(newTaskButton, BorderLayout.SOUTH);

		// Initialize Scroll Pane
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setBorder(null);
		frame.add(scrollPane);

		// Initialize Menu

		menuBar = new JMenuBar();
		menu = new JMenu("Menu");

		saveMenuItem = new JMenuItem("Save");
		exitMenuItem = new JMenuItem("Exit");

		menu.add(saveMenuItem);
		menu.add(exitMenuItem);

		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		saveMenuItem.addActionListener(e -> {
			saveTasks();
		});
		exitMenuItem.addActionListener(e -> System.exit(0));

		// Load Tasks
		loadTasks();

	}

	// Task Template
	private JPanel addTask(String task, STATE state) {
		JPanel taskPanel = new JPanel();
		JCheckBox checkbox = new JCheckBox();
		JTextPane taskText = new JTextPane();
		JButton removeButton = new JButton("X");
		JButton editButton;

		// Disable Checkbox by default
		checkbox.setEnabled(false);

		// Enable Checkbox Only If Task Was Added
		// Disable Task Text When Task Was Loaded In From File
		if (task.isEmpty()) {
			editButton = new JButton("Add");

		} else {
			editButton = new JButton("Edit");
			checkbox.setEnabled(true);
			taskText.setEnabled(false);
		}

		// Change Task Panel Size
		taskPanel.setMaximumSize(new Dimension(600, 45));

		// Customize Edit Button
		editButton.setFocusable(false);
		editButton.setPreferredSize(new Dimension(70, 30));

		// Edit Button Functionality
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (editButton.getText().equals("Add")) {
					taskText.setEnabled(false);
					editButton.setText("Edit");

					// Enable Checkbox Only If Task Was Added
					checkbox.setEnabled(true);
				} else if (editButton.getText().equals("Edit")) {
					taskText.setEnabled(true);
					editButton.setText("Done");
				} else if (editButton.getText().equals("Done")) {
					taskText.setEnabled(false);
					editButton.setText("Edit");
				}

			}

		});

		// Add Checkbox Functionality
		// Create Strikethrough Style
		StyledDocument doc = taskText.getStyledDocument();
		Style style = taskText.addStyle("StrikethroughStyle", null);

		checkbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				// Striked Text When Checkbox Is Checked
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						StyleConstants.setStrikeThrough(style, true);
						String text = taskText.getText();
						taskText.setText("");
						doc.insertString(doc.getLength(), text, style);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
				// Disable Striked Text When Checkbox Is Unchecked
				if (e.getStateChange() == ItemEvent.DESELECTED) {

					try {
						StyleConstants.setStrikeThrough(style, false);
						String text = taskText.getText();
						taskText.setText("");
						doc.insertString(doc.getLength(), text, style);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}

			}
		});

		// Customize Remove Button
		removeButton.setFocusable(false);
		removeButton.setPreferredSize(new Dimension(50, 30));

		// Remove Button Functionality
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				taskPanel.setVisible(false);
				tasks.remove(taskPanel);
				frame.revalidate();
				frame.repaint();
			}

		});

		// Add Text From Parameter To Task Text
		// This Is Useful When Loading Tasks From File
		taskText.setText(task);

		// Customize Task Text
		taskText.setFont(new Font("Arial", Font.PLAIN, 25));
		taskText.setPreferredSize(new Dimension(350, 30));
		taskText.setDisabledTextColor(Color.gray);
		taskText.setBorder(new LineBorder(Color.black, 1));

		// Add Elements To Task Panel
		taskPanel.add(checkbox);
		taskPanel.add(taskText);
		taskPanel.add(editButton);
		taskPanel.add(removeButton);

		// Checkbox State Handling (Strikethrough Text And Select Checkbox If State == 1
		// Useful For Loading From File
		if (state == STATE.CHECKED) {
			checkbox.setSelected(true);
			try {
				StyleConstants.setStrikeThrough(style, true);
				String text = taskText.getText();
				taskText.setText("");
				doc.insertString(doc.getLength(), text, style);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (state == STATE.UNCHECKED) {
			checkbox.setSelected(false);

		}

		return taskPanel;

	}

	// Saving Task Text Data To Text File
	// Saving Task Checkbox State To Text File
	private void saveTasks() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"));

			// Find Checkbox For Every Task in Tasks List
			// Find Task Text For Every Task in Tasks List
			// Write 0 Into Line To Mark Checkbox State As Unchecked
			// Write 1 Into Line To Mark Checkbox State As Checked
			for (JPanel panel : tasks) {
				for (Component component : panel.getComponents()) {

					if (component instanceof JCheckBox) {
						JCheckBox checkbox = (JCheckBox) component;

						if (checkbox.isSelected()) {
							writer.write("1");

						} else {
							writer.write("0");
						}

					}

					if (component instanceof JTextPane) {
						JTextPane textPane = (JTextPane) component;

						writer.write(textPane.getText());
						writer.write("\n");

					}

				}
			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTasks() {

		try {
			BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));

			String line;
			String textWithoutStateNumber;

			// Read State Number And Set Checkbox State Accordingly
			// Create Task Without State Number In The Text
			while ((line = reader.readLine()) != null) {

				if ((line.contains("0"))) {
					textWithoutStateNumber = "";
					char[] array = line.toCharArray();

					for (int i = 0; i < array.length; i++) {
						if (array[i] != '0') {
							textWithoutStateNumber += array[i];
						}
					}

					tasks.add(addTask(textWithoutStateNumber, STATE.UNCHECKED));
					mainPanel.add(tasks.getLast());

				} else if ((line.contains("1"))) {
					textWithoutStateNumber = "";
					char[] array = line.toCharArray();

					for (int i = 0; i < array.length; i++) {
						if (array[i] != '1') {
							textWithoutStateNumber += array[i];
						}

					}

					tasks.add(addTask(textWithoutStateNumber, STATE.CHECKED));
					mainPanel.add(tasks.getLast());
				}

			}

			reader.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
