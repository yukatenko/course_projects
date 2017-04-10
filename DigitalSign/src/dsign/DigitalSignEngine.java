package dsign;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DigitalSignEngine implements ActionListener, DocumentListener {

	DigitalSign parent;	
	BigInteger p, a, gamma, x, y, m, u, z, zz, k, g, s, left, right;
	int h;
		
	Random rnd = new Random();
	DigitalSignEngine(DigitalSign parent) {
		this.parent = parent;
	}
	
	void checkP() {
		if (!p.equals(new BigInteger (parent.pTextField.getText()))) {
			p = new BigInteger (parent.pTextField.getText());
		}
	}
	void checkA() {
		if (!a.equals(new BigInteger (parent.aTextField.getText()))) {
			a = new BigInteger (parent.aTextField.getText());
		}
	}
	void checkGamma() {
		if (!gamma.equals(new BigInteger (parent.gammaTextField.getText()))) {
			gamma = new BigInteger (parent.gammaTextField.getText());
		}
	}
	void checkX() {
		if (!x.equals(new BigInteger (parent.xTextField.getText()))) {
			x = new BigInteger (parent.xTextField.getText());
		}
	}
	void checkY() {
		if (!y.equals(new BigInteger (parent.yTextField.getText()))) {
			y = new BigInteger (parent.yTextField.getText());
		}
	}
	void checkM() {
		if (!m.equals(new BigInteger (parent.mTextField.getText()))) {
			m = new BigInteger (parent.mTextField.getText());
		}
	}
	void checkU() {
		if (!u.equals(new BigInteger (parent.uTextField.getText()))) {
			u = new BigInteger (parent.uTextField.getText());
		}
	}
	void checkZ() {
		if (!z.equals(new BigInteger (parent.zTextField.getText()))) {
			z = new BigInteger (parent.zTextField.getText());
		}
	}
	void checkZz() {
		if (!zz.equals(new BigInteger (parent.zzTextField.getText()))) {
			zz = new BigInteger (parent.zzTextField.getText());
		}
	}
	void checkK() {
		if (!k.equals(new BigInteger (parent.kTextField.getText()))) {
			k = new BigInteger (parent.kTextField.getText());
		}
	}
	void checkG() {
		if (!g.equals(new BigInteger (parent.gTextField.getText()))) {
			g = new BigInteger (parent.gTextField.getText());
		}
	}
	void checkS() {
		if (!s.equals(new BigInteger (parent.sTextField.getText()))) {
			s = new BigInteger (parent.sTextField.getText());
		}
	}
	
	
	// обработка нажати€ клавиш
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
				
		// initButton
		if (src == parent.initButton) {
			// генерируем простое число p
			p = new BigInteger(1024, 1, rnd);
			parent.pTextField.setText(p.toString());
			
			// гамма
			gamma = p.subtract(BigInteger.ONE);
			parent.gammaTextField.setText(gamma.toString());
			
			// генерируем а, пока a>p и a^gamma != 1 mod p
			do {
				a = new BigInteger(1024, rnd);
			} while ((a.compareTo(p) == 1) && (!a.modPow(gamma, p).equals(BigInteger.ONE)));
			
			parent.aTextField.setText(a.toString());	
			
		} 
		
		// keyButton
		else if (src == parent.keyButton) {
			// проверка неизменности p и a
			
			p = new BigInteger(parent.pTextField.getText());
			a = new BigInteger(parent.aTextField.getText());
			
			// генерируем секретный ключ x
			x = new BigInteger(160, rnd);
			parent.xTextField.setText(x.toString());
			
			// вычисл€ем значение открытого ключа у
			y = a.modPow(x, p);
			parent.yTextField.setText(y.toString());	
			
		} 
		
		// msgButton
		else if (src == parent.msgButton) {
			// генерируем сообщение
			m = new BigInteger(460, rnd);
			parent.mTextField.setText(m.toString());
		} 
		
		// uzzButton
		else if (src == parent.uzzButton) {
			checkP();
			checkA();
			checkM();
			
			// генерируем U и вычисл€ем Z и Z', пока Ќќƒ(Z', gamma) != 1 (Z' должно быть обратимым)
			do {
				u = new BigInteger(160, rnd);
				z = a.modPow(u, p);
				zz = a.modPow(u, p).multiply(BigInteger.valueOf(m.hashCode())).mod(p);				
			} while (!zz.gcd(gamma).equals(BigInteger.ONE));
			
			parent.uTextField.setText(u.toString());			
			parent.zTextField.setText(z.toString());			
			parent.zzTextField.setText(zz.toString());	
			
		} 
		
		// signButton
		else if (src == parent.signButton) {
			checkP();
			checkA();
			checkGamma();
			checkX();
			checkU();
			checkZ();
			checkZz();

			// вычисл€ем k
			k = u.subtract(x.multiply(z).multiply(zz.modInverse(gamma))).mod(gamma);
			parent.kTextField.setText(k.toString());
			
			// вычисл€ем g			
			g = x.multiply(z).multiply(zz.modInverse(gamma)).mod(gamma);
			parent.gTextField.setText(g.toString());
			
			// вычисл€ем S			
			s = a.modPow(g, p);
			parent.sTextField.setText(s.toString());
			
		} 
		
		// checkButton
		else if (src == parent.checkButton) {
			checkP();
			checkA();
			checkY();
			checkM();
			checkK();
			checkS();
			
			// вычисл€ем левую часть проверочного сравнени€
			left = s.modPow(BigInteger.valueOf(m.hashCode()).multiply(a.modPow(k, p)).multiply(s).mod(p), p);
			parent.lTextField.setText(left.toString());
			
			// вычисл€ем правую часть проверочного сравнени€
			right = y.modPow((a.modPow(k, p)).multiply(s).mod(p), p);
			parent.rTextField.setText(right.toString());
			
			// проверка подписи - сравниваем левую и правую части
			if (left.equals(right)) {
				parent.checkLabel.setText("ѕодпись верна");
			} else {
				parent.checkLabel.setText("ѕодпись не верна");
			}
		}
	}
	
	
	// обработка текстовых полей
	@Override
	public void insertUpdate(DocumentEvent e) {
		// p
		if (e.getDocument().equals(parent.pTextField.getDocument())) {
			// keyButton
			if (!parent.aTextField.getText().isEmpty()) {
				parent.keyButton.setEnabled(true);
			}
			
			// uzzButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(true);
			}
			
			//signButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.xTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
			
			// checkButton
			if (!parent.aTextField.getText().isEmpty() && !parent.yTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.kTextField.getText().isEmpty() && !parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}

		}
		
		// a
		else if (e.getDocument().equals(parent.aTextField.getDocument())) {
			// keyButton
			if (!parent.pTextField.getText().isEmpty()) {
				parent.keyButton.setEnabled(true);
			}
			
			// uzzButton
			if (!parent.pTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(true);
			}
			
			//signButton
			if (!parent.pTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.xTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
			
			// checkButton
			if (!parent.pTextField.getText().isEmpty() && !parent.yTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.kTextField.getText().isEmpty() && !parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}			
		}
		
		// gamma
		else if (e.getDocument().equals(parent.gammaTextField.getDocument())) {
			// uzzButton
			if (!parent.aTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(true);
			}
			
			//signButton
			if (!parent.aTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.xTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
		}
		
		// x
		else if (e.getDocument().equals(parent.xTextField.getDocument())) {
			//signButton		
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
		}
		
		// y
		else if (e.getDocument().equals(parent.yTextField.getDocument())) {
			// checkButton
			if (!parent.aTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.kTextField.getText().isEmpty() && !parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}

		}
		
		// m
		else if (e.getDocument().equals(parent.mTextField.getDocument())) {
			// uzzButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(true);
			}
			
			// checkButton
			if (!parent.aTextField.getText().isEmpty() && !parent.yTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() &&
					!parent.kTextField.getText().isEmpty() && !parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}
		}
		
		// u
		else if (e.getDocument().equals(parent.uTextField.getDocument())) {
			//signButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
		}
		
		// z
		else if (e.getDocument().equals(parent.zTextField.getDocument())) {
			//signButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty() &&
					!parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
		}
		
		// z'
		else if (e.getDocument().equals(parent.zzTextField.getDocument())) {
			//signButton
			if (!parent.aTextField.getText().isEmpty() && !parent.gammaTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty() && !parent.uTextField.getText().isEmpty() && !parent.zTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(true);
			}
		}
		
		// k
		else if (e.getDocument().equals(parent.kTextField.getDocument())) {
			// checkButton
			if (!parent.aTextField.getText().isEmpty() && !parent.yTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.pTextField.getText().isEmpty() && !parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}
		}
		
		// s
		else if (e.getDocument().equals(parent.sTextField.getDocument())) {
			// checkButton
			if (!parent.aTextField.getText().isEmpty() && !parent.yTextField.getText().isEmpty() && !parent.mTextField.getText().isEmpty() &&
					!parent.kTextField.getText().isEmpty() && !parent.pTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(true);
			}
		}
	}
	

	@Override
	public void removeUpdate(DocumentEvent e) {
		// p
		if (e.getDocument().equals(parent.pTextField.getDocument())) {
			// keyButton
			if (parent.pTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty()) {
				parent.keyButton.setEnabled(false);
			}
			
			// uzzButton
			if (parent.pTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(false);
			}
			
			//signButton
			if (parent.pTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.xTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
			
			// checkButton
			if (parent.pTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.yTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.kTextField.getText().isEmpty() || parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}

		}
		
		// a
		else if (e.getDocument().equals(parent.aTextField.getDocument())) {
			// keyButton
			if (parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty()) {
				parent.keyButton.setEnabled(false);
			}
			
			// uzzButton
			if (parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(false);
			}
			
			//signButton
			if (parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.xTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
			
			// checkButton
			if (parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.yTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.kTextField.getText().isEmpty() || parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}			
		}
		
		// gamma
		else if (e.getDocument().equals(parent.gammaTextField.getDocument())) {
			// uzzButton
			if (parent.gammaTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(false);
			}
			
			//signButton
			if (parent.gammaTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.xTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
		}
		
		// x
		else if (e.getDocument().equals(parent.xTextField.getDocument())) {
			//signButton		
			if (parent.xTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
		}
		
		// y
		else if (e.getDocument().equals(parent.yTextField.getDocument())) {
			// checkButton
			if (parent.yTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.kTextField.getText().isEmpty() || parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}

		}
		
		// m
		else if (e.getDocument().equals(parent.mTextField.getDocument())) {
			// uzzButton
			if (parent.mTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty()) {
				parent.uzzButton.setEnabled(false);
			}
			
			// checkButton
			if (parent.mTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.yTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() ||
					parent.kTextField.getText().isEmpty() || parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}
		}
		
		// u
		else if (e.getDocument().equals(parent.uTextField.getDocument())) {
			//signButton
			if (parent.uTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
		}
		
		// z
		else if (e.getDocument().equals(parent.zTextField.getDocument())) {
			//signButton
			if (parent.zTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty() ||
					parent.zzTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
		}
		
		// z'
		else if (e.getDocument().equals(parent.zzTextField.getDocument())) {
			//signButton
			if (parent.zzTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.gammaTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty() || parent.uTextField.getText().isEmpty() || parent.zTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty()) {
				parent.signButton.setEnabled(false);
			}
		}
		
		// k
		else if (e.getDocument().equals(parent.kTextField.getDocument())) {
			// checkButton
			if (parent.kTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.yTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.pTextField.getText().isEmpty() || parent.sTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}
		}
		
		// s
		else if (e.getDocument().equals(parent.sTextField.getDocument())) {
			// checkButton
			if (parent.sTextField.getText().isEmpty() || parent.aTextField.getText().isEmpty() || parent.yTextField.getText().isEmpty() || parent.mTextField.getText().isEmpty() ||
					parent.kTextField.getText().isEmpty() || parent.pTextField.getText().isEmpty()) {
				parent.checkButton.setEnabled(false);
			}
		}
		
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	} 	
}
