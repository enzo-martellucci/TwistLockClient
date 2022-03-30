package com.twistlock.model;

import com.twistlock.Parameter;

public class Docker
{
	//Attributs
	private String name;
	private char   color;
	private int    score;
	private int    nbLock;

	// Constructeur
	public Docker(String name, char color)
	{
		this.name   = name;
		this.color  = color;
		this.score  = Parameter.START_SCORE;
		this.nbLock = Parameter.START_LOCK;
	}

	// Getters
	public String getName()
	{
		return this.name;
	}
	public char getColor()
	{
		return this.color;
	}
	public int getScore()
	{
		return this.score;
	}
	public int getNbLock()
	{
		return this.nbLock;
	}


	// Methods
	public void removeLock(int n)
	{
		this.nbLock -= n;
		if (this.nbLock < 0)
			this.nbLock = 0;
	}

	public void addPoint(int n)
	{
		this.score += n;
	}

	public void removePoint(int n)
	{
		this.score -= n;
	}
}
