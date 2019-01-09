package memor;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class BoardSizeChoosing extends JFrame {
	private JPanel mainPanel;
	private JSlider widthSlider;
	private JSlider heightSlider;
	private JLabel aboutSizeInformer;
	private JLabel sizeValidatoInformer;
	private JButton gameStartButton;
	
	public BoardSizeChoosing() {
		setTitle("Ustawianie wymiarów planszy");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 400));
		
		mainPanel = new JPanel();
		mainPanel.setBackground(MainMenu.BACKGROUND_COLOR);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		addTitle();

		widthSlider = createSizeSlider();
		addSizeSelectorSection("Szerokoœæ", widthSlider);

		heightSlider = createSizeSlider();
		addSizeSelectorSection("Wysokoœæ", heightSlider);
		
		addSizeValidationSection();
		
		add(mainPanel);
		pack();
		setVisible(true);
	}

	private void addTitle() {
		JLabel title = new JLabel("Wybierz ustawienia planszy", JLabel.CENTER);
		Font font = title.getFont().deriveFont(20f);
		title.setFont(font);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		mainPanel.add(title);
	}
	
	private void addSizeSelectorSection(String name, JSlider slider) {
		JPanel section = new JPanel();
		section.setBackground(MainMenu.BACKGROUND_COLOR);
		BoxLayout layout = new BoxLayout(section, BoxLayout.Y_AXIS);
		section.setLayout(layout);
		JLabel label = new JLabel(name + ": " + slider.getValue(), JLabel.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		slider.addChangeListener(event -> {
			label.setText(String.valueOf(name + ": " + slider.getValue()));
			refreshInformerState();
		});
		section.add(label);
		section.add(slider);
		section.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		mainPanel.add(section);
	}
	
	private JSlider createSizeSlider() {
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 1, 7, 4);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setBackground(MainMenu.BACKGROUND_COLOR);
		return slider;
	}
	
	private void addSizeValidationSection() {
		JPanel extSection = new JPanel(new GridBagLayout());
		extSection.setBackground(MainMenu.BACKGROUND_COLOR);
		aboutSizeInformer = new JLabel();
		sizeValidatoInformer = new JLabel();
		JButton backButton = new JButton("Powrót");
		backButton.addActionListener(event -> {
			dispose();
			new MainMenu();
		});
		gameStartButton = new JButton("Start!");
		gameStartButton.addActionListener(event -> {
			dispose();
			new Board(widthSlider.getValue(), heightSlider.getValue());
		});
		JPanel butSection = new JPanel();
		butSection.setBackground(MainMenu.BACKGROUND_COLOR);

		
		butSection.add(backButton);
		butSection.add(gameStartButton);
		
		GridBagConstraints constraints;
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 0, 0, 50);
		extSection.add(aboutSizeInformer, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		extSection.add(butSection, constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		extSection.add(sizeValidatoInformer, constraints);
		mainPanel.add(extSection);
		refreshInformerState();
	}
	
	private void refreshInformerState() {
		aboutSizeInformer.setText("Wybrane wymiary: " + widthSlider.getValue() + "x" + heightSlider.getValue());
		if(widthSlider.getValue() * heightSlider.getValue() % 2 == 1) {
			sizeValidatoInformer.setText("Nieprawid³owe wymiary planszy! (nieparzysta iloœæ kafelków)");
			sizeValidatoInformer.setForeground(Color.RED);
			sizeValidatoInformer.setVisible(true);
			gameStartButton.setEnabled(false);
		} else {
			sizeValidatoInformer.setVisible(false);
			gameStartButton.setEnabled(true);
		}
	}
}