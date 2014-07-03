<?php
	if(!defined('INCLUDE_CHECK')) die("You don't have permissions to run this");
	include_once("loger.php");
	/* Метод хеширования пароля для интеграции с различними плагинами/сайтами/cms/форумами
	'hash_md5' 			- md5 хеширование
	'hash_authme'   	- интеграция с плагином AuthMe
	'hash_cauth' 		- интеграция с плагином Cauth
	'hash_xauth' 		- интеграция с плагином xAuth
	'hash_joomla' 		- интеграция с Joomla (v1.6- v1.7)
	'hash_ipb' 			- интеграция с IPB
	'hash_xenforo' 		- интеграция с XenForo
	'hash_wordpress' 	- интеграция с WordPress
	'hash_vbulletin' 	- интеграция с vBulletin
	'hash_dle' 			- интеграция с DLE
	'hash_drupal'     	- интеграция с Drupal (v.7)
	'hash_launcher'		- интеграция с лаунчером sashok724 (Регистрация через лаунчер)
	*/
	$crypt 				= 'hash_wordpress';
	
	$db_host			= 'localhost'; // Ip-адрес MySQL
	$db_port			= '3306'; // Порт базы данных
	$db_user			= 'root'; // Пользователь базы данных
	$db_pass			= 'root'; // Пароль базы данных
	$db_database		= 'wb'; //База данных
	
	$db_table       	= 'wp_users'; //Таблица с пользователями
	$db_columnId  		= 'id'; //Колонка с ID пользователей
	$db_columnUser  	= 'user_login'; //Колонка с именами пользователей
	$db_columnPass  	= 'user_pass'; //Колонка с паролями пользователей
	$db_tableOther 		= 'xf_user_authenticate'; //Дополнительная таблица для XenForo, не трогайте
	$db_columnSesId	 	= 'session'; //Колонка с сессиями пользователей, не трогайте
	$db_columnServer	= 'server'; //Колонка с серверами пользователей, не трогайтe
	$db_columnSalt  	= 'members_pass_salt'; //Настраивается для IPB и vBulletin: , IPB - members_pass_salt, vBulletin - salt
    $db_columnIp  		= 'ip'; //Колонка с IP пользователей
	
	$db_columnDatareg   = 'create_time'; // Колонка даты регистрации
	$db_columnMail      = 'email'; // Колонка mail

	$banlist            = 'banlist'; //Таблица плагина Ultrabans
	
	$useban             =  false; //Бан на сервере = бан в лаунчере, Ultrabans плагин
	$useantibrut        =  true; //Защита от частых подборов пароля (Пауза 1 минута при неправильном пароле)
	
	$masterversion  	= 'final_RC4'; //Мастер-версия лаунчера
	$protectionKey		= '1234567890'; 
	$key1               = "1234567891234567";  //16 Character Key Ключ пост запросов
	$key2               = "1234567891234567"; //16 Character  Key  Ключ пост запросов

    $checklauncher      = false; //Проверка хеша лаунчера
	$md5launcherexe     = md5(@file_get_contents("launcher/fix.exe"));  // Сверяем MD5
	$md5launcherjar     = md5(@file_get_contents("launcher/fix.jar"));  // Сверяем MD5

	$assetsfolder       = false; //Скачивать assets из папки, или из архива (true=из папки false=из архива)

//========================= Настройки ЛК =======================//	

	$db_columnMoney		= 'realmoney'; //Колонка с деньгами
	
	$db_tableMoneyKeys  = 'sashok724_launcher_keys'; //Таблица с ключами
	$db_columnKey		= 'key'; 	//Колонка с ключами
	$db_columnAmount	= 'amount'; //Колонка с ценами ключей
	
	$uploaddirs         = 'MinecraftSkins';  //Папка скинов
	$uploaddirp         = 'MinecraftCloaks'; //Папка плащей
	
	$usePersonal 		=  true; //Использовать личный кабинет
	$canUploadSkin		=  true; //Можно ли заливать скины
	$canUploadCloak		=  true; //Можно ли заливать плащи
	$canBuyVip			=  true; //Можно ли покупать VIP
	$canBuyPremium		=  true; //Можно ли покупать Premium
	$canBuyUnban		=  true; //Можно ли покупать разбан
	$canActivateVaucher =  true; //Можно ли активировать ваучер
	$canExchangeMoney   =  true; //Можно ли обменивать Realmoney -> IConomy
	$canUseJobs			=  true; //Можно ли использовать работы
	$usecheck			=  true; //Можно ли использовать регистрацию в лаунчере
	
	$cloakPrice			=  0;   //Цена плаща (В рублях)
	$vipPrice			=  100;  //Цена випа (В руб/мес)
	$premiumPrice		=  250;  //Цена премиума (В руб/мес)
	$unbanPrice			=  150;  //Цена разбана (В рублях)
	
	$initialIconMoney	=  30;  //Сколько денег дается при регистрации в IConomy
	$exchangeRate		=  200; //Курс обмена Realmoney -> IConomy
	
	//ВСЕ ЧТО НИЖЕ - НЕ ТРОГАТЬ!
	try {
		$db = new PDO("mysql:host=$db_host;port=$db_port;dbname=$db_database", $db_user, $db_pass);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$db->exec("set names utf8");
	} catch(PDOException $pe) {
		die("errorsql".$logger->WriteLine($log_date.$pe));  //вывод ошибок MySQL в m.log
	}
?>