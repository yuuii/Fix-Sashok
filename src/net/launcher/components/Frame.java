package net.launcher.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.launcher.run.Settings;
import net.launcher.utils.BaseUtils;
import net.launcher.utils.EncodingUtils;
import net.launcher.utils.ImageUtils;
import net.launcher.utils.ThemeUtils;
import net.launcher.utils.ThreadUtils;
import static net.launcher.utils.BaseUtils.*;

import com.sun.awt.AWTUtilities;

public class Frame extends JFrame implements ActionListener, FocusListener
{
	
	private static final long serialVersionUID = 1L;

	public static Frame main;
		public Panel panel = new Panel(0);
			public Dragger dragger = new Dragger();
			public Button toGame = new Button("Копать");
			public Button toPersonal = new Button("Войти в ЛК");
			public Button toOptions = new Button("Настройки");
            public Button toRegister = new Button("Регистрация");
			public Checkbox savePass = new Checkbox("Сохранить пароль");
			public JTextPane browser = new JTextPane();
			public JTextPane personalBrowser = new JTextPane();
			public JScrollPane bpane = new JScrollPane(browser);
			public JScrollPane personalBpane = new JScrollPane(personalBrowser);
			public Textfield login = new Textfield();
			public Passfield password = new Passfield();
			public Combobox servers = new Combobox(getServerNames(), 0);
			public Serverbar serverbar = new Serverbar();

			public LinkLabel[] links = new LinkLabel[Settings.links.length];

			public Dragbutton hide = new Dragbutton();
			public Dragbutton close = new Dragbutton();

			public Button update_exe = new Button("exe");
			public Button update_jar = new Button("jar");
			public Button update_no = new Button("Выход");

			public Checkbox loadnews = new Checkbox("Загружать новости");
            public Checkbox Music = new Checkbox("Музыка в лаунчере");
			public Checkbox updatepr = new Checkbox("Принудительное обновление");
		    public Checkbox cleanDir = new Checkbox("Очистить папку");
			public Checkbox fullscreen = new Checkbox("Запустить в полный экран");
			public Textfield memory = new Textfield();
                        
                        
            public Textfield loginReg = new Textfield();
            public Passfield passwordReg = new Passfield();
            public Passfield password2Reg = new Passfield();
            public Textfield mailReg = new Textfield();
            public Button okreg = new Button("Регистрация");
            public Button closereg = new Button("Отмена");
                        
			public Button options_close = new Button("Закрыть");

			public Button buyCloak = new Button("Купить плащ");
			public Button changeSkin = new Button("Сменить скин");
			public Textfield vaucher = new Textfield();
			public Button vaucherButton = new Button("Пополнить");
			public Button buyVaucher = new Button("Купить");
			public Textfield exchangeFrom = new Textfield();
			public Textfield exchangeTo = new Textfield();
			public Button exchangeButton= new Button("Обменять");
			public Button buyVip = new Button(BaseUtils.empty);
			public Button buyPremium = new Button(BaseUtils.empty);
			public Button buyUnban = new Button("Купить разбан");
			public Button toGamePersonal = new Button("В игру");

