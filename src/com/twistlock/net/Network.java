package com.twistlock.net;

import com.twistlock.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import static com.twistlock.net.Code.*;

public class Network
{
	// Attributes
	private Controller ctrl;

	private DatagramSocket socket;

	private InetAddress serverIp;
	private int         serverPort;


	// Constructor
	public Network(Controller ctrl, String rawIp, int serverPort) throws Exception
	{
		this.ctrl = ctrl;

		this.socket = new DatagramSocket();

		this.serverIp   = InetAddress.getByName(rawIp);
		this.serverPort = serverPort;
	}


	// Methods
	public void sendName()
	{
		// Enter a name
		System.out.print("Nom : ");
		Scanner input = new Scanner(System.in);
		String  name  = input.nextLine();
		if (name == null || name.equals(""))
			name = "Docker N°" + (int) (Math.random() * 5000);

		// Send name and receive welcome message
		this.send(name);
		System.out.println(this.receive(512));
	}

	public void parseStart()
	{
		// Wait for start message and parse it
		String start = null;
		while (start == null || !start.startsWith(START))
			start = this.receive(512);

		// Parse dockers name, grid, and id
		String[] lstDockerName = start.substring(start.indexOf("(") + 1, start.indexOf(")")).split(",");
		int[][]  gridValue     = this.parseGrid(start);

		String rawId = this.receive(128);
		int    id    = 1;
		if (rawId != null)
			id = Integer.parseInt(rawId.substring(rawId.indexOf("=") + 1));
		System.out.println(rawId);

		this.ctrl.init(lstDockerName, gridValue, id - 1);
	}

	private int[][] parseGrid(String start)
	{
		String   rawMap  = start.substring(start.indexOf("=") + 1, start.lastIndexOf("|"));
		String[] rawLine = rawMap.split("\\|");

		String[] splitLine = rawLine[0].split(":");
		int[][]  gridValue = new int[rawLine.length][splitLine.length];

		for (int l = 0; l < rawLine.length; l++)
		{
			splitLine = rawLine[l].split(":");
			for (int c = 0; c < splitLine.length; c++)
			     gridValue[l][c] = Integer.parseInt(splitLine[c]);
		}

		return gridValue;
	}

	public void listen()
	{
		String message;
		String code = "-1";

		while (!code.equals(END))
		{
			message = this.receive(512);
			code    = message.substring(0, message.indexOf("-"));

			if (code.equals(OPP_PLAY))
				this.oppPlay(message);
			else if (code.equals(OPP_ILLEGAL))
				this.ctrl.fakePlay();
			System.out.println(message);
		}
	}

	public void play(int line, int col)
	{
		int corner = 3;
		if (line == 0 && col == 0)
			corner = 1;
		else if (line == 0)
			corner = 2;
		else if (col == 0)
			corner = 4;

		if (line != 0)
			line--;
		if (col != 0)
			col--;

		this.send(String.format("%d%c%s", line + 1, 'A' + col, corner));
	}

	private void oppPlay(String msg)
	{
		String rawPlayed = msg.substring(msg.indexOf(":") + 1);
		int    l         = Integer.parseInt(rawPlayed.substring(0, 1)) - 1;
		int    c         = rawPlayed.charAt(1) - 'A';
		int    corner    = Integer.parseInt(rawPlayed.substring(2, 3));

		int lCorn = l + (corner == 4 || corner == 3 ? 1 : 0);
		int cCorn = c + (corner == 2 || corner == 3 ? 1 : 0);
		this.ctrl.play(lCorn, cCorn, true);
	}

	private void send(String msg)
	{
		try
		{
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), this.serverIp, this.serverPort);
			this.socket.send(packet);
		}
		catch (Exception e) { this.ctrl.stop(); }
	}

	private String receive(int buffer)
	{
		String msg = null;
		try
		{
			DatagramPacket packet = new DatagramPacket(new byte[buffer], buffer);
			this.socket.receive(packet);
			msg = new String(packet.getData());
			msg = msg.substring(0, msg.indexOf(0));
		}
		catch (IOException e) { this.ctrl.stop(); }
		return msg;
	}
}
