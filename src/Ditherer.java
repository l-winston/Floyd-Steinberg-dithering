import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Ditherer {
	static final int round = 100;
	String imagePath;
	BufferedImage image;

	JFrame display;

	public static void main(String[] args) {
		Ditherer d = new Ditherer("res/lion.jpg");
		d.roundColors();
		//d.dither();
	}

	public void roundColors() {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, roundColor(toColor(image.getRGB(x, y))).getRGB());
			}
		}
	}

	public void dither() {

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color oldColor = toColor(image.getRGB(x, y));
				Color newColor = roundColor(oldColor);

				image.setRGB(x, y, newColor.getRGB());

				Color error = new Color(oldColor.getRed() - newColor.getRed(),
						oldColor.getGreen() - newColor.getGreen(), oldColor.getBlue() - newColor.getBlue());
				try {
					image.setRGB(x + 1, y,
							addColor(toColor(image.getRGB(x + 1, y)), multColor(error, 7 / 16f)).getRGB());
				} catch (Exception e) {
				}

				try {
					image.setRGB(x - 1, y + 1,
							addColor(toColor(image.getRGB(x - 1, y + 1)), multColor(error, 3 / 16f)).getRGB());
				} catch (Exception e) {
				}

				try {
					image.setRGB(x, y + 1,
							addColor(toColor(image.getRGB(x, y + 1)), multColor(error, 5 / 16f)).getRGB());
				} catch (Exception e) {
				}

				try {
					image.setRGB(x + 1, y + 1,
							addColor(toColor(image.getRGB(x + 1, y + 1)), multColor(error, 1 / 16f)).getRGB());
				} catch (Exception e) {
				}
			}
		}
	}

	public Color multColor(Color c, double scalar) {
		return new Color((int) Math.round(c.getRed() * scalar), (int) Math.round(c.getGreen() * scalar),
				(int) Math.round(c.getBlue() * scalar));
	}

	public Color addColor(Color c1, Color c2) {
		return new Color(Math.min(c1.getRed() + c2.getRed(), 255), Math.min(c1.getGreen() + c2.getGreen(), 255),
				Math.min(c1.getBlue() + c2.getBlue(), 255));
	}

	public Color toColor(int rgb) {
		return new Color((rgb & 0x00ff0000) >> 16, (rgb & 0x0000ff00) >> 8, rgb & 0x000000ff);
	}

	public Color roundColor(Color c) {
		int red = c.getRed() / round;
		red *= round;
		int green = c.getGreen() / round;
		green *= round;
		int blue = c.getBlue() / round;
		blue *= round;
		return new Color(red, green, blue);
	}

	public Ditherer(String imagePath) {
		this.imagePath = imagePath;
		try {
			image = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			System.err.println("Image File Error!");
			e.printStackTrace();
		}
		display = new JFrame();
		display.setVisible(true);
		display.setResizable(false);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.getContentPane().add(new JLabel(new ImageIcon(image)));
		display.pack();
		display.setLocationRelativeTo(null);
	}

}