	public Frame()
	{	
		//Подготовка окна
		setIconImage(BaseUtils.getLocalImage("favicon"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.DARK_GRAY);
		setForeground(Color.DARK_GRAY);
		setLayout(new BorderLayout());
		setUndecorated(Settings.customframe && BaseUtils.getPlatform() != 0);
		if(isUndecorated())
		AWTUtilities.setWindowOpaque(this, false);
		setResizable(false);

		for(int i = 0; i < links.length; i++)
		{
			String[] s = Settings.links[i].split("::");
			links[i] = new LinkLabel(s[0], s[1]);
			links[i].setEnabled(BaseUtils.checkLink(s[1]));
		}

		try
		{
			ThemeUtils.updateStyle(this);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		//Добавление слушателей
		toGame.addActionListener(this);
		toPersonal.addActionListener(this);
		toPersonal.setVisible(Settings.usePersonal);
		toOptions.addActionListener(this);
        toRegister.addActionListener(this);
        toRegister.setVisible(Settings.useRegister);
		login.setText("Логин...");
		login.addActionListener(this);
		login.addFocusListener(this);
		password.setText("itspassword");
		password.setEchoChar('*');
		password.addActionListener(this);
		password.addFocusListener(this);

		bpane.setOpaque(false);
		bpane.getViewport().setOpaque(false);
		bpane.setBorder(null);

		personalBpane.setOpaque(false);
		personalBpane.getViewport().setOpaque(false);
		personalBpane.setBorder(null);

		personalBrowser.setOpaque(false);
		personalBrowser.setBorder(null);
		personalBrowser.setContentType("text/html");
		personalBrowser.setEditable(false);
		personalBrowser.setFocusable(false);
		personalBrowser.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					openURL(e.getURL().toString());
				}
			}
		});	

		browser.setOpaque(false);
		browser.setBorder(null);
		browser.setContentType("text/html");
		browser.setEditable(false);
		browser.setFocusable(false);
		browser.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					if(Settings.useStandartWB) openURL(e.getURL().toString());
					else ThreadUtils.updateNewsPage(e.getURL().toString());
				}
			}
		});
		hide.addActionListener(this);
		close.addActionListener(this);
                
		update_exe.addActionListener(this);
		update_jar.addActionListener(this);
		update_no.addActionListener(this);
		servers.addMouseListener(new MouseListener()
		{
			public void mouseReleased(MouseEvent e){}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e)  {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e)
			{
				if(servers.getPressed() || e.getButton() != MouseEvent.BUTTON1) return;

				ThreadUtils.pollSelectedServer();
				setProperty("server", servers.getSelectedIndex());
			}
		});

		options_close.addActionListener(this);
        closereg.addActionListener(this);
        okreg.addActionListener(this);
		loadnews.addActionListener(this);
        Music.addActionListener(this);
		fullscreen.addActionListener(this);

		buyCloak.addActionListener(this);
		changeSkin.addActionListener(this);
		vaucherButton.addActionListener(this);
		buyVaucher.addActionListener(this);
		exchangeButton.addActionListener(this);
		buyVip.addActionListener(this);
		buyPremium.addActionListener(this);
		buyUnban.addActionListener(this);
		toGamePersonal.addActionListener(this);

		login.setText(getPropertyString("login"));
		String pass = getPropertyString("password");
		password.setText(pass == null ? "itspassword" : EncodingUtils.decode(pass));
		savePass.setSelected(pass != null);
		servers.setSelectedIndex(getPropertyInt("server"));

		exchangeFrom.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				warn();
			}
			public void removeUpdate(DocumentEvent e)
			{
				warn();
			}
			public void insertUpdate(DocumentEvent e)
			{
				warn();
			}

			public void warn()
			{
				try
				{
					int i = Integer.parseInt(exchangeFrom.getText());
					exchangeTo.setText(String.valueOf((long)i * (long)panel.pc.exchangeRate) + " Монет");
				} catch(Exception e){ exchangeTo.setText("<N/A>"); }
			}
		});

		addAuthComp();
		addFrameComp();
		add(panel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);
		validate();
		repaint();
		setVisible(true);
	}

	public void addFrameComp()
	{
		if(Settings.customframe)
		{
			panel.add(hide);
			panel.add(close);
			panel.add(dragger);
		}
	}

	public void setAuthComp()
	{
		panel.type = 0;
		panel.timer.stop();
		panel.removeAll();
		addFrameComp();
		addAuthComp();
		repaint();
	}

	/** Добавление элементов авторизации*/
	public void addAuthComp()
	{
		panel.add(servers);
		panel.add(serverbar);
		for(LinkLabel link : links) panel.add(link);
		panel.add(toGame);
		panel.add(toPersonal);
		panel.add(toOptions);
                panel.add(toRegister);
		panel.add(login);
		panel.add(bpane);
		panel.add(password);
		panel.add(savePass);
	}

	//Старт программы
	public static void start()
	{
		try
		{
			send("****launcher****");
			try
			{
				send("Setting new LaF...");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception e)
			{
				send("Fail setting LaF");
			}
			send("Running debug methods...");

			new Runnable()
			{
				public void run()
				{
					Settings.onStart();
				}
			}.run();

			main = new Frame();

			ThreadUtils.updateNewsPage(buildUrl("news.php"));
			ThreadUtils.pollSelectedServer();
			try
			{
				main.memory.setText(String.valueOf(getPropertyInt("memory", 512)));
				main.fullscreen.setSelected(getPropertyBoolean("fullscreen"));
				main.loadnews.setSelected(getPropertyBoolean("loadnews", true));
                main.Music.setSelected(getPropertyBoolean("Music", true));
			} catch(Exception e){}
		} catch(Exception e)
		{
			throwException(e, main);
		}
	}

	public static String jar;
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == hide) setExtendedState(ICONIFIED);
		if(e.getSource() == close || e.getSource() == update_no) System.exit(0);

		if(e.getSource() == update_exe)
		{
			jar = ".exe";
			new Thread() { public void run() { try
			{
				panel.type = 8;
				update_exe.setEnabled(false);
				update_no.setText("Отмена");
				panel.repaint();
				BaseUtils.updateLauncher();
			} catch(Exception e1)
			{
				e1.printStackTrace();
				send("Error updating launcher!");
				update_no.setText("Выйти");
				update_exe.setEnabled(true);
				panel.type = 9;
				panel.repaint();
			}}}.start();
		}
		
		if(e.getSource() == update_jar)
		{
			jar = ".jar";
			new Thread() { public void run() { try
			{
				panel.type = 8;
				update_jar.setEnabled(false);
				update_no.setText("Отмена");
				panel.repaint();
				BaseUtils.updateLauncher();
			} catch(Exception e1)
			{
				e1.printStackTrace();
				send("Error updating launcher!");
				update_no.setText("Выйти");
				update_jar.setEnabled(true);
				panel.type = 9;
				panel.repaint();
			}}}.start();
		}

		if(e.getSource() == login || e.getSource() == password || e.getSource() == toGame || e.getSource() == toPersonal || e.getSource() == toGamePersonal)
		{
			boolean personal = false;
			if(e.getSource() == toPersonal) personal = true;
			setProperty("login", login.getText());
			setProperty("server", servers.getSelectedIndex());
			if(savePass.isSelected()) setProperty("password", EncodingUtils.encode(new String(password.getPassword())));
			else setProperty("password", empty);
			panel.remove(hide);
			panel.remove(close);
			BufferedImage screen = ImageUtils.sceenComponent(panel);
			panel.removeAll();
			addFrameComp();
			panel.setAuthState(screen);
			ThreadUtils.auth(personal);
		}

		if(e.getSource() == toOptions)
		{
			setOptions();
		}

		if(e.getSource() == toRegister)
		{
			setRegister();
		}             
                
		if(e.getSource() == options_close)
		{
			if(!memory.getText().equals(getPropertyString("memory")))
			{
				try
				{
					int i = Integer.parseInt(memory.getText());
					setProperty("memory", i);
				} catch(Exception e1){}
				restart();
			}
			setAuthComp();
		}

		if(e.getSource() == fullscreen || e.getSource() == loadnews || e.getSource() == Music)
		{
			setProperty("fullscreen", fullscreen.isSelected());
			setProperty("loadnews",   loadnews.isSelected());
            setProperty("Music",   Music.isSelected());
		}

		if(e.getSource() == buyCloak)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new SkinFilter(1));
			chooser.setAcceptAllFileFilterUsed(false);
			int i = chooser.showDialog(main, "Купить");

			if(i == JFileChooser.APPROVE_OPTION)
			{
				setLoading();
				ThreadUtils.upload(chooser.getSelectedFile(), 1);
			}
		}

		if(e.getSource() == changeSkin)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new SkinFilter(0));
			chooser.setAcceptAllFileFilterUsed(false);
			int i = chooser.showDialog(main, "Сменить");

			if(i == JFileChooser.APPROVE_OPTION)
			{
				setLoading();
				ThreadUtils.upload(chooser.getSelectedFile(), 0);
			}
		}

		if(e.getSource() == vaucherButton)
		{
			setLoading();
			ThreadUtils.vaucher(vaucher.getText());
		}

		if(e.getSource() == okreg)
		{
                   setLoading();
                   ThreadUtils.register(loginReg.getText(), passwordReg.getText(), password2Reg.getText(), mailReg.getText());
		}      
		if(e.getSource() == closereg)
		{
			setAuthComp();
		}                
		if(e.getSource() == buyVaucher){
			openURL(Settings.buyVauncherLink);
		}

		if(e.getSource() == exchangeButton)
		{
			setLoading();
			ThreadUtils.exchange(exchangeFrom.getText());
		}

		if(e.getSource() == buyVip)
		{
			setLoading();
			ThreadUtils.buyVip(0);
		}

		if(e.getSource() == buyPremium)
		{
			setLoading();
			ThreadUtils.buyVip(1);
		}

		if(e.getSource() == buyUnban)
		{
			setLoading();
			ThreadUtils.unban();
		}
	}

	public void focusGained(FocusEvent e)
	{
		if(e.getSource() == login && login.getText().equals("Логин...")) login.setText(empty);
		if(e.getSource() == password && new String(password.getPassword()).equals("itspassword")) password.setText(empty);
	}

	public void focusLost(FocusEvent e)
	{
		if(e.getSource() == login && login.getText().equals(empty)) login.setText("Логин...");
		if(e.getSource() == password && new String(password.getPassword()).equals(empty)) password.setText("itspassword");
	}

	public void setUpdateComp(String version)
	{
		panel.removeAll();
		addFrameComp();
		panel.setUpdateState(version);
		panel.add(update_exe);
		panel.add(update_jar);
		panel.add(update_no);
		repaint();
	}

	public void setUpdateState()
	{
		panel.removeAll();
		addFrameComp();
		panel.setUpdateStateMC();
		repaint();
	}

	public void setRegister()
	{
		panel.remove(hide);
		panel.remove(close);
		BufferedImage screen = ImageUtils.sceenComponent(panel);
		panel.removeAll();
		addFrameComp();
		panel.setRegister(screen);

		panel.add(loginReg);
        panel.add(passwordReg);
        panel.add(password2Reg);
        panel.add(mailReg);
                
        panel.add(okreg);
		panel.add(closereg);

		repaint();
	}

	public void setOptions()
	{
		panel.remove(hide);
		panel.remove(close);
		BufferedImage screen = ImageUtils.sceenComponent(panel);
		panel.removeAll();
		addFrameComp();
		panel.setOptions(screen);
		panel.add(loadnews);
        panel.add(Music);
		panel.add(updatepr);
		panel.add(cleanDir);
		panel.add(fullscreen);
		panel.add(memory);
		panel.add(options_close);
		repaint();
	}        
        
	public void setPersonal(PersonalContainer pc)
	{
		panel.removeAll();
		addFrameComp();

		if(pc.canUploadCloak) panel.add(buyCloak);
		if(pc.canUploadSkin) panel.add(changeSkin);
		if(pc.canActivateVaucher)
		{
			panel.add(vaucher);
			panel.add(vaucherButton);
			panel.add(buyVaucher);
		}

		if(pc.canExchangeMoney)
		{
			panel.add(exchangeFrom);
			panel.add(exchangeTo);
			panel.add(exchangeButton);
		}

		if(pc.canBuyVip) panel.add(buyVip);
		if(pc.canBuyPremium) panel.add(buyPremium);

		if(pc.canBuyUnban) panel.add(buyUnban);

		buyVip.setText("Купить VIP");
		buyVip.setEnabled(true);

		buyPremium.setText("Купить Premium");
		buyPremium.setEnabled(true);

		if(pc.ugroup.equals("Banned"))
		{
			buyPremium.setEnabled(false);
			buyVip.setEnabled(false);
		} else if(pc.ugroup.equals("VIP"))
		{
			buyVip.setText("Продлить VIP");
			buyPremium.setEnabled(false);
			buyUnban.setEnabled(false);
		} else if(pc.ugroup.equals("Premium"))
		{
			buyPremium.setText("Продлить Premium");
			buyVip.setEnabled(false);
			buyUnban.setEnabled(false);
		} else if(pc.ugroup.equals("User"))
		{
			buyUnban.setEnabled(false);
		}

		panel.add(toGamePersonal);

		panel.setPersonalState(pc);
		repaint();
	}

	public void setLoading()
	{
		panel.remove(hide);
		panel.remove(close);
		BufferedImage screen = ImageUtils.sceenComponent(panel);
		panel.removeAll();
		addFrameComp();
		panel.setLoadingState(screen, "Выполнение...");
	}

	public void setError(String s)
	{
		panel.removeAll();
		addFrameComp();
		panel.setErrorState(s);
	}

}