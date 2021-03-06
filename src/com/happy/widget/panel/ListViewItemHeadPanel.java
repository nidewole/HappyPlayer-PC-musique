package com.happy.widget.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.happy.common.Constants;
import com.happy.manage.MediaManage;
import com.happy.model.Category;
import com.happy.model.EventIntent;
import com.happy.model.SongInfo;
import com.happy.observable.ObserverManage;
import com.happy.util.AudioFilter;
import com.happy.util.MediaUtils;
import com.happy.widget.button.BaseButton;

/**
 * 播放列表面板
 * 
 * @author zhangliangming
 * 
 */
public class ListViewItemHeadPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285370418675829685L;

	/**
	 * 高度
	 */
	private int height = 40;

	/**
	 * 宽度
	 */
	private int width = 0;
	/**
	 * 标题
	 */
	private String titleName;

	/**
	 * 歌曲数目
	 */
	private int size = 0;
	/**
	 * 不展开图标标志
	 */
	private JLabel statusLeftJLabel;

	/**
	 * 展开图标标志
	 */
	private JLabel statusDownJLabel;

	/**
	 * 标题标签
	 */
	private JLabel titleNameJLabel;
	/**
	 * 判断是否展开
	 */
	private boolean isShow = false;

	/**
	 * 
	 */
	private JPanel playListPanel;
	/**
	 * 歌曲内容面板
	 */
	private ListViewItemComPanel listViewItemComPanel;
	/**
	 * 菜单按钮
	 */
	private BaseButton menuButton;

	/**
	 * 定义添加按钮的弹出菜单
	 */
	private JPopupMenu addPop;

	/**
	 * 定义一个添加歌曲菜单
	 */
	private JMenuItem addSongMenu;

	/**
	 * 定义一个添加歌曲文件菜单
	 */
	public JMenuItem addSongFiledMenu;
	/**
	 * 删除播放列表菜单
	 */
	public JMenuItem delPlaylistMenu;

	/**
	 * 歌曲文件数组
	 */
	private File[] songfiles;

	/**
	 * 歌曲列表
	 */
	private List<SongInfo> mSongInfo;

	/**
	 * 播放列表索引
	 */
	private int pindex = 0;
	/**
	 * listview内容面板
	 */
	private JPanel listViewPanel;

	public ListViewItemHeadPanel(JPanel mplayListPanel, JPanel mlistViewPanel,
			int width, String titleName, int size,
			ListViewItemComPanel listViewItemComPanel,
			List<SongInfo> mSongInfo, int index) {
		this.pindex = index;
		this.mSongInfo = mSongInfo;
		this.listViewPanel = mlistViewPanel;
		this.listViewItemComPanel = listViewItemComPanel;
		this.playListPanel = mplayListPanel;
		this.width = width;
		this.titleName = titleName;
		this.size = size;
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {

				EventIntent eventIntent = new EventIntent();
				eventIntent.setType(EventIntent.PLAYLIST);
				eventIntent.setpShowIndex(pindex);
				eventIntent.setShow(isShow);

				ObserverManage.getObserver().setMessage(eventIntent);
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		initComponent();
		this.setOpaque(false);
	}

	/**
	 * 重绘状态图标
	 */
	private void repaintUI() {
		if (isShow) {
			statusLeftJLabel.setVisible(false);
			statusDownJLabel.setVisible(true);
			// 显示歌曲列表内容
			listViewItemComPanel.setVisible(true);
		} else {
			statusLeftJLabel.setVisible(true);
			statusDownJLabel.setVisible(false);
			// 隐藏歌曲列表内容
			listViewItemComPanel.setVisible(false);
		}
		// playListPanel.revalidate();
		// playListPanel.repaint();
		playListPanel.updateUI();
	}

	public boolean getShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
		repaintUI();
	}

	private ImageIcon leftIcon;
	private ImageIcon downIcon;

	public void initComponent() {
		this.setLayout(null);

		String leftIconPath = Constants.PATH_ICON + File.separator
				+ "arrow_left.png";
		leftIcon = new ImageIcon(leftIconPath);
		leftIcon.setImage(leftIcon.getImage().getScaledInstance(height / 3,
				height / 3, Image.SCALE_SMOOTH));

		String downIconPath = Constants.PATH_ICON + File.separator
				+ "arrow_down.png";
		downIcon = new ImageIcon(downIconPath);
		downIcon.setImage(downIcon.getImage().getScaledInstance(height / 3,
				height / 3, Image.SCALE_SMOOTH));

		statusLeftJLabel = new JLabel(leftIcon);
		statusLeftJLabel.setBounds(10, (height - height / 3) / 2, height / 3,
				height / 3);

		statusDownJLabel = new JLabel(downIcon);
		statusDownJLabel.setBounds(10, (height - height / 3) / 2, height / 3,
				height / 3);
		statusDownJLabel.setVisible(false);

		titleNameJLabel = new JLabel(titleName + "[" + size + "]");
		// titleNameJLabel.setForeground(new Color(71, 72, 73));
		titleNameJLabel.setBounds(
				statusLeftJLabel.getX() + statusLeftJLabel.getWidth() + 10,
				(height - height / 2) / 2, width / 2, height / 2);

		/**
		 * 基本图标路径
		 */
		String iconPath = Constants.PATH_ICON + File.separator;
		String menuButtonBaseIconPath = iconPath + "list_btn_menu1.png";
		String menuButtonOverIconPath = iconPath + "list_btn_menu2.png";
		String menuButtonPressedIconPath = iconPath + "list_btn_menu3.png";
		menuButton = new BaseButton(menuButtonBaseIconPath,
				menuButtonOverIconPath, menuButtonPressedIconPath, height / 2,
				height / 2);
		menuButton.setToolTipText("列表菜单");
		menuButton.setBounds(width - height, (height - height / 2) / 2,
				height / 2, height / 2);

		addPop = new JPopupMenu();
		addSongMenu = new JMenuItem("添加歌曲");
		addSongFiledMenu = new JMenuItem("添加歌曲文件");

		delPlaylistMenu = new JMenuItem("删除播放列表");

		addPop.add(addSongMenu);
		addPop.add(addSongFiledMenu);
		if (pindex != 0) {
			addPop.addSeparator();
			addPop.add(delPlaylistMenu);
		}

		//
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {
						addPop.show(menuButton, 0, height / 2);
					}
				}.start();
			}
		});

		//
		menuButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
				// playListPanel.updateUI();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// playListPanel.revalidate();
				playListPanel.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// playListPanel.revalidate();
				// playListPanel.repaint();
			}
		});

		addSongMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {

						/**
						 * 打开默认的路径
						 */
						JFileChooser songchooser = new JFileChooser();
						/**
						 * 利用方法加入过滤的文件类型,用这种方法可以加入多种文件的类型
						 * 若只想筛选一种文件类型，可使用setFileFilter()的方法
						 */
						songchooser.addChoosableFileFilter(new AudioFilter());
						/**
						 * 只能选择文件列表
						 */
						songchooser
								.setFileSelectionMode(JFileChooser.FILES_ONLY);
						songchooser.setMultiSelectionEnabled(true); // 实现多选

						int result = songchooser
								.showOpenDialog(ListViewItemHeadPanel.this);
						boolean hasUpdate = false;
						if (result == JFileChooser.APPROVE_OPTION) {
							songfiles = songchooser.getSelectedFiles();
							for (int i = 0; i < songfiles.length; i++) {// 支持多选
								File file = songfiles[i];
								String filePath = file.getPath();
								if (!isSongExists(filePath, mSongInfo)) {
									hasUpdate = true;
									SongInfo songInfo = MediaUtils
											.getSongInfoByFile(filePath);
									if (songInfo != null) {
										mSongInfo.add(songInfo);
										// 添加单首歌曲
										refreshListViewItemComPanelUI(pindex,
												mSongInfo.size() - 1,
												listViewItemComPanel, songInfo);

										titleNameJLabel.setText(titleName + "["
												+ mSongInfo.size() + "]");
									}
								}
							}
							if (hasUpdate) {

								// playListPanel.revalidate();
								// playListPanel.repaint();

								playListPanel.updateUI();

								MediaManage.getMediaManage()
										.upDateSongListData(pindex, mSongInfo);

							}
						} else if (result == JFileChooser.CANCEL_OPTION) {
						}
						// playListPanel.revalidate();
						// playListPanel.repaint();

					}
				}.start();
			}
		});

		addSongFiledMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {

						JFileChooser songfile = new JFileChooser();
						songfile.setMultiSelectionEnabled(true);
						songfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						songfile.showOpenDialog(ListViewItemHeadPanel.this);
						if (songfile.getSelectedFile() != null) {
							String playlistname = JOptionPane.showInputDialog(
									ListViewItemHeadPanel.this, "请输入歌曲列表名",
									"新建列表");
							if (playlistname == null)
								return;
							if (playlistname.equals("")) {
								playlistname = "新建列表";
							}

							File[] files = songfile.getSelectedFile()
									.listFiles();
							// 更新内容面板，添加新播放列表数据
							refreshListViewComPanelUI(playlistname, files);

						}
					}
				}.start();
			}
		});

		delPlaylistMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					@Override
					public void run() {
						new Thread() {

							@Override
							public void run() {
								int result = JOptionPane.showConfirmDialog(
										ListViewItemHeadPanel.this, "删除该播放列表?",
										"确认", JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									removePlayListByPIndex(pindex);
								}
							}
						}.start();
					}
				}.start();
			}
		});

		this.add(statusLeftJLabel);
		this.add(statusDownJLabel);
		this.add(titleNameJLabel);
		this.add(menuButton);

	}

	/**
	 * 删除播放列表
	 * 
	 * @param pindex
	 */
	private void removePlayListByPIndex(int pindex) {
		if (pindex >= listViewPanel.getComponentCount())
			return;

		int currentPIndex = MediaManage.getMediaManage().getPindex();
		if (pindex == currentPIndex) {
			MediaManage.getMediaManage().stopToPlay();
			MediaManage.getMediaManage().setSongInfo(null);
			// 当前播放列表含有当前播放的歌曲
			MediaManage.getMediaManage().setPindex(-1);
			MediaManage.getMediaManage().setSindex(-1);
		}

		// 更新数据
		List<Category> categorys = MediaManage.getMediaManage().getmCategorys();
		Category category = categorys.get(pindex);
		category.setStatus(Category.DEL);
		categorys.remove(pindex);
		categorys.add(pindex, category);
		MediaManage.getMediaManage().setmCategorys(categorys);

		// 更新ui,在这里只是对播放列表进行隐藏处理，不做删除处理
		listViewPanel.getComponent(pindex).setVisible(false);
		listViewPanel.validate();
		listViewPanel.repaint();
		listViewPanel.updateUI();
		playListPanel.updateUI();
	}

	/**
	 * 添加播放列表数据
	 * 
	 * @param playlistname
	 *            播放列表名称
	 * @param files
	 *            文件列表
	 */
	private void refreshListViewComPanelUI(String playlistname, File[] files) {

		List<Category> categorys = MediaManage.getMediaManage().getmCategorys();
		int i = categorys.size();
		Category category = new Category(playlistname);
		// 歌曲内容面板
		ListViewItemComPanel listViewItemComPanel = new ListViewItemComPanel();
		listViewItemComPanel.setVisible(false);
		//
		int size = 0;
		//
		List<SongInfo> songInfos = new ArrayList<SongInfo>();
		// for (int j = 0; j < files.length; j++) {
		// SongInfo songInfo = MediaUtils
		// .getSongInfoByFile(files[j].getPath());
		// if (songInfo != null) {
		// songInfos.add(songInfo);
		// refreshListViewItemComPanelUI(i, size, listViewItemComPanel,
		// songInfo);
		// size++;
		// }
		// }
		//
		// 列表标题面板
		ListViewItemHeadPanel listViewItemHeadPanel = new ListViewItemHeadPanel(
				playListPanel, listViewPanel, width, playlistname, size,
				listViewItemComPanel, songInfos, i);

		// listviewitem面板
		ListViewItemPanel itemPanel = new ListViewItemPanel();
		itemPanel.add(listViewItemHeadPanel, 0);
		itemPanel.add(listViewItemComPanel, 1);

		listViewPanel.add(itemPanel);
		// playListPanel.revalidate();
		// playListPanel.repaint();

		playListPanel.updateUI();

		// 更新数据
		category.setmCategoryItem(songInfos);
		categorys.add(category);
		MediaManage.getMediaManage().setmCategorys(categorys);

		updateListViewComPanelUI(i, playlistname, files);
	}

	/**
	 * 更新歌曲列表数据
	 * 
	 * @param i
	 * @param playlistname
	 * @param files
	 */
	private void updateListViewComPanelUI(final int pindex,
			final String playlistname, final File[] files) {
		new Thread() {

			@Override
			public void run() {
				if (pindex >= listViewPanel.getComponentCount())
					return;
				ListViewItemPanel itemPanel = (ListViewItemPanel) listViewPanel
						.getComponent(pindex);
				ListViewItemHeadPanel listViewItemHeadPanel = (ListViewItemHeadPanel) itemPanel
						.getComponent(0);
				ListViewItemComPanel listViewItemComPanel = (ListViewItemComPanel) itemPanel
						.getComponent(1);

				boolean hasUpdate = false;
				List<SongInfo> songInfos = new ArrayList<SongInfo>();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (!file.exists() || file.isDirectory()
							|| !AudioFilter.acceptFilter(file))
						continue;
					String filePath = file.getPath();
					if (!isSongExists(filePath, songInfos)) {
						hasUpdate = true;
						SongInfo songInfo = MediaUtils
								.getSongInfoByFile(filePath);
						if (songInfo != null) {
							songInfos.add(songInfo);
							// 添加单首歌曲
							refreshListViewItemComPanelUI(pindex,
									songInfos.size() - 1, listViewItemComPanel,
									songInfo);
							listViewItemHeadPanel.getTitleNameJLabel()
									.setText(
											playlistname + "["
													+ songInfos.size() + "]");
						}
					}
				}
				if (hasUpdate) {
					playListPanel.updateUI();
					MediaManage.getMediaManage().upDateSongListData(pindex,
							songInfos);
				}
			}

		}.start();
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @param listViewItemComPanel
	 * @param songInfo
	 */
	private void refreshListViewItemComPanelUI(int pindex, int sindex,
			ListViewItemComPanel listViewItemComPanel, SongInfo songInfo) {
		ListViewItemComItemPanel listViewItemComItemPanel = new ListViewItemComItemPanel(
				playListPanel, listViewPanel, pindex, sindex, songInfo, width);
		listViewItemComPanel.add(listViewItemComItemPanel);
	}

	/**
	 * 判断歌曲文件是否存在
	 * 
	 * @param filePath
	 * @param mSongInfo
	 * @return
	 */
	protected boolean isSongExists(String filePath, List<SongInfo> mSongInfo) {
		for (int i = 0; i < mSongInfo.size(); i++) {
			SongInfo tempSongInfo = mSongInfo.get(i);
			if (tempSongInfo.getStatus() == SongInfo.DEL)
				continue;
			if (tempSongInfo.getFilePath().equals(filePath)) {
				return true;
			}
		}
		return false;
	}

	public void paintBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 消除线条锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// 设定渐变 -滑过区域的颜色设置
		g2d.setPaint(new Color(240, 240, 240, 100));
		// g2d.setPaint(Color.BLUE);
		BasicStroke stokeLine = new BasicStroke(2.5f);
		g2d.setStroke(stokeLine);
		if (!isShow) {
			g2d.drawLine(0, height, width, height);
		}
		// g2d.drawRect(0, 0, width, height);
		// g2d.drawRect(0, 0, width - 1, height - 1);
		// g2d.drawRect(0, 0, width - 2, height - 2);
		// g2d.drawRect(0, 0, width - 3, height - 2);
	}

	public JLabel getTitleNameJLabel() {
		return titleNameJLabel;
	}
}
