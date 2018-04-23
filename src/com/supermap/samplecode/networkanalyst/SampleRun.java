package com.supermap.samplecode.networkanalyst;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.supermap.analyst.networkanalyst.DirectionType;
import com.supermap.analyst.networkanalyst.PathGuide;
import com.supermap.analyst.networkanalyst.PathGuideItem;
import com.supermap.analyst.networkanalyst.SideType;
import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.analyst.networkanalyst.WeightFieldInfo;
import com.supermap.analyst.networkanalyst.WeightFieldInfos;

import com.supermap.data.DatasetVector;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PointM;
import com.supermap.data.Recordset;
import com.supermap.data.Size2D;
import com.supermap.data.TextPart;
import com.supermap.data.Workspace;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Selection;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.GeometrySelectedListener;
import com.supermap.ui.MapControl;

/**
 * <p>
 * Title:最佳路径分析
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------版权声明----------------------------
 * 此文件为SuperMap Objects Java 的示范代码 
 * 版权所有：北京超图软件股份有限公司
 * ----------------------------------------------------------------
 * ---------------------SuperMap iObjects Java 示范程序说明------------------------
 * 
 * 1、范例简介：示范如何进行最佳路径分析
 * 2、示例数据：安装目录\SampleData\City\Changchun.udb
 * 3、关键类型/成员: 
 * 		MapControl.GeometrySelectedEvent 事件
 * 		MapControl.MouseEvent 事件
 *      TransportationAnalystSetting.setNetworkDataset 方法
 *      TransportationAnalystSetting.setEdgeIDField 方法
 *      TransportationAnalystSetting.setNodeIDField 方法
 *      TransportationAnalystSetting.setTolerance 方法
 *      TransportationAnalystSetting.setWeightFieldInfo 方法
 *      TransportationAnalystSetting.ssetFNodeIDField 方法
 *      TransportationAnalystSetting.setTNodeIDField 方法
 *      TransportationAnalyst.load 方法
 *      TransportationAnalyst.findPPath 方法
 *      TransportationAnalyst.setAnalystSetting 方法
 *      TransportationAnalystParameter.points 方法
 *      TransportationAnalystParameter.setBarrierEdges 方法
 *      TransportationAnalystParameter.setBarrierNodes 方法
 *      TransportationAnalystParameter.setWeightName 方法
 *      TransportationAnalystParameter.setPoints 方法
 *      TransportationAnalystParameter.setNodesReturn 方法
 *      TransportationAnalystParameter.setEdgesReturn 方法
 *      TransportationAnalystParameter.setPathGuidesReturn 方法
 *      TransportationAnalystParameter.setStopIndexesReturn 方法
 *      TransportationAnalystParameter.setRoutesReturn 方法
 *      TransportationAnalystResult.getRoutes 方法
 *      TransportationAnalystResult.getPathGuides 方法
 * 4、使用步骤：
 *   (1)选取路径分析经过点及障碍点（或障碍边）
 *   (2)分析得出结果，导引可模拟进行路径行驶
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * Company: 北京超图软件股份有限公司
 * </p>
 * 
 */
