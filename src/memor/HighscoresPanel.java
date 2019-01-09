package memor;


import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HighscoresPanel extends JFrame {
	public HighscoresPanel() {
		setTitle("Wysokie wyniki");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(MainMenu.BACKGROUND_COLOR);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
		LayoutManager boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(boxLayout);
		
		JLabel title = new JLabel("Wysokie wyniki", JLabel.CENTER);
		Font font = title.getFont().deriveFont(20f);
		title.setFont(font);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		mainPanel.add(title);
		
		List<Highscore> highscores = HighscoresInfo.get().getList();
		int size = highscores.size();
		JPanel highscoresPanel = new JPanel(new GridLayout(size, 2));
		highscoresPanel.setBackground(MainMenu.BACKGROUND_COLOR);
		
		for(int i = 1; i <= size; i++) {
			Highscore highscore = highscores.get(i-1);
			highscoresPanel.add(new JLabel(i + ". " + highscore.getNick()));
			highscoresPanel.add(new JLabel(String.valueOf(highscore.getScore())));
		}
		
		mainPanel.add(highscoresPanel);
		
		JButton backButton = new JButton("Powrót");
		backButton.addActionListener(event -> {
			dispose();
			new MainMenu();
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		buttonPanel.setBackground(MainMenu.BACKGROUND_COLOR);
		buttonPanel.add(backButton);
		mainPanel.add(buttonPanel);
		
		add(mainPanel);
		pack();
		setVisible(true);
	}
}