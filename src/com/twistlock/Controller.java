package com.twistlock;

import com.twistlock.model.Game;
import com.twistlock.net.Network;
import com.twistlock.view.ViewGUI;

public class Controller
{
	// Attributes
	private Network network;
	private Game    game;
	private ViewGUI viewGUI;

	private int tracked;

	// Constructor
	public Controller(String serverIp, int serverPort)
	{
		// Initialisation
		this.game = new Game();

		try
		{
			this.network = new Network(this, serverIp, serverPort);
		}
		catch (Exception e) { return; }

		this.network.sendName();
		this.network.parseStart();
		this.network.listen();
	}

	// Methods
	public void init(String[] lstName, int[][] gridValue, int tracked)
	{
		this.game.initDocker(lstName);
		this.game.initGrid(gridValue);
		this.tracked = tracked;

		this.viewGUI = new ViewGUI(this, this.game);
	}

	public void play(int l, int c, boolean fromNet)
	{
		if (!fromNet)
		{
			if (this.game.getDocker() != this.tracked)
				return;
			this.network.play(l, c);
		}

		this.game.play(l, c);
		this.viewGUI.maj();

		if (this.game.isGameOver())
			this.viewGUI.end(this.game.getWinner(), false);
	}

	public void fakePlay()
	{
		this.game.fakePlay();
		this.viewGUI.maj();
	}

	public void stop()
	{
		this.viewGUI.end(this.game.getWinner(), true);
	}


	// Main
	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage : Controller serverIp serverPort");
			return;
		}

		new Controller(args[0], Integer.parseInt(args[1]));
	}
}
