package memor;


import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Board extends JFrame {
	private static List<ImageIcon> images = new ArrayList<>();
	private static ImageIcon MemoryIcon;
	public static void loadAllImages() {
		for(int i = 1; i <= 25; i++)
			images.add(new ImageIcon("Z:\\MemoryGame\\image" + i + ".jpg"));
		MemoryIcon = new ImageIcon("Z:\\MemoryGame\\Memory.jpg");
	}
	private int[][] numbers;
	private boolean[][] collected;

	private int remainingPairs;
	
	
	private List<ImageIcon> scaledImages = new ArrayList<>();
	private ImageIcon scaledMemory;
	
	private Thread clockThread;
	private Clock clock;
	private JLabel timer;
	
	public Board(int length, int height) {
		setTitle("Memory The Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel grid = new JPanel(new GridLayout(height, length));
		clock = new Clock();
		clockThread = new Thread(clock);
		timer = new JLabel();
		timer.setAlignmentX(CENTER_ALIGNMENT);
		timer.setFont(timer.getFont().deriveFont(40f));
		clockThread.start();
		
		remainingPairs = length * height / 2;
		numbers = new int[height][length];
		collected = new boolean[height][length];
		
		List<Integer> imagePairIDs = new ArrayList<>();
		int size =   Math.min(1000/length, 700/height);
		for(int i = 0; i < remainingPairs; i++) {
		
			imagePairIDs.add(i);
			imagePairIDs.add(i);
			scaledImages.add(new ImageIcon(images.get(i).getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT)));
		}
		Collections.shuffle(imagePairIDs); 
		
		scaledMemory = new ImageIcon(MemoryIcon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
		for(int i = 0; i < height; i++)
			for(int j = 0; j < length; j++) {
				int pairNumber = imagePairIDs.get(i * length + j);
				numbers[i][j] = pairNumber;
				JLabel entity = new JLabel(scaledMemory);
				
				
				final int x = j;
				final int y = i;
				
			
				entity.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						handleClicking(entity, x, y);
					}
				});
				grid.add(entity);
			}
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(timer);
		grid.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		add(grid);
		pack();
		setVisible(true);
	}
	
	private Game firstVisible = new Game();
	private Game secondVisible = new Game();
	private TimerTask firstEntityHider, secondEntityHider;
	private void handleClicking(JLabel clickedEntity, int x, int y) {
		if(collected[y][x])	
			return;
		int pairNumber = numbers[y][x];
		if(!firstVisible.isMarkedAsVisible()) {	
			handleNoVisible(clickedEntity, x, y);
			return;
		} else if(!secondVisible.isMarkedAsVisible()) {	
			secondVisible.markAsVisible(clickedEntity, x, y);
			clickedEntity.setIcon(scaledImages.get(pairNumber));
			
			
			firstEntityHider.cancel();

			if(secondVisible.getEntityNumber() == firstVisible.getEntityNumber() && (secondVisible.getX()!=firstVisible.getX() || secondVisible.getY()!=firstVisible.getY())) {	
				int firstX = firstVisible.x;
				int firstY = firstVisible.y;
				collected[firstY][firstX] = true;
				int secondX = secondVisible.x;
				int secondY = secondVisible.y;
				collected[secondY][secondX] = true;
				remainingPairs--;
				if(remainingPairs == 0) {	
					clockThread.interrupt();
					int time = clock.time;
					double sizeBonus = Math.pow(collected[0].length * collected.length / 2, 2);	
					int score = (int) (time/sizeBonus);
					String baseMessage = "Uda³o Ci siê ukoñczyæ poziom!\n"
							+ "Twój czas: " + time + "\n"
							+ "Dzielnik czasu za wybrany wymiar planszy: " + sizeBonus + "\n"
							+ "Ostateczny wynik: " + score + "\n\n";
					if(HighscoresInfo.get().isTop10(time)) {
						JTextArea msg = new JTextArea(baseMessage + "Gratulacje! Twój wynik znajdzie siê na liœcie wysokich wyników!\n"
								+ "Wpisz swoj¹ nazwê");
						String nick = JOptionPane.showInputDialog(null, msg);
						if(nick == null) {
							dispose();
							new MainMenu();
						} else {
							Highscore highscore = new Highscore(nick, score);
							HighscoresInfo.get().addHighscore(highscore);
							dispose();
							new HighscoresPanel();
						}
					} else {
						JTextArea msg = new JTextArea(baseMessage + "Niestety, Twój wynik jest za du¿y, aby znalaz³ siê na liœcie wyników:(");
						JOptionPane.showMessageDialog(null, msg);
						dispose();
						new MainMenu();
					}
				}
				
				
				firstVisible.markAsInvisible();
				secondVisible.markAsInvisible();
			} else {	
				secondEntityHider = new TimerTask() {
					@Override
					public void run() {
						hideBoth();
					}
				};
				new Timer().schedule(secondEntityHider, 2000);	
				return;
			}
		} else {
			
			hideBoth();
			secondEntityHider.cancel();	
			handleNoVisible(clickedEntity, x, y);
		}
	}
	
	private void handleNoVisible(JLabel clickedEntity, int x, int y) {
		int pairNumber = numbers[y][x];
		firstVisible.markAsVisible(clickedEntity, x, y);
		clickedEntity.setIcon(scaledImages.get(pairNumber));

		firstEntityHider = new TimerTask() {
			@Override
			public void run() {
				firstVisible.markAsInvisible();
				clickedEntity.setIcon(scaledMemory);
			}
		};
		new Timer().schedule(firstEntityHider, 2000);
	}
	
	private void hideBoth() {
		firstVisible.entity.setIcon(scaledMemory);
		secondVisible.entity.setIcon(scaledMemory);
		firstVisible.markAsInvisible();
		secondVisible.markAsInvisible();
	}
	
	class Game {
		int x; 
		int y;
		JLabel entity;
		
		public int getEntityNumber() {	
			return numbers[y][x];
		}
		
		public boolean isMarkedAsVisible() {
			return entity != null;
		}
		
		public void markAsVisible(JLabel entity, int x, int y) {	
			this.entity = entity;
			this.x = x;
			this.y = y;
		}
		
		public void markAsInvisible() {	
			this.entity = null;
		}
		public int getX()
		{
			return x;
		}
		public int getY()
		{
			return y;
		}
	}
	
	class Clock implements Runnable {
		private int time;

		@Override
		public void run() {
			try {
				while(!Thread.currentThread().isInterrupted()) {
					time++;
					SwingUtilities.invokeLater(() -> timer.setText("Czas: " + time + "ms"));
					Thread.sleep(1);
				}
			} catch (InterruptedException e) {}	
		}		
	}
}