/**
 * <p>
 *Title: Optimal Path Analysis
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------Copyright Statement----------------------------
 * SuperMap Objects Java Sample Code 
 * Copyright: SuperMap Software Co., Ltd. All Rights Reserved.
 * ------------------------------------------------------------------------------
 *-----------------------Description--------------------------
 * 
 * 1. Introduction: This sample show you how to make optimal path analysis
 * 2. Sample Data: Installation directory\SampleData\City\Changchun.udb
 * 3. Key classes and members 
 *      MapControl.GeometrySelectedEvent event
 *      MapControl.MouseEvent event
 *      TransportationAnalystSetting.setNetworkDataset method
 *      TransportationAnalystSetting.setEdgeIDField method
 *      TransportationAnalystSetting.setNodeIDField method
 *      TransportationAnalystSetting.setTolerance method
 *      TransportationAnalystSetting.setWeightFieldInfo method
 *      TransportationAnalystSetting.ssetFNodeIDField method
 *      TransportationAnalystSetting.setTNodeIDField method
 *      TransportationAnalyst.load method
 *      TransportationAnalyst.findPPath method
 *      TransportationAnalyst.setAnalystSetting method
 *      TransportationAnalystParameter.points method
 *      TransportationAnalystParameter.setBarrierEdges method
 *      TransportationAnalystParameter.setBarrierNodes method
 *      TransportationAnalystParameter.setWeightName method
 *      TransportationAnalystParameter.setPoints method
 *      TransportationAnalystParameter.setNodesReturn method
 *      TransportationAnalystParameter.setEdgesReturn method
 *      TransportationAnalystParameter.setPathGuidesReturn method
 *      TransportationAnalystParameter.setStopIndexesReturn method
 *      TransportationAnalystParameter.setRoutesReturn method
 *      TransportationAnalystResult.getRoutes method
 *      TransportationAnalystResult.getPathGuides method
 * 4. Steps:
 * (1) Select points along the path, and barrier nodes
 * (2) Get the result
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * CopyRight: SuperMap Software Co., Ltd.
 * </p>
 * 
 */
public class SampleRun extends MouseAdapter implements GeometrySelectedListener, ActionListener
{

	private static String m_datasetName = "RoadNet";

	private static String m_nodeID = "SmNodeID";

	private static String m_edgeID = "SmEdgeID";

	private SelectMode m_selectMode;

	private MapControl m_mapControl;

	private Workspace m_workspace;

	private DatasetVector m_datasetLine;

	private DatasetVector m_datasetPoint;

	private Layer m_layerLine;

	private Layer m_layerPoint;

	private TrackingLayer m_trackingLayer;

	private Point2Ds m_Points;

	private GeoStyle m_style;

	private ArrayList<Integer> m_barrierNodes;

	private ArrayList<Integer> m_barrierEdges;

	private TransportationAnalyst m_analyst;

	private TransportationAnalystResult m_result;

	private JTable m_tableResult;

	private Timer m_timer;

	private int m_count;

	private int m_flag;

	/**
	 * 选择模式枚举
     * Select mode enum
	 */
	public enum SelectMode
	{
		SELECTPOINT, SELECTBARRIER, NONE
	}

	/**
	 * 根据workspace、mapControl及boxRoutes、tableResult构造SampleRun对象
     * Initialize the SampleRun object with the specified workspace, mapControl, boxRoutes, and tableResults.
	 */
	public SampleRun(Workspace workspace, MapControl mapControl,
			JTable tableResult)
	{
		m_workspace = workspace;
		m_mapControl = mapControl;
		m_tableResult = tableResult;

		m_mapControl.getMap().setWorkspace(workspace);
		initialize();
	}

