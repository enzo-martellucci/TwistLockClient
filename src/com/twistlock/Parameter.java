package com.twistlock;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Parameter
{
	// Constants
	public static final int START_SCORE = 0;
	public static final int START_LOCK  = 20;

	public static final char                  NEUTRAL   = '.';
	public static final char[]                LST_COLOR = new char[]{ 'R', 'G', 'B', 'Y' };
	public static final Map<Character, Color> OWNER     = new HashMap<>();

	public static final int   PADDING_GRID = 20;
	public static final int   SPACING_GRID = 6;
	public static final int   GRID_LOCK_R  = 10;
	public static final int   GRID_LOCK_D  = GRID_LOCK_R * 2;
	public static final Color GRID_BG      = new Color(36, 21, 113);
	public static final Color GRID_BORDER  = Color.BLACK;

	public static final int   PADDING_DOCKER = 10;
	public static final int   DOCKER_LOCK_D  = 10;
	public static final Color DOCKER_COL_BG  = new Color(154, 123, 79);
	public static final Color DOCKER_BG      = new Color(83, 41, 21);

	public static final Font   FONT   = new Font("Arial", Font.BOLD, 20);
	public static final Stroke STROKE = new BasicStroke(2);

	static
	{
		Parameter.OWNER.put(NEUTRAL, Color.WHITE);
		Parameter.OWNER.put(LST_COLOR[0], new Color(168, 22, 32));
		Parameter.OWNER.put(LST_COLOR[1], new Color(87, 157, 28));
		Parameter.OWNER.put(LST_COLOR[2], new Color(114, 159, 207));
		Parameter.OWNER.put(LST_COLOR[3], new Color(236, 240, 0));
	}

	private Parameter() { }
}
