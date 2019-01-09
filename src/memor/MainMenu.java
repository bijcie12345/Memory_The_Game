package memor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainMenu extends JFrame {
	public static void main(String[] args) {
		Board.loadAllImages();
		SwingUtilities.invokeLater(() -> {
			new MainMenu();			
		});
	}
	
	public static final Color BACKGROUND_COLOR = new Color(255, 204, 128);

	private static final Dimension BUTTON_SIZE = new Dimension(150, 40);
	
	
	private JPanel centerPanel;
	
	public MainMenu() {
		setTitle("Memory The Game");
        setPreferredSize(new Dimension(300, 400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initNorthPanel();
		initCenterPanel();
		
		pack();
		setVisible(true);
	}
	
	private void initNorthPanel() {
		JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("Z:\\MemoryGame\\Memory.png");
		JLabel iconLabel = new JLabel(icon);
		JLabel eskaLabel = new JLabel("s16428", JLabel.CENTER);
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(eskaLabel, BorderLayout.SOUTH);
        panel.setBackground(BACKGROUND_COLOR);
        add(panel, BorderLayout.NORTH);
	}
	
	private void initCenterPanel() {
		centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
		
		addButtonToCenterPanel("Nowa gra", event -> {
			dispose();	
			new BoardSizeChoosing();
		});
        addButtonToCenterPanel("Wysokie wyniki", event -> {
        	dispose();
        	new HighscoresPanel();
        });
        addButtonToCenterPanel("Opuœæ grê", event -> System.exit(0));
        
        add(centerPanel, BorderLayout.CENTER);
	}
	
	private void addButtonToCenterPanel(String name, ActionListener onClickAction) {
		JButton button = new JButton(name);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(BUTTON_SIZE);
	    button.setMaximumSize(BUTTON_SIZE);
	    button.addActionListener(onClickAction);
		centerPanel.add(button);
	}
}