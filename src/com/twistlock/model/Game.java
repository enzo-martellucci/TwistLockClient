package com.twistlock.model;

import java.util.Arrays;

import static com.twistlock.Parameter.LST_COLOR;
import static com.twistlock.Parameter.NEUTRAL;

public class Game
{
	// Attributes
	private Docker[] lstDocker;
	private int      docker;

	private int[][]  gridValue;
	private char[][] gridColor;
	private char[][] gridCorner;

	private boolean gameOver;


	// Constructor
	public Game()
	{
		this.gameOver = false;
	}

	public void initDocker(String[] lstName)
	{
		this.lstDocker = new Docker[lstName.length];
		for (int i = 0; i < this.lstDocker.length; i++)
		     this.lstDocker[i] = new Docker(lstName[i], LST_COLOR[i]);
	}

	public void initGrid(int[][] gridValue)
	{
		this.gridValue  = gridValue;
		this.gridColor  = new char[gridValue.length][gridValue[0].length];
		this.gridCorner = new char[gridValue.length + 1][gridValue[0].length + 1];

		for (int l = 0; l < this.gridColor.length; l++)
		     Arrays.fill(this.gridColor[l], NEUTRAL);

		for (int l = 0; l < this.gridCorner.length; l++)
		     Arrays.fill(this.gridCorner[l], NEUTRAL);
	}


	// Getters
	public Docker[] getLstDocker() { return this.lstDocker; }
	public int getDocker()          { return this.docker; }

	public int[][] getGridValue()   { return this.gridValue; }
	public char[][] getGridColor()  { return this.gridColor; }
	public char[][] getGridCorner() { return this.gridCorner; }

	public boolean isGameOver()     { return this.gameOver; }
	public Docker getWinner()
	{
		int max = 0;
		for (int i = 1; i < this.lstDocker.length; i++)
			if (this.lstDocker[i].getScore() > this.lstDocker[max].getScore())
				max = i;

		return this.lstDocker[max];
	}


	// Methods
	public void play(int lCorn, int cCorn)
	{
		if (this.gridCorner[lCorn][cCorn] != NEUTRAL)
			this.lstDocker[this.docker].removeLock(2);
		else
		{
			this.lstDocker[this.docker].removeLock(1);
			this.placeCorner(lCorn, cCorn);
		}

		this.nextDocker();
	}

	private void placeCorner(int lCorn, int cCorn)
	{
		int  cpt, maxValue;
		char color = NEUTRAL;

		this.gridCorner[lCorn][cCorn] = this.lstDocker[this.docker].getColor();

		int lMin = Math.max(0, lCorn - 1);
		int lMax = Math.min(this.gridColor.length, lCorn + 1);
		int cMin = Math.max(0, cCorn - 1);
		int cMax = Math.min(this.gridColor[0].length, cCorn + 1);
		for (int l = lMin; l < lMax; l++)
		{
			for (int c = cMin; c < cMax; c++)
			{
				maxValue = 0;

				for (int i = 0; i < this.lstDocker.length; i++)
				{
					cpt = 0;
					if (this.gridCorner[l][c] == this.lstDocker[i].getColor()) cpt++;
					if (this.gridCorner[l][c + 1] == this.lstDocker[i].getColor()) cpt++;
					if (this.gridCorner[l + 1][c] == this.lstDocker[i].getColor()) cpt++;
					if (this.gridCorner[l + 1][c + 1] == this.lstDocker[i].getColor()) cpt++;

					if (cpt == maxValue)
						color = NEUTRAL;
					else if (cpt > maxValue)
					{
						maxValue = cpt;
						color    = this.lstDocker[i].getColor();
					}
				}

				this.changeOwner(l, c, color);
			}
		}
	}

	private void changeOwner(int l, int c, char color)
	{
		Docker previousOwner = null, nextOwner = null;

		for (int i = 0; i < this.lstDocker.length; i++)
		{
			if (this.lstDocker[i].getColor() == this.gridColor[l][c])
				previousOwner = this.lstDocker[i];
			if (this.lstDocker[i].getColor() == color)
				nextOwner = this.lstDocker[i];
		}

		if (previousOwner != null)
			previousOwner.removePoint(this.gridValue[l][c]);
		if (nextOwner != null)
			nextOwner.addPoint(this.gridValue[l][c]);

		this.gridColor[l][c] = color;
	}

	public void nextDocker()
	{
		this.majGameOver();
		if (this.gameOver)
			return;

		do
		{
			this.docker = (docker + 1) % this.lstDocker.length;
		}
		while (this.lstDocker[this.docker].getNbLock() == 0);
	}

	private void majGameOver()
	{
		boolean lockLeft   = false;
		boolean cornerFree = false;

		for (int i = 0; i < this.lstDocker.length; i++)
			if (this.lstDocker[i].getNbLock() > 0)
			{
				lockLeft = true;
				break;
			}


		exit:
		for (int l = 0; l < this.gridCorner.length; l++)
			for (int c = 0; c < this.gridCorner.length; c++)
				if (this.gridCorner[l][c] == NEUTRAL)
				{
					cornerFree = true;
					break exit;
				}

		this.gameOver = !cornerFree || !lockLeft;
	}
}
