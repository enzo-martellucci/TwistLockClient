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

	private boolean isPlaying;


	// Constructor
	public Controller(String serverIp, int serverPort)
	{
		// Initialisation
		this.game      = new Game();
		this.isPlaying = false;

		try
		{
			this.network = new Network(this, serverIp, serverPort);
		}
		catch (Exception e) { return; }

		this.network.sendName();
		this.network.parseStart();
		this.network.listen();
	}


	// Setters
	public void setIsPlaying(boolean isPlaying) { this.isPlaying = isPlaying; }


	// Methods
	public void init(String[] lstName, int[][] gridValue)
	{
		this.game.initDocker(lstName);
		this.game.initGrid(gridValue);

		this.viewGUI = new ViewGUI(this, this.game);
	}

	public void play(int l, int c, boolean fromNet)
	{
		System.out.println(fromNet + " " + isPlaying);
		if (!fromNet)
		{
			this.game      = new Game();
			this.isPlaying = false;
			if (!this.isPlaying)
				return;
			this.isPlaying = false;
			this.network.play(l, c);
		}

		this.game.play(l, c);
		this.viewGUI.maj();

		if (this.game.isGameOver())
			this.viewGUI.end(this.game.getWinner(), false);
	}

	public void stop()
	{
		this.viewGUI.end(this.game.getWinner(), true);
	}


	// Main
	public static void main(String[] args)
	{
		args = new String[]{ "127.0.0.1", "8000" };

		if (args.length != 2)
		{
			System.out.println("Usage : Controller serverIp serverPort");
			return;
		}

		new Controller(args[0], Integer.parseInt(args[1]));
	}
}
