package com.twistlock.view;

import com.twistlock.Controller;
import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.twistlock.Parameter.*;

public class PanelGrid extends JPanel
{
	// Attributes
	private Controller ctrl;
	private Game       game;

	private MouseListener mouseListener;

	private int oX;
	private int oY;
	private int maxX;
	private int maxY;

	private int containerW;
	private int containerH;


	// Constructor
	public PanelGrid(Controller ctrl, Game game)
	{
		this.ctrl = ctrl;
		this.game = game;

		this.mouseListener = new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();

				if (x < PanelGrid.this.oX || x > PanelGrid.this.maxX || y < PanelGrid.this.oY || y > PanelGrid.this.maxY)
					return;

				int closestX = x - (x - PanelGrid.this.oX) % (containerW + SPACING_GRID);
				int closestY = y - (y - PanelGrid.this.oY) % (containerH + SPACING_GRID);
				x -= GRID_LOCK_R;
				y -= GRID_LOCK_R;

				double distance = Math.sqrt(Math.pow(x - closestX, 2) + Math.pow(y - closestY, 2));
				if (distance <= GRID_LOCK_R)
				{
					int l = (closestY - PanelGrid.this.oY) / containerH;
					int c = (closestX - PanelGrid.this.oX) / containerW;
					PanelGrid.this.ctrl.play(l, c, false);
				}
			}
		};
		this.addMouseListener(this.mouseListener);
	}


	// Methods
	public void paintComponent(Graphics gSrc)
	{
		super.paintComponent(gSrc);
		Graphics2D g = (Graphics2D) gSrc;

		/* Calculus */
		char[][] gridColor = this.game.getGridColor();
		int[][]  gridValue = this.game.getGridValue();
		char[][] gridLock  = this.game.getGridCorner();

		// Width for containers only
		int width  = this.getWidth() - (2 * PADDING_GRID + (gridColor[0].length - 1) * SPACING_GRID);
		int height = this.getHeight() - (2 * PADDING_GRID + (gridColor.length - 1) * SPACING_GRID);

		// Height and width of containers
		this.containerH = (int) Math.min((double) height / gridColor.length, width / (gridColor[0].length * 1.5));
		this.containerW = (int) (1.5 * containerH);

		// Origin and max to center the whole container & lock group
		this.oX   = PADDING_GRID + (width - gridColor[0].length * containerW) / 2;
		this.oY   = PADDING_GRID + (height - gridColor.length * containerH) / 2;
		this.maxX = this.oX + gridColor[0].length * (containerW + SPACING_GRID) + GRID_LOCK_R;
		this.maxY = this.oY + gridColor.length * (containerH + SPACING_GRID) + GRID_LOCK_R;

		// Shift for the value in the container
		int shiftValX = containerW / 2;
		int shiftValY = containerH / 2;

		/* Background */
		g.setStroke(STROKE);
		g.setFont(FONT);
		g.setColor(GRID_BG);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		/* Container */
		FontMetrics metrics = g.getFontMetrics();
		String      value;
		for (int l = 0, prevY = oY; l < gridColor.length; l++, prevY += containerH + SPACING_GRID)
			for (int c = 0, prevX = oX; c < gridColor[l].length; c++, prevX += containerW + SPACING_GRID)
			{
				g.setColor(OWNER.get(gridColor[l][c]));
				g.fillRect(prevX, prevY, containerW, containerH);
				g.setColor(GRID_BORDER);
				g.drawRect(prevX, prevY, containerW, containerH);

				value = "" + gridValue[l][c];
				g.drawString(value, prevX + shiftValX - metrics.stringWidth(value) / 2, prevY + shiftValY + metrics.getAscent() / 2);
			}

		/* Lock */
		oX = oX - GRID_LOCK_D / 2 - SPACING_GRID / 2;
		oY = oY - GRID_LOCK_D / 2 - SPACING_GRID / 2;
		for (int l = 0, prevY = oY; l < gridLock.length; l++, prevY += containerH + SPACING_GRID)
			for (int c = 0, prevX = oX; c < gridLock[l].length; c++, prevX += containerW + SPACING_GRID)
			{
				g.setColor(OWNER.get(gridLock[l][c]));
				g.fillOval(prevX, prevY, GRID_LOCK_D, GRID_LOCK_D);
				g.setColor(GRID_BORDER);
				g.drawOval(prevX, prevY, GRID_LOCK_D, GRID_LOCK_D);
			}
	}

	public void end()
	{
		this.removeMouseListener(this.mouseListener);
	}
}
