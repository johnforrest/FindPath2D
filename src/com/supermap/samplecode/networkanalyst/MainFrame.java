package com.supermap.samplecode.networkanalyst;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.supermap.data.Workspace;
import com.supermap.samplecode.networkanalyst.SampleRun.SelectMode;
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
public class MainFrame extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel m_contentPane;

	private MapControl m_mapControl;

	private JToolBar m_toolBar;

	private JRadioButton m_radioButtonPoint;

	private JRadioButton m_radioButtonBarrier;

	private JButton m_buttonAnalyst;

	private JButton m_buttonClear;

	private Workspace m_workspace;

	private SampleRun m_sampleRun;

	private JPanel m_panelResult;

	private JScrollPane m_scrollPaneResult;

	private JTable m_tableResult;

	private JButton m_buttonPlay;

	private JButton m_buttonStop;

	/**
	 * 程序入口点
     * Entrance to the program
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * 构造函数
     * Constructor
	 */
	public MainFrame()
	{
		super();
		initialize();
	}

	/**
	 * 初始化窗体
     * Initialize the form.
	 */
	private void initialize()
	{
		// 最大化显示窗体
        // Maximize the form
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setSize(800, 500);
		this.setContentPane(getJContentPane());
		this.setTitle(getText("thisTitle"));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			// 在主窗体加载时，初始化SampleRun类型，来完成功能的展现
            // Initialize the SampleRun type when loading the main window
			public void windowOpened(java.awt.event.WindowEvent e)
			{
				m_workspace = new Workspace();
				m_sampleRun = new SampleRun(m_workspace, m_mapControl,
						m_tableResult);

				ButtonGroup group = new ButtonGroup();
				group.add(getRadioButtonBarrier());
				group.add(getRadioButtonPoint());
			}

			// 在窗体关闭时，需要释放相关的资源
            // Release resources when you close the window
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				m_mapControl.dispose();
				m_workspace.dispose();
			}
		});
	}

	/**
	 * 获得m_contentPane
     * Get m_contentPane
	 */
	private JPanel getJContentPane()
	{
		if (m_contentPane == null)
		{
			m_contentPane = new JPanel();
			m_contentPane.setLayout(new BorderLayout());
			m_contentPane.add(getMapControl(), BorderLayout.CENTER);
			m_contentPane.add(getToolBar(), BorderLayout.NORTH);
			m_contentPane.add(getPanelResult(), BorderLayout.SOUTH);
		}
		return m_contentPane;
	}

	/**
	 * 获得m_mapControl
     * Get m_mapControl
	 */
	private MapControl getMapControl()
	{
		if (m_mapControl == null)
		{
			m_mapControl = new MapControl();
		}
		return m_mapControl;
	}

	/**
	 * 获得m_toolBar
     * Get m_toolBar
	 */
	private JToolBar getToolBar()
	{
		if (m_toolBar == null)
		{
			m_toolBar = new JToolBar();
			m_toolBar.setFloatable(false);
			m_toolBar.add(getRadioButtonPoint());
			m_toolBar.add(getRadioButtonBarrier());
			m_toolBar.add(getButtonAnalyst());
			m_toolBar.add(getButtonPlay());
			m_toolBar.add(getButtonStop());
			m_toolBar.add(getButtonClear());
		}
		return m_toolBar;
	}

	/**
	 * 获得m_radioButtonPoint
     * Get the m_radioButtonPoint
	 */
	private JRadioButton getRadioButtonPoint()
	{
		if (m_radioButtonPoint == null)
		{
			m_radioButtonPoint = new JRadioButton();
			m_radioButtonPoint.setText(getText("m_radioButtonPoint"));
			m_radioButtonPoint.setSelected(true);
			m_radioButtonPoint.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					m_sampleRun.setSelectMode(SelectMode.SELECTPOINT, false);
				}

			});
		}
		return m_radioButtonPoint;
	}

	/**
	 * 获得m_radioButtonBarrier
	 * Get m_radioButtonBarrier
	 */
	private JRadioButton getRadioButtonBarrier()
	{
		if (m_radioButtonBarrier == null)
		{
			m_radioButtonBarrier = new JRadioButton();
			m_radioButtonBarrier.setText(getText("m_radioButtonBarrier"));
			m_radioButtonBarrier.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					m_sampleRun.setSelectMode(SelectMode.SELECTBARRIER, true);
				}

			});
		}
		return m_radioButtonBarrier;
	}

	/**
	 * 获得m_buttonAnalyst
	 * Get m_buttonAnalyst
	 */
	private JButton getButtonAnalyst()
	{
		if (m_buttonAnalyst == null)
		{
			m_buttonAnalyst = new JButton();
			m_buttonAnalyst.setText(getText("m_buttonAnalyst"));
			m_buttonAnalyst.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					if (m_sampleRun.analyst())
					{
						enabled(false);
					}
				}
			});
		}
		return m_buttonAnalyst;
	}

	/**
	 * 获得m_buttonClear
	 * Get m_buttonClear
	 */
	private JButton getButtonClear()
	{
		if (m_buttonClear == null)
		{
			m_buttonClear = new JButton();
			m_buttonClear.setText(getText("m_buttonClear"));
			m_buttonClear.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					m_sampleRun.clear();
					SelectMode mode = SelectMode.SELECTPOINT;
					m_sampleRun.setSelectMode(mode, false);
					if (m_radioButtonBarrier.isSelected()) {
						mode = SelectMode.SELECTBARRIER;
						m_sampleRun.setSelectMode(mode, true);
					}

					enabled(true);
				}

			});
		}
		return m_buttonClear;
	}

	/**
	 * 获得m_panelResult
	 * Get m_panelResult
	 */
	private JPanel getPanelResult()
	{
		if (m_panelResult == null)
		{
			m_panelResult = new JPanel();
			m_panelResult.setLayout(new BorderLayout());
			m_panelResult.setPreferredSize(new java.awt.Dimension(100, 150));
			m_panelResult.add(getScrollPaneResult(), BorderLayout.CENTER);
		}
		return m_panelResult;
	}

	/**
	 * 获得m_scrollPaneResult
	 * Get m_scrollPaneResult
	 */
	private JScrollPane getScrollPaneResult()
	{
		if (m_scrollPaneResult == null)
		{
			m_scrollPaneResult = new JScrollPane();
			m_scrollPaneResult.setViewportView(getTableResult());
		}
		return m_scrollPaneResult;
	}

	/**
	 * 获得m_tableResult
	 * Get m_tableResult
	 */
	private JTable getTableResult()
	{
		if (m_tableResult == null)
		{
			m_tableResult = new JTable();
			m_tableResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultTableModel model = new DefaultTableModel()
			{

				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column)
				{
					return false;
				}
			};
			model.addColumn(getText("Column0"));
			model.addColumn(getText("Column1"));
			model.addColumn(getText("Column2"));
			model.addColumn(getText("Column3"));
			m_tableResult.setModel(model);
		}
		return m_tableResult;
	}

	/**
	 * 获得m_buttonPlay
	 * Get m_buttonPlay
	 * 
	 */
	private JButton getButtonPlay()
	{
		if (m_buttonPlay == null)
		{
			m_buttonPlay = new JButton();
			m_buttonPlay.setText(getText("m_buttonPlay"));
			m_buttonPlay.setEnabled(false);
			m_buttonPlay.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					m_sampleRun.play();
				}

			});
		}
		return m_buttonPlay;
	}

	/**
	 * 获取m_buttonStop
	 * Get m_buttonStop
	 * 
	 */
	private JButton getButtonStop()
	{
		if (m_buttonStop == null)
		{
			m_buttonStop = new JButton();
			m_buttonStop.setText(getText("m_buttonStop"));
			m_buttonStop.setEnabled(false);
			m_buttonStop.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{
					m_sampleRun.stop();
				}

			});
		}
		return m_buttonStop;
	}

	/**
	 * 设置各对象的状态
     * Set the status of objects
	 */
	private void enabled(boolean isEnabled)
	{
		m_radioButtonBarrier.setEnabled(isEnabled);
		m_radioButtonPoint.setEnabled(isEnabled);
		m_buttonAnalyst.setEnabled(isEnabled);
		m_buttonPlay.setEnabled(!isEnabled);
		m_buttonStop.setEnabled(!isEnabled);
	}
	// 根据语言设置text是中文还是英文
	// To change text to English or Chinese by CurrentCulture
	private String getText(String id) {		
		String text = "";
        if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
        {      	        
        	if(id == "thisTitle")
        	{       		
        		text = "最佳路径分析";
        	}
        	if(id == "m_radioButtonPoint")
        	{       		
        		text = "路径经过点";
        	}
        	if(id == "m_radioButtonBarrier")
        	{       		
        		text = "障碍";
        	}
        	if(id == "m_buttonAnalyst")
        	{       		
        		text = "分析";
        	}
         	if(id == "m_buttonClear")
        	{       		
        		text = "清除";
        	}
        	if(id == "Column0")
        	{       		
        		text = "序号";
        	}
        	if(id == "Column1")
        	{       		
        		text = "导引";
        	}
        	if(id == "Column2")
        	{       		
        		text = "耗费";
        	}
        	if(id == "Column3")
        	{       		
        		text = "距离";
        	}
        	if(id == "m_buttonPlay")
        	{       		
        		text = "导引";
        	}
         	if(id == "m_buttonStop")
        	{       		
        		text = "停止";
        	}
        }
        else
        {
        	if(id == "thisTitle")
        	{       		
        		text = "Optimal Path Analysis";
        	}
        	if(id == "m_radioButtonPoint")
        	{       		
        		text = "Point along path";
        	}
        	if(id == "m_radioButtonBarrier")
        	{       		
        		text = "Barrier";
        	}
        	if(id == "m_buttonAnalyst")
        	{       		
        		text = "Analyze";
        	}
        	if(id == "m_buttonClear")
        	{       		
        		text = "Clear";
        	}
        	if(id == "Column0")
        	{       		
        		text = "Serial Number";
        	}
        	if(id == "Column1")
        	{       		
        		text = "Guide";
        	}
        	if(id == "Column2")
        	{       		
        		text = "Cost";
        	}
        	if(id == "Column3")
        	{       		
        		text = "Distance";
        	}
        	if(id == "m_buttonPlay")
        	{       		
        		text = "Guide";
        	}
         	if(id == "m_buttonStop")
        	{       		
        		text = "Stop";
        	}
        }        
        return text;	
	}
}

