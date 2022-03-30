package com.twistlock.view;

import com.twistlock.Controller;
import com.twistlock.model.Docker;
import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static com.twistlock.Parameter.DOCKER_COL_BG;

public class ViewGUI extends JFrame
{
	// Attributes
	private PanelDocker[] lstPanelDocker;
	private PanelGrid     panelGrid;


	// Constructor
	public ViewGUI(Controller ctrl, Game game)
	{
		// Create Panel
		Docker[] lstDocker = game.getLstDocker();
		this.lstPanelDocker = new PanelDocker[lstDocker.length];
		for (int i = 0; i < this.lstPanelDocker.length; i++)
		     this.lstPanelDocker[i] = new PanelDocker(game, lstDocker[i]);
		this.panelGrid = new PanelGrid(ctrl, game);

		// Place panel
		JPanel panelL = new JPanel(new BorderLayout());
		JPanel panelR = new JPanel(new BorderLayout());
		panelL.setBackground(DOCKER_COL_BG);
		panelR.setBackground(DOCKER_COL_BG);

		panelL.add(this.lstPanelDocker[0], BorderLayout.NORTH);
		panelR.add(this.lstPanelDocker[1], BorderLayout.NORTH);
		if (this.lstPanelDocker.length > 2)
			panelL.add(this.lstPanelDocker[2], BorderLayout.SOUTH);
		if (this.lstPanelDocker.length > 3)
			panelR.add(this.lstPanelDocker[3], BorderLayout.SOUTH);

		this.add(panelL, BorderLayout.WEST);
		this.add(panelR, BorderLayout.EAST);
		this.add(this.panelGrid, BorderLayout.CENTER);


		// Parameters
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				Dimension panelDockerSize = new Dimension((int) (0.15 * ViewGUI.this.getWidth()), (int) (0.25 * ViewGUI.this.getHeight()));
				for (int i = 0; i < ViewGUI.this.lstPanelDocker.length; i++)
				{
					ViewGUI.this.lstPanelDocker[i].setPreferredSize(panelDockerSize);
					ViewGUI.this.lstPanelDocker[i].revalidate();
				}
			}
		});

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (0.8 * screen.width), (int) (0.8 * screen.height));
		this.setLocation((int) (0.1 * screen.width), (int) (0.1 * screen.height));

		this.setTitle("Twist Lock Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}


	// Methods
	public void maj()
	{
		for (int i = 0; i < this.lstPanelDocker.length; i++)
		     this.lstPanelDocker[i].maj();
		this.panelGrid.repaint();
	}

	public void end(Docker winner, boolean disconnected)
	{
		this.panelGrid.end();
		this.setTitle((disconnected ? "(disconnected) " : "") + winner.getName() + " win with " + winner.getScore() + " point(s)");
	}
}