	/**
	 * 打开网络数据集并初始化相应变量
     * Open the network dataset and initialize variables
	 */
	private void initialize()
	{
		try
		{
			// 打开数据源,得到点线数据集
            // Open datasource and get the point , line datasets
			DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo(
					"../../SampleData/City/Changchun.udb", "findPath2D", "");
			connectionInfo.setEngineType(EngineType.UDB);
			m_workspace.getDatasources().open(connectionInfo);
			m_datasetLine = (DatasetVector)m_workspace.getDatasources().get(0)
					.getDatasets().get(m_datasetName);
			m_datasetPoint = m_datasetLine.getChildDataset();
			m_trackingLayer = m_mapControl.getMap().getTrackingLayer();
			m_trackingLayer.setAntialias(true);

			// 初始化各变量
            // Initialize variables
			m_flag = 1;
			m_Points = new Point2Ds();
			m_style = new GeoStyle();
			m_barrierNodes = new ArrayList<Integer>();
			m_barrierEdges = new ArrayList<Integer>();
			m_selectMode = SelectMode.SELECTPOINT;
			m_timer = new Timer(200, this);

			// 加载点数据集及线数据集并设置各自风格
            // Add point, line datasets and set their styles
			m_layerLine = m_mapControl.getMap().getLayers().add(m_datasetLine,
					true);
			m_layerLine.setSelectable(false);
			LayerSettingVector lineSetting = (LayerSettingVector)m_layerLine
					.getAdditionalSetting();
			GeoStyle lineStyle = new GeoStyle();
			lineStyle.setLineColor(Color.LIGHT_GRAY);
			lineStyle.setLineWidth(0.1);
			lineSetting.setStyle(lineStyle);

			m_layerPoint = m_mapControl.getMap().getLayers().add(
					m_datasetPoint, true);
			LayerSettingVector pointSetting = (LayerSettingVector)m_layerPoint
					.getAdditionalSetting();
			GeoStyle pointStyle = new GeoStyle();
			pointStyle.setLineColor(new Color(180, 180, 180));
			pointStyle.setMarkerSize(new Size2D(2, 2));
			pointSetting.setStyle(pointStyle);

			// 调整mapControl的状态
            // Adjust the status of mapControl
			m_mapControl.setWaitCursorEnabled(false);
			m_mapControl.getMap().setAntialias(true);
			m_mapControl.getMap().refresh();
			m_mapControl.addMouseListener(this);
			m_mapControl.addGeometrySelectedListener(this);

			load();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 加载环境设置对象
     * Add the TransportationAnalystSetting object
	 */
	public void load()
	{
		try
		{
			// 设置网络分析基本环境，这一步骤需要设置　分析权重、节点、弧段标识字段、容限
            // Set the basic environment of network analysis, including weight, node, edge, tolerance.
			TransportationAnalystSetting setting = new TransportationAnalystSetting();
			setting.setNetworkDataset(m_datasetLine);
			setting.setEdgeIDField(m_edgeID);
			setting.setNodeIDField(m_nodeID);
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				setting.setEdgeNameField("roadName");
	        }
			else
			{
				setting.setEdgeNameField("roadName_en");
			}	
			setting.setTolerance(89);

			WeightFieldInfos weightFieldInfos = new WeightFieldInfos();
			WeightFieldInfo weightFieldInfo = new WeightFieldInfo();
			weightFieldInfo.setFTWeightField("smLength");
			weightFieldInfo.setTFWeightField("smLength");
			weightFieldInfo.setName("length");
			weightFieldInfos.add(weightFieldInfo);
			setting.setWeightFieldInfos(weightFieldInfos);
			setting.setFNodeIDField("SmFNode");
			setting.setTNodeIDField("SmTNode");

			// 构造交通网络分析对象，加载环境设置对象
            // Build the TransportationAnalyst object , and add environment setting object
			m_analyst = new TransportationAnalyst();
			m_analyst.setAnalystSetting(setting);
			m_analyst.load();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 进行最短路径分析
     * Optimal path analysis
	 */
	public boolean analyst()
	{
		try
		{
			m_count = 0;
			TransportationAnalystParameter parameter = new TransportationAnalystParameter();
			// 设置障碍点及障碍边
            // Set barrier nodes and edges
			int[] barrierEdges = new int[m_barrierEdges.size()];
			for (int i = 0; i < barrierEdges.length; i++)
			{
				barrierEdges[i] = m_barrierEdges.get(i);
			}
			parameter.setBarrierEdges(barrierEdges);

			int[] barrierNodes = new int[m_barrierNodes.size()];
			for (int i = 0; i < barrierNodes.length; i++)
			{
				barrierNodes[i] = m_barrierNodes.get(i);
			}
			parameter.setBarrierNodes(barrierNodes);
			parameter.setWeightName("length");

			// 设置最佳路径分析的返回对象
            // Set the return object of the optimal path analysis
			parameter.setPoints(m_Points);
			parameter.setNodesReturn(true);
			parameter.setEdgesReturn(true);
			parameter.setPathGuidesReturn(true);
			parameter.setRoutesReturn(true);

			// 进行分析并显示结果
            // Analyze
			m_result = m_analyst.findPath(parameter, false);
			if (m_result == null)
			{
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {
					JOptionPane.showMessageDialog(null, "分析失败");
		        }
				else
				{
					JOptionPane.showMessageDialog(null, "Failed to analyze");
				}
				return false;
			}
			showResult();
			m_selectMode = SelectMode.NONE;
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				JOptionPane.showMessageDialog(null, "分析失败");
	        }
			else
			{
				JOptionPane.showMessageDialog(null, "Failed to analyze");
			}
			return false;
		}
	}

	/**
	 * 显示结果
     * Show result
	 */
	public void showResult()
	{
		try
		{
			// 删除原有结果
            // Delete the original result
			int count = m_trackingLayer.getCount();
			for (int i = 0; i < count; i++)
			{
				int index = m_trackingLayer.indexOf("result");
				if (index != -1)
					m_trackingLayer.remove(index);
			}
			fillResultTable(0);

			for (int i = 0; i < m_result.getRoutes().length; i++)
			{
				GeoLineM geoLineM = m_result.getRoutes()[0];
				m_style.setLineColor(Color.BLUE);
				m_style.setLineWidth(1);
				geoLineM.setStyle(m_style);
				m_trackingLayer.add(geoLineM, "result");
			}
			m_mapControl.getMap().refreshTrackingLayer();

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 对结果表进行填充
     * Fill result table
	 */
	public void fillResultTable(int pathNum)
	{
		try
		{
			// 清除原数据，添加初始点信息
            // Clear original data and add start point information
			DefaultTableModel model = (DefaultTableModel)m_tableResult
					.getModel();
			while (model.getRowCount() != 0)
			{
				model.removeRow(0);
			}
			Object[] objs = new Object[4];
			objs[0] = model.getRowCount() + 1;
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				objs[1] = "从起始点出发";
	        }
			else
			{
				objs[1] = "Start";
			}
			objs[2] = "--";
			objs[3] = "--";
			model.addRow(objs);

			// 得到行驶导引对象，根据导引子项类型的不同进行不同的填充
            // Get the PathGuide object, and make different fill according to different items
			PathGuide[] pathGuides = m_result.getPathGuides();
			PathGuide pathGuide = pathGuides[pathNum];

			for (int j = 1; j < pathGuide.getCount(); j++)
			{
				PathGuideItem item = pathGuide.get(j);
				objs[0] = model.getRowCount() + 1;
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {
					// 导引子项为站点的添加方式
	                // If the item is a stop
					if (item.isStop())
					{
						String side = "无";
						if (item.getSideType() == SideType.LEFT)
							side = "左侧";
						if (item.getSideType() == SideType.RIGHT)
							side = "右侧";
						if (item.getSideType() == SideType.MIDDLE)
							side = "道路上";
						String dis = NumberFormat.getInstance().format(
								item.getDistance());
						if (item.getIndex() == -1 && item.getID() == -1)
						{
							continue;
						}
						if (j != pathGuide.getCount() - 1)
						{
							objs[1] = "到达[" + item.getIndex() + "号路由点],在道路" + side
									+ dis;
						}
						else
						{
							objs[1] = "到达终点,在道路" + side + dis;
						}
						objs[2] = "";
						objs[3] = "";
						model.addRow(objs);
					}
					// 导引子项为弧段的添加方式
	                // If the item is a edge
					if (item.isEdge())
					{
						String direct = "直行";
						if (item.getDirectionType() == DirectionType.EAST)
							direct = "东";
						if (item.getDirectionType() == DirectionType.WEST)
							direct = "西";
						if (item.getDirectionType() == DirectionType.SOUTH)
							direct = "南";
						if (item.getDirectionType() == DirectionType.NORTH)
							direct = "北";
						String weight = NumberFormat.getInstance().format(
								item.getWeight());
						String roadName = item.getName();
						if (weight.equals("0") && roadName.equals(""))
						{
							objs[1] = "朝" + direct + "行走"+ item.getLength();
							objs[2] = weight;
							objs[3] = item.getLength();
							model.addRow(objs);
						}
						else
						{
							String roadString = roadName.equals("") ? "匿名路段" : roadName;
							objs[1] = "沿着[" + roadString + "],朝" + direct + "行走"
									+ item.getLength();
							objs[2] = weight;
							objs[3] = item.getLength();
							model.addRow(objs);
						}
					}
		        }
				else
				{
					// If the item is a stop
                    if (item.isStop())
                    {
                          String side = "None";
                          if (item.getSideType() == SideType.LEFT)
                                side = "Left";
                          if (item.getSideType() == SideType.RIGHT)
                                side = "Right";
                          if (item.getSideType() == SideType.MIDDLE)
                                side = "On the road";
                          String dis = NumberFormat.getInstance().format(
                                      item.getDistance());
                          if (item.getIndex() == -1 && item.getID() == -1)
                          {
                                continue;
                          }
                          if (j != pathGuide.getCount() - 1)
                          {
                                objs[1] = "Arrive at [" + item.getIndex() + " route], on the " + side
                                            + dis;
                          }
                          else
                          {
                                objs[1] = "Arrive at destination " + side + dis;
                          }
                          objs[2] = "";
                          objs[3] = "";
                          model.addRow(objs);
                    }
                    // If the item is a edge
                    if (item.isEdge())
                    {
                          String direct = "Go ahead";
                          if (item.getDirectionType() == DirectionType.EAST)
                                direct = "East";
                          if (item.getDirectionType() == DirectionType.WEST)
                                direct = "West";
                          if (item.getDirectionType() == DirectionType.SOUTH)
                                direct = "South";
                          if (item.getDirectionType() == DirectionType.NORTH)
                                direct = "North";
                          String weight = NumberFormat.getInstance().format(
                                      item.getWeight());
                          String roadName = item.getName();
                          if (weight.equals("0") && roadName.equals(""))
                          {
                                objs[1] = "Go " + direct + " " + item.getLength();
                                objs[2] = weight;
                                objs[3] = item.getLength();
                                model.addRow(objs);
                          }
                          else
                          {
                                String roadString = roadName.equals("") ? "Anonymous road" : roadName;
                                objs[1] = "Go along with [" + roadString + "], " + direct + " direction"
                                            + item.getLength();
                                objs[2] = weight;
                                objs[3] = item.getLength();
                                model.addRow(objs);
                          }
                    }
				}
				
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 鼠标按下事件
     * Mouse down event
	 */
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == 1) {
			Point point = new Point(e.getX(), e.getY());
			Point2D mapPoint = m_mapControl.getMap().pixelToMap(point);
			if (m_mapControl.getMap().getBounds().contains(mapPoint)) {
				if (m_mapControl.getAction() == Action.SELECT2
						&& m_selectMode == SelectMode.SELECTPOINT) {
					addPoint(mapPoint);
				}
			}
		}
	}

	/**
	 * 添加分析经过点
     * Add points along the path
	 */
	public void addPoint(Point2D mapPoint)
	{
		// 在跟踪图层上添加点
        // Add points on the tracking layer
		m_Points.add(mapPoint);
		GeoPoint geoPoint = new GeoPoint(mapPoint);
		m_style.setLineColor(Color.GREEN);
		m_style.setMarkerSize(new Size2D(10, 10));
		geoPoint.setStyle(m_style);
		m_trackingLayer.add(geoPoint, "Point");

		// 在跟踪图层上添加文本对象
        // Add text objects on the tracking layer
		TextPart part = new TextPart();
		part.setX(geoPoint.getX());
		part.setY(geoPoint.getY());
		part.setText(String.valueOf(m_flag));
		m_flag++;
		GeoText geoText = new GeoText(part);
		m_trackingLayer.add(geoText, "Point");
		m_mapControl.getMap().refreshTrackingLayer();
	}

	/**
	 * 对象选择事件
     * Object selected event
	 */
	public void geometrySelected(GeometrySelectedEvent e)
	{
		if (m_selectMode != SelectMode.SELECTBARRIER) {
			return;
		}
		Selection selection = m_layerPoint.getSelection();
		if (selection.getCount() <= 0) {
			selection = m_layerLine.getSelection();
		}
		m_style.setLineColor(Color.RED);
		m_style.setMarkerSize(new Size2D(6,6));
		m_style.setLineWidth(0.5);
		Recordset recordset = selection.toRecordset();
		try
		{
			Geometry geometry = recordset.getGeometry();
			// 捕捉到点时，将捕捉到的点添加到障碍点列表中
            // If a point is snapped, the point is added to the barrier list
			if (geometry.getType() == GeometryType.GEOPOINT)
			{
				GeoPoint geoPoint = (GeoPoint)geometry;
				int nodeID = recordset.getInt32("SMNODEID");
				m_barrierNodes.add(nodeID);

				geoPoint.setStyle(m_style);
				m_trackingLayer.add(geoPoint, "barrierNode");
			}
			// 捕捉到线时，将线对象添加到障碍线列表中
            // If a line is snapped, the line is added to the barrier list
			if (geometry.getType() == GeometryType.GEOLINE)
			{
				GeoLine geoLine = (GeoLine)geometry;
				int edgeID = recordset.getInt32("SMEDGEID");
				m_barrierEdges.add(edgeID);

				geoLine.setStyle(m_style);
				m_trackingLayer.add(geoLine, "barrierEdge");
			}
			m_mapControl.getMap().refresh();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			recordset.dispose();
		}
	}

	/**
	 * 开始导引
     * Start guiding
	 */
	public void play()
	{
		m_timer.start();
	}

	/**
	 * 停止导引
     * Stop guiding
	 */
	public void stop()
	{
		m_timer.stop();
	}

	/**
	 * 清除分析结果
     * Clear the analysis result
	 */
	public void clear()
	{
		if (m_timer != null)
			m_timer.stop();
		DefaultTableModel model = (DefaultTableModel)m_tableResult.getModel();
		while (model.getRowCount() != 0)
		{
			model.removeRow(0);
		}

		m_flag = 1;
		m_Points = new Point2Ds();
		m_barrierNodes = new ArrayList<Integer>();
		m_barrierEdges = new ArrayList<Integer>();
		m_layerLine.getSelection().clear();
		m_layerPoint.getSelection().clear();
		m_trackingLayer.clear();
		m_mapControl.getMap().refresh();
	}

	/**
	 * 设置选择模式
     * Set the select mode
	 */
	public void setSelectMode(SelectMode mode, boolean canSelectLine)
	{
		m_selectMode = mode;
		m_layerLine.setSelectable(canSelectLine);
	}

	/**
	 * 进行行驶导引
     * Path guide
	 */
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			int index = m_trackingLayer.indexOf("playPoint");
			if (index != -1)
				m_trackingLayer.remove(index);

			GeoLineM lineM = m_result.getRoutes()[0];
			PointM pointM = lineM.getPart(0).getItem(m_count);

			// 构造模拟对象
            // Build simulation object
			GeoPoint point = new GeoPoint(pointM.getX(), pointM
					.getY());
			GeoStyle style = new GeoStyle();
			style.setLineColor(Color.RED);
			style.setMarkerSize(new Size2D(5, 5));
			point.setStyle(style);
			m_trackingLayer.add(point, "playPoint");

			// 跟踪对象
            // Tracking object
			m_count++;
			if (m_count >= lineM.getPart(0).getCount())
			{
				m_count = 0;
			}
			m_mapControl.getMap().setCenter(point.getInnerPoint());
			m_mapControl.getMap().refresh();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}

