package net.launcher.utils;

import static net.launcher.utils.BaseUtils.buildUrl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import net.launcher.components.Frame;
import net.launcher.components.PersonalContainer;
import net.launcher.run.Settings;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ThreadUtils
{
	public static UpdaterThread updaterThread;
	public static Thread serverPollThread;
	
	public static void updateNewsPage(final String url)
	{
		BaseUtils.send("Updating news page...");
		if(!BaseUtils.getPropertyBoolean("loadnews", true))
		{
			Frame.main.browser.setText("<center><font color=\"#F0F0F0\" style=\"font-family:Tahoma\">Загрузка новостей не включена</font></center>");
			return;
		}
		Frame.main.browser.setText("<center><font color=\"#F0F0F0\" style=\"font-family:Tahoma\">Обновление страницы...</font></center>");
		Thread t = new Thread() { public void run()
		{
			try
			{
				Frame.main.browser.setPage(url);
				BaseUtils.send("Updating news page sucessful!");
			} catch (Exception e)
			{
				Frame.main.browser.setText("<center><font color=\"#FF0000\" style=\"font-family:Tahoma\"><b>Ошибка загрузки новостей:<br>" + e.toString() + "</b></font></center>");
				BaseUtils.send("Updating news page fail! (" + e.toString() + ")");
			}
			interrupt();
		}};
		t.setName("Update news thread");
		t.start();
	}
	
	public static void auth(final boolean personal)
	{
		BaseUtils.send("Logging in, login: " + Frame.main.login.getText());
		Thread t = new Thread() {
		public void run()
		{ try {
			String answer2 = BaseUtils.execute(BaseUtils.buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt("auth:"+BaseUtils.getClientName()+":"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword())+":"+GuardUtils.hash(ThreadUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL()), Settings.key2),
			});
			BaseUtils.send(answer2);
            String answer = null;
			try {
				answer = decrypt(answer2, Settings.key1);
			} catch (Exception e) {}
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("badlauncher<$>"))
			{
				Frame.main.setUpdateComp(answer.replace("badlauncher<$>_", "" ));
				return;
			} else if(answer.contains("errorLogin<$>"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql<$>"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("client<$>"))
			{
				Frame.main.panel.tmpString = "Ошибка: "+answer.replace("client<$>", "клиент")+" не найден";
				error = true;
			} else if(answer.contains("temp<$>"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;	
			} else if(answer.contains("badhash<$>"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ хеширования";
				error = true;	
			} else if(answer.split("<br>").length != 3)
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setAuthComp();
			} else
			{
				String version = answer.split("<br>")[0].split("<:>")[0];
				
				if(!version.equals(Settings.masterVersion))
				{
					Frame.main.setUpdateComp(version);
					return;
				}
				BaseUtils.send("Logging in successful");
				
				if(personal)
				{
					Frame.main.panel.tmpString = "Загрузка данных...";
					String personal = BaseUtils.execute(BaseUtils.buildUrl("launcher.php"), new Object[]
					{
						"action", encrypt("getpersonal:0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
					});
					
                    if(personal.contains("=="))
					{
						personal = decrypt(personal, Settings.key1);
					}

					if(personal == null)
					{
						Frame.main.panel.tmpString = "Ошибка подключения";
						error = true;
					} else if(answer.contains("errorLogin"))
					{
						Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
						error = true;
					} else if(answer.contains("errorsql"))
					{
						Frame.main.panel.tmpString = "Ошибка sql";
						error = true;
					} else if(answer.contains("temp"))
					{
						Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
						error = true;
					} else if(answer.contains("noactive"))
					{
						Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
						error = true;	
					} else if(answer.contains("badhash"))
					{
						Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
						error = true;	
					} else if(personal.split("<:>").length != 13 || personal.split("<:>")[0].length() != 7)
					{
						Frame.main.panel.tmpString = personal;
						error = true;
					} if(error)
					{
						Frame.main.panel.tmpColor = Color.red;
						try
						{
							sleep(2000);
						} catch (InterruptedException e) {}
						Frame.main.setAuthComp();
						return;
					} else
					{
						try {
						Frame.main.panel.tmpString = "Загрузка скина...";
						BufferedImage skinImage   = BaseUtils.getSkinImage(answer.split("<br>")[1].split("<:>")[0]);
						Frame.main.panel.tmpString = "Загрузка плаща...";
						BufferedImage cloakImage  = BaseUtils.getCloakImage(answer.split("<br>")[1].split("<:>")[0]);
						Frame.main.panel.tmpString = "Парсинг скина...";
						skinImage = ImageUtils.parseSkin(skinImage);
						Frame.main.panel.tmpString = "Парсинг плаща...";
						cloakImage= ImageUtils.parseCloak(cloakImage);
						Frame.main.panel.tmpString = BaseUtils.empty;
						PersonalContainer pc = new PersonalContainer(personal.split("<:>"), skinImage, cloakImage);
						Frame.main.setPersonal(pc);
						
						return;
						} catch(Exception e){ BaseUtils.throwException(e, Frame.main); return; }
					}
				}
				runUpdater(answer);
			} interrupt(); } catch(Exception e){ e.printStackTrace(); }
		}};
		t.setName("Auth thread");
		t.start();
	}
    
        
	public static void runUpdater(String answer)
	{
		boolean zipupdate = false;
		boolean asupdate = false;
		List<String> files = GuardUtils.updateMods(answer);
		
		String folder = BaseUtils.getMcDir().getAbsolutePath()+File.separator;
		String asfolder = BaseUtils.getAssetsDir().getAbsolutePath()+File.separator;
		if(!answer.split("<br>")[0].split("<:>")[2].split("<>")[0].equals(BaseUtils.getPropertyString(BaseUtils.getClientName() + "_zipmd5")) ||
		!new File(folder+"config").exists() || 
		Frame.main.updatepr.isSelected())
		{ 
			GuardUtils.filesize += Integer.parseInt(answer.split("<br>")[0].split("<:>")[2].split("<>")[1]);
			files.add("/"+BaseUtils.getClientName()+"/config.zip");  zipupdate = true;
		}
		
		if(!Settings.assetsfolder)
		{
			if(!answer.split("<br>")[0].split("<:>")[3].split("<>")[0].equals(BaseUtils.getPropertyString("assets_aspmd5")) ||
			!new File(asfolder+"assets").exists() ||
			Frame.main.updatepr.isSelected())
			{
				GuardUtils.filesize += Integer.parseInt(answer.split("<br>")[0].split("<:>")[3].split("<>")[1]);
				files.add("/assets.zip");  asupdate = true;
			}
		}
		
		BaseUtils.send("---- Filelist start ----");
		for(Object s : files.toArray())
		{
			BaseUtils.send("- " + (String) s);
		}
		BaseUtils.send("---- Filelist end ----");
		BaseUtils.send("Running updater...");
		updaterThread = new UpdaterThread(files, zipupdate, asupdate, answer);
		updaterThread.setName("Updater thread");
		Frame.main.setUpdateState();
		updaterThread.run();
	}
	
	public static void pollSelectedServer()
	{
		try
		{
			serverPollThread.interrupt();
			serverPollThread = null;
		} catch (Exception e) {}
		
		BaseUtils.send("Refreshing server state... (" + Frame.main.servers.getSelected() + ")");
		serverPollThread = new Thread()
		{
			public void run()
			{
				Frame.main.serverbar.updateBar("Обновление...", BaseUtils.genServerIcon(new String[]{null, "0", null}));
				int sindex = Frame.main.servers.getSelectedIndex();
				String ip = Settings.servers[sindex].split(", ")[1];
				int port = BaseUtils.parseInt(Settings.servers[sindex].split(", ")[2], 25565);
				String[] status = BaseUtils.pollServer(ip, port);
				String text = BaseUtils.genServerStatus(status);
				BufferedImage img = BaseUtils.genServerIcon(status);
				Frame.main.serverbar.updateBar(text, img);
				
				serverPollThread.interrupt();
				serverPollThread = null;
				BaseUtils.send("Refreshing server done!");
			}
		};
		serverPollThread.setName("Server poll thread");
		serverPollThread.start();
	}

	public static void upload(final File file, final int type)
	{
		new Thread(){ public void run()
		{
			String get = type > 0 ? "uploadcloak" : "uploadskin";
			String answer = BaseUtils.execute(buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt(get+":0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
				"ufile",  file
			});
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("nofile"))
			{
				Frame.main.panel.tmpString = "Файл не выбран";
				error = true;
			} else if(answer.contains("skinerr"))
			{
				Frame.main.panel.tmpString = "Этот файл не является файлом скина";
				error = true;
			} else if(answer.contains("cloakerr"))
			{
				Frame.main.panel.tmpString = "Этот файл не является файлом плаща";
				error = true;
			} else if(answer.contains("fileerr"))
			{
				Frame.main.panel.tmpString = "Ошибка загрузки файла!";
				error = true;
			} else if(answer.contains("errorLogin"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("temp"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;
			} else if(answer.contains("noactive"))
			{
				Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
				error = true;	
			} else if(answer.contains("badhash"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
				error = true;	
			} else if(!answer.contains("success"))
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setPersonal(Frame.main.panel.pc);
				return;
			} else
			{
				if(type > 0)
				{
					Frame.main.panel.pc.realmoney = Integer.parseInt(answer.replaceAll("success:", BaseUtils.empty));
					Frame.main.panel.pc.cloak = ImageUtils.parseCloak(BaseUtils.getCloakImage(Frame.main.login.getText()));
				} else Frame.main.panel.pc.skin = ImageUtils.parseSkin(BaseUtils.getSkinImage(Frame.main.login.getText()));
				Frame.main.setPersonal(Frame.main.panel.pc);
			}
		}}.start();
	}
	
	public static void vaucher(final String vaucher)
	{
		new Thread(){ public void run()
		{
			String answer = BaseUtils.execute(buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt("activatekey:0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
				"key", vaucher,
			});
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("keyerr"))
			{
				Frame.main.panel.tmpString = "Ключ введен неверно!";
				error = true;
			} else if(answer.contains("errorLogin"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("temp"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;
			} else if(answer.contains("noactive"))
			{
				Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
				error = true;	
			} else if(answer.contains("badhash"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
				error = true;	
			} else if(!answer.contains("success"))
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setPersonal(Frame.main.panel.pc);
				return;
			} else
			{
				Frame.main.panel.pc.realmoney = Integer.parseInt(answer.replaceAll("success:", BaseUtils.empty));
				Frame.main.setPersonal(Frame.main.panel.pc);
			}
		}}.start();
	}

	public static void exchange(final String text)
	{
		new Thread(){ public void run()
		{
			String answer = BaseUtils.execute(buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt("exchange:0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
				"buy", text,
			});
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("econo"))
			{
				Frame.main.panel.tmpString = "Вас нет в базе Fe Economy";
				error = true;
			} else if(answer.contains("ecoerr"))
			{
				Frame.main.panel.tmpString = "Вы не ввели сумму";
				error = true;
			} else if(answer.contains("moneyno"))
			{
				Frame.main.panel.tmpString = "У вас недостаточно средств!";
				error = true;
			} else if(answer.contains("errorLogin"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("temp"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;
			} else if(answer.contains("noactive"))
			{
				Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
				error = true;	
			} else if(answer.contains("badhash"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
				error = true;	
			} else if(!answer.contains("success"))
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setPersonal(Frame.main.panel.pc);
				return;
			} else
			{
				String[] moneys = answer.replaceAll("success:", BaseUtils.empty).split(":");
				Frame.main.panel.pc.realmoney = Integer.parseInt(moneys[0]);
				Frame.main.panel.pc.iconmoney = Double.parseDouble(moneys[1]);
				Frame.main.vaucher.setText(BaseUtils.empty);
				Frame.main.setPersonal(Frame.main.panel.pc);
			}
		}}.start();
	}

	public static void buyVip(final int i)
	{
		new Thread(){ public void run()
		{
			String z = i > 0 ? "buypremium" : "buyvip";
			String answer = BaseUtils.execute(buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt(z+":0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
			});
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("moneyno"))
			{
				Frame.main.panel.tmpString = "У вас недостаточно средств!";
				error = true;
			} else if(answer.contains("errorLogin"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("temp"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;
			} else if(answer.contains("noactive"))
			{
				Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
				error = true;	
			} else if(answer.contains("badhash"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
				error = true;	
			} else if(!answer.contains("success"))
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setPersonal(Frame.main.panel.pc);
				return;
			} else
			{
				String[] data = answer.replaceAll("success:", BaseUtils.empty).split(":");
				Frame.main.panel.pc.realmoney = Integer.parseInt(data[0]);
				Frame.main.panel.pc.dateofexpire = BaseUtils.unix2hrd(Long.parseLong(data[1]));
				Frame.main.panel.pc.ugroup = i > 0 ? "Premium" : "VIP";
				Frame.main.setPersonal(Frame.main.panel.pc);
			}
		}}.start();
	}
        
	public static void register(final String name, final String pass, final String pass2,final String mail)
	{
		new Thread(){
		public void run()
		{
			String answer1 = BaseUtils.execute(BaseUtils.buildUrl("reg.php"), new Object[]
			{
				"action", "register",
			    "user",name, 
			    "password",pass, 
			    "password2",pass2, 
			    "email",mail
			});
			boolean error = false;
			if(answer1.contains("done"))
			{
				Frame.main.panel.tmpString = "Регистрация успешно завершена";
				error = false;
			} else if(answer1.contains("errorField"))
			{
				Frame.main.panel.tmpString = "Заполнены не все поля";
				error = true;
			} else if(answer1.contains("errorMail"))
			{
				Frame.main.panel.tmpString = "eMail адрес введен некорректно";
				error = true;
			} else if(answer1.contains("errorMail2"))
			{
				Frame.main.panel.tmpString = "eMail адрес содержит запрещенные символы";
				error = true;
			} else if(answer1.contains("errorLoginSymbol"))
			{
				Frame.main.panel.tmpString = "Логин содержит запрещенные символы";
				error = true;	
			} else if(answer1.contains("passErrorSymbol"))
			{
				Frame.main.panel.tmpString = "Пароль содержит запрещенные символы";
				error = true;
			} else if(answer1.contains("errorPassToPass"))
			{
				Frame.main.panel.tmpString = "Пароль не совпадает";
				error = true;	
			} else if(answer1.contains("errorSmallLogin"))
			{
				Frame.main.panel.tmpString = "Логин должен содержать 2-20 символов";
				error = true;
			} else if(answer1.contains("errorPassSmall"))
			{
				Frame.main.panel.tmpString = "Пароль должен содержать 6-20 символов";
				error = true;
			} else if(answer1.contains("emailErrorPovtor"))
			{
				Frame.main.panel.tmpString = "eMail уже зарегестрирован";
				error = true;
			} else if(answer1.contains("Errorip"))
			{
				Frame.main.panel.tmpString = "С вашего ip уже была регистрация";
				error = true;	
			} else if(answer1.contains("loginErrorPovtor"))
			{
				Frame.main.panel.tmpString = "Пользователем с таким логином уже зарегистрирован";
				error = true;
			} else if(answer1.contains("errorMail"))
			{
				Frame.main.panel.tmpString = "Неправильный адрес eMail";
				error = true;
			} else if(answer1.contains("errorField"))
			{
				Frame.main.panel.tmpString = "Заполнены не все поля";
				error = true;
			} else if(answer1.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer1.contains("registeroff"))
			{
				Frame.main.panel.tmpString = "Регистрация выключена!";
				error = true;	
			}else {
	  	    	Frame.main.panel.tmpString = "Неизвестная ошибка (" + answer1 +")";
				error = true;
	  	  	} 
                        
                        
                        if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setRegister();
				return;
			} else
			{
				Frame.main.panel.tmpColor = Color.GREEN;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setAuthComp();
				return;
				
			}
		}}.start();
	}
	
	public static void unban()
	{
		new Thread(){ public void run()
		{
			String answer = BaseUtils.execute(buildUrl("launcher.php"), new Object[]
			{
				"action", encrypt("buyunban:0:"+Frame.main.login.getText()+":"+new String(Frame.main.password.getPassword()), Settings.key2),
			});
			boolean error = false;
			if(answer == null)
			{
				Frame.main.panel.tmpString = "Ошибка подключения";
				error = true;
			} else if(answer.contains("moneyno"))
			{
				Frame.main.panel.tmpString = "У вас недостаточно средств!";
				error = true;
			} else if(answer.contains("banno"))
			{
				Frame.main.panel.tmpString = "Вы не забанены";
				error = true;
			} else if(answer.contains("moneyno"))
			{
				Frame.main.panel.tmpString = "У вас недостаточно средств!";
				error = true;
			} else if(answer.contains("errorLogin"))
			{
				Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
				error = true;
			} else if(answer.contains("errorsql"))
			{
				Frame.main.panel.tmpString = "Ошибка sql";
				error = true;
			} else if(answer.contains("temp"))
			{
				Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
				error = true;
			} else if(answer.contains("noactive"))
			{
				Frame.main.panel.tmpString = "Ваш аккаунт не активирован!";
				error = true;	
			} else if(answer.contains("badhash"))
			{
				Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ шифровки";
				error = true;	
			} else if(!answer.contains("success"))
			{
				Frame.main.panel.tmpString = answer;
				error = true;
			} if(error)
			{
				Frame.main.panel.tmpColor = Color.red;
				try
				{
					sleep(2000);
				} catch (InterruptedException e) {}
				Frame.main.setPersonal(Frame.main.panel.pc);
				return;
			} else
			{
				String[] s = answer.split(":");
				Frame.main.panel.pc.ugroup = s[2];
				Frame.main.buyUnban.setEnabled(false);
				Frame.main.panel.pc.realmoney = Integer.parseInt(s[1]);
				Frame.main.setPersonal(Frame.main.panel.pc);
			}
		}}.start();
		
	}
	
	static String encrypt(String input, String key){
		  byte[] crypted = null;
		  try{
		    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.ENCRYPT_MODE, skey);
		      crypted = cipher.doFinal(input.getBytes());
		    }catch(Exception e){
		    	System.err.println("Ключ должен быть 16 символов");
		    }
		    return new String(new sun.misc.BASE64Encoder().encode(crypted));
		}

    static String decrypt(String input, String key){
		    byte[] output = null;
		    try{
		      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.DECRYPT_MODE, skey);
		      output = cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(input));
		    }catch(Exception e){
		      System.err.println("Ключ шифрование не совпадает или больше 16 символов, или полученна ошибка от launcher.php");
		      System.err.println("Проверьте настройку  в Settings.java или connect.php");
		    }
		    return new String(output);
		} 
}