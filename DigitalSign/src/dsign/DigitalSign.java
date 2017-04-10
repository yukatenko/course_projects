package dsign;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DigitalSign {
	
	JTextField pTextField, aTextField, gammaTextField, xTextField, yTextField, mTextField, hTextField, 
			 	uTextField, zTextField, zzTextField, kTextField, gTextField, sTextField, lTextField, rTextField;
	JLabel checkLabel;
	JButton initButton, msgButton;
	CalcButton keyButton, uzzButton, signButton, checkButton;
	
	DigitalSign() {
		// создаем фрейм
		JFrame frame = new JFrame("Digital Sign");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// основная панель
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// проверочное сравнение
		JPanel compPanel = new JPanel();
		compPanel.setBorder(new TitledBorder("Проверочное сравнение"));
		compPanel.add(new JLabel("<html><h1>S^(H * a^k * S mod p) = y^(a^k * S mod p) mod p</h1></html>"));
		panel.add(compPanel);
		
		// центральная панель		
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.X_AXIS));
		
		// левая панель		
		JPanel lPanel = new JPanel();
		lPanel.setLayout(new BoxLayout(lPanel, BoxLayout.Y_AXIS));
		
		// начальные параметры
		JPanel initPanel = new JPanel();
		initPanel.setLayout(new GridLayout(4, 2, 5, 5));		
		initPanel.setBorder(new TitledBorder("Начальные параметры"));
		initPanel.add(new JLabel("Простое число p"));
		initPanel.add(pTextField = new JTextField());
		initPanel.add(new JLabel("Число a"));
		initPanel.add(aTextField = new JTextField());
		initPanel.add(new JLabel("Число gamma"));
		initPanel.add(gammaTextField = new JTextField());		
		initPanel.add(initButton = new JButton("Генерировать"));		
		lPanel.add(initPanel);
		
		// генерация ключей
		JPanel keyPanel = new JPanel();
		keyPanel.setLayout(new GridLayout(3, 2, 5, 5));		
		keyPanel.setBorder(new TitledBorder("Генерация ключей"));
		keyPanel.add(new JLabel("Секретный ключ x"));
		keyPanel.add(xTextField = new JTextField());
		keyPanel.add(new JLabel("Открытый ключ y"));
		keyPanel.add(yTextField = new JTextField());
		keyPanel.add(keyButton = new CalcButton("Генерировать"));
		lPanel.add(keyPanel);
		
		// сообщение и хэш
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new GridLayout(2, 2, 5, 5));		
		msgPanel.setBorder(new TitledBorder("Сообщение"));
		msgPanel.add(new JLabel("Сообщение M"));
		msgPanel.add(mTextField = new JTextField());
		msgPanel.add(msgButton = new JButton("Генерировать"));
		lPanel.add(msgPanel);		
		
		// правая панель		
		JPanel rPanel = new JPanel();
		rPanel.setLayout(new BoxLayout(rPanel, BoxLayout.Y_AXIS));
		
		// генерация U, вычисление Z и Z'
		JPanel uzzPanel = new JPanel();
		uzzPanel.setLayout(new GridLayout(4, 2, 5, 5));		
		uzzPanel.setBorder(new TitledBorder("Генерация U, вычисление Z и Z'"));
		uzzPanel.add(new JLabel("Случайное число U"));
		uzzPanel.add(uTextField = new JTextField());
		uzzPanel.add(new JLabel("Z = a^U"));
		uzzPanel.add(zTextField = new JTextField());
		uzzPanel.add(new JLabel("Z' = H*a^U"));
		uzzPanel.add(zzTextField = new JTextField());
		uzzPanel.add(uzzButton = new CalcButton("Генерировать"));	
		rPanel.add(uzzPanel);
		
		// вычисление k, g и S
		JPanel signPanel = new JPanel();
		signPanel.setLayout(new GridLayout(4, 2, 5, 5));		
		signPanel.setBorder(new TitledBorder("Вычисление k, g и S"));
		signPanel.add(new JLabel("k = U - x*Z/Z'"));
		signPanel.add(kTextField = new JTextField());
		signPanel.add(new JLabel("g = x*Z/Z'"));
		signPanel.add(gTextField = new JTextField());
		signPanel.add(new JLabel("S = a^g"));
		signPanel.add(sTextField = new JTextField());
		signPanel.add(signButton = new CalcButton("Вычислить"));	
		rPanel.add(signPanel);
		
		cPanel.add(lPanel);
		cPanel.add(rPanel);	
		panel.add(cPanel);
		
		// проверка подписи
		JPanel checkPanel = new JPanel();
		checkPanel.setBorder(new TitledBorder("Проверка подписи"));
		checkPanel.setLayout(new GridLayout(3, 2, 5, 5));
		checkPanel.add(new JLabel("Левая часть"));		
		checkPanel.add(new JLabel("Правая часть"));
		checkPanel.add(lTextField = new JTextField());
		//lTextField.setFocusable(false);
		checkPanel.add(rTextField = new JTextField());
		//rTextField.setFocusable(false);
		checkPanel.add(checkButton = new CalcButton ("Вычислить"));
		checkPanel.add(checkLabel = new JLabel());	
		
		panel.add(checkPanel);
		
		// задаем фрейм, устанавливаем панель
		frame.setContentPane(panel);
						
		// задаем размеры и видимость
		frame.pack();
		frame.setVisible(true);
		
		

		DigitalSignEngine dsEngine = new DigitalSignEngine(this);
		pTextField.getDocument().addDocumentListener(dsEngine);
		aTextField.getDocument().addDocumentListener(dsEngine);
		gammaTextField.getDocument().addDocumentListener(dsEngine);
		xTextField.getDocument().addDocumentListener(dsEngine);
		yTextField.getDocument().addDocumentListener(dsEngine);
		mTextField.getDocument().addDocumentListener(dsEngine);
		uTextField.getDocument().addDocumentListener(dsEngine);
		zTextField.getDocument().addDocumentListener(dsEngine);
		zzTextField.getDocument().addDocumentListener(dsEngine);
		kTextField.getDocument().addDocumentListener(dsEngine);
		gTextField.getDocument().addDocumentListener(dsEngine);
		sTextField.getDocument().addDocumentListener(dsEngine);
		
		
		// обработка нажатия на клавиши
		initButton.addActionListener(dsEngine);
		keyButton.addActionListener(dsEngine);
		msgButton.addActionListener(dsEngine);
		uzzButton.addActionListener(dsEngine);
		signButton.addActionListener(dsEngine);
		checkButton.addActionListener(dsEngine);	
	}
	public static void main(String[] args) {
		DigitalSign ds = new DigitalSign();		
	}
	
	class CalcButton extends JButton {
		CalcButton(String s) {
			super(s);
			setEnabled(false);
		}
	}
}
