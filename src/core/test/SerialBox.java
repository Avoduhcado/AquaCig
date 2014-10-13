package core.test;

import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SerialBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
	
	public SerialBox() {
		for(int x = 0; x<1000; x++) {
			rects.add(new Rectangle2D.Float(x, 2 * x, 10, 11));
		}
		
		try (FileOutputStream fileOut= new FileOutputStream("tmp/employee.ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(this);
			System.out.println("Serialized data is saved in tmp/employee.ser");
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	public static void deserialize() {
		SerialBox e = null;
		try (FileInputStream fileIn = new FileInputStream("tmp/employee.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			e = (SerialBox) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c)
		{
			System.out.println("SerialBox class not found");
			c.printStackTrace();
			return;
		}
		System.out.println("Deserialized SerialBox...");
		for(int x = 0; x<e.rects.size(); x++) {
			System.out.println("Rects: " + e.rects.get(x));
		}
	}

}
