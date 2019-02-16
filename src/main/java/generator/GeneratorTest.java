package generator;

import java.awt.Label;

import javax.swing.JFrame;

public class GeneratorTest extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public GeneratorTest() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Run.bat test");
		add(new Label("Hello world!"));
		setSize(100, 100);
	}
	
	public static void main(String[] args) {
		new GeneratorTest().setVisible(true);
	}

}
