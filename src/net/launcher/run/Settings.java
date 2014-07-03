/*launcher, сборка за 30.10.2012, индекс: 121 */

//Добавлена регистрация под webmcr (md5) в тестовом режиме, ставить на боевом сайте крайне не рекомендуется

package net.launcher.run;

public class Settings
{
	/** Настройка заголовка лаунчера */
	public static final String  title		         = "Launcher"; //Заголовок лаунчера
	public static final String  titleInGame  	     = "Minecraft"; //Заголовок лаунчера после авторизации
	public static final String  baseconf		     = "voxelaria"; //Папка с файлом конфигурации
	public static final String  pathconst		     = "voxelaria/%SERVERNAME%"; //Конструктор пути к папке с MC
	public static final String  skins                = "MinecraftSkins/"; //Папка скинов
	public static final String  cloaks               = "MinecraftCloaks/"; //Папка плащей
	/** Параметры подключения */
	public static final String  domain	 	         = "alexandrage.ssh22.net";//Домен сайта
	public static final String  siteDir		         = "site";//Папка с файлами лаунчера на сайте
	public static final String  updateFile		     = "http://alexandrage.ssh22.net/site";//Ссылка на обновления лаунчера. Не писать на конце ".exe .jar"!
	public static final String  buyVauncherLink      = "http://plati.ru/"; //Ссылка на страницу покупки ваучеров
	public static final String  iMusicname           = "001.mp3";
	
	public static int height                         = 532;      //Высота окна клиента
	public static int width                          = 900;      //Ширина окна клиента
        
	public static String[] servers =
	{
		"Offline, localhost, 25565, 1.5.2",
	};

	/** Настройка панели ссылок **/
	public static final String[] links = 
	{
		//Для отключения добавьте в адрес ссылки #
		" Регистрация ::http://",
	};

	/** Настройки структуры лаунчера */
	public static boolean useAutoenter	         =  false;  //Использовать функцию автозахода на выбранный сервер
	public static boolean useRegister		     =  false;  //Использовать Регистрацию в лаунчере
	public static boolean useMulticlient		 =  true;   //Использовать функцию "по клиенту на сервер"
	public static boolean useStandartWB		     =  true;   //Использовать стандартный браузер для открытия ссылок
	public static boolean usePersonal		     =  true;   //Использовать Личный кабинет
	public static boolean customframe 		     =  true;   //Использовать кастомный фрейм
	public static boolean useConsoleHider		 =  false;  //Использовать скрытие консоли клиента
	public static boolean useModCheckerTimer	 =  true;   //Перепроверка jar через 30 секунд
	public static int     useModCheckerint       =  2;      //Количество раз пеепроверки jar во времяигры
	public static boolean assetsfolder           =  false;  //Скачивать assets из папки, или из архива (true=из папки false=из архива) в connect.php должно быть так же.

	public static final String protectionKey	 = "1234567890"; //Ключ защиты сессии. Никому его не говорите.
	public static final String key1              = "1234567891234567"; //16 Character Key Ключ пост запросов
	public static final String key2              = "1234567891234567"; //16 Character Key Ключ пост запросов
	

	public static boolean debug		 	         =  true; //Отображать все действия лаунчера (отладка)(true/false)
	public static boolean drawTracers		     =  false; //Отрисовывать границы элементов лаунчера
	public static final String masterVersion     = "final_RC4"; //Версия лаунчера

	public static boolean patchDir 		         =  true; //Использовать автоматическую замену директории игры (true/false)
	
	public static void onStart() {}
	public static void onStartMinecraft() {}
	
}
