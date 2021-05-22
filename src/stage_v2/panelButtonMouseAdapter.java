package stage_v2;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class panelButtonMouseAdapter extends MouseAdapter {
	
	private JPanel panel;
	public panelButtonMouseAdapter(JPanel panel) {
		this.panel=panel;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		panel.setBackground(new Color(112,124,144));
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		panel.setBackground(Color.white);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		panel.setBackground(new Color(60,179,117));
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		panel.setBackground(new Color(112,124,144));
	}
}
