package memor;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoresInfo implements Serializable {
	private static HighscoresInfo singleton;
	public static HighscoresInfo get() {
		if(singleton != null)
			return singleton;
		try {
			File file = new File("Rekordy.txt");
			if(!file.exists()) {

				singleton = new HighscoresInfo();
				return singleton;
			}
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			singleton = (HighscoresInfo) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return singleton;
	}
	 
	private HighscoresInfo() {}
	
	
	private List<Highscore> highscores = new ArrayList<>();
	
	public boolean isTop10(int result) {
		if(highscores.size() < 10)
			return true;

		Highscore worst = highscores.get(9);	
		if(result < worst.getScore())	
			return true;
		else
			return false;
	}
	
	public List<Highscore> getList() {
		return highscores;
	}
	
	public void addHighscore(Highscore highscore) {
		highscores.add(highscore);
		Collections.sort(highscores, Collections.reverseOrder());	
		save();
	}
	
	public void save() {
		File file = new File("Rekordy.txt");
		try {
			if(!file.exists())
				file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
class Highscore implements Serializable, Comparable<Highscore> {
	private String nick;
	private int score;	

	public Highscore(String nick, int score) {
		this.nick = nick;
		this.score = score;
	}
	
	
	@Override
	public int compareTo(Highscore arg0) {
		return arg0.score - score;
	}
	
	public String getNick() {
		return nick;
	}
	
	public int getScore() {
		return score;
	}
}