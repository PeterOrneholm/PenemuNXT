package org.penemunxt.projects.penemunxtexplorer.pc;

import java.awt.Color;
import java.util.ArrayList;

import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.*;
import org.penemunxt.projects.penemunxtexplorer.pc.map.processing.processors.*;

public class PenemuNXTDefaultMapProcessors {
	final static boolean LATEST_POS_SHOW_DEFAULT = true;
	final static boolean DRIVING_PATH_SHOW_DEFAULT = true;
	final static boolean BUMPING_BUMPER_SHOW_DEFAULT = true;
	final static boolean BUMPING_DISTANCE_SHOW_DEFAULT = true;
	final static boolean ALIGNED_TO_WALL_SHOW_DEFAULT = true;
	final static boolean HEAD_MAP_SHOW_DEFAULT = true;
	final static boolean HOT_SPOTS_SHOW_DEFAULT = false;
	final static boolean FIND_WALLS_SHOW_DEFAULT = true;
	final static boolean CLEAR_AREA_SHOW_DEFAULT = true;
	final static boolean BACKGROUND_GRID_SHOW_DEFAULT = true;
	final static boolean TARGET_POS_SHOW_DEFAULT = true;

	final static Color DEFAULT_CIRCLE_COLOR = Color.BLACK;
	final static Color LATEST_POS_CIRCLE_COLOR = Color.GREEN;
	final static Color DRIVING_PATH_CIRCLE_COLOR = Color.LIGHT_GRAY;
	final static Color BUMPING_BUMPER_CIRCLE_COLOR = Color.RED;
	final static Color BUMPING_DISTANCE_CIRCLE_COLOR = Color.BLUE;
	final static Color ALIGNED_TO_WALL_CIRCLE_COLOR = Color.CYAN;
	final static Color HEAD_MAP_CIRCLE_COLOR = Color.BLACK;
	final static Color CLEAR_AREA_LINE_COLOR = Color.WHITE;
	final static Color BACKGROUND_GRID_LINE_COLOR = Color.WHITE;
	final static Color TARGET_POS_CIRCLE_COLOR = Color.YELLOW;

	final static int DEFAULT_CIRCLE_SIZE = 10;
	final static int LATEST_POS_CIRCLE_SIZE = 75;
	final static int DRIVING_PATH_CIRCLE_SIZE = 10;
	final static int BUMPING_BUMPER_CIRCLE_SIZE = 50;
	final static int BUMPING_DISTANCE_CIRCLE_SIZE = 50;
	final static int ALIGNED_TO_WALL_CIRCLE_SIZE = 50;
	final static int HEAD_MAP_CIRCLE_SIZE = 25;
	final static int HOT_SPOTS_MAX_CIRCLE_SIZE = 100;
	final static int CLEAR_AREA_LINE_SIZE = 25;
	final static int BACKGROUND_GRID_LINE_SIZE = 2;
	final static int FIND_WALLS_LINE_SIZE = 5;
	final static int TARGET_POS_CIRCLE_SIZE = 35;

	public static ArrayList<IMapProcessor> getDefaultProcessors() {
		ArrayList<IMapProcessor> defaultProcessors = new ArrayList<IMapProcessor>();

		MapAligned mapProcessorAligned;
		MapBumpBumper mapProcessorBumpBumper;
		MapCurrentPos mapProcessorCurrentPos;
		MapTargetPos mapProcessorsTargetPos;
		MapBumpDistance mapProcessorBumpDistance;
		MapDrivingPath mapProcessorDrivingPath;
		MapFindLines mapProcessorFindLines;
		MapHeadObjects mapProcessorHeadObjects;
		MapHotspots mapProcessorHotspots;
		MapClearArea mapProcessorClearArea;
		MapBackgroundGrid mapProcessorBackgroundGrid;

		mapProcessorAligned = new MapAligned(ALIGNED_TO_WALL_CIRCLE_COLOR,
				ALIGNED_TO_WALL_CIRCLE_SIZE, ALIGNED_TO_WALL_SHOW_DEFAULT);
		mapProcessorBumpBumper = new MapBumpBumper(BUMPING_BUMPER_CIRCLE_COLOR,
				BUMPING_BUMPER_CIRCLE_SIZE, BUMPING_BUMPER_SHOW_DEFAULT);
		mapProcessorBumpDistance = new MapBumpDistance(
				BUMPING_DISTANCE_CIRCLE_COLOR, BUMPING_DISTANCE_CIRCLE_SIZE,
				BUMPING_DISTANCE_SHOW_DEFAULT);
		mapProcessorCurrentPos = new MapCurrentPos(LATEST_POS_CIRCLE_COLOR,
				LATEST_POS_CIRCLE_SIZE, LATEST_POS_SHOW_DEFAULT);
		mapProcessorsTargetPos = new MapTargetPos(TARGET_POS_CIRCLE_COLOR,
				TARGET_POS_CIRCLE_SIZE, TARGET_POS_SHOW_DEFAULT);
		mapProcessorDrivingPath = new MapDrivingPath(DRIVING_PATH_CIRCLE_COLOR,
				DRIVING_PATH_CIRCLE_SIZE, DRIVING_PATH_SHOW_DEFAULT);
		mapProcessorHeadObjects = new MapHeadObjects(HEAD_MAP_CIRCLE_COLOR,
				HEAD_MAP_CIRCLE_SIZE, HEAD_MAP_SHOW_DEFAULT);

		mapProcessorHotspots = new MapHotspots(HOT_SPOTS_MAX_CIRCLE_SIZE,
				HOT_SPOTS_SHOW_DEFAULT);
		mapProcessorFindLines = new MapFindLines(FIND_WALLS_LINE_SIZE,
				FIND_WALLS_SHOW_DEFAULT);
		mapProcessorClearArea = new MapClearArea(CLEAR_AREA_LINE_COLOR,
				CLEAR_AREA_LINE_SIZE, CLEAR_AREA_SHOW_DEFAULT);
		mapProcessorBackgroundGrid = new MapBackgroundGrid(
				BACKGROUND_GRID_LINE_COLOR, BACKGROUND_GRID_LINE_SIZE,
				BACKGROUND_GRID_SHOW_DEFAULT);

		defaultProcessors.add(mapProcessorBackgroundGrid);
		defaultProcessors.add(mapProcessorClearArea);
		defaultProcessors.add(mapProcessorDrivingPath);
		defaultProcessors.add(mapProcessorHeadObjects);

		defaultProcessors.add(mapProcessorBumpBumper);
		defaultProcessors.add(mapProcessorBumpDistance);
		defaultProcessors.add(mapProcessorAligned);

		defaultProcessors.add(mapProcessorHotspots);
		defaultProcessors.add(mapProcessorFindLines);

		defaultProcessors.add(mapProcessorCurrentPos);
		defaultProcessors.add(mapProcessorsTargetPos);

		return defaultProcessors;
	}
}
